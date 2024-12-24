package ir.amirrezaanari.barandehplanning.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import androidx.room.Database
import androidx.room.RoomDatabase
import android.app.Application
import androidx.room.Room
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "planned_tasks")
data class PlannedTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val startTime: String,
    val endTime: String
)

@Entity(tableName = "completed_tasks")
data class CompletedTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val startTime: String,
    val endTime: String
)


@Dao
interface TaskDao {

    @Insert
    suspend fun insertPlannedTask(task: PlannedTask)

    @Insert
    suspend fun insertCompletedTask(task: CompletedTask)

    @Query("SELECT * FROM planned_tasks")
    suspend fun getAllPlannedTasks(): List<PlannedTask>

    @Query("SELECT * FROM completed_tasks")
    suspend fun getAllCompletedTasks(): List<CompletedTask>

    @Update
    suspend fun updatePlannedTask(task: PlannedTask)

    @Update
    suspend fun updateCompletedTask(task: CompletedTask)

    @Delete
    suspend fun deletePlannedTask(task: PlannedTask)

    @Delete
    suspend fun deleteCompletedTask(task: CompletedTask)
}



@Database(entities = [PlannedTask::class, CompletedTask::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}



class TaskApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }
}





class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    private val _plannedTasks = MutableStateFlow<List<PlannedTask>>(emptyList())
    val plannedTasks: StateFlow<List<PlannedTask>> = _plannedTasks

    private val _completedTasks = MutableStateFlow<List<CompletedTask>>(emptyList())
    val completedTasks: StateFlow<List<CompletedTask>> = _completedTasks

    init {
        loadPlannedTasks()
        loadCompletedTasks()
    }

    private fun loadPlannedTasks() {
        viewModelScope.launch {
            _plannedTasks.value = dao.getAllPlannedTasks().sortedBy { task ->
                task.startTime.toTime()
            }
        }
    }

    private fun loadCompletedTasks() {
        viewModelScope.launch {
            _completedTasks.value = dao.getAllCompletedTasks().sortedBy { it.startTime.toTime() }
        }
    }

    fun addPlannedTask(task: PlannedTask) {
        viewModelScope.launch {
            dao.insertPlannedTask(task)
            loadPlannedTasks() // بعد از اضافه کردن، لیست را دوباره بارگذاری کن
        }
    }

    fun addCompletedTask(task: CompletedTask) {
        viewModelScope.launch {
            dao.insertCompletedTask(task)
            loadCompletedTasks() // بعد از اضافه کردن، لیست را دوباره بارگذاری کن
        }
    }

}




class TaskViewModelFactory(private val dao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun String.toTime(): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))
}