package ir.amirrezaanari.barandehplanning.ai.ui.components
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.LayoutDirection
//import com.colintheshots.twain.MarkdownText
//import ir.amirrezaanari.barandehplanning.R
//import ir.amirrezaanari.barandehplanning.ui.theme.CustomFontFamily
//import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
//
//@Composable
//fun MarkdownCustom(text: String) {
//    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//        MarkdownText(
//            markdown = text,
//            color = mainwhite,
//            style = TextStyle(fontFamily = CustomFontFamily),
//            textAlign = TextAlign.Start,
//            fontResource = R.font.yekanbakh
//            )
//    }
//}