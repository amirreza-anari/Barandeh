package ir.amirrezaanari.barandehplanning.ai.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import ir.amirrezaanari.barandehplanning.ai.ui.components.Picker
import ir.amirrezaanari.barandehplanning.ai.ui.components.rememberPickerState
import ir.amirrezaanari.barandehplanning.homescreen.InfoCombination
import ir.amirrezaanari.barandehplanning.planning.components.toPersianDigits
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.ui.theme.CustomFontFamily
import ir.amirrezaanari.barandehplanning.ui.theme.CustomTypography
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import kotlinx.coroutines.launch
import java.net.URLEncoder

// فرض می‌شود که `samplePrompts` و دیگر وابستگی‌ها مانند `Picker` در پروژه شما تعریف شده‌اند.

@Composable
fun AiFirstScreen(navController: NavHostController, viewModel: PlannerViewModel) {

    val coroutineScope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(0) }

    // State برای کنترل نمایش دیالوگ
    var showDayPickerDialog by remember { mutableStateOf(false) }

    val navigateToChat: (Int) -> Unit = { selectedDays ->
        coroutineScope.launch {
            val systemPrompt = samplePrompts[currentIndex].systemPrompt
            val encodedPrompt = URLEncoder.encode(systemPrompt, "UTF-8")

            var statistics = ""
            if (selectedDays > 0) {
                statistics = viewModel.getLastNDaysStatistics(selectedDays)
            }

            val route = if (statistics.isNotBlank()) {
                val encodedStats = URLEncoder.encode(statistics, "UTF-8")
                "chat/${encodedPrompt}?stats=${encodedStats}"
            } else {
                "chat/${encodedPrompt}"
            }
            navController.navigate(route)
        }
    }

    if (showDayPickerDialog) {
        DaySelectionDialog(
            onDismissRequest = { showDayPickerDialog = false },
            onConfirm = { selectedDays ->
                showDayPickerDialog = false
                navigateToChat(selectedDays)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AiFirstScreenTopAppBar(
            name = samplePrompts[currentIndex].name,
            job = samplePrompts[currentIndex].job,
            onPreviousClick = {
                if (currentIndex > 0) {
                    currentIndex--
                }
            },
            onNextClick = {
                if (currentIndex < samplePrompts.size - 1) {
                    currentIndex++
                }
            }
        )
        Spacer(Modifier.height(5.dp))

        // --- تغییر اصلی: استفاده از AnimatedContent برای انیمیشن محتوا ---
        AnimatedContent(
            targetState = currentIndex,
            label = "character-animation",
            transitionSpec = {
                // انیمیشن هوشمند: تشخیص جهت حرکت (به جلو یا به عقب)
                if (targetState > initialState) {
                    // حرکت به جلو: محتوای جدید از راست وارد، قبلی به چپ خارج
                    (slideInHorizontally(animationSpec = tween(200)) { fullWidth -> fullWidth } + fadeIn(animationSpec = tween(200)))
                        .togetherWith(slideOutHorizontally(animationSpec = tween(200)) { fullWidth -> -fullWidth } + fadeOut(animationSpec = tween(200)))
                } else {
                    // حرکت به عقب: محتوای جدید از چپ وارد، قبلی به راست خارج
                    (slideInHorizontally(animationSpec = tween(200)) { fullWidth -> -fullWidth } + fadeIn(animationSpec = tween(200)))
                        .togetherWith(slideOutHorizontally(animationSpec = tween(200)) { fullWidth -> fullWidth } + fadeOut(animationSpec = tween(200)))
                }
            }
        ) { targetIndex ->
            // محتوای داخل انیمیشن بر اساس targetIndex نمایش داده می‌شود
            val currentPrompt = samplePrompts[targetIndex]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(currentPrompt.picture),
                    contentDescription = "role picture",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = currentPrompt.description,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                if (samplePrompts[currentIndex].showPicker) {
                    showDayPickerDialog = true
                } else {
                    navigateToChat(0)
                }
            },
            shape = RoundedCornerShape(25),
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = mainwhite
            ),
        ) {
            Text(
                text = "شروع چت",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}


/**
 * کامپوننت دیالوگ برای انتخاب تعداد روزها.
 * @param onDismissRequest تابعی که هنگام درخواست بسته شدن دیالوگ (مانند کلیک روی "لغو") فراخوانی می‌شود.
 * @param onConfirm تابعی که هنگام تایید (با پارامتر تعداد روزهای انتخابی) فراخوانی می‌شود.
 */
@Composable
fun DaySelectionDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val dayPickerItems = remember { (0..30).map { it.toString().toPersianDigits() } }
    val pickerState = rememberPickerState()

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = primary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "انتخاب تعداد روز",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Text(
                    text = "آمار برنامه ریزی چند روز گذشته تو میخوای که من بررسی کنم؟ 😉",
                    textAlign = TextAlign.Center,
                )

                Picker(
                    modifier = Modifier
                        .width(60.dp)
                        .padding(vertical = 10.dp),
                    items = dayPickerItems,
                    state = pickerState,
                    textModifier = Modifier.padding(5.dp),
                    textStyle = TextStyle(
                        fontFamily = CustomFontFamily,
                        fontSize = 20.sp
                    )
                )

                Button(
                    onClick = {
                        val selectedDays = pickerState.selectedItem.toIntOrNull() ?: 0
                        onConfirm(selectedDays)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainwhite
                    )
                ) {
                    Text("باشه", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AiFirstScreenTopAppBar(
    name: String,
    job: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
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
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(name != "کیوان") {
                IconButton(onClick = onNextClick) {
                    Icon(
                        Icons.AutoMirrored.Rounded.NavigateBefore,
                        contentDescription = "Previous Prompt",
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier.size(40.dp),
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    style = CustomTypography.titleLarge
                )
                Text(
                    text = job,
                    style = CustomTypography.titleSmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            if (name != "امیر") {
                IconButton(onClick = onPreviousClick) {
                    Icon(
                        Icons.AutoMirrored.Rounded.NavigateNext,
                        contentDescription = "Next Prompt",
                        modifier = Modifier.size(40.dp),
                    )
                }
            } else {
                Box(
                    modifier = Modifier.size(40.dp),
                )
            }
        }
    }
}