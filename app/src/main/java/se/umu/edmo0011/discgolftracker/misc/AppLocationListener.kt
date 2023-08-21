package se.umu.edmo0011.discgolftracker.misc

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

class AppLocationListener(
    onProvDisabled: ((String) -> Unit)? = null,
    onProviderEnabled: ((String) -> Unit)? = null,
    onLocationChanged: (Location) -> Unit
): LocationListener
{
    private val opd: ((String)->Unit)?
    private val ope: ((String)->Unit)?
    private val olc: (Location)->Unit

    init{
        opd = onProvDisabled
        ope = onProviderEnabled
        olc = onLocationChanged
    }

    override fun onLocationChanged(l: Location)
    {
        olc.invoke(l)
    }

    override fun onProviderDisabled(provider: String) {
        opd?.invoke(provider)
    }

    override fun onProviderEnabled(provider: String) {
        ope?.invoke(provider)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }
}