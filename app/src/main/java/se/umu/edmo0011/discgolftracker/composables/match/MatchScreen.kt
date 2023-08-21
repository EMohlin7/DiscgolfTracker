package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.dataClasses.Hole
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.composables.general.TextInput
import se.umu.edmo0011.discgolftracker.misc.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.graphs.OngoingMatchGraph

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

    MatchScreenContent(model, pad)
}

@Composable
fun MatchScreenContent(model: MatchViewModel, pad: PaddingValues)
{
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {

        MatchScreenTabBar(model.selectedTab, holeNumber = model.playedHoles[model.currentHoleIndex].number){
            model.onSelectedTab(it)
        }

        if(!model.showScore)
            HoleInput(curHoleIndex = model.currentHoleIndex, holes = model.playedHoles,
                onEdit = model::editHole, onNext = model::nextHole, onPrev = model::prevHole)
        else
            Score(holes = model.playedHoles, pad)
    }
}

@Composable
fun MatchScreenTabBar(selected: Int, holeNumber: Int, onSelect: (Int)->Unit)
{
    val colors = arrayOf(MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.onBackground)
    val titles = listOf<@Composable ()->Unit>(
        {Text(text = stringResource(id = R.string.Hole)+" $holeNumber",
            color = if(selected == 0) colors[0] else colors[1])},

        {Text(text = stringResource(id = R.string.Score),
            color = if(selected == 1) colors[0] else colors[1])}
    )

    val s = List<(Int)->Unit>(2){{onSelect.invoke(it)}}
    TabBar(selected = selected, height = TAB_BAR_HEIGHT.dp, width = LocalConfiguration.current.screenWidthDp.dp,
        titles = titles, onSelected = s)
}


@Composable
fun HoleInput(curHoleIndex: Int, holes: List<Hole>, onEdit: (Int, List<Int>)->Unit, onNext: ()->Unit, onPrev: ()->Unit)
{
    val hole = holes[curHoleIndex]
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 5.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.size(10.dp))
        TextInput(
            modifier = Modifier.padding(bottom = 3.dp),
            value = if(hole.par == 0) "" else hole.par.toString(),
            onValueChange = {onEdit.invoke(it.toIntOrNull()?:0, hole.throws)},
            label = { Text(text = stringResource(id = R.string.Par))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
        )

        ScoreInputColumn(players = hole.players, throws = hole.throws.toMutableList()){
            onEdit.invoke(hole.par, it)
        }

        Row(
            Modifier
                .fillMaxWidth(0.85f)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {onPrev.invoke()}) {
                Icon(Icons.Default.ArrowBack, null)
                Text(text = stringResource(id = R.string.Previous_hole))

            }
            Button(onClick = {onNext.invoke()}) {
                Text(text = stringResource(id = R.string.Next_hole))
                Icon(Icons.Default.ArrowForward, null)
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreInputColumn(players: List<String>, throws: MutableList<Int>, onEdit: (List<Int>) -> Unit)
{
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        for(i in players.indices)
        {
            val action = if(i == players.size - 1) ImeAction.Done else ImeAction.Next

            TextInput(
                modifier = Modifier.padding(bottom = 3.dp),
                value = if(throws[i] == 0) "" else throws[i].toString(),
                onValueChange = {throws[i] = it.toIntOrNull() ?: 0; onEdit.invoke(throws)},
                label = {Text(players[i])},
                supportingText = { Text(text = stringResource(id = R.string.Number_of_throws))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = action)
            )
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












