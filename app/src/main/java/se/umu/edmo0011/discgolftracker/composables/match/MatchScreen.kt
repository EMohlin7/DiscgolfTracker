package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.Hole
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel

const val TAB_BAR_HEIGHT = 48

@Composable
fun MatchScreen(navCon: NavController, scafState: ScaffoldState, pad: PaddingValues)
{
    val model = navCon.currentBackStackEntry?.sharedViewModel<MatchViewModel>(navCon, MatchGraph.route) ?: return
    var pop by remember{mutableStateOf(false)}

    //Add function to the first action in the top bar
    scafState.topBar?.actions?.add(0){pop = true}
    SaveMatchPopup(show = pop, onSave = {model.saveGame(navCon, it)}) {
        pop = false
    }


    val l = List<@Composable ()->Unit>(2){ {Text(text = "$it")}}
    val s = List<(Int)->Unit>(2){{model.onSelectedTab(it)}}
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        TabBar(selected = model.selectedTab, height = TAB_BAR_HEIGHT.dp, titles = l, onSelected = s)
        
        if(!model.showScore)
            HoleScores(hole = model.currentHole, onNext = model::nextHole, onPrev = model::prevHole, {})
        else
            ScoreGrid(holes = model.playedHoles, pad)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoleScores(hole: Hole, onNext: (Hole)->Unit, onPrev: (Hole)->Unit, onEnd: (Hole)->Unit)
{
    val throws = remember(hole) { mutableStateListOf<Int>(*hole.throws.toTypedArray()) }
    var par by remember(hole) { mutableStateOf(hole.par) }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = hole.number.toString())
        TextField(value = par.toString(), onValueChange = {par = it.toInt()}, label = { Text(text = "Par")})

        for(i in hole.players.indices)
        {
            TextField(value = throws[i].toString(), onValueChange = {throws[i] = it.toInt()},
                label = {Text(hole.players[i])})
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
fun ScoreGrid(holes: List<Hole>, pad: PaddingValues)
{
    val labelWidth = 80
    val height = LocalConfiguration.current.screenHeightDp.dp - pad.calculateBottomPadding() - pad.calculateTopPadding() - TAB_BAR_HEIGHT.dp
    ScoreSheet(holes = holes, labelWidth = labelWidth, height)
}












