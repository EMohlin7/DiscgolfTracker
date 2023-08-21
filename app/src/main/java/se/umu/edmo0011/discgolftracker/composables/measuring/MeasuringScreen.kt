package se.umu.edmo0011.discgolftracker.composables.measuring

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.composables.general.BigButton
import se.umu.edmo0011.discgolftracker.graphs.MeasureGraph
import se.umu.edmo0011.discgolftracker.viewModels.MeasureViewModel
import se.umu.edmo0011.discgolftracker.misc.sharedViewModel
import kotlin.math.roundToInt

@Composable
fun MeasuringScreen(navCon: NavController, scafState: ScaffoldState)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}
    val model = navCon.currentBackStackEntry?.sharedViewModel<MeasureViewModel>(navCon, MeasureGraph.route) ?: return

    Measurements(model.startPos, model.distance.roundToInt(), { model.navToSave(navCon) })
}



@Composable
fun Measurements(start: Location, distance: Int, onClickSave: ()->Unit)
{
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${distance}m", style = MaterialTheme.typography.displayLarge)
        Text(text = stringResource(id = R.string.measuring_text), style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.size(15.dp))
        BigButton(text = stringResource(id = R.string.measuring_button),
            textStyle = MaterialTheme.typography.displaySmall) {
            onClickSave.invoke()
        }
    }
}
