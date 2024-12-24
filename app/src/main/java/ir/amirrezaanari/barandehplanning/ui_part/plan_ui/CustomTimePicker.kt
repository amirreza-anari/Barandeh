package ir.amirrezaanari.barandehplanning.ui_part.plan_ui

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.amirrezaanari.barandehplanning.ui.theme.CustomTypography
import kotlinx.coroutines.launch
import java.time.LocalTime


private fun toPersianNumber(number: String): String {
    val persianDigits = arrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    val builder = StringBuilder()
    number.forEach {
        builder.append(
            if (it.isDigit()) persianDigits[it - '0'] else it
        )
    }
    return builder.toString()
}

@SuppressLint("DefaultLocale")
@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    is24TimeFormat: Boolean,
    itemHeight: Dp = 40.dp,
    dividerConfig: DividerConfig = DividerConfig(),
    textStyles: TimePickerTextStyles = TimePickerTextStyles(),
    currentTime: LocalTime,
    onTimeChanged: (LocalTime) -> Unit
) {
    val timeState = remember {
        mutableStateOf(parseTime(currentTime, is24TimeFormat))
    }

    val hours = if (is24TimeFormat) (0..23).toList() else (1..12).toList()
    val minutes = (0..59).toList()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScrollableNumberPicker(
                items = hours,
                selectedItem = timeState.value.hours,
                itemHeight = itemHeight,
                dividerConfig = dividerConfig,
                textStyles = textStyles,
                formatNumber = { toPersianNumber(if (is24TimeFormat) String.format("%02d", it) else it.toString()) },
                modifier = Modifier
                    .weight(1f)
                    .height(itemHeight * 5),
                onItemSelected = {
                    timeState.value = timeState.value.copy(hours = it)
                    onTimeChanged(timeState.value.toLocalTime())
                }
            )

            Text(
                text = ":",
                style = textStyles.separatorStyle,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            ScrollableNumberPicker(
                items = minutes,
                selectedItem = timeState.value.minutes,
                itemHeight = itemHeight,
                dividerConfig = dividerConfig,
                textStyles = textStyles,
                formatNumber = { toPersianNumber(String.format("%02d", it)) },
                modifier = Modifier
                    .weight(1f)
                    .height(itemHeight * 5),
                onItemSelected = {
                    timeState.value = timeState.value.copy(minutes = it)
                    onTimeChanged(timeState.value.toLocalTime())
                }
            )

            if (!is24TimeFormat) {
                timeState.value.period?.let {
                    AmPmPicker(
                        selectedPeriod = it,
                        itemHeight = itemHeight,
                        dividerConfig = dividerConfig,
                        textStyles = textStyles,
                        modifier = Modifier
                            .weight(1f)
                            .height(itemHeight * 5),
                        onPeriodSelected = {
                            timeState.value = timeState.value.copy(period = it)
                            onTimeChanged(timeState.value.toLocalTime())
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> ScrollableNumberPicker(
    items: List<T>,
    selectedItem: T,
    itemHeight: Dp,
    dividerConfig: DividerConfig,
    textStyles: TimePickerTextStyles,
    formatNumber: (T) -> String,
    modifier: Modifier = Modifier,
    onItemSelected: (T) -> Unit
) {
    val scope = rememberCoroutineScope()
    var itemHeightPx by remember { mutableStateOf(0) }

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        val centerOffset = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % items.size
        val selectedIndex = items.indexOf(selectedItem)
        LazyListState(
            firstVisibleItemIndex = centerOffset + selectedIndex,
            firstVisibleItemScrollOffset = 0
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = itemHeight * 2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(Int.MAX_VALUE) { index ->
                val item = items[index % items.size]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .onGloballyPositioned {
                            if (itemHeightPx == 0) {
                                itemHeightPx = it.size.height
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatNumber(item),
                        style = if (index == listState.firstVisibleItemIndex &&
                            listState.firstVisibleItemScrollOffset == 0)
                            textStyles.selectedTextStyle else textStyles.defaultTextStyle
                    )
                }
            }
        }

        if (dividerConfig.shown) {
            HorizontalDividers(
                color = dividerConfig.color,
                thickness = dividerConfig.thickness,
                padding = dividerConfig.padding,
                itemHeight = itemHeight
            )
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && itemHeightPx > 0) {
            val offset = listState.firstVisibleItemScrollOffset
            if (offset != 0) {
                scope.launch {
                    val targetOffset = if (offset > itemHeightPx / 2) {
                        itemHeightPx - offset
                    } else {
                        -offset
                    }
                    listState.animateScrollBy(targetOffset.toFloat())

                    val selectedIndex = listState.firstVisibleItemIndex % items.size
                    onItemSelected(items[selectedIndex])
                }
            }
        }
    }
}

@Composable
private fun AmPmPicker(
    selectedPeriod: DayPeriod,
    itemHeight: Dp,
    dividerConfig: DividerConfig,
    textStyles: TimePickerTextStyles,
    modifier: Modifier = Modifier,
    onPeriodSelected: (DayPeriod) -> Unit
) {
    ScrollableNumberPicker(
        items = DayPeriod.entries,
        selectedItem = selectedPeriod,
        itemHeight = itemHeight,
        dividerConfig = dividerConfig,
        textStyles = textStyles,
        formatNumber = { it.display },
        modifier = modifier,
        onItemSelected = onPeriodSelected
    )
}

@Composable
private fun HorizontalDividers(
    color: Color,
    thickness: Dp,
    padding: Dp,
    itemHeight: Dp
) {
    Column {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding)
                .offset(y = -itemHeight / 2),
            thickness = thickness,
            color = color
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding)
                .offset(y = itemHeight / 2),
            thickness = thickness,
            color = color
        )
    }
}

enum class DayPeriod(val display: String) {
    AM("AM"), PM("PM")
}

data class TimeState(
    val hours: Int,
    val minutes: Int,
    val period: DayPeriod? = null
)

data class DividerConfig(
    val shown: Boolean = true,
    val color: Color = Color.White.copy(alpha = 0.3f),
    val thickness: Dp = 1.dp,
    val padding: Dp = 0.dp
)

data class TimePickerTextStyles(
    val defaultTextStyle: TextStyle = CustomTypography.titleMedium,
    val selectedTextStyle: TextStyle = CustomTypography.titleLarge,
    val separatorStyle: TextStyle = CustomTypography.titleLarge
)

private fun parseTime(time: LocalTime, is24TimeFormat: Boolean): TimeState {
    return TimeState(
        hours = if (!is24TimeFormat && time.hour > 12) time.hour - 12 else time.hour,
        minutes = time.minute,
        period = if (is24TimeFormat) null else if (time.hour >= 12) DayPeriod.PM else DayPeriod.AM
    )
}

private fun TimeState.toLocalTime(): LocalTime {
    val hour = when {
        period == DayPeriod.PM -> hours % 12 + 12
        period == DayPeriod.AM -> hours % 12
        else -> hours
    }
    return LocalTime.of(hour, minutes)
}