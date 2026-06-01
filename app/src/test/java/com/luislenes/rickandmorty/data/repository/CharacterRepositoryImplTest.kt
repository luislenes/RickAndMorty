package com.luislenes.rickandmorty.data.repository

import com.luislenes.rickandmorty.data.remote.api.RickAndMortyApi
import com.luislenes.rickandmorty.data.remote.dto.CharacterDto
import com.luislenes.rickandmorty.data.remote.dto.CharacterResponseDto
import com.luislenes.rickandmorty.data.remote.dto.LocationDto
import com.luislenes.rickandmorty.model.Character
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CharacterRepositoryImplTest {

    private val api: RickAndMortyApi = mockk()
    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        repository = CharacterRepositoryImpl(api)
    }

    // region getCharacters

    @Test
    fun `getCharacters returns Success with mapped characters when API succeeds`() = runTest {
        coEvery { api.getCharacters() } returns CharacterResponseDto(listOf(characterDto()))

        val result = repository.getCharacters()

        assertTrue(result.isSuccess)
        assertEquals(listOf(expectedCharacter()), result.getOrNull())
    }

    @Test
    fun `getCharacters returns Failure when API throws an exception`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { api.getCharacters() } throws exception

        val result = repository.getCharacters()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getCharacters calls the API exactly once`() = runTest {
        coEvery { api.getCharacters() } returns CharacterResponseDto(emptyList())

        repository.getCharacters()

        coVerify(exactly = 1) { api.getCharacters() }
    }

    // endregion

    // region getCharacterById

    @Test
    fun `getCharacterById returns Success with mapped character when API succeeds`() = runTest {
        coEvery { api.getCharacterById(1) } returns characterDto()

        val result = repository.getCharacterById(1)

        assertTrue(result.isSuccess)
        assertEquals(expectedCharacter(), result.getOrNull())
    }

    @Test
    fun `getCharacterById returns Failure when API throws an exception`() = runTest {
        val exception = RuntimeException("Not found")
        coEvery { api.getCharacterById(99) } throws exception

        val result = repository.getCharacterById(99)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getCharacterById passes the correct id to the API`() = runTest {
        coEvery { api.getCharacterById(42) } returns characterDto(id = 42)

        repository.getCharacterById(42)

        coVerify(exactly = 1) { api.getCharacterById(42) }
    }

    // endregion

    // region DTO mapper

    @Test
    fun `toDomain maps id correctly`() = runTest {
        coEvery { api.getCharacters() } returns CharacterResponseDto(listOf(characterDto(id = 7)))

        val character = repository.getCharacters().getOrNull()?.first()

        assertEquals(7, character?.id)
    }

    @Test
    fun `toDomain maps image url to imageUrl field`() = runTest {
        val dto = characterDto().copy(image = "https://example.com/rick.png")
        coEvery { api.getCharacters() } returns CharacterResponseDto(listOf(dto))

        val character = repository.getCharacters().getOrNull()?.first()

        assertEquals("https://example.com/rick.png", character?.imageUrl)
    }

    @Test
    fun `toDomain maps origin name from nested LocationDto`() = runTest {
        val dto = characterDto().copy(origin = LocationDto("Bird World"))
        coEvery { api.getCharacters() } returns CharacterResponseDto(listOf(dto))

        val character = repository.getCharacters().getOrNull()?.first()

        assertEquals("Bird World", character?.origin)
    }

    @Test
    fun `toDomain maps location name from nested LocationDto`() = runTest {
        val dto = characterDto().copy(location = LocationDto("Citadel of Ricks"))
        coEvery { api.getCharacters() } returns CharacterResponseDto(listOf(dto))

        val character = repository.getCharacters().getOrNull()?.first()

        assertEquals("Citadel of Ricks", character?.location)
    }

    @Test
    fun `toDomain maps all fields correctly`() = runTest {
        coEvery { api.getCharacters() } returns CharacterResponseDto(listOf(characterDto()))

        val character = repository.getCharacters().getOrNull()?.first()

        assertEquals(expectedCharacter(), character)
    }

    // endregion

    // region Helpers

    private fun characterDto(id: Int = 1) = CharacterDto(
        id       = id,
        name     = "Rick Sanchez",
        status   = "Alive",
        species  = "Human",
        image    = "https://example.com/rick.png",
        gender   = "Male",
        type     = "",
        origin   = LocationDto("Earth (C-137)"),
        location = LocationDto("Citadel of Ricks")
    )

    private fun expectedCharacter(id: Int = 1) = Character(
        id       = id,
        name     = "Rick Sanchez",
        status   = "Alive",
        species  = "Human",
        imageUrl = "https://example.com/rick.png",
        gender   = "Male",
        type     = "",
        origin   = "Earth (C-137)",
        location = "Citadel of Ricks"
    )

    // endregion
}

