package com.example.therickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.therickandmorty.model.dataclass.CharacterData
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAll(): Flow<List<CharacterData>>

    @Query(
        """
        SELECT * FROM characters
        WHERE (:name IS NULL OR name LIKE '%' || :name || '%')
        AND (:status IS NULL OR status = :status)
    """,
    )
    fun getFiltered(
        name: String?,
        status: String?,
    ): Flow<List<CharacterData>>

    @Insert
    suspend fun insertAll(characters: List<CharacterData>)

    @Insert
    suspend fun insertCharacter(character: CharacterData)

    @Query("DELETE FROM characters WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Int)

    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): CharacterData?

    @Query("SELECT * FROM characters")
    fun getAllFavorites(): Flow<List<CharacterData>>

    @Query("DELETE FROM characters")
    suspend fun clearAll()
}
