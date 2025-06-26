package ir.amirrezaanari.barandehplanning.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.planning.components.toPersianDigits
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.secondary

@Composable
fun StatsSection(viewModel: PlannerViewModel) {

    val totalPlannedTasks by viewModel.totalPlannedTasks.collectAsState()
    val totalCompletedTasks by viewModel.totalCompletedTasks.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
    ) {
        StatCard(
            text = "برنامه های ریخته شده",
            count = totalPlannedTasks,
            modifier = Modifier
                .padding(end = 4.dp)

        )

        StatCard(
            text = "برنامه های انجام شده",
            count = totalCompletedTasks,
            modifier = Modifier.fillMaxWidth().padding(start = 4.dp)
        )
    }

}

@Composable
fun StatCard(
    text: String,
    count: Int,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = secondary
        ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = count.toString().toPersianDigits(),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )

            Text(text, fontSize = 15.sp)
        }
    }
}