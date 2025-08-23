package com.example.therickandmorty.data.remote.dtos

import kotlinx.serialization.SerialName

// DTO para manter o formato Json dos dados que vem da Api

data class CharacterResponseDto(
    @SerialName("info")
    val info: InfoDto,
    @SerialName("results")
    val results: List<CharacterDto>,
)
