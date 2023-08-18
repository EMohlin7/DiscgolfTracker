package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.Hole
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.OngoingMatchGraph
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel

const val TAB_BAR_HEIGHT = 48

@Composable
fun MatchScreen(navCon: NavController, scafState: ScaffoldState, pad: PaddingValues)
{
    val model = navCon.currentBackStackEntry?.sharedViewModel<MatchViewModel>(navCon, OngoingMatchGraph.route) ?: return
    var showSavePop by remember{mutableStateOf(false)}
    var showStopPop by remember { mutableStateOf(false) }

    //Add function to the first action in the top bar
    scafState.topBar?.navAction = {showStopPop = true}
    scafState.topBar?.actions?.add(0){showSavePop = true}
    SaveMatchPopup(show = showSavePop, onSave = {model.saveGame(navCon, it)}) {
        showSavePop = false
    }
    StopMatchPopup(show = showStopPop, onStopMatch = { model.stopGame(navCon) }) {
       showStopPop = false
    }

    val l = listOf<@Composable ()->Unit>({Text(text = "Current hole")}, {Text(text = "Score")})
    val s = List<(Int)->Unit>(2){{model.onSelectedTab(it)}}
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        TabBar(selected = model.selectedTab, height = TAB_BAR_HEIGHT.dp, titles = l, onSelected = s)
        
        if(!model.showScore)
            HoleScores(hole = model.currentHole, onNext = model::nextHole, onPrev = model::prevHole)
        else
            Score(holes = model.playedHoles, pad)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoleScores(hole: Hole, onNext: (Hole)->Unit, onPrev: (Hole)->Unit)
{
    val throws = remember(hole) { mutableStateListOf<Int>(*hole.throws.toTypedArray()) }
    var par by remember(hole) { mutableIntStateOf(hole.par) }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = hole.number.toString())
        TextField(value = par.toString(), onValueChange = {par = it.toIntOrNull() ?: 0}, label = { Text(text = "Par")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))

        for(i in hole.players.indices)
        {
            TextField(value = throws[i].toString(), onValueChange = {throws[i] = it.toIntOrNull() ?: 0},
                label = {Text(hole.players[i])},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        }
        
        Button(onClick = {onNext.invoke(hole.copy(par = par, throws = throws))}) {
            Text(text = "Next hole")
        }
        Button(onClick = {onPrev.invoke(hole.copy(par = par, throws = throws))}) {
            Text(text = "Prev hole")
        }
    }


}

@Composable
fun Score(holes: List<Hole>, pad: PaddingValues)
{
    val labelWidth = 80
    val height = LocalConfiguration.current.screenHeightDp.dp - pad.calculateBottomPadding() - pad.calculateTopPadding() - TAB_BAR_HEIGHT.dp
    ScoreSheet(holes = holes, labelWidth = labelWidth, height)
}












