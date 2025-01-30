package ir.amirrezaanari.barandehplanning.planning.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary

@Composable
fun SectionFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    borderCondition: Boolean,
    modifier: Modifier
){
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 15.sp

            )
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        border = if (borderCondition) null else BorderStroke(
            1.dp,
            SolidColor(Color.White)
        ),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = mainwhite,
            selectedLabelColor = primary,
            containerColor = Color.Transparent,
            labelColor = mainwhite,
        )
    )
}