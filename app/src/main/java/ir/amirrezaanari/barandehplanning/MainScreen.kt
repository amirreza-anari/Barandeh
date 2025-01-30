package ir.amirrezaanari.barandehplanning

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ir.amirrezaanari.barandehplanning.navigation.BottomNavigationBar
import ir.amirrezaanari.barandehplanning.navigation.NavigationGraph
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel


@Composable
fun MainScreen(plannerViewModel: PlannerViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues
                )

        ) {
            NavigationGraph(navController, plannerViewModel)
        }
    }
}
