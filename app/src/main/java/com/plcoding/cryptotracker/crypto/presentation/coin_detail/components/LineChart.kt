package com.plcoding.cryptotracker.crypto.presentation.coin_detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.ChartStyle
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.DataPoint
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import kotlin.random.Random

@Composable
fun LineChart(
    dataPoints: List<DataPoint>,
    style: ChartStyle,
    visibleDataPointIndices: IntRange,
    modifier: Modifier = Modifier,
    selectedDataPointIndex: Int? = null,
    onSelectedDataPoint: (Int?) -> Unit = {},
    onXLabelWidthChange: (Float) -> Unit = {},
) {

    val visibleDataPoints = remember(dataPoints, visibleDataPointIndices) {
        dataPoints.slice(visibleDataPointIndices)
    }

    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0F
    }
    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0F
    }

    var xLabelWidth by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }


    var drawPoints by remember {
        mutableStateOf(listOf<DataPoint>())
    }

    var isShowingDataPoints by remember {
        mutableStateOf(selectedDataPointIndex != null)
    }

    // Add state to track if we're currently dragging
    var isDragging by remember { mutableStateOf(false) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawPoints, xLabelWidth) {
                detectHorizontalDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        onSelectedDataPoint(null)
                    },
                    onDragCancel = {
                        isDragging = false
                        onSelectedDataPoint(null)
                    },
                    onHorizontalDrag = { change, _ ->
                        val newSelectedDataPointIndex = getSelectedDataPointIndex(
                            touchOffsetX = change.position.x,
                            triggerWidth = xLabelWidth,
                            drawPoints = drawPoints
                        )

                        isShowingDataPoints =
                            (newSelectedDataPointIndex + visibleDataPointIndices.first) in
                                    visibleDataPointIndices

                        if (isShowingDataPoints) {
                            onSelectedDataPoint(newSelectedDataPointIndex)
                        }
                    }
                )
            }
    ) {

        val viewPortHeightPx = size.height
        // Viewport
        val viewPortTopY = 0f
        val viewPortBottomY = size.height


        xLabelWidth = size.width / visibleDataPointIndices.last

        drawPoints = visibleDataPointIndices.map {
            val x = (it - visibleDataPointIndices.first) * xLabelWidth
            val ratio = (dataPoints[it].y - minYValue) / (maxYValue - minYValue)
            val y = viewPortBottomY - (ratio * viewPortHeightPx)
            DataPoint(
                x = x,
                y = y,
            )
        }

        val connectionPoint1 = mutableListOf<DataPoint>()
        val connectionPoint2 = mutableListOf<DataPoint>()
        for (i in 1 until drawPoints.size) {
            val p0 = drawPoints[i-1]
            val p1 = drawPoints[i]

            val x = (p1.x + p0.x) / 2f
            val y1 = p0.y
            val y2 = p1.y

            connectionPoint1.add(DataPoint(x, y1))
            connectionPoint2.add(DataPoint(x, y2))
        }

        val linePath = Path().apply {
            if(drawPoints.isNotEmpty()) {
                moveTo(drawPoints.first().x, drawPoints.first().y)

                for (i in 1 until drawPoints.size) {
                    cubicTo(
                        x1 = connectionPoint1[i - 1].x,
                        y1 = connectionPoint1[i - 1].y,
                        x2 = connectionPoint2[i - 1].x,
                        y2 = connectionPoint2[i - 1].y,
                        x3 = drawPoints[i].x,
                        y3 = drawPoints[i].y,
                    )
                }
            }
        }

        // Create gradient path that extends to bottom
        val gradientPath = Path().apply {
            if(drawPoints.isNotEmpty()) {
                // Start from bottom-left
                moveTo(drawPoints.first().x, viewPortBottomY)
                
                // Draw line to first point
                lineTo(drawPoints.first().x, drawPoints.first().y)

                // Follow the same curve as the line
                for (i in 1 until drawPoints.size) {
                    cubicTo(
                        x1 = connectionPoint1[i - 1].x,
                        y1 = connectionPoint1[i - 1].y,
                        x2 = connectionPoint2[i - 1].x,
                        y2 = connectionPoint2[i - 1].y,
                        x3 = drawPoints[i].x,
                        y3 = drawPoints[i].y,
                    )
                }

                // Draw line to bottom-right and close the path
                lineTo(drawPoints.last().x, viewPortBottomY)
                close()
            }
        }

        // Draw gradient background
        drawPath(
            path = gradientPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    style.lineColor.copy(alpha = 0.5f),
                    style.lineColor.copy(alpha = 0.0f)
                ),
                startY = viewPortTopY,
                endY = viewPortBottomY
            )
        )

        // Draw the line path on top
        drawPath(
            path = linePath,
            color = style.lineColor,
            style = Stroke(
                width = style.chartLineThickness,
                cap = StrokeCap.Round
            ),
        )

        // Draw single vertical line and circle for selected point
        if ((selectedDataPointIndex != null || isDragging) && isShowingDataPoints) {
            val selectedPoint = drawPoints.getOrNull(selectedDataPointIndex!!)
            selectedPoint?.let { point ->
                // Draw vertical line
                drawLine(
                    color = style.lineColor,
                    start = Offset(x = point.x, y = viewPortTopY),
                    end = Offset(x = point.x, y = viewPortBottomY),
                    strokeWidth = style.helperLineThickness
                )

                // Draw colored border circle
                drawCircle(
                    color = style.lineColor,
                    radius = style.selectedPointRadius,
                    center = Offset(x = point.x, y = point.y),
                )
            }
        }
    }
}

private fun getSelectedDataPointIndex(
    touchOffsetX: Float,
    triggerWidth: Float,
    drawPoints: List<DataPoint>
): Int {
    val triggerRangeLeft = touchOffsetX - triggerWidth / 2f
    val triggerRangeRight = touchOffsetX + triggerWidth / 2f
    return drawPoints.indexOfFirst {
        it.x in triggerRangeLeft..triggerRangeRight
    }
}


@Preview(widthDp = 500)
@Composable
private fun LineChartPreview() {
    CryptoTrackerTheme {
        val coinHistoryRandomized = remember {
            (1..20).map {
                CoinPrice(
                    priceUsd = Random.nextFloat() * 1000.0,
                    dateTime = ZonedDateTime.now().plusHours(it.toLong())
                )
            }
        }

        val style = ChartStyle(
            lineColor = Color.Black,
            chartLineThickness = 1f,
            helperLineThickness = 1f,
            padding = 8.dp,
            selectedPointRadius = 8f,
            selectedPointStrokeWidth = 2f,
            selectedBackgroundCircleRadius = 16f,
            circleColor = TODO()
        )

        val dataPoints = remember {
            coinHistoryRandomized.map {
                DataPoint(
                    x = it.dateTime.hour.toFloat(),
                    y = it.priceUsd.toFloat(),
                )
            }
        }
        LineChart(
            dataPoints = dataPoints,
            style = style,
            visibleDataPointIndices = 0..19,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}


