package dev.ahmedmourad.composedestinationscrash

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.spec.NavGraphSpec

enum class HomeBottomTabs(
    val graph: NavGraphSpec,
    val icon: ImageVector,
    val label: String
) {
    Discover(NavGraphs.discover, Icons.Default.Home, "Discover"),
    Calender(NavGraphs.calender, Icons.Default.DateRange, "Calender"),
    Map(NavGraphs.map, Icons.Default.AccountBox, "Map"),
    Profile(NavGraphs.profile, Icons.Default.AccountCircle, "Profile")
}

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val selectedItem by navController.currentScreenAsState()
            BottomBar(
                selected = selectedItem,
                onSelect = { selected ->
                    navController.navigateTo(selected) {
                        launchSingleTop = true
                        restoreState = true

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }, modifier = Modifier
    ) {
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.root,
            modifier = Modifier
        )
    }
}

@Composable
fun BottomBar(
    selected: NavGraphSpec,
    onSelect: (NavGraphSpec) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomNavigation(modifier = modifier) {
        HomeBottomTabs.values().forEach { destination ->
            BottomNavigationItem(
                selected = selected == destination.graph,
                onClick = { onSelect(destination.graph) },
                icon = { Icon(destination.icon, contentDescription = destination.label)},
                label = { Text(destination.label) },
            )
        }
    }
}

@Composable
private fun NavController.currentScreenAsState(): State<NavGraphSpec> {
    val selectedItem = remember { mutableStateOf<NavGraphSpec>(NavGraphs.discover) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.navGraph() ?: selectedItem.value
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

fun NavDestination.navGraph(): NavGraphSpec? {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }

    return null //the following exceptions is always triggered for "home_screen" when the app is launched
    throw RuntimeException("Unknown nav graph for destination $route")
}
