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
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.my_watch_app.R
import com.example.my_watch_app.services.GeoLocationServices
import kotlinx.coroutines.delay

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
            AppNavigation()
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
        /*   GeofenceManager.getInstance(this).addGeoFence(
               13.200247,
               77.728224,
               40F,
               this
           )*/
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
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, // You should create a NavController
        startDestination = "splashScreen"
    ) {
        // Define your destinations using composable
        composable("splashScreen") {
            SplashScreen(navController)
        }
        composable("NavigateToDashboardScreen") {
            NavigateToDashboardScreen(navController)
        }
        composable("NavigateToReportHazards") {
            NavigateToReportHazards(navController)
        }
        composable(
            route = "NavigateToReportWetHazards/{hazardId}/{iconId}/{areaId}",
            arguments = listOf(navArgument("hazardId") {
                type = NavType.IntType
                defaultValue = 0
                nullable = false
            }, navArgument("areaId") {
                type = NavType.StringType
                defaultValue = ""
                nullable = false
            },
                navArgument("iconId") {
                    type = NavType.IntType
                    defaultValue = R.drawable.button_fire
                    nullable = false
                }
            )
        ) { navBackStackEntry ->
            val hazardId = navBackStackEntry.arguments?.getInt("hazardId", 0) ?: 0
            val areaId = navBackStackEntry.arguments?.getString("areaId", "") ?: ""
            val iconId = navBackStackEntry.arguments?.getInt("iconId", R.drawable.button_fire)
                ?: R.drawable.button_fire
            NavigateToReportWetHazards(
                navController,
                hazardId,
                iconId,
                areaId
            ) { hazard1, icon, areaId1 ->
                navController.navigate("NavigateToReportSummary/${hazard1}/${icon}/${areaId1}")
            }
        }

        composable(
            "NavigateToReportSummary/{hazardId}/{iconId}/{areaId}",
            arguments = listOf(navArgument("hazardId") {
                type = NavType.IntType
                defaultValue = 0
                nullable = false
            }, navArgument("areaId") {
                type = NavType.StringType
                defaultValue = ""
                nullable = false
            })
        ) { navBackStackEntry ->
            val hazardId = navBackStackEntry.arguments?.getInt("hazardId", 0) ?: 0
            val areaId = navBackStackEntry.arguments?.getString("areaId", "") ?: ""
            NavigateToReportSummary(navController, hazardId, areaId) {
                navController.popBackStack("NavigateToDashboardScreen", inclusive = false)
            }
        }
    }
}


@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(key1 = Unit) {
        delay(3000)
        navController.navigate("NavigateToDashboardScreen")
    }
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
}


@Composable
fun NavigateToDashboardScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
    ) {
        AppBar()
        DashboardButton(text = "Report Hazard", R.drawable.hazard_icon) {
            navController.navigate("NavigateToReportHazards")
        }

        DashboardButton(text = "Notifications", R.drawable.notifications) {

        }

        CircleButton(text = "More") {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        modifier = Modifier.background(Color.Black),
        colors = TopAppBarDefaults.topAppBarColors(Color.Black),
        title = {

        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)

                    //.padding(start = 40.dp, end = 8.dp)

                )
            }
        }
    )
}

@Composable
fun ColumnScope.CircleButton(text: String, listener: () -> Unit) {
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

@Composable
fun DashboardButton(text: String, iconId: Int, listener: () -> Unit) {
    Button(
        onClick = { listener() },
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
                painter = painterResource(id = iconId),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, style = TextStyle.Default)
        }
    }
}


data class GridItemData(val id: Int, val icon: Int, val title: String)


val hazardItems = mutableListOf(
    GridItemData(1, R.drawable.fall_hazard_button, "Fall Hazard"),
    GridItemData(2, R.drawable.button_fire, "Fire Hazard"),
    GridItemData(3, R.drawable.height_hazard_button, "Height Hazard"),
    GridItemData(4, R.drawable.electricity, "Electricity Hazard"),
    GridItemData(5, R.drawable.noise_hazard_button, "Noise Hazard"),
    GridItemData(6, R.drawable.manual_handling_button, "Manual Handling Hazard"),

    // Add more items here
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateToReportHazards(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val items = remember {
        hazardItems
    }
    val selectedOption: MutableState<String> = remember { mutableStateOf("Area 1") }
    val options = listOf("Area 1", "Area 2", "Area 3", "Area 4", "Area 5", "Area 6")
    val expanded = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar()

        AreaSelector(selectedOption, expanded, options)

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            GridCells.Fixed(3), // 2 items per row
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 40.dp, end = 40.dp)
        ) {
            items(items) { item ->
                GridItem(item) {
                    navController.navigate("NavigateToReportWetHazards/${item.id}/${item.icon}/${selectedOption.value}")
                }
            }
        }
    }
}

@Composable
fun NavigateToReportWetHazards(
    navController: NavHostController,
    hazard: Int,
    iconId: Int,
    areaId: String,
    onCreateHazard: (hazard: Int, iconId: Int, areaId: String) -> Unit
) {
    val scrollState = rememberScrollState()
    val selectedOption: MutableState<String> = remember { mutableStateOf("High") }
    val options = listOf("High", "Medium", "Low")
    val expanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.Black)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar()
        AreaSelector(selectedOption, expanded, options)
        Image(
            painter = painterResource(id = iconId), // Replace with your drawable resource ID
            contentDescription = null, // You can provide a content description if needed
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
            /*.clickable {
                onClick(hazard, iconId, areaId)
            } // Modify the layout using Modifier if needed*/
        )
        Icon(
            Icons.Default.CheckCircle, // Replace with your drawable resource ID
            contentDescription = null, // You can provide a content description if needed
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onCreateHazard(hazard, iconId, areaId)
                } // Modify the layout using Modifier if needed
        )
    }
}

private val onCreateHazard = {

}

@Composable
fun NavigateToReportSummary(
    navController: NavHostController,
    hazard: Int,
    areaId: String,
    onClick: () -> Unit
) {
    val selectedOption: MutableState<String> = remember { mutableStateOf("High") }
    val options = listOf("High", "Medium", "Low")
    val expanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        AppBar()
        Text(text = areaId, modifier = Modifier.clickable {
            onClick()
        })
        val hazardItem = hazardItems.find { it.id == hazard }
        Text(text = "${hazardItem?.title} is logged now",
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(30.dp)
        )
    }
}

@Composable
fun ColumnScope.AreaSelector(
    selectedOption: MutableState<String>,
    expanded: MutableState<Boolean>,
    options: List<String>
) {
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .height(20.dp)
            .background(color = Color.Transparent, shape = RoundedCornerShape(4.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = !expanded.value },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = selectedOption.value, color = Color.White)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .padding(16.dp),
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOption.value = option
                    expanded.value = false
                },
                    text = {
                        Text(
                            text = option,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    })

            }
        }
    }
}

@Composable
fun GridItem(item: GridItemData, onClick: () -> Unit) {
    IconButton(
        onClick = {
            onClick()
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
