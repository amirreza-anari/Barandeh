package ir.amirrezaanari.barandehplanning.planning.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    singleline: Boolean,
    enabled: Boolean = true,
    maxlines: Int = 1
) {
    OutlinedTextField(
        modifier = modifier,
        enabled = enabled,
        value = value,
//        label = { Text(label) },
        placeholder = { Text(label) },
        onValueChange = onValueChange,
        singleLine = singleline,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done // تنظیم عمل کیبورد روی Done
        ),
        maxLines = maxlines,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            unfocusedLabelColor = mainwhite.copy(alpha = 0.8f),
            focusedContainerColor = Color.Transparent,
            unfocusedPlaceholderColor = mainwhite.copy(alpha = 0.8f),
            unfocusedIndicatorColor = mainwhite.copy(alpha = 0.8f),
            unfocusedTextColor = mainwhite,
            focusedIndicatorColor = mainwhite,
            focusedTextColor = mainwhite,
            focusedLabelColor = mainwhite,
            focusedPlaceholderColor = mainwhite.copy(alpha = 0.8f),
            cursorColor = mainwhite,
            disabledContainerColor = Color.Transparent,
            disabledTextColor = mainwhite.copy(alpha = 0.7f),
            disabledIndicatorColor = Color.Gray
        )
    )
}