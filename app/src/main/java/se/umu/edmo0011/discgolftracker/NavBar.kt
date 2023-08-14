package se.umu.edmo0011.discgolftracker

import android.content.Intent
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
        NavigationBarItem(
            selected = curDest?.hierarchy?.any{it.route == MatchGraph.route} == true,
            onClick =
            {
                navCon.navigate(MatchGraph.route){
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
                    restoreState = true
                }
            },
            icon = { /*TODO*/ },
            label = { Text(text = "Ga")}
        )

        NavigationBarItem(
            selected = curDest?.hierarchy?.any{it.route == HistoryGraph.route} == true,
            onClick =
            {
                navCon.navigate(HistoryGraph.route){
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
                    restoreState = true
                }
            },
            icon = { /*TODO*/ },
            label = { Text(text = "His")}
        )

        NavigationBarItem(
            selected = curDest?.hierarchy?.any{it.route == MeasuredThrowsGraph.route} == true,
            onClick =
            {
                navCon.navigate(MeasuredThrowsGraph.route){
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
                    restoreState = false
                }
            },
            icon = { /*TODO*/ },
            label = { Text(text = "Measure")}
        )
    }
}