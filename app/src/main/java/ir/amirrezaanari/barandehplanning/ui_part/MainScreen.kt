package ir.amirrezaanari.barandehplanning.ui_part

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues -> // دریافت فضای خالی برای محتوای اصلی
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // اعمال padding برای در نظر گرفتن فضای bottomBar
        ) {
            NavigationGraph(navController)
        }
    }
}
