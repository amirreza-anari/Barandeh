package ir.amirrezaanari.barandehplanning.planning.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * [PlannerViewModel] is a ViewModel responsible for managing the UI-related data and logic
 * for the planner screen. It interacts with the [PlannerRepository] to fetch, insert, update,
 * and delete tasks. It also handles date selection and loading tasks for a specific date.
 *
 * @property repository The [PlannerRepository] used to interact with the underlying data source.
 */
class PlannerViewModel(
    private val repository: PlannerRepository
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _plannedTasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val plannedTasks: StateFlow<List<TaskEntity>> = _plannedTasks

    private val _completedTasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val completedTasks: StateFlow<List<TaskEntity>> = _completedTasks

    private val _totalPlannedTasks = MutableStateFlow(0)
    val totalPlannedTasks: StateFlow<Int> = _totalPlannedTasks

    private val _totalCompletedTasks = MutableStateFlow(0)
    val totalCompletedTasks: StateFlow<Int> = _totalCompletedTasks

    init {
        // Load tasks for the current date when the ViewModel is created
        loadTasks(_selectedDate.value)

        viewModelScope.launch {
            repository.getTotalPlannedTasks().collect {
                _totalPlannedTasks.value = it
            }
        }
        viewModelScope.launch {
            repository.getTotalCompletedTasks().collect {
                _totalCompletedTasks.value = it
            }
        }
    }

    suspend fun getLastNDaysStatistics(days: Int): String {
        val result = StringBuilder()
        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for (i in 0 until days) {
            val date = currentDate.minusDays(i.toLong())
            val dateString = date.format(dateFormatter)

            val plannedTasks = repository.getPlannedTasksForDate(dateString).first()
            val completedTasks = repository.getCompletedTasksForDate(dateString).first()

            // تبدیل تاریخ میلادی به شمسی با استفاده از PersianDate
            val persianDate = PersianDate().apply {
                setGrgYear(date.year)
                setGrgMonth(date.monthValue)
                setGrgDay(date.dayOfMonth)
            }
            val jalaliDate = "${persianDate.shYear}/${persianDate.shMonth}/${persianDate.shDay}"

            result.append("$jalaliDate:\n")
            result.append("برنامه های ریخته شده:\n")
            plannedTasks.forEach { task ->
                result.append("${task.title} از ساعت ${task.startTime} تا ${task.endTime}\n")
            }

            result.append("برنامه های انجام شده:\n")
            completedTasks.forEach { task ->
                result.append("${task.title} از ساعت ${task.startTime} تا ${task.endTime}\n")
            }

            result.append("\n") // فاصله بین روزها
        }

        return result.toString()
    }

    fun copyAllPlannedToCompleted() {
        viewModelScope.launch {
            val plannedTasks = _plannedTasks.value
            plannedTasks.forEach { task ->
                if (!task.isChecked) {
                    val completedTask = task.copy(
                        id = 0,
                        isPlanned = false,
                        parentId = task.id
                    )
                    repository.insertTask(completedTask)
                    repository.updateTask(task.copy(isChecked = true))
                }
            }
            loadTasks(_selectedDate.value)
        }
    }

    fun selectDate(date: LocalDate) {
        viewModelScope.launch {
            _selectedDate.value = date
            loadTasks(date)
        }
    }

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
            // Also insert the date if it doesn't exist
            repository.insertDate(DateEntity(task.date))
            // Reload tasks for the current date
            loadTasks(_selectedDate.value)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadTasks(_selectedDate.value) // Reload tasks after update
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadTasks(_selectedDate.value) // Reload tasks after deletion
        }
    }

    fun toggleTaskCheckStatus(task: TaskEntity) {
        viewModelScope.launch {
            if (task.isPlanned) {
                if (task.isChecked) {
                    // حذف کپی از انجام شده‌ها
                    repository.deleteCompletedCopies(task.id)
                } else {
                    // ایجاد کپی در انجام شده‌ها
                    val completedTask = task.copy(
                        id = 0,
                        isPlanned = false,
                        isChecked = false,
                        parentId = task.id
                    )
                    repository.insertTask(completedTask)
                }
                // آپدیت وضعیت تسک اصلی
                repository.updateTask(task.copy(isChecked = !task.isChecked))
                loadTasks(_selectedDate.value)
            }
        }
    }


    private fun loadTasks(date: LocalDate) {
        val dateString = date.toString()
        viewModelScope.launch {
            val plannedTasks = repository.getPlannedTasksForDate(dateString)
                .first()
                .sortedBy { it.startTime }

            val completedTasks = repository.getCompletedTasksForDate(dateString)
                .first()
                .sortedBy { it.startTime }

            val completedIds = completedTasks.map { it.parentId ?: 0 }.toSet()

            _plannedTasks.value = plannedTasks.map { task ->
                task.copy(isChecked = completedIds.contains(task.id))
            }

            _completedTasks.value = completedTasks
        }
    }
}

class PlannerViewModelFactory(
    private val repository: PlannerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlannerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlannerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}