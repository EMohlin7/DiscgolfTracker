package se.umu.edmo0011.discgolftracker.composables.general

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun AppPopup(show: Boolean, widthFraction: Float, heightFraction: Float, onDismiss: ()->Unit, content: @Composable ()->Unit)
{
    if(!show)
        return
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier
            .fillMaxWidth(widthFraction)
            .fillMaxHeight(heightFraction)) {

            content.invoke()
        }
    }
}