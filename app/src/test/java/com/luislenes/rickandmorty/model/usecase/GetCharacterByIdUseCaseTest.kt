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

class GetCharacterByIdUseCaseTest {

    private val repository: CharacterRepository = mockk()
    private lateinit var useCase: GetCharacterByIdUseCase

    @Before
    fun setUp() {
        useCase = GetCharacterByIdUseCase(repository)
    }

    @Test
    fun `invoke returns Success with character from repository`() = runTest {
        val character = fakeCharacter()
        coEvery { repository.getCharacterById(1) } returns Result.success(character)

        val result = useCase(1)

        assertTrue(result.isSuccess)
        assertEquals(character, result.getOrNull())
    }

    @Test
    fun `invoke returns Failure when repository returns Failure`() = runTest {
        val exception = RuntimeException("Not found")
        coEvery { repository.getCharacterById(99) } returns Result.failure(exception)

        val result = useCase(99)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke passes the correct id to the repository`() = runTest {
        coEvery { repository.getCharacterById(42) } returns Result.success(fakeCharacter(id = 42))

        useCase(42)

        coVerify(exactly = 1) { repository.getCharacterById(42) }
    }

    @Test
    fun `invoke does not call getCharacters on the repository`() = runTest {
        coEvery { repository.getCharacterById(1) } returns Result.success(fakeCharacter())

        useCase(1)

        coVerify(exactly = 0) { repository.getCharacters() }
    }

    private fun fakeCharacter(id: Int = 1) = Character(
        id = id, name = "Rick Sanchez", status = "Alive", species = "Human",
        imageUrl = "", gender = "Male", type = "", origin = "Earth (C-137)",
        location = "Citadel of Ricks"
    )
}

