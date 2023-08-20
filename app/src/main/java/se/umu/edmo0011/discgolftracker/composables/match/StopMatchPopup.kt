package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.umu.edmo0011.discgolftracker.composables.general.AppPopup
import se.umu.edmo0011.discgolftracker.R

@Composable
fun StopMatchPopup(show: Boolean, onStopMatch: ()->Unit, onDismiss: ()->Unit)
{
    AppPopup(show = show, widthFraction = 1f, heightFraction = 0.4f, onDismiss = onDismiss) {
        StopMatchPopupContent(onStopMatch = onStopMatch, onDismiss)
    }
}

@Composable
fun StopMatchPopupContent(onStopMatch: () -> Unit, onDismiss: () -> Unit )
{
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.Dismiss_match), style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        Text(text = stringResource(id = R.string.text_dismiss_match), textAlign = TextAlign.Center)


        Row(Modifier.fillMaxWidth(0.95f), horizontalArrangement = Arrangement.SpaceBetween) {
            val width = 120.dp
            Button(modifier = Modifier.defaultMinSize(minWidth = width), onClick = onDismiss) {
                Text(text = stringResource(id = R.string.Cancel))
            }
            Button(modifier = Modifier.defaultMinSize(minWidth = width), onClick = onStopMatch) {
                Text(text = stringResource(id = R.string.Stop_match))
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