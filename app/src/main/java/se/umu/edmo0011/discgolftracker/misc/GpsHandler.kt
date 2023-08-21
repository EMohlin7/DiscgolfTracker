package se.umu.edmo0011.discgolftracker.misc

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

//Singleton
class GpsHandler
{
    companion object
    {
        private var handler: GpsHandler? = null

        fun getInstance(lm: LocationManager): GpsHandler?
        {
            if(handler != null)
                return handler as GpsHandler

            handler = GpsHandler(lm)
            return handler as GpsHandler
        }
    }

    private constructor(lm: LocationManager)
    {
        locationManager = lm
    }
    private val locationManager: LocationManager
    //private var listener: AppLocationListener = AppLocationListener{}
    //var listening: Boolean = false; private set



    @SuppressLint("MissingPermission")
    fun subscribeToUpdates(
        c: Context,
        minTimeMs: Long,
        minDistanceM: Float,
        listener: AppLocationListener
    )
        /*onProviderDisabled: ((String)->Unit)? = null,
        onProviderEnabled: ((String)->Unit)? = null,
        onLocationChanged: (Location)->Unit)*/
    {
        if(!haveGpsPermission(c))
            throw Exception("Gps access is needed but permission is not granted")



        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            minTimeMs,
            minDistanceM,
            listener
        )

    }

    fun unsubscribeToUpdates(listener: AppLocationListener)
    {
        locationManager.removeUpdates(listener)
    }


    @SuppressLint("MissingPermission")
    fun getCurrentLocation(c: Context, maxWaitMs: Long, onLocation: (Location?)->Unit)
    {
        if(!haveGpsPermission(c))
            throw Exception("Gps access is needed but permission is not granted")


        if(Build.VERSION.SDK_INT >= 31) {
            locationManager.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                LocationRequest.Builder(10).setDurationMillis(maxWaitMs).build(),
                null,
                Executors.newSingleThreadExecutor(),
                onLocation
            )
        }
        else
        {
            getCurrentPositionOldApi(c, maxWaitMs, onLocation)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentPositionOldApi(c: Context, maxWaitMs: Long, onLocation: (Location?)->Unit)
    {
        if(!haveGpsPermission(c))
            throw Exception("Gps access is needed but permission is not granted")
        var received = false
        val listener = AppLocationListener(onLocationChanged = {received = true; onLocation.invoke(it)})
        val looper = Looper.myLooper()

        locationManager.requestSingleUpdate(
            LocationManager.GPS_PROVIDER,
            listener, looper)

        val handler = Handler(looper!!)
        //Runs after max wait
        handler.postDelayed({
            run{
                if(!received)
                {
                    locationManager.removeUpdates(listener)
                    onLocation.invoke(null)
                }
            }
        }, maxWaitMs)
    }

    fun haveGpsPermission(c: Context): Boolean
    {
        return ContextCompat.checkSelfPermission(
            c, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun gpsEnabled(): Boolean
    {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}

