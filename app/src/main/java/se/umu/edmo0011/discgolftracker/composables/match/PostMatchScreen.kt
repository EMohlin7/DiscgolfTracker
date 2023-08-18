package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.OngoingMatchGraph
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel

@Composable
fun PostMatchScreen(navCon: NavController, pad: PaddingValues, scafState: ScaffoldState)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}

    val model = navCon.currentBackStackEntry?.sharedViewModel<MatchViewModel>(
        navCon = navCon, graphRoute = OngoingMatchGraph.route) ?: return
    val height = LocalConfiguration.current.screenHeightDp.dp - pad.calculateBottomPadding() - pad.calculateTopPadding()
    ScoreSheet(holes = model.playedHoles, labelWidth = 80, height = height)
}