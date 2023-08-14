package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.umu.edmo0011.discgolftracker.composables.general.AppPopup

@Composable
fun StopMatchPopup(show: Boolean, onStopMatch: ()->Unit, onDismiss: ()->Unit)
{
    AppPopup(show = show, widthFraction = 1f, heightFraction = 0.5f, onDismiss = onDismiss) {
        StopMatchPopupContent(onStopMatch = onStopMatch, onDismiss)
    }
}

@Composable
fun StopMatchPopupContent(onStopMatch: () -> Unit, onDismiss: () -> Unit )
{
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Dismiss match", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "This text will be replaced asdojdsifjdöksjfdökafdöaklsjdflkajsdflkasfjdaölkdsfj",
            style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.size(20.dp))

        Row {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
            Button(onClick = onStopMatch) {
                Text(text = "Stop match")
            }
        }
    }
}

@Preview
@Composable
fun StopMatchPreview()
{
    StopMatchPopup(show = true,{}) {
        
    }
}