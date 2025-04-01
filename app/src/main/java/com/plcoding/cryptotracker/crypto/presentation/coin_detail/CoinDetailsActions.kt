package com.plcoding.cryptotracker.crypto.presentation.coin_detail

sealed interface CoinDetailsActions {
    data object OnClose: CoinDetailsActions
}