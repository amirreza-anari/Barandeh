package ir.amirrezaanari.barandehplanning.task_part

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Data Entities
@Entity(tableName = "dates")
data class DateEntity(
    @PrimaryKey val date: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val title: String,
    val startTime: String,
    val endTime: String,
    val details: String,
    val color: Int,
    val isPlanned: Boolean
)

// Data Access Objects (DAO)
@Dao
interface DateDao {
    @Query("SELECT * FROM dates ORDER BY date DESC")
    fun getAllDates(): Flow<List<DateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDate(date: DateEntity)

    @Delete
    suspend fun deleteDate(date: DateEntity)
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE date = :date AND isPlanned = 1")
    fun getPlannedTasksForDate(date: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE date = :date AND isPlanned = 0")
    fun getCompletedTasksForDate(date: String): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}

// Database
@Database(entities = [DateEntity::class, TaskEntity::class], version = 1)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun dateDao(): DateDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: PlannerDatabase? = null

        fun getInstance(context: Context): PlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlannerDatabase::class.java,
                    "planner_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Repository
class PlannerRepository(private val dateDao: DateDao, private val taskDao: TaskDao) {
    // Date operations
    fun getAllDates() = dateDao.getAllDates()

    suspend fun insertDate(date: DateEntity) {
        dateDao.insertDate(date)
    }

    // Task operations
    fun getPlannedTasksForDate(date: String) = taskDao.getPlannedTasksForDate(date)

    fun getCompletedTasksForDate(date: String) = taskDao.getCompletedTasksForDate(date)

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}