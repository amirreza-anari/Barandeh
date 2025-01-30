package ir.amirrezaanari.barandehplanning.planning.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite


@Composable
fun TimePickerDialogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true
){

    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = if (enabled) BorderStroke(1.dp, mainwhite.copy(alpha = 0.7f)) else BorderStroke(1.dp, Color.Gray),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = mainwhite,
            disabledContentColor = mainwhite.copy(alpha = 0.7f),
            disabledContainerColor = Color.Transparent
        )
    ) {
        Text(text.toPersianDigits())
    }
}