package com.example.therickandmorty.di

import androidx.room.Room
import com.example.therickandmorty.data.local.AppDatabase
import com.example.therickandmorty.data.remote.ApiService
import com.example.therickandmorty.data.remote.CharacterApi
import com.example.therickandmorty.model.repository.CharacterRepository
import com.example.therickandmorty.model.repository.CharacterRepositoryImpl
import com.example.therickandmorty.model.usecase.LoadListUseCaseInt
import com.example.therickandmorty.model.usecase.LoadListUseCaseIntImpl
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import com.example.therickandmorty.presentation.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val modules =
    module {
        single<CharacterApi> { ApiService.characterApi }

        single {
            Room
                .databaseBuilder(
                    androidContext(),
                    AppDatabase::class.java,
                    "rick_and_morty_db",
                ).build()
        }
        single { get<AppDatabase>().characterDao() }
        single<CharacterRepository> {
            CharacterRepositoryImpl(
                characterApi = get(),
                characterDao = get(),
            )
        }
        single<LoadListUseCaseInt> {
            LoadListUseCaseIntImpl(
                repository = get(),
            )
        }

        factory { CharacterListViewModel(loadListUseCaseInt = get()) }
        factory { SearchViewModel(loadListUseCaseInt = get()) }
        factory { FavoritesViewModel(repository = get()) }
    }
