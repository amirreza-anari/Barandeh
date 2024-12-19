package ir.amirrezaanari.barandehplanning.ui_part

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val markdown = """
# نمونه متن فارسی

این یک متن نمونه به زبان فارسی است که با استفاده از Markdown نوشته شده است. در ادامه، یک جدول نمونه نیز قرار داده شده است.

## جدول نمونه

| نام       | سن | شغل          |
|-----------|----|---------------|
| علی       | ۲۵ | مهندس نرم‌افزار |
| زهرا      | ۳۰ | طراح گرافیک  |
| محمد      | ۲۷ | مدیر پروژه   |

این جدول شامل سه ستون است: نام، سن، و شغل. هر ردیف اطلاعات مربوط به یک فرد را نمایش می‌دهد.

""".trimIndent()

        LazyColumn {
            item {
//                Markdown(markdown)
                MarkdownCustom(markdown)
            }
        }

    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Profile Screen", style = MaterialTheme.typography.bodyLarge)
    }
}

