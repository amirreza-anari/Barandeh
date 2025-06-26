package ir.amirrezaanari.barandehplanning.planning.database

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
// --- Import های جدید برای Gemini ---
import ir.amirrezaanari.barandehplanning.planning.GeminiResponse
import ir.amirrezaanari.barandehplanning.planning.RetrofitInstance
import ir.amirrezaanari.barandehplanning.planning.TaskItem
import ir.amirrezaanari.barandehplanning.planning.VoiceRequest
import ir.amirrezaanari.barandehplanning.planning.voicetask.VoiceProcessingState
// --- Import های تم شما ---
import ir.amirrezaanari.barandehplanning.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PlannerViewModel(
    private val repository: PlannerRepository
) : ViewModel() {

    private val taskColors: List<Color> = listOf(
        red, orange, green, mint, cyan, blue, indigo, purple, pink, brown
    )

    // --- State Management for Voice Processing ---
    private val _voiceProcessingState = MutableStateFlow<VoiceProcessingState>(VoiceProcessingState.Idle)
    val voiceProcessingState: StateFlow<VoiceProcessingState> = _voiceProcessingState.asStateFlow()

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

    // --- START: بلوک کد جدید برای سرویس Gemini ---

    /**
     * Resets the voice processing state back to Idle. Called from the UI when the error dialog is dismissed.
     */
    fun resetVoiceProcessingState() {
        _voiceProcessingState.value = VoiceProcessingState.Idle
    }

    /**
     * Public function to be called from the UI to start processing the recognized text via Gemini.
     * @param text The voice command recognized by the Speech-to-Text engine.
     */
    fun processVoiceCommand(text: String) {
        if (text.isBlank() || text.startsWith("خطا")) {
            _voiceProcessingState.value = VoiceProcessingState.Error("لطفاً یک دستور معتبر بگویید.")
            return
        }
        _voiceProcessingState.value = VoiceProcessingState.Loading
        sendToGeminiWorker(text)
    }

    private fun sendToGeminiWorker(text: String) {
        val apiService = RetrofitInstance.api
        val request = VoiceRequest(text = text)

        apiService.getTasksFromText(request).enqueue(object : Callback<GeminiResponse> {
            override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
                if (response.isSuccessful) {
                    val geminiResponse = response.body()
                    if (geminiResponse != null && geminiResponse.tasks.isNotEmpty()) {
                        // Loop through the list of tasks from Gemini and add each one
                        geminiResponse.tasks.forEach { taskItem ->
                            createAndAddTask(taskItem)
                        }
                        _voiceProcessingState.value = VoiceProcessingState.Idle // Success
                    } else {
                        _voiceProcessingState.value = VoiceProcessingState.Error("وظیفه‌ای در متن شما پیدا نشد یا پاسخ سرور نامعتبر بود.")
                    }
                } else {
                    Log.e("PlannerViewModel", "API Error: ${response.code()} - ${response.errorBody()?.string()}")
                    _voiceProcessingState.value = VoiceProcessingState.Error("خطا در پردازش درخواست (${response.code()})")
                }
            }

            override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                Log.e("PlannerViewModel", "Network Failure", t)
                _voiceProcessingState.value = VoiceProcessingState.Error("مشکل در اتصال به شبکه: ${t.message}")
            }
        })
    }

    /**
     * Creates a TaskEntity from the structured data received from Gemini and calls the main addTask function.
     */
    private fun createAndAddTask(taskItem: TaskItem) {
        // Validate that essential fields are not empty before creating a task
        if (taskItem.task_name.isNotBlank() && taskItem.start_time.isNotBlank() && taskItem.end_time.isNotBlank()) {
            val newTask = TaskEntity(
                date = _selectedDate.value.toString(),
                title = taskItem.task_name,
                startTime = taskItem.start_time,
                endTime = taskItem.end_time,
                details = "",
                color = 0, // Let the main addTask handle color assignment
                isPlanned = true,
                isChecked = false,
                parentId = null
            )
            addTask(newTask) // Use your existing addTask function which already handles random colors
        } else {
            Log.w("PlannerViewModel", "Skipping task creation due to blank fields: $taskItem")
        }
    }

    // --- END: بلوک کد جدید برای سرویس Gemini ---


    // --- توابع موجود شما (بدون تغییر) ---

    suspend fun getLastNDaysStatistics(days: Int): String {
        val result = StringBuilder()
        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for (i in 0 until days) {
            val date = currentDate.minusDays(i.toLong())
            val dateString = date.format(dateFormatter)

            val plannedTasks = repository.getPlannedTasksForDate(dateString).first()
            val completedTasks = repository.getCompletedTasksForDate(dateString).first()

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
            result.append("\n")
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
            val taskWithColor = if (task.color == 0) {
                task.copy(color = taskColors.random().toArgb())
            } else {
                task
            }
            repository.insertTask(taskWithColor)
            repository.insertDate(DateEntity(taskWithColor.date))
            loadTasks(_selectedDate.value)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadTasks(_selectedDate.value)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadTasks(_selectedDate.value)
        }
    }

    fun toggleTaskCheckStatus(task: TaskEntity) {
        viewModelScope.launch {
            if (task.isPlanned) {
                if (task.isChecked) {
                    repository.deleteCompletedCopies(task.id)
                } else {
                    val completedTask = task.copy(
                        id = 0,
                        isPlanned = false,
                        isChecked = false,
                        parentId = task.id
                    )
                    repository.insertTask(completedTask)
                }
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