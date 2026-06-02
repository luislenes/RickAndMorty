package com.luislenes.rickandmorty.presentation.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.luislenes.rickandmorty.R
import com.luislenes.rickandmorty.presentation.ui.theme.ImageErrorBg
import com.luislenes.rickandmorty.presentation.ui.theme.ImageErrorIcon
import com.luislenes.rickandmorty.presentation.ui.theme.ImagePlaceholderBg
import com.luislenes.rickandmorty.presentation.ui.theme.ImagePlaceholderShimmer

@Composable
fun CharacterImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    shape: Shape = CircleShape
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(shape),
        loading = {
            ShimmerBox(shape = shape)
        },
        error = {
            ErrorBox(shape = shape)
        },
        success = {
            SubcomposeAsyncImageContent()
        }
    )
}

@Composable
private fun ShimmerBox(shape: Shape) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(ImagePlaceholderBg.copy(alpha = alpha))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ImagePlaceholderShimmer.copy(alpha = alpha * 0.5f))
        )
    }
}

@Composable
private fun ErrorBox(shape: Shape) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(ImageErrorBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.BrokenImage,
            contentDescription = stringResource(R.string.content_desc_image_error),
            tint = ImageErrorIcon
        )
    }
}

@Preview(showBackground = true, name = "Image — Loading shimmer")
@Composable
private fun PreviewCharacterImageLoading() {
    ShimmerBox(shape = CircleShape)
}

@Preview(showBackground = true, name = "Image — Error state")
@Composable
private fun PreviewCharacterImageError() {
    Box(modifier = Modifier.size(80.dp)) {
        ErrorBox(shape = CircleShape)
    }
}

@Preview(showBackground = true, name = "Image — Error state (rounded)")
@Composable
private fun PreviewCharacterImageErrorRounded() {
    Box(modifier = Modifier.size(200.dp)) {
        ErrorBox(shape = RoundedCornerShape(16.dp))
    }
}

