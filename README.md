# Rick & Morty — Android App

Android application that consumes the [Rick and Morty API](https://rickandmortyapi.com/api) and displays a list of characters with navigation to a detail screen. The project demonstrates clean architecture, reactive state management and solid technical decisions in modern Kotlin.

---

## Screenshots

| Character List | Character Detail |
|---|---|
| `CharacterListScreen` | `CharacterDetailScreen` |

---

## Technologies

### Language
| Technology | Version | Justification |
|---|---|---|
| **Kotlin** | 2.2.x | Official Android language. Null safety, native coroutines, expressiveness and conciseness. |

### Architecture
| Pattern | Justification |
|---|---|
| **MVVM + Clean Architecture** | Clear separation of concerns across 3 layers (data, domain, presentation). Each layer can be tested in isolation. |
| **StateFlow** | Reactive state emitter, lifecycle-aware when combined with `collectAsStateWithLifecycle()`. Replaces `LiveData` while being fully idiomatic Kotlin. |
| **Sealed class UiState** | Represents mutually exclusive states (`Loading`, `Success`, `Error`). The compiler enforces exhaustive `when` expressions, eliminating impossible states. |
| **Use Cases** | Each use case has a single responsibility. They act as the correct extension point for future business logic (filtering, sorting) without touching the repository or ViewModel. |

### UI
| Technology | Version | Justification |
|---|---|---|
| **Jetpack Compose** | BOM 2026.02.01 | Google's modern declarative UI toolkit. Eliminates XML + View binding boilerplate and makes building reactive UI directly from state straightforward. |
| **Material 3** | (via BOM) | Google's updated design system with dynamic theming and accessible components. |
| **Navigation Compose** | 2.9.0 | Official navigation solution for Compose. Manages back stack, route arguments and screen return types in a type-safe manner. |

### Networking
| Technology | Version | Justification |
|---|---|---|
| **Retrofit 2** | 2.11.0 | Industry-standard library for consuming REST APIs on Android. Native support for `suspend fun` without additional adapters. |
| **OkHttp Logging Interceptor** | 4.12.0 | Allows inspecting requests/responses in Logcat during development. Can be excluded or reduced in production. |
| **Gson Converter** | 2.11.0 | Automatic JSON serialization/deserialization to DTOs with `@SerializedName` annotations. |

### Images
| Technology | Version | Justification |
|---|---|---|
| **Coil 3** | 3.2.0 | 100% Kotlin image loading library with native support for Coroutines and Compose (`AsyncImage`). Lighter than Glide/Picasso and better integrated with the Kotlin ecosystem. |

### Dependency Injection
| Technology | Version | Justification |
|---|---|---|
| **Koin** | 4.0.4 | DI framework with no annotation processing (no KSP/KAPT required), simplifying the build and reducing compile times. Its Kotlin DSL is readable and straightforward. The new `viewModelOf` in Koin 4.x handles `SavedStateHandle` automatically. |

---

## Project Structure

```
com.luislenes.rickandmorty/
│
├── data/                               # Data layer
│   ├── remote/
│   │   ├── api/RickAndMortyApi.kt      # Retrofit interface
│   │   └── dto/                        # Network models (DTOs)
│   │       ├── CharacterDto.kt
│   │       ├── CharacterResponseDto.kt
│   │       └── LocationDto.kt
│   └── repository/
│       └── CharacterRepositoryImpl.kt  # Implementation + DTO → Domain mapping
│
├── model/                              # Domain layer
│   ├── Character.kt                    # Pure domain entity (no frameworks)
│   ├── repository/
│   │   └── CharacterRepository.kt      # Repository contract
│   └── usecase/
│       ├── GetCharactersUseCase.kt
│       └── GetCharacterByIdUseCase.kt
│
├── di/                                 # Dependency injection (Koin)
│   ├── NetworkModule.kt
│   ├── RepositoryModule.kt
│   ├── DomainModule.kt
│   └── ViewModelModule.kt
│
├── presentation/                       # Presentation layer
│   ├── CharactersUiState.kt
│   ├── CharacterViewModel.kt
│   ├── navigation/
│   │   ├── Screen.kt                   # Route definitions
│   │   └── AppNavGraph.kt              # Main NavHost
│   ├── ui/
│   │   ├── CharacterListScreen.kt
│   │   └── components/
│   │       ├── StatusBadge.kt          # Reusable component
│   │       └── StatusBadgeColorProvider.kt
│   └── detail/
│       ├── CharacterDetailUiState.kt
│       ├── CharacterDetailViewModel.kt
│       └── CharacterDetailScreen.kt
│
└── RickAndMortyApp.kt                  # Application with Koin bootstrap
```

---

## Data Flow

```
API (Retrofit)
  └─▶ CharacterDto (data/dto)
        └─▶ CharacterRepositoryImpl.toDomain()
              └─▶ Character (domain entity)
                    └─▶ UseCase
                          └─▶ ViewModel (StateFlow<UiState>)
                                └─▶ Composable (collectAsStateWithLifecycle)
```

---

## Improvement Suggestions

### 1. Pagination with Paging 3
The API returns the first page (~20 characters). Integrating `androidx.paging:paging-compose` would enable infinite scroll with progressive loading, per-page state management and in-memory caching without manual offset logic.

### 2. Local cache with Room
Adding a Room database between the API and the repository, implementing an **offline-first** pattern: show cached data immediately and refresh in the background. This improves the offline experience and reduces repeated network calls.

### 3. Unit and integration tests
- `CharacterViewModel`: test with `TestCoroutineDispatcher` and a fake repository.
- `GetCharactersUseCase`: verify it correctly delegates to the repository.
- `CharacterRepositoryImpl`: test the DTO → domain mapper.
- UI: composable tests with `ComposeTestRule` for all 3 `UiState` states.

### 4. More granular error handling
Currently any `Throwable` is caught. An improvement would be to type domain errors with a sealed class `AppError` (e.g., `NetworkError`, `TimeoutError`, `ServerError`) to show more descriptive user messages and differentiated retry policies.

### 5. Filters and search
The API supports query params (`?name=`, `?status=`, `?species=`). Adding a search bar and filter chips directly in the list would be the next natural feature, reusing the `CharacterRepository` with optional parameters.

### 6. Inject the Dispatcher into the ViewModel
To make testing easier, the `CoroutineDispatcher` should be injected via Koin (`Dispatchers.IO` in production, `TestCoroutineDispatcher` in tests) instead of relying on `viewModelScope`'s default dispatcher.

### 7. ProGuard / R8 for production
Enable `isMinifyEnabled = true` in the `release` build type and add the necessary ProGuard rules for Gson, Retrofit and Koin to reduce APK size.
