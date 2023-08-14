package se.umu.edmo0011.discgolftracker.composables.general

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun DismissableAlert(
    show: Boolean,
    @StringRes title: Int,
    onDismiss: ()->Unit,
    @StringRes dismissText: Int,
    @StringRes text: Int,
    icon: @Composable (()->Unit)? = null,
    modifier: Modifier = Modifier
)
{
    if(show)
        AlertDialog(
            title = { Text(text = stringResource(id = title)) },
            onDismissRequest = onDismiss,
            confirmButton = { Button(onClick = onDismiss) { Text(text = stringResource(id = dismissText)) } },
            text = { Text(text = stringResource(id = text)) },
            icon = icon,
            modifier = modifier
        )
}
