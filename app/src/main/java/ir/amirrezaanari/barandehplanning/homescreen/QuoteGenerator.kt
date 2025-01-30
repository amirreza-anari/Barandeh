package ir.amirrezaanari.barandehplanning.homescreen

import java.time.LocalDate

val motivationalQuotes = listOf(
    "هر روز یک شروع تازه است!",
    "امروز بهتر از دیروز باش!",
    "تو قوی‌تر از چیزی هستی که فکرش را می‌کنی!",
    "مسیر موفقیت با اولین قدم شروع می‌شود!",
    "هر چالشی فرصتی برای رشد است!",
    "هیچ‌وقت تسلیم نشو!",
    "رویایت را دنبال کن، حتی اگر سخت باشد!",
    "از شکست‌ها درس بگیر، نه ناامید!",
    "موفقیت، تکرار کارهای کوچک است!",
    "ذهن مثبت، زندگی مثبت می‌سازد!"
)

// تابع بازگشتی برای دریافت جمله انگیزشی روز
fun getMotivationalQuote(index: Int = LocalDate.now().dayOfYear % motivationalQuotes.size): String {
    return if (index < 0) getMotivationalQuote(index + motivationalQuotes.size)
    else motivationalQuotes[index % motivationalQuotes.size]
}
