package ir.amirrezaanari.barandehplanning.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import ir.amirrezaanari.barandehplanning.planning.components.IconAndText
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary

@Composable
fun HomeScreen(viewModel: PlannerViewModel) {

    val quote = remember { getMotivationalQuote() }

    Scaffold(
        topBar = {
            HomeScreenTopAppBar()
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp, top = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\"$quote\"", color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                }
//                PagesSection(navController)
                StatsSection(viewModel)
                Spacer(Modifier.height(10.dp))
                NotesSection()
            }
        }

    }
}

@Composable
fun HomeScreenTopAppBar() {

    val showInfoDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "به برنده خوش آمدی!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Black
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = { showInfoDialog.value = true }
            ) {
                Icon(
                    Icons.Rounded.Info,
                    contentDescription = "App info",
                    modifier = Modifier.size(30.dp),
                    tint = mainwhite
                )
            }
        }
        if (showInfoDialog.value) {
            InfoDialog(
                onDismissRequest = {
                    showInfoDialog.value = false
                }
            )
        }
    }
}

@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit,
) {

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        text = "مشخصات",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )

                    InfoCombination(
                        icon = Icons.Rounded.Person,
                        title = "سازنده",
                        text = "امیررضا اناری"
                    )

                    InfoCombination(
                        icon = Icons.Rounded.School,
                        title = "مدرسه",
                        text = """
                        هنرستان سمپاد شهید بهشتی
                        دوره دوم، پایه یازدهم
                    """.trimIndent()
                    )

                    InfoCombination(
                        icon = Icons.Rounded.LocationCity,
                        title = "شهر",
                        text = "بیرجند"
                    )
                }
                item {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainwhite
                        )
                    ) {
                        Text("باشه")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCombination(
    icon: ImageVector,
    title: String,
    text: String
) {
    IconAndText(
        text = title,
        icon = icon
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}