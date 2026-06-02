package com.luislenes.rickandmorty.presentation.components

import androidx.compose.ui.graphics.Color
import com.luislenes.rickandmorty.presentation.theme.StatusAlive
import com.luislenes.rickandmorty.presentation.theme.StatusBadgeText
import com.luislenes.rickandmorty.presentation.theme.StatusDead
import com.luislenes.rickandmorty.presentation.theme.StatusUnknown

data class StatusBadgeColors(
    val background: Color,
    val content: Color
)

fun statusBadgeColors(status: String): StatusBadgeColors = when (status.lowercase()) {
    "alive" -> StatusBadgeColors(background = StatusAlive,   content = StatusBadgeText)
    "dead"  -> StatusBadgeColors(background = StatusDead,    content = StatusBadgeText)
    else    -> StatusBadgeColors(background = StatusUnknown, content = StatusBadgeText)
}

