package ir.amirrezaanari.barandehplanning.planning.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerDialog
import ir.amirrezaanari.barandehplanning.ui.theme.CustomFontFamily
import ir.amirrezaanari.barandehplanning.ui.theme.CustomTypography
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import ir.huri.jcal.JalaliCalendar
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.time.LocalDate

@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }


    if (showDatePicker.value) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            JalaliDatePickerDialog(
                fontFamily = CustomFontFamily,
                backgroundColor = secondary,
                openDialog = showDatePicker,
                onSelectDay = {},
                fontSize = 16.sp,
                initialDate = JalaliCalendar(selectedDate),
                onConfirm = { selectedJalaliDate ->
                    val newPersianDate = PersianDate().apply {
                        setShYear(selectedJalaliDate.year)
                        setShMonth(selectedJalaliDate.month)
                        setShDay(selectedJalaliDate.day)
                    }

                    val newLocalDate = LocalDate.of(
                        newPersianDate.grgYear,
                        newPersianDate.grgMonth,
                        newPersianDate.grgDay
                    )

                    onDateSelected(newLocalDate)
                    showDatePicker.value = false
                }
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            contentColor = mainwhite,
            containerColor = secondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onDateSelected(selectedDate.minusDays(1)) }
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.NavigateBefore,
                    contentDescription = "Previous Day",
                    modifier = Modifier.size(40.dp)
                )
            }


            AnimatedContent(
                targetState = selectedDate,
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                }
            ) { targetDate ->
                val persianDate = PersianDate().apply {
                    setGrgYear(targetDate.year)
                    setGrgMonth(targetDate.monthValue)
                    setGrgDay(targetDate.dayOfMonth)
                }
                val pdFormater = PersianDateFormat("l، j F Y", PersianDateFormat.PersianDateNumberCharacter.FARSI)
                val formattedDate = pdFormater.format(persianDate)

                Text(
                    text = formattedDate,
                    style = CustomTypography.titleLarge,
                    modifier = Modifier.clickable { showDatePicker.value = true }
                )
            }

            IconButton(
                onClick = { onDateSelected(selectedDate.plusDays(1)) }
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.NavigateNext,
                    contentDescription = "Next Day",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
