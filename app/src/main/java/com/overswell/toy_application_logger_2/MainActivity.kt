package com.overswell.toy_application_logger_2

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    val vm : MainViewModel = get()
    private val myLocationPermission = "locationPermission"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        log_location.setOnClickListener {
            if (!isPermissionGranted()) {
                requestPermissions()
            } else {
                try {
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        if (it != null) {
                            vm.locationUpdate(it)
                            textView.text = "Alt: ${it.altitude}, Long: ${it.longitude}, Lat: ${it.latitude}"
                        }
                    }
                } catch (e: SecurityException) {

                }
            }
        }
    }

    private fun requestPermissions() =
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, myLocationPermission), 1)

    private fun isPermissionGranted() =
        checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

}

class MainViewModel(
    val db: DatabaseReference
) : ViewModel() {

    fun locationUpdate(location: Location) {
        with(location) {
            db.child("visit").push().setValue(LocationRecord(altitude,longitude,latitude))
        }
    }

}

data class LocationRecord (
    val altitude: Double,
    val longitude: Double,
    val latitude: Double
)
