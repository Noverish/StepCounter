package me.noverish.stepcounter.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhn.android.maps.NMapContext
import com.nhn.android.maps.NMapOverlayItem
import com.nhn.android.maps.NMapView
import com.nhn.android.maps.maplib.NGeoPoint
import me.noverish.stepcounter.R

class NaverMapFragment: Fragment() {
    private val CLIENT_ID = "S9_GX86Quuutb3Z8xrkt"
    private val DEFAULT_LAT = 37.586263
    private val DEFAULT_LNG = 127.029363
    private val DEFAULT_ZOOM = 10

    private var mapContext: NMapContext? = null
    var mapView: NMapView? = null
    var overlayItem: NMapOverlayItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapContext = NMapContext(activity)
        mapContext?.onCreate()

        mapView = NMapView(context)
        mapView?.let {
            it.setClientId(CLIENT_ID)
            it.isClickable = true
            it.isEnabled = true
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }

        val point = NGeoPoint(DEFAULT_LNG, DEFAULT_LAT)
        val drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_location_on_black_24dp)
        overlayItem = NMapOverlayItem(point, "asdf", "fdsa", drawable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return mapView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapContext?.setupMapView(mapView)

        mapView?.let { v ->
            v.setScalingFactor(resources.displayMetrics.density, true)
            v.mapController.setMapCenter(DEFAULT_LNG, DEFAULT_LAT, DEFAULT_ZOOM)
            v.mapController.setPanEnabled(false)
        }
    }

    override fun onStart() {
        super.onStart()
        mapContext?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapContext?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapContext?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapContext?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapContext?.onDestroy()
    }
}