package com.example.therickandmorty.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO para manter o formato Json dos dados que vem da Api

@Serializable
data class InfoDto(
    @SerialName("count")
    val count: Int,
    @SerialName("pages")
    val pages: Int,
    @SerialName("next")
    val next: String?,
    @SerialName("prev")
    val prev: String?,
)
