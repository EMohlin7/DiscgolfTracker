package se.umu.edmo0011.discgolftracker.composables.general

import android.app.Activity
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(value: String,
              onValueChange: (String) -> Unit,
              modifier: Modifier = Modifier,
              enabled: Boolean = true,
              readOnly: Boolean = false,
              textStyle: TextStyle = LocalTextStyle.current,
              label: @Composable (() -> Unit)? = null,
              placeholder: @Composable (() -> Unit)? = null,
              leadingIcon: @Composable (() -> Unit)? = null,
              trailingIcon: @Composable (() -> Unit)? = null,
              supportingText: @Composable (() -> Unit)? = null,
              isError: Boolean = false,
              visualTransformation: VisualTransformation = VisualTransformation.None,
              keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
              keyboardActions: KeyboardActions = KeyboardActions.Default,
              singleLine: Boolean = true,
              maxLines: Int = Int.MAX_VALUE,
              interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
              shape: Shape = TextFieldDefaults.filledShape,
              colors: TextFieldColors = TextFieldDefaults.textFieldColors())

{
    Card(modifier = modifier) {
        TextField(
            modifier = Modifier.padding(start = 15.dp, bottom = 7.dp, end = 15.dp),
            value = value,
            onValueChange = onValueChange,
            label = label,
            keyboardOptions = keyboardOptions,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )
    }
}