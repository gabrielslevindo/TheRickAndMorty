package com.example.therickandmorty.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiService {
    private const val BASE_URL = "https://rickandmortyapi.com/"

    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    private val retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType()),
            ).build()

    val characterApi: CharacterApi by lazy {
        retrofit.create(CharacterApi::class.java)
    }
}
