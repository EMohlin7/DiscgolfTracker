package se.umu.edmo0011.discgolftracker.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.misc.ScreenState
import se.umu.edmo0011.discgolftracker.composables.general.Screen
import se.umu.edmo0011.discgolftracker.composables.matchHistory.MatchHistoryScreen
import se.umu.edmo0011.discgolftracker.composables.matchHistory.OldMatchScreen
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.dataClasses.TopBarState

class HistoryGraph
{
    companion object
    {
        const val route: String = "history graph"
    }
    object MatchHistory : ScreenState("match history", ScaffoldState(topBar = TopBarState(title = R.string.title_history_screen)))
    object Match : ScreenState("old match score", ScaffoldState(
        topBar = TopBarState(title = R.string.title_history_screen, topNavIcon = Icons.Default.ArrowBack)
    )
    )
}

fun NavGraphBuilder.historyGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
{
    navigation(startDestination = HistoryGraph.MatchHistory.route, route = HistoryGraph.route) {
        composable(HistoryGraph.MatchHistory.route,){Screen(HistoryGraph.MatchHistory, pad, scafState) {
            //Disable back button
            BackHandler(enabled = true) {}
            MatchHistoryScreen(navCon = navCon)
        }}

        composable(HistoryGraph.Match.route+"/{date}",
            arguments = listOf(navArgument("date") { type = NavType.LongType })
        ){ backStackEntry->
            Screen(HistoryGraph.Match, pad, scafState) {
                OldMatchScreen(navCon = navCon, pad = pad, scafState.value, date = backStackEntry.arguments?.getLong("date")?:0)
            }
        }
    }
}