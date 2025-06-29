package ir.amirrezaanari.barandehplanning.planning.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.amirrezaanari.barandehplanning.ui.theme.blue
import ir.amirrezaanari.barandehplanning.ui.theme.brown
import ir.amirrezaanari.barandehplanning.ui.theme.cyan
import ir.amirrezaanari.barandehplanning.ui.theme.green
import ir.amirrezaanari.barandehplanning.ui.theme.indigo
import ir.amirrezaanari.barandehplanning.ui.theme.mint
import ir.amirrezaanari.barandehplanning.ui.theme.orange
import ir.amirrezaanari.barandehplanning.ui.theme.pink
import ir.amirrezaanari.barandehplanning.ui.theme.purple
import ir.amirrezaanari.barandehplanning.ui.theme.red

data class CardColor(
    val color: Color
)

val cardColors = listOf(
    CardColor(red),
    CardColor(orange),
//    CardColor(yellow),
    CardColor(green),
    CardColor(mint),
//    CardColor(teal),
    CardColor(cyan),
    CardColor(blue),
    CardColor(indigo),
    CardColor(purple),
    CardColor(pink),
    CardColor(brown),

    )

@Composable
fun CardColorPicker(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(45.dp)
            .padding(3.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = Color.White,
                shape = CircleShape
            )
            .clickable { onClick() }
    ) {}
}