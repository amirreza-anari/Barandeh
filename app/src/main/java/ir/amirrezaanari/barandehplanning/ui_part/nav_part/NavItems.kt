package ir.amirrezaanari.barandehplanning.ui_part.nav_part

import ir.amirrezaanari.barandehplanning.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int, // آیکون پیش‌فرض (انتخاب نشده)
    val iconSelected: Int, // آیکون انتخاب شده
    val title: String
) {
    object Home : BottomNavItem(
        "home",
        R.drawable.home, // آیکون خانه پیش‌فرض
        R.drawable.home_selected, // آیکون خانه انتخاب شده
        "خانه"
    )

    object AiAssistant : BottomNavItem(
        "aiassistant",
        R.drawable.ai, // آیکون هوشیار پیش‌فرض
        R.drawable.ai_selected, // آیکون هوشیار انتخاب شده
        "هوشیار"
    )
}
