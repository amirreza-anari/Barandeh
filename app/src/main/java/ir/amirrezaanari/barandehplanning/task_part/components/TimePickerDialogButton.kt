package ir.amirrezaanari.barandehplanning.task_part.components

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
import ir.amirrezaanari.barandehplanning.ui_part.plan_ui.toPersianDigits


@Composable
fun TimePickerDialogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
){

    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, mainwhite.copy(alpha = 0.7f)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = mainwhite
        )
    ) {
        Text(text.toPersianDigits())
    }
}