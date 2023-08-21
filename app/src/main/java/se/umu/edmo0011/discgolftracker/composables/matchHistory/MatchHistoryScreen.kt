package se.umu.edmo0011.discgolftracker.composables.matchHistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.dataClasses.Match
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.composables.general.AppList
import se.umu.edmo0011.discgolftracker.composables.general.AppListItem
import se.umu.edmo0011.discgolftracker.misc.formatDateMs
import se.umu.edmo0011.discgolftracker.misc.formatDurationMs
import se.umu.edmo0011.discgolftracker.graphs.HistoryGraph
import se.umu.edmo0011.discgolftracker.misc.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.HistoryViewModel

@Composable
fun MatchHistoryScreen(navCon: NavController)
{
    val model = navCon.currentBackStackEntry?.sharedViewModel<HistoryViewModel>(navCon, HistoryGraph.route) ?: return
    var list by remember { mutableStateOf(model.loadMatches(navCon.context)) }

    val content = MatchHistoryListContent(list = list,
        onClick = { navCon.navigate(HistoryGraph.Match.route + "/${it.dateMs}") }) {
        list = model.deleteMatch(navCon.context, it.dateMs)
    }
    AppList(content = content)
}

@Composable
fun MatchHistoryListContent(list: List<Match>, onClick: (Match) -> Unit, onDelete: (Match) -> Unit): List<@Composable ()->Unit>
{
    val content = mutableListOf<@Composable ()->Unit>()
    for (m in list.sortedByDescending { it.dateMs }) {
        content.add {
            MatchHistoryListItem(
                match = m,
                onClick = {onClick.invoke(m)},
                onDelete = { onDelete.invoke(m) }
            )
        }
    }
    return content
}

@Composable
fun MatchHistoryListItem(match: Match, onClick: ()->Unit, onDelete: ()->Unit)
{
    val date = formatDateMs(match.dateMs)
    val labels = listOf(
        stringResource(id = R.string.Course),
        stringResource(id = R.string.Players),
        stringResource(id = R.string.Duration)
    )

    val duration = formatDurationMs(match.duration)
    var players = ""

    //Don't add colon after last player
    for(i in 0 until match.holes[0].players.size-1)
        players += "${match.holes[0].players[i]}, "
    players += match.holes[0].players[match.holes[0].players.size-1]

    val items = listOf(match.course, players, duration)
    Box(modifier = Modifier.clickable { onClick.invoke() }){
        AppListItem(title = date, items = items, labels = labels, itemsPerRow = 1, onDelete = onDelete)
    }
}

