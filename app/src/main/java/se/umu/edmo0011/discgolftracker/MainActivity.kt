package se.umu.edmo0011.discgolftracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.compose.DiscgolfTrackerTheme
import se.umu.edmo0011.discgolftracker.composables.general.Fab
import se.umu.edmo0011.discgolftracker.composables.general.TopBar
import se.umu.edmo0011.discgolftracker.composables.match.MatchScreen
import se.umu.edmo0011.discgolftracker.composables.match.NewMatchScreen
import se.umu.edmo0011.discgolftracker.composables.match.PostMatchScreen
import se.umu.edmo0011.discgolftracker.composables.match.SetupMatchScreen
import se.umu.edmo0011.discgolftracker.composables.matchHistory.MatchHistoryScreen
import se.umu.edmo0011.discgolftracker.composables.matchHistory.OldMatchScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.MeasuredThrowsScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.MeasuringScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.SaveMeasurementScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.StartMeasureScreen

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //SharedPreferencesHelper.saveList<Match>(this, emptyList(), MATCHES_KEY)
        //SharedPreferencesHelper.saveList<Match>(this, emptyList(), THROWS_KEY)

        setContent {
            DiscgolfTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val navCon = rememberNavController()

                    val scafState = remember{ mutableStateOf(ScaffoldState()) }
                    Scaffold(bottomBar = { NavBar(navCon) }, topBar = { TopBar(state = scafState.value.topBar) },
                        floatingActionButton = { Fab(state = scafState.value.fab) }) { pad->

                        NavHost(navController = navCon, startDestination = MatchGraph.route) {
                            matchGraph(navCon, pad, scafState)
                            historyGraph(navCon, pad, scafState)
                            measuredThrowsGraph(navCon, pad, scafState)
                        }
                    }
                }
            }
        }
    }

    fun NavGraphBuilder.matchGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
    {
        navigation(startDestination = MatchGraph.NewMatch.route, route = MatchGraph.route) {

            composable(MatchGraph.NewMatch.route){Screen(MatchGraph.NewMatch, pad, scafState){
                NewMatchScreen(navCon = navCon)
            } }

            ongoingMatchGraph(navCon, pad, scafState)
        }
    }

    fun NavGraphBuilder.ongoingMatchGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
    {
        navigation(startDestination = OngoingMatchGraph.SetupMatch.route, route = OngoingMatchGraph.route) {
            composable(OngoingMatchGraph.SetupMatch.route){Screen(OngoingMatchGraph.SetupMatch, pad, scafState) {
                SetupMatchScreen(navCon = navCon, scafState.value)
            }}
            composable(OngoingMatchGraph.Match.route){Screen(OngoingMatchGraph.Match, pad, scafState) {
                //Disable the back button when in a match
                BackHandler(enabled = true) {}

                MatchScreen(navCon = navCon, scafState.value, pad)
            }}

            composable(OngoingMatchGraph.PostMatch.route){Screen(OngoingMatchGraph.PostMatch, pad, scafState){
                PostMatchScreen(navCon = navCon, pad = pad, scafState = scafState.value)
            } }
        }
    }


    fun NavGraphBuilder.historyGraph(navCon: NavController, pad: PaddingValues, scafState: MutableState<ScaffoldState>)
    {
        navigation(startDestination = HistoryGraph.MatchHistory.route, route = HistoryGraph.route) {
            composable(HistoryGraph.MatchHistory.route,){Screen(HistoryGraph.MatchHistory, pad, scafState) {
                //Disable back button
                BackHandler(enabled = true) {}
                MatchHistoryScreen(navCon = navCon)
            }}

            composable(HistoryGraph.Match.route+"/{date}",
                arguments = listOf(navArgument("date") { type = NavType.LongType })){ backStackEntry->
                Screen(HistoryGraph.Match, pad, scafState) {
                    OldMatchScreen(navCon = navCon, pad = pad, scafState.value, date = backStackEntry.arguments?.getLong("date")?:0)
                }
            }
        }
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





    // This composable should be a parent to all screens
    @Composable
    fun Screen(screen: Screen, pad: PaddingValues, currentScaffoldState: MutableState<ScaffoldState>, content: @Composable ()->Unit)
    {
        val side = 0.dp
        val p = PaddingValues(start= side, end = side, bottom = pad.calculateBottomPadding(), top= pad.calculateTopPadding())

        currentScaffoldState.value = screen.scaffoldState
        Box(modifier = Modifier
            .padding(p)
            .fillMaxSize()){
            content.invoke()
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}




