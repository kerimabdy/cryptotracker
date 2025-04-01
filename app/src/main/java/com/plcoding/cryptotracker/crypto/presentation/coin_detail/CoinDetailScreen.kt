package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.components.CoinDetailsHeader
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.components.InfoCard
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.components.LineChart
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListState
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.PriceChangeChip
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import com.plcoding.cryptotracker.crypto.presentation.models.toDisplayableNumber
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.format.DateTimeFormatter
import kotlin.collections.isNotEmpty
import kotlin.collections.lastIndex

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    state: CoinListState,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else if (state.selectedCoin != null) {
        val coin = state.selectedCoin
        var selectedDataPointIndex by remember { mutableStateOf<Int?>(null) }

        Column(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(
                    MaterialTheme.colorScheme.surface
                )
                .fillMaxSize()
        ) {
            CoinDetailsHeader(
                name = coin.name,
                symbol = coin.symbol,
                icon = coin.iconRes,
                onClose = { onClose() }
            )
            HorizontalDivider(
                color = DividerDefaults.color.copy(.5f)
            )

            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    val currentCoin = if(selectedDataPointIndex == null) coin else selectedDataPoint

                    Text(
                        text = selectedDataPointIndex?.let { index ->
                            DateTimeFormatter
                                .ofPattern("M/d, HH:MM")
                                .format(coin.coinPriceHistory[index].dateTime)
                        } ?: stringResource(R.string.price),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                    Spacer(
                        modifier = Modifier.size(4.dp)
                    )
                    val currentPrice = selectedDataPointIndex?.let { index ->
                        coin.coinPriceHistory[index].priceUsd.toDisplayableNumber().formatted
                    } ?: coin.priceUsd.formatted
                    Text(
                        text = "$${currentPrice}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )
                    PriceChangeChip(
                        change = coin.changePercent24Hr
                    )

                }

                AnimatedVisibility(
                    visible = coin.coinPriceHistory.isNotEmpty()
                ) {
                    LineChart(
                        coin.coinPriceHistory.map { it.chartValue },
                        style = ChartStyle(
                            lineColor = MaterialTheme.colorScheme.primary,
                            circleColor = MaterialTheme.colorScheme.primary,
                            helperLineThickness = 5f,
                            chartLineThickness = 5f,
                            selectedPointRadius = 18f,
                            padding = 8.dp
                        ),
                        visibleDataPointIndices = 0..coin.coinPriceHistory.lastIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3 / 2f)
                            .padding(vertical = 16.dp),
                        selectedDataPointIndex = selectedDataPointIndex,
                        onSelectedDataPoint = { selectedDataPointIndex = it },
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.market_cap),
                        formattedText = "$${coin.marketCapUsd.formatted}",
                        icon = painterResource(R.drawable.stock)
                    )

                    val absoluteChangeFormatted =
                        (coin.priceUsd.value * (coin.changePercent24Hr.value / 100)).toDisplayableNumber()

                    val isPositive = coin.changePercent24Hr.value > 0.0

                    InfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.change_last_24hr),
                        formattedText = "$${absoluteChangeFormatted.formatted}",
                        icon = if (isPositive) {
                            painterResource(R.drawable.trending_circle)
                        } else {
                            painterResource(R.drawable.trending_down_circle)
                        },
                    )
                }

            }
        }


    }
}

@PreviewLightDark
@Composable
private fun CoinDetailScreenPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(
            state = CoinListState(
                selectedCoin = previewCoin.toCoinUi()
            ),
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background
            ),
            onClose = { }
        )
    }
}
