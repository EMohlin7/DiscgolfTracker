package se.umu.edmo0011.discgolftracker.composables.general

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.umu.edmo0011.discgolftracker.misc.ScreenState
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState

// This composable should be a parent to all screens
@Composable
fun Screen(screen: ScreenState, pad: PaddingValues, currentScaffoldState: MutableState<ScaffoldState>, content: @Composable ()->Unit)
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