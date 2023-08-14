package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel

@Composable
fun NewMatchScreen(navCon: NavController)
{
    val model = navCon.currentBackStackEntry?.sharedViewModel<MatchViewModel>(navCon, MatchGraph.route) ?: return
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Button(onClick = { model.onNewMatch(navCon) }) {
            Text(text = "New Match")
        }
    }
}