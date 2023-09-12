/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.my_watch_app.presentation

//import androidx.compose.material3.Surface
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
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

// TODO
    // Geo fencing
    // Compose to add toast
    // Make notification with message
    // Button to mark current location as hazardous
    // buttons to indicate sevearity

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*      geofencingClient = LocationServices.getGeofencingClient(this)*/
        setContent {
            SplashScreen()
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
fun SplashScreen() {
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
        navigateToDashboardScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navigateToDashboardScreen() {
    // Create a scroll state to manage the scroll position
    val scrollState = rememberScrollState()
    var isNavigateToHazard by remember { mutableStateOf(false) }
    /*  var showScreen2 by remember { mutableStateOf(false) }
      var showScreen3 by remember { mutableStateOf(false) }
  */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState) // Enable vertical scrolling
    ) {
        // Content goes here
        TopAppBar(
            modifier = Modifier.background(Color.Black),
            colors = TopAppBarDefaults.topAppBarColors(Color.Black),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon), // Replace with your image resource
                        contentDescription = null, // Provide a description if needed
                        modifier = Modifier
                            .size(120.dp)
                            .padding(start = 40.dp, end = 8.dp)

                    )
                }
            }
        )
        if (isNavigateToHazard) {
            navigateToReportHazards()
        } else {

            Button(
                onClick = { isNavigateToHazard = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.hazard_icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Report Hazard", style = TextStyle.Default)
                }
            }

            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.notifications),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Notifications", style = TextStyle.Default)
                }
            }

            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text("More", style = TextStyle.Default)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navigateToReportHazards() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .height(50.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Area 0000", modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(0.dp))

      //  LazyVerticalGrid(columns = 2, content = ContentPadding() )

    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
//    DashboardScreen(navController = null)
}
