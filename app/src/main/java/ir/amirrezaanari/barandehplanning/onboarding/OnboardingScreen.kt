package ir.amirrezaanari.barandehplanning.onboarding

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ir.amirrezaanari.barandehplanning.MainActivity
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import kotlinx.coroutines.launch
import androidx.core.content.edit

@Composable
fun OnboardingScreen(navController: NavHostController, context: MainActivity) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { position ->
                OnboardingPageContent(page = pages[position])
            }

            ButtonsSection(
                pagerState = pagerState,
                currentPage = pagerState.currentPage,
                context = context,
                navController = navController
            )
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(pageCount) {
            IndicatorDot(isSelected = it == currentPage)
        }
    }
}

@Composable
fun IndicatorDot(isSelected: Boolean) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 35.dp else 15.dp,
        label = "dot_width"
    )

    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(15.dp)
            .width(width)
            .clip(CircleShape)
            .background(
                if (isSelected) mainwhite
                else mainwhite.copy(alpha = 0.25f)
            )
    )
}

@Composable
fun ButtonsSection(
    pagerState: PagerState,
    navController: NavHostController,
    context: MainActivity,
    currentPage: Int
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp, start = 30.dp, end = 30.dp)
    ) {
        if (pagerState.currentPage != pages.size - 1) {
            if (pagerState.currentPage != 0) {
                Button(
                    onClick = {
                        scope.launch {
                            val prevPage = pagerState.currentPage - 1
                            if (prevPage >= 0) {
                                pagerState.animateScrollToPage(prevPage)
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainwhite,
                        contentColor = primary
                    )
                ) {
                    Text(
                        text = "برگشت",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            PageIndicator(
                pageCount = pages.size,
                currentPage = currentPage,
                modifier = Modifier
                    .align(Alignment.Center)

            )
            Button(
                onClick = {
                    scope.launch {
                        val nextPage = pagerState.currentPage + 1
                        pagerState.animateScrollToPage(nextPage)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainwhite,
                    contentColor = primary
                )
            ) {
                Text(
                    text = "بعدی",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Button(
                onClick = {
                    onBoardingIsFinished(context = context)
                    navController.popBackStack()
                    navController.navigate("main_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = mainwhite,
                    contentColor = primary
                ),
                shape = RoundedCornerShape(25)
            ) {
                Text(
                    text = "!بزن بریم",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = page.image),
                contentDescription = "onboarding main picture",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
            )
            Spacer(Modifier.height(50.dp))
            Text(
                text = page.title,
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold,
                color = mainwhite
            )
            Spacer(Modifier.height(30.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                item {
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = mainwhite
                    )
                }
            }
        }
    }
}

private fun onBoardingIsFinished(context: Context) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean("isFinished", true)
    }
}