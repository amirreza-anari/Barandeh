package ir.amirrezaanari.barandehplanning.navigation

import ir.amirrezaanari.barandehplanning.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val iconSelected: Int,
    val title: String
) {

    object Home : BottomNavItem(
        "home",
        R.drawable.home,
        R.drawable.home_selected,
        "خانه"
    )

    object Planning: BottomNavItem(
        route = "planning",
        icon = R.drawable.planning,
        iconSelected = R.drawable.planning_selected,
        title = "برنامه ریزی"
    )

    object AiAssistant : BottomNavItem(
        "aiassistant",
        R.drawable.ai,
        R.drawable.ai_selected,
        "هوشیار"
    )
}
