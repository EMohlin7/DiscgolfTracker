package se.umu.edmo0011.discgolftracker.composables.matchHistory

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState

import se.umu.edmo0011.discgolftracker.composables.match.ScoreSheet
import se.umu.edmo0011.discgolftracker.graphs.HistoryGraph
import se.umu.edmo0011.discgolftracker.misc.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.HistoryViewModel

@Composable
fun OldMatchScreen(navCon: NavController, pad: PaddingValues, scafState: ScaffoldState, date: Long)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}

    val model = navCon.currentBackStackEntry?.sharedViewModel<HistoryViewModel>(navCon, HistoryGraph.route) ?: return

    //Check if the match exist in the already loaded matches, else load matches again and check
    val match = model.loadedMatches.find { it.dateMs == date } ?: model.loadMatches(navCon.context).find { it.dateMs == date }

    if(match == null) {
        navCon.navigateUp()
        return
    }
    val height = LocalConfiguration.current.screenHeightDp.dp - pad.calculateBottomPadding() - pad.calculateTopPadding()
    ScoreSheet(holes = match.holes, labelWidth = 80, height = height)
}