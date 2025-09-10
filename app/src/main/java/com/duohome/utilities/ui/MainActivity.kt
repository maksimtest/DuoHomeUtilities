package com.duohome.utilities.ui


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.duohome.utilities.ui.home.HomeScreen
import com.duohome.utilities.ui.utility.UtilityScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppScaffold() }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val nav = rememberNavController()

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("DuoHome Utilities") }) },
            // чтобы не получить двойные инсет-пэддинги
            contentWindowInsets = WindowInsets(0)
        ) { innerPadding ->
            Surface( // даёт фон и гарантирует измерение
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    navController = nav,
                    startDestination = "homes",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("homes") {
                        HomeScreen(
                            onOpenUtilities = { homeId -> nav.navigate("utilities/$homeId") }
                        )
                    }
                    composable(
                        route = "utilities/{homeId}",
                        arguments = listOf(navArgument("homeId") { type = NavType.LongType })
                    ) { backStack ->
                        val homeId = backStack.arguments?.getLong("homeId") ?: 0L
                        UtilityScreen(homeId = homeId, onBack = { nav.popBackStack() })
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold1() {
    MaterialTheme {
        val nav = rememberNavController()
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("DuoHome Utilities") })
            }
        ) { padding ->
            NavHost(navController = nav, startDestination = "homes") {
                composable("homes") {
                    HomeScreen(
                        onOpenUtilities = { homeId -> nav.navigate("utilities/$homeId") }
                    )
                }
                composable(
                    route = "utilities/{homeId}",
                    arguments = listOf(navArgument("homeId") { type = NavType.LongType })
                ) { backStack ->
                    val homeId = backStack.arguments?.getLong("homeId") ?: 0L
                    UtilityScreen(homeId = homeId, onBack = { nav.popBackStack() })
                }
            }
        }
    }
}
