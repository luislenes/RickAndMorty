package com.luislenes.rickandmorty.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.luislenes.rickandmorty.R
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.presentation.components.CharacterImage
import com.luislenes.rickandmorty.presentation.components.StatusBadge
import com.luislenes.rickandmorty.presentation.theme.RickAndMortyTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterViewModel = koinViewModel()
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()

    Scaffold(topBar = { CharacterTopBar() }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val refresh = characters.loadState.refresh) {
                is LoadState.Loading -> CharacterListSkeleton()
                is LoadState.Error   -> ErrorContent(
                    message = refresh.error.message.orEmpty(),
                    onRetry = { characters.retry() }
                )
                else -> CharacterList(
                    characters       = characters,
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
                text       = stringResource(R.string.app_bar_title),
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor    = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
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
        Text(text = message.ifBlank { stringResource(R.string.error_unknown) }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) { Text(stringResource(R.string.action_retry)) }
    }
}

@Composable
private fun CharacterList(
    characters: LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = characters.itemCount,
            key   = { index -> characters.peek(index)?.id ?: index }
        ) { index ->
            characters[index]?.let { character ->
                CharacterCard(
                    character = character,
                    onClick   = { onCharacterClick(character.id) }
                )
            }
        }

        item {
            when (val append = characters.loadState.append) {
                is LoadState.Loading -> {
                    Box(
                        modifier        = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is LoadState.Error -> {
                    Column(
                        modifier            = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text  = append.error.message.orEmpty().ifBlank { stringResource(R.string.error_unknown) },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { characters.retry() }) {
                            Text(stringResource(R.string.action_retry))
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
fun CharacterCard(character: Character, onClick: () -> Unit = {}) {
    Card(
        modifier  = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape     = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CharacterImage(
                imageUrl           = character.imageUrl,
                contentDescription = stringResource(R.string.content_desc_character_avatar, character.name),
                size               = dimensionResource(R.dimen.character_avatar_size),
                shape              = CircleShape
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = character.name,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text  = character.species,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                StatusBadge(status = character.status)
            }
        }
    }
}

private val previewCharacter = Character(1, "Rick Sanchez", "Alive", "Human", "", "Male", "", "Earth (C-137)", "Citadel of Ricks")

@Preview(name = "List — Loading", showBackground = true)
@Composable
private fun CharacterListLoadingPreview() {
    RickAndMortyTheme { CharacterListSkeleton() }
}

@Preview(name = "List — Error", showBackground = true)
@Composable
private fun CharacterListErrorPreview() {
    RickAndMortyTheme {
        ErrorContent(message = stringResource(R.string.error_preview_connection), onRetry = {})
    }
}

@Preview(name = "Card — Alive", showBackground = true)
@Composable
private fun CharacterCardPreview() {
    RickAndMortyTheme { CharacterCard(character = previewCharacter) }
}
