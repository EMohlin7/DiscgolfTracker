package se.umu.edmo0011.discgolftracker.composables.measuring

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MeasureGraph
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.viewModels.MeasureViewModel
import se.umu.edmo0011.discgolftracker.sharedViewModel
import kotlin.math.roundToInt

@Composable
fun MeasuringScreen(navCon: NavController, scafState: ScaffoldState)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}
    val context = navCon.context
    val model = navCon.currentBackStackEntry?.sharedViewModel<MeasureViewModel>(navCon, MeasureGraph.route) ?: return
    Log.w("Location", "Compose Measurer Screen")

    Measurements(model.startPos, model.distance.roundToInt(), { model.navToSave(navCon) })
}



@Composable
fun Measurements(start: Location, distance: Int, onClickSave: ()->Unit)
{
    Column {
        Text(text = "Lat: ${start.latitude}, Lon: ${start.longitude}")
        Text(text = "Distance: $distance")
        Button(onClick = onClickSave){
            Text(text = "Save")
        }
    }

}
