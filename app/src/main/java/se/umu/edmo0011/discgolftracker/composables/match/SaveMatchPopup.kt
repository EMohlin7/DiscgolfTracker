package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.composables.general.AppPopup

@Composable
fun SaveMatchPopup(show: Boolean, onSave: (String)->Unit, onDismiss: ()->Unit)
{
    AppPopup(show = show, widthFraction = 1f, heightFraction = 0.6f, onDismiss = onDismiss) {
        SaveMatchPopupContent(onSave = onSave, onDismiss = onDismiss)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveMatchPopupContent(onSave: (String) -> Unit, onDismiss: () -> Unit)
{
    var course by rememberSaveable { mutableStateOf("") }
    IconButton(onClick = onDismiss) {
        Icon(Icons.Default.Close, null)
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Save match", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.size(30.dp))
        TextField(modifier = Modifier.fillMaxWidth(0.75f), value = course, onValueChange = {course = it},
            placeholder =  { Text(text = stringResource(id = R.string.Course)) },
            supportingText = { Text(text = "(${stringResource(id = R.string.optional)})") })

        Spacer(modifier = Modifier.size(30.dp))
        IconButton(onClick = { onSave.invoke(course) }) {
            Icon(Icons.Default.Check, null)
        }
    }
}

@Preview
@Composable
fun SavePopupPreview()
{
    Column(modifier = Modifier.fillMaxSize(), ) {
        SaveMatchPopup(show = true, onSave = {}) {

        }
    }
}