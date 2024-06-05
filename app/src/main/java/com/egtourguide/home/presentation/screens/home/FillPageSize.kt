package com.egtourguide.home.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PageSize
import androidx.compose.ui.unit.Density
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
class FillPageSize(
    private val fraction: Float = 1F,
): PageSize {
    @ExperimentalFoundationApi
    override fun Density.calculateMainAxisPageSize(availableSpace: Int, pageSpacing: Int): Int {
        return (availableSpace * fraction).roundToInt()
    }
}