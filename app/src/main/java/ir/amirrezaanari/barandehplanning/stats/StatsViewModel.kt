package ir.amirrezaanari.barandehplanning.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ir.amirrezaanari.barandehplanning.planning.database.PlannerRepository
import ir.amirrezaanari.barandehplanning.planning.database.TaskEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Data class to hold all the state for our reports screen
data class ReportsUiState(
    val plannedVsCompleted: Pair<Int, Int> = 0 to 0,
    val weeklyTasks: List<TaskEntity> = emptyList(),
    val mostProductiveDate: LocalDate? = null,
    val tasksCompletedThisWeek: Int = 0
)

class ReportsViewModel(
    private val repository: PlannerRepository
) : ViewModel() {

    // Helper to get date strings
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Get completed tasks for the last 7 days
    private fun getCompletedTasksForLastWeek(): kotlinx.coroutines.flow.Flow<List<TaskEntity>> {
        val today = LocalDate.now()
        val startDate = today.minusDays(6).format(dateFormatter)
        val endDate = today.format(dateFormatter)

        return repository.getCompletedTasksForDateRange(startDate, endDate)
    }

    // Combine multiple flows from the repository into a single UI state
    val uiState: StateFlow<ReportsUiState> = combine(
        repository.getTotalPlannedTasks(),
        repository.getTotalCompletedTasks(),
        getCompletedTasksForLastWeek()
    ) { plannedCount, completedCount, weeklyTasks ->

        // Find the most productive day based on time spent
        // Find the most productive day based on time spent
        val mostProductiveDayString = weeklyTasks
            .groupBy { it.date }
            .maxByOrNull { (_, tasks) ->
                tasks.sumOf { task ->
                    calculateDurationMinutes(task.startTime, task.endTime).toLong()
                }
            }
            ?.key

        // --- MODIFIED HERE ---
        // We now parse the date string into a LocalDate object and pass it to the UI state.
        // We no longer format it into a string here.
        val mostProductiveDateObject = mostProductiveDayString?.let {
            LocalDate.parse(it, dateFormatter)
        }

        ReportsUiState(
            plannedVsCompleted = plannedCount to completedCount,
            weeklyTasks = weeklyTasks,
            mostProductiveDate = mostProductiveDateObject, // Pass the LocalDate object
            tasksCompletedThisWeek = weeklyTasks.size
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReportsUiState()
    )
}

// Helper function to calculate duration from "HH:mm" strings
private fun calculateDurationMinutes(startTime: String, endTime: String): Long {
    return try {
        val start = startTime.split(":").map { it.toInt() }
        val end = endTime.split(":").map { it.toInt() }
        val startMinutes = start[0] * 60L + start[1]
        val endMinutes = end[0] * 60L + end[1]
        // Handle overnight tasks if necessary, for now assume same day
        if (endMinutes < startMinutes) 0 else endMinutes - startMinutes
    } catch (e: Exception) {
        0L // Return 0 if time format is incorrect
    }
}


class ReportsViewModelFactory(
    private val repository: PlannerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}