package com.example.therickandmorty.domain.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterData(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val species: String,
    val status: String,
    val gender: String,
    val type: String,
)
