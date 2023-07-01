package com.example.myapplication6.ui.address

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
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

// 데이터 모델 클래스
@Entity(tableName = "gallery_db")
data class ImageUrl(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
)

// 데이터 액세스 객체 (DAO)
@Dao
interface MyDataDao {
    @Query("SELECT * FROM gallery_db")
    fun getAllData(): LiveData<List<ImageUrl>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: ImageUrl)

    @Delete
    fun deleteData(data: ImageUrl)
}

// 데이터베이스 클래스
@Database(entities = [ImageUrl::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDataDao(): MyDataDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/*앱에서 데이터베이스 인스턴스를 가져오기
val database = MyDatabase.getDatabase(context)
val myDataDao = database.myDataDao()

// 데이터 추가
val newData = MyData(name = "John", age = 25)
myDataDao.insertData(newData)

// 데이터 조회
val allData: LiveData<List<MyData>> = myDataDao.getAllData()
allData.observe(viewLifecycleOwner) { dataList ->
    // 데이터 리스트 처리
}

// 데이터 삭제
val dataToDelete = // 선택한 데이터
    myDataDao.deleteData(dataToDelete)
*/