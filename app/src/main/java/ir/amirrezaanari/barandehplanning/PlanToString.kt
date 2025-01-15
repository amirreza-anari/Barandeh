package ir.amirrezaanari.barandehplanning

import ir.amirrezaanari.barandehplanning.task_part.PlannerRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter

suspend fun getLastSevenDaysStatistics(repository: PlannerRepository): String {
    val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy") // فرمت تاریخ
    val currentDate = LocalDate.now() // تاریخ امروز
    val sevenDaysAgo = currentDate.minusDays(7) // تاریخ ۷ روز قبل

    val result = StringBuilder() // برای ذخیره‌ی نتیجه نهایی

    // حلقه برای ۷ روز گذشته
    for (i in 0 until 7) {
        val targetDate = sevenDaysAgo.plusDays(i.toLong()) // تاریخ مورد نظر
        val dateString = targetDate.format(dateFormatter) // تاریخ به صورت رشته

        // دریافت تسک‌های ریخته‌شده و انجام‌شده برای تاریخ مورد نظر
        val plannedTasks = repository.getPlannedTasksForDate(dateString).first()
        val completedTasks = repository.getCompletedTasksForDate(dateString).first()

        // افزودن تاریخ به نتیجه
        result.append("$dateString:\n")

        // افزودن برنامه‌های ریخته‌شده
        result.append("برنامه های ریخته شده:\n")
        if (plannedTasks.isEmpty()) {
            result.append("هیچ برنامه‌ای وجود ندارد.\n")
        } else {
            plannedTasks.forEach { task ->
                result.append("${task.title} از ساعت ${task.startTime} تا ${task.endTime}\n")
            }
        }

        // افزودن برنامه‌های انجام‌شده
        result.append("برنامه های انجام شده:\n")
        if (completedTasks.isEmpty()) {
            result.append("هیچ برنامه‌ای وجود ندارد.\n")
        } else {
            completedTasks.forEach { task ->
                result.append("${task.title} از ساعت ${task.startTime} تا ${task.endTime}\n")
            }
        }

        result.append("\n") // فاصله بین روزها
    }

    return result.toString() // بازگشت نتیجه نهایی
}