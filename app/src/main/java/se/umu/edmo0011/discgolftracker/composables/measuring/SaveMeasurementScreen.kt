package se.umu.edmo0011.discgolftracker.composables.measuring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MeasureGraph
import se.umu.edmo0011.discgolftracker.viewModels.MeasureViewModel
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.composables.general.TextInput
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



    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        TextInput(value = "${distance}m", enabled = false, onValueChange = {},
            label = {Text(text = stringResource(id = R.string.Distance))},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.size(20.dp))

        SaveThrowInput(value = disc,
            label = stringResource(id = R.string.Disc),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)){
            disc = it
        }

        val courseAction = if(!course.isNullOrEmpty()) ImeAction.Next else ImeAction.Done
        SaveThrowInput(value = course,
            label = stringResource(id = R.string.Course),
            keyboardOptions = KeyboardOptions(imeAction = courseAction)){
            course = it
        }

        SaveThrowInput(value = hole,
            label = stringResource(id = R.string.Hole),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            enabled = !course.isNullOrEmpty()){
            hole = it
        }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = { onSave.invoke(distance, disc, course, hole) }) {
            Text(text = stringResource(id = R.string.Save), style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SaveThrowInput(value: String, label: String, keyboardOptions: KeyboardOptions, enabled: Boolean = true,
                   onValueChanged: (String)->Unit)
{
    @Composable
    fun optional()
    {
        Text(text = "(" +stringResource(id = R.string.optional)+")")
    }

    TextInput(value = value, onValueChange = onValueChanged,
        label = { Text(text = label)},
        supportingText = {optional()},
        keyboardOptions = keyboardOptions,
        enabled = enabled
    )
    Spacer(modifier = Modifier.size(20.dp))
}

@Composable
@Preview
fun SaveMeasurementFormPrev()
{
    SaveMeasurementForm(distance = 100, onSave = { _,_,_,_->})
}