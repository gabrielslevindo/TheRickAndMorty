package com.example.therickandmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.therickandmorty.domain.dataclass.CharacterData

@Database(entities = [CharacterData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}
