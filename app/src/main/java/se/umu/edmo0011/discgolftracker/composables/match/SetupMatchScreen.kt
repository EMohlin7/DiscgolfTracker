package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.sharedViewModel
import se.umu.edmo0011.discgolftracker.viewModels.MatchViewModel


@Composable
fun SetupMatchScreen(navCon: NavController)
{
    val model = navCon.currentBackStackEntry?.sharedViewModel<MatchViewModel>(navCon,MatchGraph.route) ?: return

    SetupMatch(){
        model.onCreateMatch(navCon, it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupMatch(onCreate: (List<String>)->Unit)
{
    val players = remember { mutableStateListOf("") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        for(i in 0 until players.size)
        {
            PlayerNameInput(value = players[i], {players[i] = it}){
                players.removeAt(i)
            }
        }
        IconButton(onClick = { players.add("")}) {
            Icon(Icons.Default.Add, null)
        }

        Button(onClick = { onCreate.invoke(players.toList()) }) {
            Text(text = "Create match")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerNameInput(value: String, onValueChanged: (String)->Unit, onDelete: ()->Unit)
{
    Row {
        TextField(value = value, onValueChange = onValueChanged)
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, null)
        }
    }
}