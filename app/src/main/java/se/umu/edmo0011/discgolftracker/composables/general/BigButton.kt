package se.umu.edmo0011.discgolftracker.composables.general

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import se.umu.edmo0011.discgolftracker.R

@Composable
fun BigButton(text: String, textStyle: TextStyle, onClick: ()->Unit)
{
    Button(
        modifier = Modifier.size(300.dp, 150.dp),
        shape = RoundedCornerShape(30.dp),
        onClick = onClick) {
        Text(text = text, style = textStyle, textAlign = TextAlign.Center)
    }
}