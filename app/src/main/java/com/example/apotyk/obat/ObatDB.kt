package com.example.apotyk.obat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Obat::class],
    version = 1
)
abstract class ObatDB: RoomDatabase() {
    abstract fun obatDao() : ObatDao

    companion object {
        @Volatile private var instance : ObatDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ObatDB::class.java,
                "obat.db"
            ).build()
    }
}