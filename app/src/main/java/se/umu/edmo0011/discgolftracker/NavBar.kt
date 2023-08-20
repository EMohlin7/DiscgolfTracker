package se.umu.edmo0011.discgolftracker

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState




@Composable
fun NavBar(navCon: NavController)
{
    val c = LocalContext.current
    val navBackStackEntry by navCon.currentBackStackEntryAsState()
    val curDest = navBackStackEntry?.destination

    NavigationBar(){
        val matchSelected = curDest?.hierarchy?.any{it.route == MatchGraph.route} ?: false
        NavigationBarItem(
            selected = matchSelected,
            onClick =
            {
                navBarNavigate(navCon, MatchGraph.route)
            },
            icon = { val icon = if(matchSelected) Icons.Filled.PlayArrow else Icons.Outlined.PlayArrow
                Icon(icon, contentDescription = null) },
            label = { Text(text = stringResource(id = R.string.Match))}
        )

        val historySelected = curDest?.hierarchy?.any{it.route == HistoryGraph.route} ?: false
        NavigationBarItem(
            selected = historySelected,
            onClick =
            {
                navBarNavigate(navCon, HistoryGraph.route)
            },
            icon = { val icon = if(historySelected) Icons.Filled.Info else Icons.Outlined.Info
                Icon(icon, contentDescription = null) },
            label = { Text(text = stringResource(id = R.string.History))}
        )

        val measureSelected = curDest?.hierarchy?.any{it.route == MeasuredThrowsGraph.route} ?: false
        NavigationBarItem(
            selected = measureSelected,
            onClick =
            {
                navBarNavigate(navCon, MeasuredThrowsGraph.route)
            },
            icon = { val icon = if(measureSelected) Icons.Filled.Send else Icons.Outlined.Send
                Icon(icon, contentDescription = null) },
            label = { Text(text = stringResource(id = R.string.Throws))}
        )
    }
}


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