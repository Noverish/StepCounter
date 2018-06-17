package me.noverish.stepcounter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.nhn.android.maps.maplib.NGeoPoint
import kotlinx.android.synthetic.main.activity_main.*
import me.noverish.stepcounter.gps.GPSCallback
import me.noverish.stepcounter.gps.GPSManager
import me.noverish.stepcounter.map.NaverMapFragment
import me.noverish.stepcounter.step.StepDetector
import me.noverish.stepcounter.step.StepListener
import android.content.Intent
import android.view.MenuItem
import me.noverish.stepcounter.utils.SharedPreferenceManager


class MainActivity : AppCompatActivity(), SensorEventListener, StepListener, GPSCallback {

    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private var numSteps: Int = 0
    private var isStarted: Boolean = false

    private var gpsManager: GPSManager? = null

    private var fragment: NaverMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gpsManager = GPSManager(this)
        gpsManager!!.callback = this

        // Get an instance of the SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        simpleStepDetector = StepDetector()
        simpleStepDetector!!.registerListener(this)

        step_counter_start_btn.setOnClickListener {
            isStarted = !isStarted

            if (isStarted) {
                step_counter_start_btn.text = getString(R.string.step_counter_stop)
                sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)
            } else {
                step_counter_start_btn.text = getString(R.string.step_counter_start)
                sensorManager!!.unregisterListener(this)
            }
        }

        step_counter_reset_btn.setOnClickListener {
            numSteps = 0
            step_label.text = getString(R.string.init_step_number)
        }

        fragment = NaverMapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.map_layout, fragment!!)
            .commit()
    }

    // SensorEventListener
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector!!.updateAccelerometer(event.timestamp, event.values[0], event.values[1], event.values[2])
        }
    }

    // StepListener
    override fun step(timeNs: Long) {
        step_label.text = (++numSteps).toString()

        if (numSteps % 50 == 0) {
            if (lat_label.text.isNotEmpty()) {

                val lat = lat_label.text.toString().toDouble()
                val lng = lng_label.text.toString().toDouble()

                SharedPreferenceManager.putRecords(this, lat, lng, numSteps)

            }
        }
    }

    // GPSCallback
    override fun onStatusChanged(status: String) {
        gps_status_label.text = status
    }

    override fun onLocationChanged(lat: Double, lng: Double) {
        lat_label.text = lat.toString()
        lng_label.text = lng.toString()

        val point = NGeoPoint(lng, lat)

        fragment?.mapView?.mapController?.animateTo(point)
        map_pin.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val menuItem = item ?: return super.onOptionsItemSelected(item)

        return when (menuItem.itemId) {
            R.id.record -> {
                startActivity(Intent(this, RecordActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
