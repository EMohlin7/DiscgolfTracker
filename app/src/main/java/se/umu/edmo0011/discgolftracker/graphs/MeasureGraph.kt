package se.umu.edmo0011.discgolftracker.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.misc.ScreenState
import se.umu.edmo0011.discgolftracker.composables.general.Screen
import se.umu.edmo0011.discgolftracker.composables.measuring.MeasuredThrowsScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.MeasuringScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.SaveMeasurementScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.StartMeasureScreen
import se.umu.edmo0011.discgolftracker.dataClasses.FabState
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.dataClasses.TopBarState

class MeasuredThrowsGraph
{
    companion object
    {
        const val route: String = "measured throws graph"
    }

    object Throws: ScreenState("measured throws",
        ScaffoldState(
            fab = FabState(Icons.Default.Add),
            topBar = TopBarState(R.string.title_measured_throws_screen)
        )
    )
}

class MeasureGraph()
{
    companion object
    {
        const val route: String = "measure graph"
    }

    object StartMeasure: ScreenState("start measure", ScaffoldState(
        topBar = TopBarState(title = R.string.title_start_measure_screen, topNavIcon = Icons.Default.ArrowBack)
    )
    )
    object Measuring : ScreenState("measuring", ScaffoldState(
        topBar = TopBarState(title = R.string.title_measuring_screen, topNavIcon = Icons.Default.ArrowBack)
    )
    )
    object Save : ScreenState("save measurement", ScaffoldState(
        topBar = TopBarState(title = R.string.title_save_throw, topNavIcon = Icons.Default.ArrowBack)
    )
    )
}

fun NavGraphBuilder.measuredThrowsGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
{
    navigation(startDestination = MeasuredThrowsGraph.Throws.route, route = MeasuredThrowsGraph.route){
        composable(MeasuredThrowsGraph.Throws.route){Screen(MeasuredThrowsGraph.Throws, pad, scafState){
            //Disable back button
            BackHandler(enabled = true) {}
            MeasuredThrowsScreen(navCon = navCon, scafState.value)
        } }

        measureGraph(navCon, pad, scafState)
    }
}

fun NavGraphBuilder.measureGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
{
    navigation(startDestination = MeasureGraph.StartMeasure.route, route = MeasureGraph.route){
        composable(MeasureGraph.StartMeasure.route){Screen(MeasureGraph.StartMeasure, pad, scafState){
            StartMeasureScreen(navCon = navCon, scafState.value)
        } }

        composable(MeasureGraph.Measuring.route){Screen(MeasureGraph.Measuring, pad, scafState){
            MeasuringScreen(navCon = navCon, scafState.value)
        } }
        composable(MeasureGraph.Save.route){Screen(MeasureGraph.Save, pad, scafState) {
            SaveMeasurementScreen(navCon = navCon, scafState.value)
        }}
    }
}