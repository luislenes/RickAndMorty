package com.luislenes.rickandmorty.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luislenes.rickandmorty.R
import com.luislenes.rickandmorty.presentation.components.shimmerBrush
import com.luislenes.rickandmorty.presentation.theme.RickAndMortyTheme
import com.luislenes.rickandmorty.presentation.theme.ShimmerBase

@Composable
fun CharacterDetailSkeleton() {
    val brush = shimmerBrush()
    val cornerRadius = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(dimensionResource(R.dimen.character_detail_image_size))
                .height(dimensionResource(R.dimen.character_detail_image_size))
                .clip(cornerRadius)
                .background(brush)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.height(24.dp))
        InfoCardSkeleton(brush = brush, cornerRadius = cornerRadius)
    }
}

@Composable
private fun InfoCardSkeleton(
    brush: androidx.compose.ui.graphics.Brush,
    cornerRadius: RoundedCornerShape
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = cornerRadius,
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoRowSkeleton(brush = brush, valueFraction = 0.45f)
            InfoRowSkeleton(brush = brush, valueFraction = 0.30f)
            HorizontalDivider(color = ShimmerBase.copy(alpha = 0.4f))
            InfoRowSkeleton(brush = brush, valueFraction = 0.55f)
            InfoRowSkeleton(brush = brush, valueFraction = 0.60f)
        }
    }
}

@Composable
private fun InfoRowSkeleton(
    brush: androidx.compose.ui.graphics.Brush,
    valueFraction: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(valueFraction)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
    }
}

@Preview(name = "Detail Skeleton", showBackground = true)
@Composable
private fun CharacterDetailSkeletonPreview() {
    RickAndMortyTheme { CharacterDetailSkeleton() }
}

