package se.umu.edmo0011.discgolftracker.composables.matchHistory

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MATCHES_KEY
import se.umu.edmo0011.discgolftracker.Match
import se.umu.edmo0011.discgolftracker.ScaffoldState

import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.composables.match.ScoreSheet
import se.umu.edmo0011.discgolftracker.composables.match.TAB_BAR_HEIGHT

@Composable
fun OldMatchScreen(navCon: NavController, pad: PaddingValues, scafState: ScaffoldState, date: Long)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}

    val list = SharedPreferencesHelper.getList<Match>(navCon.context, MATCHES_KEY)
    val match = list.find { it.date == date }

    if(match == null) {
        navCon.navigateUp()
        return
    }
    val height = LocalConfiguration.current.screenHeightDp.dp - pad.calculateBottomPadding() - pad.calculateTopPadding()
    ScoreSheet(holes = match.holes, labelWidth = 80, height = height)
}