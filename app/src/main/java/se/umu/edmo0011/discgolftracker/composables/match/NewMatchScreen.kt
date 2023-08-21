package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.misc.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.composables.general.BigButton
import se.umu.edmo0011.discgolftracker.graphs.MatchGraph

@Composable
fun NewMatchScreen(navCon: NavController)
{
    val model = navCon.currentBackStackEntry?.sharedViewModel<MatchViewModel>(navCon, MatchGraph.route) ?: return
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        BigButton(text = stringResource(id = R.string.New_match), MaterialTheme.typography.displayMedium) {
            model.onNewMatch(navCon)
        }
    }
}