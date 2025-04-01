package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class ChartStyle(
    val lineColor: Color,
    val circleColor: Color,
    val helperLineThickness: Float,
    val chartLineThickness: Float,
    val padding: Dp,
    val selectedPointRadius: Float = 8f,
    val selectedPointStrokeWidth: Float = 2f,
    val selectedBackgroundCircleRadius: Float = 16f
)
