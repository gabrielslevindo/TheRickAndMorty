package com.example.therickandmorty.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://rickandmortyapi.com"

    private val gson = GsonBuilder().create()

    private val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    val characterApi: CharacterApi by lazy { retrofit.create(CharacterApi::class.java) }
}
