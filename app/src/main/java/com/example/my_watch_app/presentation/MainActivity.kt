/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.my_watch_app.presentation

//import androidx.compose.material3.Surface

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
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
import com.example.my_watch_app.database.FireBaseDBManager
import com.example.my_watch_app.services.GeoLocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionCode = 1

// TODO
    // Geo fencing
    // Compose to add toast
    // Make notification with message
    // Button to mark current location as hazardous
    // buttons to indicate sevearity

    @OptIn(ExperimentalMaterial3Api::class)
    private val viewModel: MainActivityViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE
            ), locationPermissionCode
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.FOREGROUND_SERVICE
                ), locationPermissionCode
            )
        } else {
            launchService()
            FireBaseDBManager().getAllHazardLocations()
        }
    }

    private fun launchService() {
        val serviceIntent = Intent(this, GeoLocationServices::class.java)
        startForegroundService(serviceIntent)
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
                launchService()
            } else {
                Log.d("MainActivity", "onRequestPermissionsResult failed")
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

        composable("NavigateToNoticeScreen") {
            NavigateToNoticeScreen(navController)
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
            navController.navigate("NavigateToNoticeScreen")
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

private fun getNotificationData(): Pair<String, String>? {
    GeoLocationServices.lastKnownLocation?.let { location ->
        val hazardLoc = FireBaseDBManager.hazardPoints.find { locData ->
            val storedLoc = locData.location
            val radius: Long = locData.radius ?: 0
            val distance = (storedLoc?.distanceTo(location) ?: 0).toLong()
            distance < radius
        }
        if (hazardLoc != null) {
            // return  GeoLocationServices.lastKnownLocation
            return Pair("Caution", "Found ${hazardLoc.name} with severity ${hazardLoc.severity}")
        }
    }
    return null
}


data class GridItemData(val id: Int, val icon: Int, val title: String)


val hazardItems = mutableListOf(
    GridItemData(1, R.drawable.fall_hazard_button, "Fall"),
    GridItemData(2, R.drawable.button_fire, "Fire"),
    GridItemData(3, R.drawable.height_hazard_button, "Height"),
    GridItemData(4, R.drawable.electricity, "Electricity"),
    GridItemData(5, R.drawable.noise_hazard_button, "Noise"),
    GridItemData(6, R.drawable.manual_handling_button, "Manual Handling"),

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
fun NavigateToNoticeScreen(navController: NavHostController) {

    val items = remember {
        hazardItems
    }

    val notification = getNotificationData()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar()

        val modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.popBackStack()
            }
        if (notification == null) {
            Text(
                text = "There is no notification", modifier,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = notification.first ?: "", modifier,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = notification?.second ?: "", modifier,
                color = Color.White,
                textAlign = TextAlign.Center
            )
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
                    val hazardItem = hazardItems.find { it.id == hazard }
                    FireBaseDBManager().addHazard(
                        GeoLocationServices.lastKnownLocation!!,
                        "Hazard ${hazardItem?.title!!}",
                        hazardItem.title,
                        selectedOption.value,
                        {
                            onCreateHazard(hazard, iconId, areaId)
                        }) {

                    }
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
        }, style = TextStyle(fontWeight = FontWeight.Bold)
        )
        val hazardItem = hazardItems.find { it.id == hazard }
        Text(
            text = "Hazard '${hazardItem?.title}' is logged",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Just now",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        BackButton("Back", onClick)
        /*    BackButton( = "Just now",
                modifier = Modifier
                    .clickable {
                        onClick()
                    }
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )*/
    }
}

@Composable
fun ColumnScope.BackButton(text: String, listener: () -> Unit) {
    Button(
        onClick = listener,
        modifier = Modifier
            .padding(8.dp)
            .align(Alignment.CenterHorizontally)
            .size(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.DarkGray,
            contentColor = Color.White
        )
    ) {
        Text("Back", style = TextStyle.Default)
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
