# Rick & Morty App

Aplicativo Android desenvolvido com **Jetpack Compose, Coroutines, Retrofit, Koin, Room, Voyager e Paging 3**.  
Ele consome a API pública do Rick & Morty, permite listar personagens, visualizar detalhes e marcar favoritos localmente.

---

## 🌟 Funcionalidades

- 📋 **Lista de personagens** com nome e imagem  
- 🔍 **Filtros** por nome e status do personagem  
- 📌 **Tela de detalhes** com informações: species, gender, type  
- ⭐ **Favoritos**: marcar e desmarcar personagens favoritos  
- 🧱 **Persistência local** usando Room Database  
- ⚡ **Paginação** para carregar personagens de forma eficiente  
- 🧪 **Testes unitários** para repository e viewmodels  

---

## 🧱 Arquitetura

O projeto segue a **Clean Architecture** com separação clara de responsabilidades:

```
presentation/
├── screens/ -> Composables da UI (Jetpack Compose)
├── state/ -> Gerenciamento de estado da UI
└── viewmodel/ -> ViewModels com lógica da UI
domain/
├── dataclass/ -> Entidades de domínio
├── repository/ -> Interfaces de repositórios
└── usecase/ -> Casos de uso / regras de negócio
data/
├── local/ -> Implementação Room (DAO, Entity)
├── remote/ -> Retrofit client e DTOs
└── repository/ -> Implementação concreta dos repositórios

```


---

## 📄 Resumo das Classes

- **CharacterRepository**: interface para buscar personagens da API e manipular favoritos  
- **CharacterRepositoryImpl**: implementação do repositório usando API e Room  
- **FakeCharacterRepository**: mock para testes unitários  
- **CharacterListViewModel**: gerencia a lista de personagens, filtros e paginação  
- **FavoritesViewModel**: gerencia os personagens favoritos, inserção e remoção  
- **StateView**: classe de estado para representar Loading, Success e Error na UI  
- **CharacterDao**: DAO do Room para salvar personagens favoritos  
- **CharacterData**: data class de domínio  
- **CharacterDto / CharacterResponseDto / InfoDto / OriginDto / LocationDto**: data classes que representam os dados da API  
- **Screens (Compose)**: `CharacterListScreen`, `CharacterDetailScreen`, `FavoritesScreen`  
- **Testes**: unitários para repository (`CharacterRepositoryTest`) e viewmodels (`CharacterListViewModelTest`, `FavoritesViewModelTest`)  

---

## 🧪 Tecnologias Utilizadas

| Categoria             | Tecnologia / Biblioteca                                | Documentação / Link                                                          |
|----------------------|--------------------------------------------------------|-----------------------------------------------------------------------------|
| UI                   | Jetpack Compose, Material3, AndroidX Activity Compose  | [Docs](https://developer.android.com/jetpack/compose)                       |
| UI                   | Coil (Carregamento de imagens)                         | [Docs](https://coil-kt.github.io/coil/)                                     |
| Estado / ViewModel   | AndroidX Lifecycle ViewModel + StateFlow               | [Docs](https://developer.android.com/topic/libraries/architecture/viewmodel)|
| Paginação            | Paging 3 (runtime + compose)                           | [Docs](https://developer.android.com/topic/libraries/architecture/paging)   |
| DI                   | Koin (core, android, compose)                          | [Docs](https://insert-koin.io/)                                             |
| Networking           | Retrofit + Gson Converter                               | [Docs Retrofit](https://square.github.io/retrofit/) / [Gson](https://github.com/google/gson) |
| Logging              | OkHttp + Logging Interceptor                            | [Docs](https://square.github.io/okhttp/)                                     |
| Serialização         | Kotlin Serialization JSON                               | [Docs](https://kotlinlang.org/docs/serialization.html)                       |
| Coroutines            | Kotlin Coroutines Core + Android                        | [Docs](https://kotlinlang.org/docs/coroutines-overview.html)                |
| Banco de Dados       | Room Database + Kapt Compiler                           | [Docs](https://developer.android.com/jetpack/androidx/releases/room)        |
| Testes Unitários     | JUnit 4/5, MockK, Coroutine Test, Core Testing         | [JUnit](https://junit.org/junit5/), [MockK](https://mockk.io/)              |
| Voyager              | Voyager Navigator + ScreenModel + Koin integration     | [Docs](https://voyager.adevinta.com/)                                       |
| Android Core         | androidx.core.ktx, lifecycle.runtime, activity          | [Docs](https://developer.android.com/kotlin/ktx)                             |

---

## 🔧 Instalação e Execução

Pré-requisitos:

- Android Studio Flamingo ou superior  
- SDK Android 34+  
- Conexão com internet (para API)  

```

git clone https://github.com/seu-usuario/RickAndMortyApp.git
cd RickAndMortyApp

# Abrir no Android Studio
# 1️⃣ Fazer "Clean Project": Build -> Clean Project
# 2️⃣ Fazer "Sync Project with Gradle Files": File -> Sync Project with Gradle Files

# Rodar o app
Run ▶️

```
***Comandos Gradle funcionais:***

```

./gradlew build
./gradlew connectedCheck
./gradlew lintKotlin

```

***🌐 Fonte dos Dados***
* API pública do Rick & Morty: https://rickandmortyapi.com

# 🧠 Aprendizados e Desafios
- Integração do Retrofit com Coroutines e Jetpack Compose
- Controle de estado assíncrono com StateFlow e ViewModel
- Paginação eficiente com Paging 3 + Compose
- Armazenamento de favoritos com Room
- Uso prático da Clean Architecture no mundo real
- Testes unitários para repository e viewmodels

# 🛠️ Melhorias Futuras
- Suporte offline completo com cache da API
- Internacionalização (i18n)
- Mais testes instrumentados e UI

***📸 Capturas de Tela***



🧑‍💻 Autor
Gabriel Levindo – [GitHub](https://github.com/gabrielslevindo) – [LinkedIn](www.linkedin.com/in/gabrielslevindo)

***License***

```

The MIT License (MIT)
Copyright (c) 2023 Gabriel Levindo. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.

```
