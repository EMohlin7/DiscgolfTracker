package se.umu.edmo0011.discgolftracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

/*
Help function to get a shared view model for every composable in a subgraph. Shamelessly copied
from https://www.youtube.com/watch?v=FIEnIBq7Ups&t=702s&ab_channel=PhilippLackner
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navCon: NavController, graphRoute: String?) : T?
{
    //I added this part to prevent it returning a new view model when navigating away from a screen
    if(destination.parent?.route != graphRoute)
        return null

    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navCon.getBackStackEntry(navGraphRoute)
    }

    return viewModel(parentEntry)
}


fun NavController.navigateAndPopUp(route: String, inclusive: Boolean)
{
    this.navigateAndPopUp(route, route, inclusive)
}

fun NavController.navigateAndPopUp(route: String, popUpTo: String, inclusive: Boolean)
{
    navigate(route){
        this.popUpTo(popUpTo){
            this.inclusive = inclusive
        }
    }
}