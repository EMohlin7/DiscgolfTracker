package se.umu.edmo0011.discgolftracker.composables.measuring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MeasureGraph
import se.umu.edmo0011.discgolftracker.viewModels.MeasureViewModel
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.sharedViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveMeasurementScreen(navCon: NavController, scafState: ScaffoldState)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}

    val model = navCon.currentBackStackEntry?.sharedViewModel<MeasureViewModel>(navCon, MeasureGraph.route) ?: return

    SaveMeasurementForm(model.distance.roundToInt()){distance, disc, course, hole ->
        model.saveMeasurment(navCon, distance, disc, course, hole)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveMeasurementForm(distance: Int, onSave: (distance: Int, disc: String, course: String, hole: String)->Unit)
{
    var disc by rememberSaveable { mutableStateOf("") }
    var course by rememberSaveable { mutableStateOf("") }
    var hole by rememberSaveable { mutableStateOf("") }

    @Composable
    fun optional()
    {
        Text(text = "(" +stringResource(id = R.string.optional)+")")
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)){
            TextField(value = "$distance", readOnly = true, onValueChange = {},
                label = {Text(text = stringResource(id = R.string.Distance)
                )}
            )

            TextField(value = "$disc", onValueChange = {disc = it},
                label = { Text(text = stringResource(id = R.string.Disc))},
                supportingText = {optional()}
            )

            TextField(value = "$course", onValueChange = {course = it},
                label = { Text(text = stringResource(id = R.string.Course))},
                supportingText = {optional()}
            )

            TextField(value = "$hole", onValueChange = {hole = it},
                label = { Text(text = stringResource(id = R.string.Hole))},
                supportingText = {optional()},
                enabled = !course.isNullOrEmpty()
            )
        }

        Spacer(modifier = Modifier.padding(20.dp))

        Button(onClick = { onSave.invoke(distance, disc, course, hole) }) {
            Text(text = stringResource(id = R.string.Save))
        }
    }
}