package com.luislenes.rickandmorty.model.usecase

import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    private val repository: CharacterRepository = mockk()
    private lateinit var useCase: GetCharactersUseCase

    @Before
    fun setUp() {
        useCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `invoke returns Success with characters list from repository`() = runTest {
        val characters = listOf(fakeCharacter())
        coEvery { repository.getCharacters() } returns Result.success(characters)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(characters, result.getOrNull())
    }

    @Test
    fun `invoke returns empty list when repository returns empty Success`() = runTest {
        coEvery { repository.getCharacters() } returns Result.success(emptyList())

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(emptyList<Character>(), result.getOrNull())
    }

    @Test
    fun `invoke returns Failure when repository returns Failure`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { repository.getCharacters() } returns Result.failure(exception)

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke delegates to repository exactly once`() = runTest {
        coEvery { repository.getCharacters() } returns Result.success(emptyList())

        useCase()

        coVerify(exactly = 1) { repository.getCharacters() }
    }

    private fun fakeCharacter(id: Int = 1) = Character(
        id = id, name = "Rick Sanchez", status = "Alive", species = "Human",
        imageUrl = "", gender = "Male", type = "", origin = "Earth (C-137)",
        location = "Citadel of Ricks"
    )
}

