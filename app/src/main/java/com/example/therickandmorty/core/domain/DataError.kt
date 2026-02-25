package com.example.therickandmorty.core.domain

sealed interface DataError {
    enum class Remote : DataError {
        NO_INTERNET,
        SERVER,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        UNKNOWN,
    }

    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN,
    }
}
