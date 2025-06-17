package ir.amirrezaanari.barandehplanning.navigation

import ir.amirrezaanari.barandehplanning.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val iconSelected: Int,
    val title: String
) {

    data object Home : BottomNavItem(
        "home",
        R.drawable.home,
        R.drawable.home_selected,
        "خانه"
    )

    data object Planning: BottomNavItem(
        route = "planning",
        icon = R.drawable.planning,
        iconSelected = R.drawable.planning_selected,
        title = "برنامه ریزی"
    )

    data object Tools: BottomNavItem(
        route = "tools",
        icon = R.drawable.tools,
        iconSelected = R.drawable.tools_selected,
        title = "ابزارها"
    )

    data object AiAssistant : BottomNavItem(
        "aiassistant",
        R.drawable.ai,
        R.drawable.ai_selected,
        "هوشیار"
    )
}
