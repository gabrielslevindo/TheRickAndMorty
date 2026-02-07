package com.example.therickandmorty.presentation.viewmodel.actions

sealed class SearchAction {
    data class ApplyFilters(
        val name: String?,
        val status: String?,
    ) : SearchAction()
}
