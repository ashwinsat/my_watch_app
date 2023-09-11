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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import com.example.my_watch_app.geofenceHelper.GeofenceManager
import com.example.my_watch_app.network.NetworkManager
import com.example.my_watch_app.network.SampleResponse
import com.example.my_watch_app.presentation.theme.My_watch_appTheme
import com.example.my_watch_app.services.GeoLocationServices
import kotlinx.coroutines.delay

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
fun SplashScreen() {
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = isLoading) {
        delay(3000)
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        NavigateToDashboardScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateToDashboardScreen() {
    val scrollState = rememberScrollState()
    var isNavigateToHazard by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
    ) {
        TopAppBar(
            modifier = Modifier.background(Color.Black),
            colors = TopAppBarDefaults.topAppBarColors(Color.Black),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(start = 40.dp, end = 8.dp)

                    )
                }
            }
        )
        if (isNavigateToHazard) {
            NavigateToReportHazards()
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
                onClick = { },
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
                onClick = { },
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


data class GridItemData(val id: Int, val icon: Int, val title: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateToReportHazards() {
    val scrollState = rememberScrollState()
    val items = remember {
        mutableListOf(
            GridItemData(1, R.drawable.slippery_button_icon, "Hazard Slippery"),
            GridItemData(2, R.drawable.button_fire, "Fire Hazard"),
            GridItemData(3, R.drawable.button_hazard, " Hazard"),
            GridItemData(4, R.drawable.button_record, " Hazard"),
            // Add more items here
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Area 0000", modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            GridCells.Fixed(2), // 2 items per row
            modifier = Modifier.fillMaxHeight()
                .padding(start = 54.dp, end = 54.dp)
        ) {
            items(items) { item ->
                GridItem(item)
            }
        }

    }
}

@Composable
fun GridItem(item: GridItemData) {
    IconButton(
        onClick = {
            // Add your click action here
        },
        modifier = Modifier
            .padding(top = 8.dp)
            .size(32.dp)
        ) {
        Image(
            painter = painterResource(id = item.icon), // Replace with your custom icon
            contentDescription = null,
        )
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
//    DashboardScreen(navController = null)
}
