package com.devinou971.minesweeperandroid.storageclasses

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// This is the singleton that will be calling the database
@Database(entities = [GameData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameDataDAO(): GameDataDAO // This will have hte GameDataDao at the end

    companion object{ // This companion has the singleton
        @Volatile private var instance : AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase?{
            if(this.instance == null){
                synchronized(AppDatabase::class){
                    if(instance == null){
                        instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "MineSweeperDatabase").build()
                    }
                }
            }
            return this.instance
        }
    }
}