package com.example.therickandmorty.core.domain

class DataException(
    val error: DataError,
) : Exception()
