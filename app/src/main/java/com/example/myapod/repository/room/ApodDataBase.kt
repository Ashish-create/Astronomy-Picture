package com.example.myapod.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [ApodEntityClass::class], version = 1, exportSchema = false)
abstract class ApodDataBase : RoomDatabase() {

    abstract fun apodDao(): ApodDAO

    companion object {

        @Volatile
        private var INSTANCE: ApodDataBase? = null

        fun getDataseClient(context: Context): ApodDataBase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, ApodDataBase::class.java, "APOD_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }

}