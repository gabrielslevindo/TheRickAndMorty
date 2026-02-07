package com.example.therickandmorty.data.local.extensions

import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.model.dataclass.CharacterData

fun CharacterDto.toData(): CharacterData =
    CharacterData(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        image = this.image,
        gender = this.gender,
        type = this.type,
    )

fun CharacterData.toDto(): CharacterDto =
    CharacterDto(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        image = this.image,
        gender = this.gender,
        type = this.type,
    )
