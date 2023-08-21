package se.umu.edmo0011.discgolftracker.viewModels

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.misc.AppLocationListener
import se.umu.edmo0011.discgolftracker.misc.GpsHandler
import se.umu.edmo0011.discgolftracker.misc.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.misc.THROWS_KEY
import se.umu.edmo0011.discgolftracker.dataClasses.Throw
import se.umu.edmo0011.discgolftracker.graphs.MeasureGraph
import se.umu.edmo0011.discgolftracker.graphs.MeasuredThrowsGraph
import se.umu.edmo0011.discgolftracker.misc.navigateAndPopUp
import java.util.Date


class MeasureViewModel() : ViewModel()
{
    private val MAX_GPS_WAIT: Long = 15000
    private val GPS_INTERVAL: Long = 400
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

        //Create a new gps listener that controls the behaviour when we receive a position from the gps
        if(gpsListener == null) {
            gpsListener = createListener {

                ++measurements
                _currentPos.value = it
                //The first measurement is usually pretty bad, so we wait for it to settle before we start
                //measuring
                if (measurements == 2)
                    startMeasuring(context, navCon, it)
            }
        }
        gps!!.subscribeToUpdates(context, GPS_INTERVAL, 0f, gpsListener!!)

        //Need to call lookForGpsSignal because it has a max time it waits for a location
        //before giving up, this way we can abort if we don't find a signal in a
        //given time frame. We don't care if it succeeds because the first measurement is most likely
        //incorrect
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

    //Called when we have successfully gotten a gps signal and can start measuring
    private fun startMeasuring(context: Context, navCon: NavController, location: Location)
    {
        lookingForGps = false
        _startPos.value = location
        _currentPos.value = location
        Log.w("Location", "On successfull start")
        val mainHandler = Handler(context.mainLooper)
        //navigate must be called on the main thread and this is called from the callback on
        //gps.getSubscribeToUpdates which isn't the main thread
        mainHandler.post{
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

    //Called from saveMeasurementsScreen. Saves the throw and navigates back to the list of throws
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







