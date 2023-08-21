package se.umu.edmo0011.discgolftracker.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.misc.ScreenState
import se.umu.edmo0011.discgolftracker.composables.general.Screen
import se.umu.edmo0011.discgolftracker.composables.match.MatchScreen
import se.umu.edmo0011.discgolftracker.composables.match.NewMatchScreen
import se.umu.edmo0011.discgolftracker.composables.match.SetupMatchScreen
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.dataClasses.TopBarState

class MatchGraph
{
    companion object
    {
        const val route: String = "match graph"
    }
    object NewMatch : ScreenState("new match", ScaffoldState(topBar = TopBarState(title = R.string.app_name)))

}

class OngoingMatchGraph
{
    companion object
    {
        const val route: String = "ongoing match graph"
    }
    object SetupMatch : ScreenState("Setup match", ScaffoldState(
        topBar = TopBarState(title = R.string.title_setup_match, topNavIcon = Icons.Default.ArrowBack)
    )
    )
    object Match : ScreenState("match", ScaffoldState(topBar = TopBarState(
        title = R.string.title_match,
        topNavIcon = Icons.Default.Close,
        actionIcons = List(1){ Icons.Default.Check})
    )
    )

    object PostMatch : ScreenState("post match screen", ScaffoldState(topBar = TopBarState(
        title = R.string.title_match,
        topNavIcon = Icons.Default.ArrowBack)
    )
    )
}

fun NavGraphBuilder.matchGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
{
    navigation(startDestination = MatchGraph.NewMatch.route, route = MatchGraph.route) {

        composable(MatchGraph.NewMatch.route){Screen(MatchGraph.NewMatch, pad, scafState){
            NewMatchScreen(navCon = navCon)
        } }

        ongoingMatchGraph(navCon, pad, scafState)
    }
}

fun NavGraphBuilder.ongoingMatchGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
{
    navigation(startDestination = OngoingMatchGraph.SetupMatch.route, route = OngoingMatchGraph.route) {
        composable(OngoingMatchGraph.SetupMatch.route){Screen(OngoingMatchGraph.SetupMatch, pad, scafState) {
            SetupMatchScreen(navCon = navCon, scafState.value)
        }}
        composable(OngoingMatchGraph.Match.route){Screen(OngoingMatchGraph.Match, pad, scafState) {
            //Disable the back button when in a match
            BackHandler(enabled = true) {}

            MatchScreen(navCon = navCon, scafState.value, pad)
        }}
    }
}