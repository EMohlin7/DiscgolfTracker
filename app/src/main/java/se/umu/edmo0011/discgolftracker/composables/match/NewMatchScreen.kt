package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.OngoingMatchGraph
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.composables.general.BigButton

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
        /*Button(
            modifier = Modifier.size(300.dp, 150.dp),
            shape = RoundedCornerShape(30.dp),
            onClick = {  }) {
            Text(text = stringResource(id = R.string.New_match), style = MaterialTheme.typography.displayMedium)
        }*/
    }
}