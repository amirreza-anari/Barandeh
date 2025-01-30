package ir.amirrezaanari.barandehplanning.notes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val color: Int, // مقدار رنگ می‌تواند یک مقدار هگز باشد مانند #FF5733
    val content: String,
    val timestamp: String = getPersianDate()
)

fun getPersianDate(): String {
    val persianDate = PersianDate()
    val format = PersianDateFormat("d / m / Y", PersianDateFormat.PersianDateNumberCharacter.FARSI)
    return format.format(persianDate)
}


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Update // روش استاندارد برای بروزرسانی یادداشت
    suspend fun updateNote(note: NoteEntity)
}

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = AppDatabase.getDatabase(application).noteDao()
    private val _allNotes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val allNotes: StateFlow<List<NoteEntity>> = _allNotes.asStateFlow()

    init {
        viewModelScope.launch {
            noteDao.getAllNotes().collect {
                _allNotes.value = it
            }
        }
    }

    fun addNote(title: String, color: Int, content: String) {
        viewModelScope.launch {
            val note = NoteEntity(
                title = title,
                color = color,
                content = content,
                timestamp = getPersianDate() // ذخیره تاریخ شمسی هنگام اضافه کردن یادداشت
            )
            noteDao.insertNote(note)
        }
    }


    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            noteDao.updateNote(note) // متد جدید برای ویرایش یادداشت
        }
    }

}

class NoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
