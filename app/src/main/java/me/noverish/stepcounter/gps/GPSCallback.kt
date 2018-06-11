package me.noverish.stepcounter.gps

interface GPSCallback {
    fun onStatusChanged(status: String)
    fun onLocationChanged(lat: Double, lng: Double)
}