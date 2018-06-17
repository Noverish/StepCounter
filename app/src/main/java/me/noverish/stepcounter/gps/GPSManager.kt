package me.noverish.stepcounter.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import me.noverish.stepcounter.R
import java.util.ArrayList


class GPSManager(private val context: Context) : LocationListener, PermissionListener {
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var callback: GPSCallback? = null

    init {
        TedPermission.with(context)
            .setPermissionListener(this)
            .setDeniedMessage(context.getString(R.string.permission_denied_gps))
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()
    }


    // PermissionListener
    override fun onPermissionGranted() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, this)
        } else {
            onPermissionDenied(null)
        }
    }

    override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
        Toast.makeText(context, R.string.permission_denied_gps, Toast.LENGTH_LONG).show()
    }


    // LocationListener
    override fun onLocationChanged(l: Location?) {
        println("location $l")
        val location = l ?: return
        callback?.onLocationChanged(location.latitude, location.longitude)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        val statusStr = when (p1) {
            LocationProvider.AVAILABLE -> "AVAILABLE"
            LocationProvider.OUT_OF_SERVICE -> "OUT_OF_SERVICE"
            LocationProvider.TEMPORARILY_UNAVAILABLE -> "TEMPORARILY_UNAVAILABLE"
            else -> "UNKNOWN"
        }

        callback?.onStatusChanged(statusStr)

        /*
        val stringBuilder = StringBuilder()
        stringBuilder.append("[")
        if (p2 != null) {
            for (key in p2.keySet()) {
                stringBuilder.append(key)
                stringBuilder.append(" : ")
                stringBuilder.append(p2.get(key))
                stringBuilder.append(" ")
            }
        }
        stringBuilder.append("]")

        println("onStatusChanged $p0 $statusStr $stringBuilder")
        */
    }

    override fun onProviderEnabled(p0: String?) {
        println("onProviderEnabled $p0")
    }

    override fun onProviderDisabled(p0: String?) {
        println("onProviderDisabled $p0")
    }
}