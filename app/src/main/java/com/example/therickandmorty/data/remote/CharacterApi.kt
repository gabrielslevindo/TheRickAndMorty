package com.example.therickandmorty.data.remote

import com.example.therickandmorty.data.remote.dtos.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("/api/character")
    suspend fun getCharacters(
        @Query("page") page: Int,
    ): CharacterResponseDto

    @GET("/api/character/")
    suspend fun getFilteredCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String?,
        @Query("status") status: String?,
    ): CharacterResponseDto
}
