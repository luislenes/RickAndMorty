package com.luislenes.rickandmorty.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.luislenes.rickandmorty.R
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.presentation.CharacterViewModel
import com.luislenes.rickandmorty.presentation.CharactersUiState
import com.luislenes.rickandmorty.presentation.ui.components.StatusBadge
import com.luislenes.rickandmorty.presentation.ui.theme.RickAndMortyTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { CharacterTopBar() }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is CharactersUiState.Loading -> LoadingContent()
                is CharactersUiState.Error   -> ErrorContent(
                    message = state.message,
                    onRetry = viewModel::fetchCharacters
                )
                is CharactersUiState.Success -> CharacterList(
                    characters = state.characters,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_bar_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.error_content_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.error_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) { Text(stringResource(R.string.action_retry)) }
    }
}

@Composable
private fun CharacterList(
    characters: List<Character>,
    onCharacterClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = characters, key = { it.id }) { character ->
            CharacterCard(
                character = character,
                onClick = { onCharacterClick(character.id) }
            )
        }
    }
}

@Composable
fun CharacterCard(character: Character, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = stringResource(R.string.content_desc_character_avatar, character.name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.character_avatar_size))
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = character.species,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                StatusBadge(status = character.status)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Preview data
// ---------------------------------------------------------------------------

private val previewCharacters = listOf(
    Character(1, "Rick Sanchez",  "Alive",   "Human",       "", "Male",   "", "Earth (C-137)", "Citadel of Ricks"),
    Character(2, "Morty Smith",   "Alive",   "Human",       "", "Male",   "", "Earth (C-137)", "Earth (C-137)"),
    Character(3, "Birdperson",    "Dead",    "Bird-Person", "", "Male",   "", "Bird World",    "Tammy's Ship"),
    Character(4, "Schleemypants", "unknown", "Schleem",     "", "unknown","", "unknown",       "unknown"),
)

// ---------------------------------------------------------------------------
// List states
// ---------------------------------------------------------------------------

@Preview(name = "List — Loading", showBackground = true)
@Composable
private fun CharacterListLoadingPreview() {
    RickAndMortyTheme { LoadingContent() }
}

@Preview(name = "List — Error", showBackground = true)
@Composable
private fun CharacterListErrorPreview() {
    RickAndMortyTheme {
        ErrorContent(
            message = "Could not connect to the server. Check your internet connection.",
            onRetry = {}
        )
    }
}

@Preview(name = "List — Success", showBackground = true)
@Composable
private fun CharacterListSuccessPreview() {
    RickAndMortyTheme {
        CharacterList(characters = previewCharacters, onCharacterClick = {})
    }
}

// ---------------------------------------------------------------------------
// Card states
// ---------------------------------------------------------------------------

@Preview(name = "Card — Alive", showBackground = true)
@Composable
private fun CharacterCardAlivePreview() {
    RickAndMortyTheme {
        CharacterCard(character = previewCharacters[0])
    }
}

@Preview(name = "Card — Dead", showBackground = true)
@Composable
private fun CharacterCardDeadPreview() {
    RickAndMortyTheme {
        CharacterCard(character = previewCharacters[2])
    }
}

@Preview(name = "Card — Unknown", showBackground = true)
@Composable
private fun CharacterCardUnknownPreview() {
    RickAndMortyTheme {
        CharacterCard(character = previewCharacters[3])
    }
}
