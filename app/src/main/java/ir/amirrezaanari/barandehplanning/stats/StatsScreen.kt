package ir.amirrezaanari.barandehplanning.stats

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.planning.components.toPersianDigits
import ir.amirrezaanari.barandehplanning.planning.database.PlannerDatabase
import ir.amirrezaanari.barandehplanning.planning.database.PlannerRepository
import ir.amirrezaanari.barandehplanning.planning.database.TaskEntity
import ir.amirrezaanari.barandehplanning.ui.theme.CustomTypography
import ir.amirrezaanari.barandehplanning.ui.theme.blue
import ir.amirrezaanari.barandehplanning.ui.theme.cyan
import ir.amirrezaanari.barandehplanning.ui.theme.green
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.orange
import ir.amirrezaanari.barandehplanning.ui.theme.purple
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import ir.huri.jcal.JalaliCalendar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Helper functions for Farsi translations
private fun getFarsiDayName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.SUNDAY -> "یکشنبه"
        DayOfWeek.MONDAY -> "دوشنبه"
        DayOfWeek.TUESDAY -> "سه‌شنبه"
        DayOfWeek.WEDNESDAY -> "چهارشنبه"
        DayOfWeek.THURSDAY -> "پنج‌شنبه"
        DayOfWeek.FRIDAY -> "جمعه"
        DayOfWeek.SATURDAY -> "شنبه"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val context = LocalContext.current
    val database = remember { PlannerDatabase.getInstance(context) }
    val repository = remember { PlannerRepository(database.dateDao(), database.taskDao()) }
    val factory = remember { ReportsViewModelFactory(repository) }
    val viewModel: ReportsViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp),
            colors = CardDefaults.cardColors(
                contentColor = mainwhite,
                containerColor = secondary
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = "گزارش\u200Cها و آمار",
                    textAlign = TextAlign.Center,
                    style = CustomTypography.titleLarge,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { OverviewSection(uiState) }
            item { PlannedVsCompletedChart(uiState.plannedVsCompleted) }
            item { WeeklyProgressChart(uiState.weeklyTasks) }
            item { TimeDistributionChart(uiState.weeklyTasks) }
            item { ProductivityInsights(uiState) }
        }
    }
}


// Data class to hold stat information
data class StatData(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun OverviewSection(uiState: ReportsUiState) {
    val totalTasks = uiState.plannedVsCompleted.first + uiState.plannedVsCompleted.second
    val completionRate = if (uiState.plannedVsCompleted.first > 0) {
        (uiState.plannedVsCompleted.second.toFloat() / uiState.plannedVsCompleted.first * 100).toInt()
    } else 0

    // List of stats to display in the grid
    val stats = listOf(
        StatData("مجموع وظایف", totalTasks.toString(), Icons.Rounded.Assessment, cyan),
        StatData("تکمیل شده", uiState.plannedVsCompleted.second.toString(), Icons.Rounded.Check, green),
        StatData("برنامه‌ریزی شده", uiState.plannedVsCompleted.first.toString(), Icons.Rounded.Schedule, orange),
        StatData("نرخ موفقیت", "$completionRate%", Icons.Rounded.TrendingUp, purple)
    )

    // 2x2 grid layout using LazyVerticalGrid
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                StatCard(
                    title = stats[0].title,
                    value = stats[0].value,
                    icon = stats[0].icon,
                    color = stats[0].color
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                StatCard(
                    title = stats[3].title,
                    value = stats[3].value,
                    icon = stats[3].icon,
                    color = stats[3].color
                )
            }
        }
        // Second row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                StatCard(
                    title = stats[2].title,
                    value = stats[2].value,
                    icon = stats[2].icon,
                    color = stats[2].color
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                StatCard(
                    title = stats[1].title,
                    value = stats[1].value,
                    icon = stats[1].icon,
                    color = stats[1].color
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = value.toPersianDigits(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PlannedVsCompletedChart(plannedVsCompleted: Pair<Int, Int>) {
    val planned = plannedVsCompleted.first
    val completed = plannedVsCompleted.second
    val total = planned + completed

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = secondary
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "نمای کلی تکمیل وظایف",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (total > 0) {
                val completedPercentage by animateFloatAsState(
                    targetValue = completed.toFloat() / total,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
                    label = "completion"
                )

                DonutChart(
                    completed = completed,
                    planned = planned,
                    animatedPercentage = completedPercentage,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem(
                        color = Color(0xFFFF9800),
                        label = "برنامه‌ریزی شده",
                        value = planned
                    )
                    LegendItem(
                        color = Color(0xFF4CAF50),
                        label = "تکمیل شده",
                        value = completed
                    )
                }
            } else {
                Text(
                    text = "داده‌ای موجود نیست",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun DonutChart(
    completed: Int,
    planned: Int,
    animatedPercentage: Float,
    modifier: Modifier = Modifier
) {
    val total = completed + planned

    Canvas(modifier = modifier) {
        val strokeWidth = 40.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2
        val center = Offset(size.width / 2, size.height / 2)

        drawCircle(
            color = Color(0xFFE0E0E0),
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        if (total > 0) {
            val completedSweep = 360f * animatedPercentage
            drawArc(
                color = Color(0xFF4CAF50),
                startAngle = -90f,
                sweepAngle = completedSweep,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )

            val plannedSweep = 360f * (planned.toFloat() / total)
            drawArc(
                color = Color(0xFFFF9800),
                startAngle = -90f + completedSweep,
                sweepAngle = plannedSweep,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String, value: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value.toString().toPersianDigits(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun WeeklyProgressChart(weeklyTasks: List<TaskEntity>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = secondary
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "فعالیت هفت روز گذشته",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            WeeklyBarChart(
                tasks = weeklyTasks,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
fun WeeklyBarChart(
    tasks: List<TaskEntity>,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = LocalDate.now()

    val weekData = (0..6).map { dayOffset ->
        val date = today.minusDays(dayOffset.toLong())
        val dateString = date.format(dateFormatter)
        val dayTasks = tasks.filter { it.date == dateString }
        val totalMinutes = dayTasks.sumOf { task ->
            calculateDurationMinutes(task.startTime, task.endTime).toLong()
        }
        getFarsiDayName(date.dayOfWeek).substring(0, 1) to totalMinutes.toFloat()
    }.reversed()

    val maxValue = weekData.maxOfOrNull { it.second } ?: 1f

    val context = LocalContext.current
    Canvas(modifier = modifier) {
        val barWidth = size.width / 7 * 0.6f
        val barSpacing = size.width / 7 * 0.4f
        val maxBarHeight = size.height * 0.8f

        weekData.forEachIndexed { index, (day, value) ->
            val barHeight = if (maxValue > 0) (value / maxValue) * maxBarHeight else 0f
            val x = index * (barWidth + barSpacing) + barSpacing / 2
            val y = size.height - barHeight - 40.dp.toPx()

            drawRect(
                color = Color(0xFF2196F3),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    day,
                    x + barWidth / 2,
                    size.height - 10.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        typeface = context.resources.getFont(R.font.yekanbakh_black)

                    }

                )
            }

            if (value > 0) {
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        "${value.toInt().toString().toPersianDigits()}د", // "د" for دقیقه (minutes)
                        x + barWidth / 2,
                        y - 8.dp.toPx(),
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 10.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            typeface = context.resources.getFont(R.font.yekanbakh)

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TimeDistributionChart(weeklyTasks: List<TaskEntity>) {
    val timeSlots = mapOf(
        "صبح" to (6..11),
        "بعد از ظهر" to (12..17),
        "عصر" to (18..23),
        "شب" to (0..5)
    )

    val timeDistribution = timeSlots.map { (period, hours) ->
        val tasksInPeriod = weeklyTasks.filter { task ->
            try {
                val startHour = task.startTime.split(":")[0].toInt()
                startHour in hours
            } catch (e: Exception) {
                false
            }
        }
        period to tasksInPeriod.size
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "توزیع زمانی",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            timeDistribution.forEach { (period, count) ->
                TimeDistributionItem(
                    period = period,
                    count = count,
                    total = weeklyTasks.size
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TimeDistributionItem(period: String, count: Int, total: Int) {
    val percentage = if (total > 0) (count.toFloat() / total) else 0f
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1000),
        label = "percentage"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = period,
            modifier = Modifier.width(80.dp),
            fontSize = 14.sp
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .background(
                    Color(0xFFE0E0E0),
                    RoundedCornerShape(10.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedPercentage)
                    .fillMaxHeight()
                    .background(
                        Color(0xFF4CAF50),
                        RoundedCornerShape(10.dp)
                    )
            )
        }

        Text(
            text = count.toString().toPersianDigits(),
            modifier = Modifier.width(40.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun ProductivityInsights(uiState: ReportsUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "بینش‌های بهره‌وری",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (uiState.mostProductiveDate != null) {
                // 1. Convert LocalDate to a java.util.Date object
                val gregorianDate = java.util.Date.from(
                    uiState.mostProductiveDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                )

                // 2. Use JalaliCalendar to convert the date
                val jalaliDate = JalaliCalendar(gregorianDate)

                // 3. Format it as a string "YYYY/M/D"
                val formattedJalaliDate = "${jalaliDate.year}/${jalaliDate.month}/${jalaliDate.day}"

                InsightItem(
                    title = "پربارترین روز",
                    value = formattedJalaliDate, // Use the formatted Jalali date
                    color = Color(0xFF4CAF50)
                )
            }

            InsightItem(
                title = "تعداد وظایف انجام شده هفت روز گذشته",
                value = uiState.tasksCompletedThisWeek.toString(),
                color = Color(0xFF2196F3)
            )

            val averageTasksPerDay = if (uiState.tasksCompletedThisWeek > 0) {
                (uiState.tasksCompletedThisWeek / 7.0).let { "%.1f".format(it) }
            } else "0"

            InsightItem(
                title = "میانگین وظایف انجام شده/روز",
                value = averageTasksPerDay,
                color = Color(0xFFFF9800)
            )
        }
    }
}

@Composable
fun InsightItem(title: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = value.toPersianDigits(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

private fun calculateDurationMinutes(startTime: String, endTime: String): Long {
    return try {
        val start = startTime.split(":").map { it.toInt() }
        val end = endTime.split(":").map { it.toInt() }
        val startMinutes = start[0] * 60L + start[1]
        val endMinutes = end[0] * 60L + end[1]
        if (endMinutes < startMinutes) 0 else endMinutes - startMinutes
    } catch (e: Exception) {
        0L
    }
}