package se.umu.edmo0011.discgolftracker

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
import se.umu.edmo0011.discgolftracker.composables.general.NavBar
import se.umu.edmo0011.discgolftracker.composables.general.TopBar
import se.umu.edmo0011.discgolftracker.composables.matchHistory.MatchHistoryScreen
import se.umu.edmo0011.discgolftracker.composables.matchHistory.OldMatchScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.MeasuredThrowsScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.MeasuringScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.SaveMeasurementScreen
import se.umu.edmo0011.discgolftracker.composables.measuring.StartMeasureScreen
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.graphs.MatchGraph
import se.umu.edmo0011.discgolftracker.graphs.historyGraph
import se.umu.edmo0011.discgolftracker.graphs.matchGraph
import se.umu.edmo0011.discgolftracker.graphs.measuredThrowsGraph

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DiscgolfTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    //The nav controller used to navigate between screens in the app
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
}





