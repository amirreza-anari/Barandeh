package ir.amirrezaanari.barandehplanning.planning.database

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
    val isPlanned: Boolean,
    val isChecked: Boolean = false,
    val parentId: Long? = null
)

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

    @Query("SELECT COUNT(*) FROM tasks WHERE isPlanned = 1")
    fun getTotalPlannedTasks(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE isPlanned = 0")
    fun getTotalCompletedTasks(): Flow<Int>

    @Query("DELETE FROM tasks WHERE parentId = :parentId AND isPlanned = 0")
    suspend fun deleteCompletedCopies(parentId: Long)

    @Query("SELECT * FROM tasks WHERE parentId = :parentId AND isPlanned = 0 LIMIT 1")
    suspend fun findCompletedCopy(parentId: Long): TaskEntity?
}

@Database(entities = [DateEntity::class, TaskEntity::class], version = 2)
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
                )
                    .fallbackToDestructiveMigration() // افزودن این خط
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class PlannerRepository(private val dateDao: DateDao, private val taskDao: TaskDao) {
//    fun getAllDates() = dateDao.getAllDates()

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

    fun getTotalPlannedTasks() = taskDao.getTotalPlannedTasks()

    fun getTotalCompletedTasks() = taskDao.getTotalCompletedTasks()

    suspend fun deleteCompletedCopies(parentId: Long) {
        taskDao.deleteCompletedCopies(parentId)
    }

}