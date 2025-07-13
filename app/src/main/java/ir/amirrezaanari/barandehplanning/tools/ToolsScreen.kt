package ir.amirrezaanari.barandehplanning.tools

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ir.amirrezaanari.barandehplanning.ui.theme.CustomTypography
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import androidx.compose.animation.AnimatedVisibility
import ir.amirrezaanari.barandehplanning.planning.database.TaskEntity

object ToolsDestinations {
    const val MAIN = "tools_main"
    const val NOTES = "notes"
    const val MINDFULNESS = "mindfulness"
    const val POMODORO = "pomodoro"
}


@Composable
fun ToolsScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        ToolsDestinations.NOTES -> toolsList.find { it.route == ToolsDestinations.NOTES }?.title
        ToolsDestinations.MINDFULNESS -> toolsList.find { it.route == ToolsDestinations.MINDFULNESS }?.title
        ToolsDestinations.POMODORO -> toolsList.find { it.route == ToolsDestinations.POMODORO }?.title
        else -> "ابزارها"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolsTopAppBar(
            title = title ?: "ابزارها",
            isMainScreen = currentRoute == ToolsDestinations.MAIN,
            onBackClick = { navController.navigateUp() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        NavHost(
            navController = navController,
            startDestination = ToolsDestinations.MAIN
        ) {
            composable(ToolsDestinations.MAIN) {
                ToolsMainScreen(navController = navController)
            }
            composable(ToolsDestinations.NOTES) {
                NotesScreen()
            }
            composable(ToolsDestinations.MINDFULNESS) {
                MindfulnessScreen()
            }
            composable(ToolsDestinations.POMODORO) {
                PomodoroScreen(
                    title = "آزاد",
                    isFromTask = false,
                    onNavigateBack = {}
                )
            }
        }
    }
}


@Composable
fun ToolsMainScreen(navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(toolsList) { tool ->
            ToolItem(tool = tool) {
                navController.navigate(tool.route)
            }
        }
    }
}

@Composable
fun ToolsTopAppBar(
    title: String,
    isMainScreen: Boolean,
    onBackClick: () -> Unit,
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
        ) {
            this@Card.AnimatedVisibility(
                visible = !isMainScreen,
                modifier = Modifier.align(Alignment.CenterStart),
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = "Navigate Back",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            AnimatedContent(
                targetState = title,
                modifier = Modifier.align(Alignment.Center),
                transitionSpec = {
                    (fadeIn(animationSpec = tween(300)))
                        .togetherWith(fadeOut(animationSpec = tween(300)))
                },
                label = "TopAppBarTitleAnimation"
            ) { targetTitle ->
                Text(
                    text = targetTitle,
                    style = CustomTypography.titleLarge,
                    color = mainwhite,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}