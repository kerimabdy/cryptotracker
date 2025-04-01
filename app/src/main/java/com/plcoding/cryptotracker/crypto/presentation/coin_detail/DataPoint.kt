package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import java.time.ZonedDateTime


data class DisplayableDataPoint(
    val priceUsd: Double,
    val dateTime: ZonedDateTime,
    val chartValue: DataPoint
)

data class DataPoint(
    val x: Float,
    val y: Float,
)

fun CoinPrice.toDisplayableDataPoint(): DisplayableDataPoint {
    return DisplayableDataPoint(
        priceUsd = priceUsd,
        dateTime = dateTime,
        chartValue = DataPoint(
            x = dateTime.hour.toFloat(),
            y = priceUsd.toFloat()
        )
    )
}
