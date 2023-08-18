package se.umu.edmo0011.discgolftracker.viewModels

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.AppLocationListener
import se.umu.edmo0011.discgolftracker.GpsHandler
import se.umu.edmo0011.discgolftracker.MeasureGraph
import se.umu.edmo0011.discgolftracker.MeasuredThrowsGraph
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.THROWS_KEY
import se.umu.edmo0011.discgolftracker.Throw
import se.umu.edmo0011.discgolftracker.navigateAndPopUp
import java.util.Date

/*TODO fix bug that keeps gps on when navigating back from measuring to start measure*/
class MeasureViewModel() : ViewModel()
{
    private val MAX_GPS_WAIT: Long = 15000
    private var gps: GpsHandler? = null

    private var gpsListener: AppLocationListener? = null

    private val _startPos = mutableStateOf(Location(LocationManager.GPS_PROVIDER))
    val startPos get() = _startPos.value

    private val _currentPos = mutableStateOf(Location(LocationManager.GPS_PROVIDER))
    val currentPos get() = _currentPos.value

    val distance by derivedStateOf { startPos.distanceTo(currentPos) }

    var lookingForGps by mutableStateOf(false); private set

    var showEnableAlert by mutableStateOf(false); private set
    var showPermissionNeededAlert by mutableStateOf(false); private set
    var showFailedAlert by mutableStateOf(false); private set

    private var measurements = 0


    fun startGps(context: Context, navCon: NavController, onNoPermission: ()->Unit)
    {
        gps = GpsHandler.getInstance(context.getSystemService(ComponentActivity.LOCATION_SERVICE) as LocationManager)

        if(!gps!!.haveGpsPermission(context))
        {
            onNoPermission?.invoke()
            return;
        }

        if(!gps!!.gpsEnabled())
        {
            showEnableAlert = true
            return
        }
        measurements = 0

        if(gpsListener == null) {
            gpsListener = createListener {

                ++measurements
                _currentPos.value = it
                Log.w("Location", "${it.accuracy}")
                //The first measurement is usually pretty bad, so we wait for it to settle before we start
                //measuring
                if (measurements == 3)
                    startMeasuring(context, navCon, it)
            }
        }
        gps!!.subscribeToUpdates(context, 500, 0f, gpsListener!!)
        //Need to call lookForGpsSignal because it has a max time it waits for a location
        //before giving up, this way we can abort if we don't find a signal in a
        //given time frame
        lookForGpsSignal(navCon, MAX_GPS_WAIT, ::onFailedSignal){}
    }

    private fun lookForGpsSignal(navCon: NavController, maxWait: Long, onFailed: ()->Unit={}, onSuccess: ()->Unit)
    {
        lookingForGps = true
        gps!!.getCurrentLocation(navCon.context, maxWait){
            if(it != null)
                onSuccess.invoke()
            else
                onFailed.invoke()
        }
    }

    private fun createListener(
        onProvDisabled: (String)->Unit = {},
        onProvEnabled: (String)->Unit = {},
        onLocation: (Location)->Unit): AppLocationListener
    {
        if(gpsListener != null)
            stopGps()
        return AppLocationListener(onProvDisabled, onProvEnabled, onLocation)
    }

    private fun startMeasuring(context: Context, navCon: NavController, location: Location)
    {
        lookingForGps = false
        Log.w("Location", "On successfull start")
        val mainHandler = Handler(context.mainLooper)
        //navigate must be called on the main thread and this is called from the callback on
        //gps.getSubscribeToUpdates which isn't the main thread
        mainHandler.post{
            _startPos.value = location
            _currentPos.value = location
            navCon.navigate(MeasureGraph.Measuring.route)
        }
    }

    private fun onFailedSignal()
    {
        Log.w("Location", "On failed start")
        showFailedAlert = true
        stopGps()
    }

    fun onNotGrantedPermission()
    {
        showPermissionNeededAlert = true
    }

    fun onDismissEnableAlert()
    {
        showEnableAlert = false
    }

    fun onDismissPermissionAlert()
    {
        showPermissionNeededAlert = false
    }
    fun onDismissFailedAlert()
    {
        showFailedAlert = false
    }

    fun navToSave(navCon: NavController)
    {
        navCon.navigateAndPopUp(MeasureGraph.Save.route, MeasureGraph.StartMeasure.route, false)
        stopGps()
    }

    fun saveMeasurment(navCon: NavController, distance: Int, disc: String, course: String, hole:String)
    {
        val list = SharedPreferencesHelper.getList<Throw>(navCon.context, THROWS_KEY).toMutableList()
        list.add(Throw(distance, startPos.latitude, startPos.longitude, disc, Date().time, course, hole))
        SharedPreferencesHelper.saveList(navCon.context, list, THROWS_KEY)

        navCon.navigateAndPopUp(MeasuredThrowsGraph.route, false)
    }

    private fun stopGps()
    {
        lookingForGps = false
        measurements = 0
        if(gpsListener == null)
            return
        gps?.unsubscribeToUpdates(gpsListener!!)
    }


    override fun onCleared() {
        super.onCleared()
        Log.w("Location", "onCleared")
        stopGps()
    }
}







