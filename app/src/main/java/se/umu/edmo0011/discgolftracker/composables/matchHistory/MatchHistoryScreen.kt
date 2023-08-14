package se.umu.edmo0011.discgolftracker.composables.matchHistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.HistoryGraph
import se.umu.edmo0011.discgolftracker.MATCHES_KEY
import se.umu.edmo0011.discgolftracker.Match
import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.composables.measuring.AppListItem

@Composable
fun MatchHistoryScreen(navCon: NavController)
{
    val list = SharedPreferencesHelper.getList<Match>(navCon.context, MATCHES_KEY)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(ScrollableState { 0f }, Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        for (m in list) {
            this.item {
                MatchHistoryListItem(match = m) {
                    navCon.navigate(HistoryGraph.Match.route + "/${m.date}")
                }
            }
        }
    }
}

@Composable
fun MatchHistoryListItem(match: Match, onClick: ()->Unit)
{
    Box(modifier = Modifier.clickable { onClick.invoke() }){
        AppListItem(title = "Match: ${match.date}", items = match.holes[0].players, itemsPerRow = 2)
    }
    Spacer(Modifier.size(2.dp))
}