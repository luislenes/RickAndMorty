package com.luislenes.rickandmorty.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.luislenes.rickandmorty.R

@Composable
fun StatusBadge(status: String) {
    val colors = statusBadgeColors(status)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.status_badge_dot_size)),
        modifier = Modifier
            .background(color = colors.background, shape = RoundedCornerShape(50))
            .padding(
                horizontal = dimensionResource(R.dimen.status_badge_padding_horizontal),
                vertical = dimensionResource(R.dimen.status_badge_padding_vertical)
            )
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.status_badge_dot_size))
                .background(color = colors.content, shape = CircleShape)
        )
        Text(
            text = status,
            color = colors.content,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
