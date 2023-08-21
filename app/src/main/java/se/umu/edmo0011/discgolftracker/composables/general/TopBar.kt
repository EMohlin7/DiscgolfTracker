package se.umu.edmo0011.discgolftracker.composables.general

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import se.umu.edmo0011.discgolftracker.dataClasses.TopBarState


// Create a top bar from a top bar state
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(state: TopBarState?)
{
    if(state == null)
        return

    TopAppBar(
        title = { state.title?.let { Text(text = stringResource(id = it)) } },
        navigationIcon = {state.topNavIcon?.let {
                IconButton(onClick = { state.navAction?.invoke() }) {
                    Icon(it, null)
                }
            }
        },
        actions = {for(i in 0 until state.actionIcons.size){
                IconButton(onClick = {try{state.actions[i].invoke()}catch (e: IndexOutOfBoundsException){} } ) {
                    Icon(state.actionIcons[i], null)
                }
            }
        }
    )
}


