/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.my_watch_app.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.my_watch_app.R
import com.example.my_watch_app.geofenceHelper.GeofenceManager
import com.example.my_watch_app.network.NetworkManager
import com.example.my_watch_app.network.SampleResponse
import com.example.my_watch_app.presentation.theme.My_watch_appTheme
import com.example.my_watch_app.services.GeoLocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    /*    private lateinit var geofencingClient: GeofencingClient
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var locationRequest: LocationRequest*/
    private val locationPermissionCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*      geofencingClient = LocationServices.getGeofencingClient(this)*/
        setContent {
            WearApp("Android")
        }

        /*        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                locationRequest = LocationRequest()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.interval = 10000 // 10 seconds*/

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.FOREGROUND_SERVICE
                ), locationPermissionCode
            )
        } else {
// requestLocationUpdates()
            launchService()
        }
// fetchDataAndHandleResponse()
        GeofenceManager.getInstance(this).addGeoFence(
            13.200247,
            77.728224,
            40F,
            this
        )
    }

    private fun launchService() {
        val serviceIntent = Intent(this, GeoLocationServices::class.java)
        startForegroundService(serviceIntent)
    }

    /*    private fun fetchDataAndHandleResponse(): SampleResponse? {
            var data: SampleResponse? = null
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    try {
                        withContext(Dispatchers.IO) {
                            val networkManager = NetworkManager()
                            data = networkManager.fetchDataFromServer()
                        }
                        // Process the data as needed
                        // You can update your UI or perform other actions here
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "After fetchDataFromServer , ${data?.brand}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "Exception occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            return data
        }*/

    /*    override fun onPause() {
            super.onPause()
            locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        }

        override fun onResume() {
            super.onResume()
            requestLocationUpdates()
        }*/
    /*
        private fun stopLocationUpdates() {
            locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        }
    */

    private var isLocationCallbackInitialized = false
    /*    private var locationCallback: LocationCallback? = null*/

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
//        if (isLocationCallbackInitialized) {
//            Log.d("Watch", "isLocationCallbackInitialized already initialized")
//            return
//        }
//        isLocationCallbackInitialized = true
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult.lastLocation?.let { location ->
//                    // Handle the location update here
//                    val latitude = location.latitude
//                    val longitude = location.longitude
//                    // Do something with the latitude and longitude
//                    Log.d("Watch", "Location update received $latitude  $longitude")
//                }
//            }
//
//            override fun onLocationAvailability(p0: LocationAvailability) {
//                super.onLocationAvailability(p0)
//            }
//        }
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback as LocationCallback, null
//        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                requestLocationUpdates()
            } else {
                // Handle permission denied
            }
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    My_watch_appTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
