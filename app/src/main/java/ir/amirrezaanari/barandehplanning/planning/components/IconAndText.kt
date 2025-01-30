package ir.amirrezaanari.barandehplanning.planning.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite

@Composable
fun IconAndText(
    icon: ImageVector,
    text: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            tint = mainwhite,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            fontSize = 15.sp,
            modifier = Modifier
                .padding(start = 5.dp),
            color = mainwhite

        )
    }
}