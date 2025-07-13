package ir.amirrezaanari.barandehplanning.tools

import ir.amirrezaanari.barandehplanning.R

data class Tool(
    val title: String,
    val image: Int,
    val route: String
)

val toolsList = listOf(
    Tool(
        title = "یادداشت",
        image = R.drawable.emptynote,
        route = "notes"
    ),
    Tool(
        title = "ذهن آگاهی",
        image = R.drawable.mindfulness,
        route = "mindfulness"
    ),
    Tool(
        title = "پومودورو",
        image = R.drawable.pomodoro,
        route = "pomodoro"
    )
)