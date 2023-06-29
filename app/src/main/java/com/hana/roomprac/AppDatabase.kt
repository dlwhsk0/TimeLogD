package com.hana.roomprac

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomMemo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomMemoDAO(): RoomMemoDAO

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries() // 메인 쓰레드에서 접근가능하도록
                    .build()
            }
            return instance
        }
    }
}
