package ir.amirrezaanari.barandehplanning.task_part

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

class PlannerViewModel(
    private val repository: PlannerRepository
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _plannedTasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val plannedTasks: StateFlow<List<TaskEntity>> = _plannedTasks

    private val _completedTasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val completedTasks: StateFlow<List<TaskEntity>> = _completedTasks

    init {
        // Load tasks for the current date when the ViewModel is created
        loadTasks(_selectedDate.value)
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
            val completedTasks = plannedTasks.map { task ->
                task.copy(id = 0, isPlanned = false) // ایجاد یک کپی جدید با `id = 0` و `isPlanned = false`
            }
            completedTasks.forEach { task ->
                repository.insertTask(task) // ذخیره تسک‌های جدید در دیتابیس
            }
            loadTasks(_selectedDate.value) // بارگذاری مجدد تسک‌ها
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

    private fun loadTasks(date: LocalDate) {
        val dateString = date.toString()
        viewModelScope.launch {
            try {
                // Use separate launches to prevent one collection from blocking the other
                launch {
                    repository.getPlannedTasksForDate(dateString).collect { tasks ->
                        _plannedTasks.value = tasks
                    }
                }
                launch {
                    repository.getCompletedTasksForDate(dateString).collect { tasks ->
                        _completedTasks.value = tasks
                    }
                }
            } catch (e: Exception) {
                // Add logging here
                e.printStackTrace()
            }
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