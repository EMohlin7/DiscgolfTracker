package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.OngoingMatchGraph
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel


@Composable
fun SetupMatchScreen(navCon: NavController, scafState: ScaffoldState)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}

    val model = navCon.currentBackStackEntry?.sharedViewModel<MatchViewModel>(navCon,OngoingMatchGraph.route) ?: return

    SetupMatch(){
        model.onCreateMatch(navCon, it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupMatch(onCreate: (List<String>)->Unit)
{
    val players = remember { mutableStateListOf("") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.Players), style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.size(10.dp))
        PlayerInputColumn(players = players)


        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally)
        {
            Button(onClick = { onCreate.invoke(players.toList()) }) {
                Text(text = stringResource(id = R.string.Create_match))
            }
            Spacer(modifier = Modifier.size(30.dp))
        }

    }
}

@Composable
fun PlayerInputColumn(players: MutableList<String>)
{
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        for(i in 0 until players.size)
        {
            PlayerNameInput(players[i], i == players.size-1, {players[i] = it}){
                players.removeAt(i)
            }
        }

        IconButton(onClick = { players.add("")}) {
            Icon(Icons.Default.Add, null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerNameInput(value: String, last: Boolean, onValueChanged: (String)->Unit, onDelete: ()->Unit)
{
    val action = if(last) ImeAction.Done else ImeAction.Next
    Card(Modifier.padding(vertical = 2.dp)) {
        Row(Modifier.padding(start = 15.dp, bottom = 7.dp, top = 7.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = value, onValueChange = onValueChanged,
                keyboardOptions = KeyboardOptions(imeAction = action))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null)
            }
        }
    }
}

@Composable
@Preview
fun PlayerNameInputPrev()
{
    PlayerNameInput(value = "Edvin", last = false, onValueChanged = {}) {

    }
}