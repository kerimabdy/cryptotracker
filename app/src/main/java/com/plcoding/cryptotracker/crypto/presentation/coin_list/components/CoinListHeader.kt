package com.plcoding.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy((-2).dp)
    ) {
        Text(
            text = "In the past 24 hours",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(.5f)
        )
        Text(
            text = "Crypto Market",
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}

@PreviewLightDark
@Composable
private fun CoinListHeaderPreview() {
    CryptoTrackerTheme {
        CoinListHeader()
    }
}