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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.HistoryGraph
import se.umu.edmo0011.discgolftracker.Match
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.composables.measuring.AppListItem
import se.umu.edmo0011.discgolftracker.formatDateMs
import se.umu.edmo0011.discgolftracker.formatDurationMs
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun MatchHistoryScreen(navCon: NavController)
{
    val model = navCon.currentBackStackEntry?.sharedViewModel<HistoryViewModel>(navCon, HistoryGraph.route) ?: return
    var list by remember { mutableStateOf(model.loadMatches(navCon.context)) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(ScrollableState { 0f }, Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        for (m in list.sortedByDescending { it.dateMs }) {
            this.item {
                MatchHistoryListItem(match = m, onClick =  { navCon.navigate(HistoryGraph.Match.route + "/${m.dateMs}") },
                    onDelete = {list = model.deleteMatch(navCon.context, m.dateMs)}
                )
            }
        }
    }
}

@Composable
fun MatchHistoryListItem(match: Match, onClick: ()->Unit, onDelete: ()->Unit)
{
    val date = formatDateMs(match.dateMs)
    val course = stringResource(id = R.string.Course)+": ${match.course}"


    val dur = formatDurationMs(match.duration)
    val duration = stringResource(id = R.string.Duration)+": $dur"

    var players = stringResource(id = R.string.Players)+": "
    //Don't add colon after last player
    for(i in 0 until match.holes[0].players.size-1)
        players += "${match.holes[0].players[i]}, "
    players += match.holes[0].players[match.holes[0].players.size-1]

    val items = listOf(course, duration, players)
    Box(modifier = Modifier.clickable { onClick.invoke() }){
        AppListItem(title = date.toString(), items = items, itemsPerRow = 1, onDelete = onDelete)
    }
    Spacer(Modifier.size(2.dp))
}

