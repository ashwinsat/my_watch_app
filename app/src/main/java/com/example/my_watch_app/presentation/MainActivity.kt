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
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.my_watch_app.R
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
import kotlinx.coroutines.delay
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
            My_watch_appTheme {
                // Initialize the NavController
                val navController = rememberNavController()

                // Define the NavHost for navigation
                NavHost(navController = navController, startDestination = "splash_screen") {
                    composable("splash_screen") {
                        // Display the splash screen
                        SplashScreen(navController = navController)
                    }
                    composable("main_screen") {
                        // Display the main screen content
                        DashboardScreen(navController = navController)
                        // WearApp("Android")
                    }
                }
            }
            // WearApp("Android")
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

@Composable
fun SplashScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }

    // Simulate loading, replace this with your actual loading logic
    LaunchedEffect(key1 = isLoading) {
        delay(3000) // Simulate a 3-second loading time
        isLoading = false
    }

    if (isLoading) {
        // Show the splash screen while loading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            // Use your app's icon as the image in the splash screen
            Image(
                painter = painterResource(id = R.drawable.app_icon), // Replace with your app's icon resource
                contentDescription = null, // Provide a description if needed
                modifier = Modifier.fillMaxSize()// Adjust the size as needed
            )
        }
    } else {
        // Loading is complete, navigate to the main screen or any other destination
        navController.navigate("main_screen") // Replace with your desired destination
    }
}

@Composable
fun DashboardScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    // Handle button click
                }
            ) {
                Text("Button 1")
            }

            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    // Handle button click
                }
            ) {
                Text("Button 2")
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
