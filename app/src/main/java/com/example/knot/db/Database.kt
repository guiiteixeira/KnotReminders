package com.example.knot.db

import android.content.Context
import androidx.room.Room

object Database {

    @get:Synchronized
    private var db: AppDatabase? = null
        private get
        private set

    fun getDatabase(context: Context): AppDatabase?{
        if (db == null){
            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-name"
            ).build()
        }
        return db
    }

}