package se.umu.edmo0011.discgolftracker.composables.general

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.graphs.HistoryGraph
import se.umu.edmo0011.discgolftracker.graphs.MatchGraph
import se.umu.edmo0011.discgolftracker.graphs.MeasuredThrowsGraph


@Composable
fun NavBar(navCon: NavController)
{
    NavigationBar(){

        NavBarItem(navCon = navCon, route = MatchGraph.route, label = stringResource(id = R.string.Match),
            selectedIcon = Icons.Filled.PlayArrow, notSelectedIcon = Icons.Outlined.PlayArrow)

        NavBarItem(navCon = navCon, route = HistoryGraph.route, label = stringResource(id = R.string.History),
            selectedIcon = Icons.Filled.Info, notSelectedIcon = Icons.Outlined.Info)

        NavBarItem(navCon = navCon, route = MeasuredThrowsGraph.route, label = stringResource(id = R.string.Throws),
            selectedIcon = Icons.Filled.Send, notSelectedIcon = Icons.Outlined.Send)
    }
}

@Composable
fun RowScope.NavBarItem(navCon: NavController, route: String, label: String,
                        selectedIcon: ImageVector, notSelectedIcon: ImageVector)
{
    val navBackStackEntry by navCon.currentBackStackEntryAsState()
    val curDest = navBackStackEntry?.destination
    val selected = curDest?.hierarchy?.any{it.route == route } ?: false
    NavigationBarItem(
        selected = selected,
        onClick = { navBarNavigate(navCon, route) },
        icon = { val icon = if(selected) selectedIcon else notSelectedIcon
            Icon(icon, contentDescription = null) },
        label = { Text(text = label)}
    )
}


//Shamelessly copied from Google's tutorial on Compose navigation
fun navBarNavigate(navCon: NavController, route: String)
{
    navCon.navigate(route){
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navCon.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = route != MeasuredThrowsGraph.route
    }
}