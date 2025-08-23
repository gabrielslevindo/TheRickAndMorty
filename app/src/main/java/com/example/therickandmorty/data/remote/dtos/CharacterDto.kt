package com.example.therickandmorty.data.remote.dtos

import kotlinx.serialization.SerialName

// DTO para manter o formato Json dos dados que vem da Api

data class CharacterDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: String,
    @SerialName("species")
    val species: String,
    @SerialName("type")
    val type: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("location")
    val location: LocationDto? = null,
    @SerialName("image")
    val image: String,
    @SerialName("episode")
    val episode: List<String> = emptyList(),
    @SerialName("url")
    val url: String = "",
    @SerialName("created")
    val created: String = "",
)
