package se.umu.edmo0011.discgolftracker.composables.general

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import se.umu.edmo0011.discgolftracker.dataClasses.FabState

// Create a fab from a fab state
@Composable
fun Fab(state: FabState?)
{
    if(state == null)
        return
    state.fabIcon?.let {
        FloatingActionButton(onClick = {state.fabAction?.invoke()}) {
            Icon(it, null)
        }
    }
}