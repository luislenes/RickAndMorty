package com.luislenes.rickandmorty.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.luislenes.rickandmorty.R
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.presentation.ui.components.StatusBadge
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CharacterDetailTopBar(
                title = if (uiState is CharacterDetailUiState.Success) {
                    (uiState as CharacterDetailUiState.Success).character.name
                } else "",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is CharacterDetailUiState.Loading -> LoadingContent()
                is CharacterDetailUiState.Error   -> ErrorContent(
                    message = state.message,
                    onRetry = viewModel::fetchCharacter
                )
                is CharacterDetailUiState.Success -> DetailContent(character = state.character)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailTopBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.content_desc_back)
                )
            }
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
private fun DetailContent(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = stringResource(R.string.content_desc_character_image, character.name),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(dimensionResource(R.dimen.character_detail_image_size))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        StatusBadge(status = character.status)
        Spacer(modifier = Modifier.height(24.dp))
        InfoCard(character = character)
    }
}

@Composable
private fun InfoCard(character: Character) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoRow(label = stringResource(R.string.label_species),  value = character.species)
            InfoRow(label = stringResource(R.string.label_gender),   value = character.gender)
            if (character.type.isNotBlank()) {
                InfoRow(label = stringResource(R.string.label_type), value = character.type)
            }
            HorizontalDivider()
            InfoRow(label = stringResource(R.string.label_origin),   value = character.origin)
            InfoRow(label = stringResource(R.string.label_location), value = character.location)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value.ifBlank { stringResource(R.string.label_empty_value) },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
