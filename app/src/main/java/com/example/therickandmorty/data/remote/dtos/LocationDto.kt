package com.example.therickandmorty.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO para manter o formato Json dos dados que vem da Api

@Serializable
data class LocationDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
)
