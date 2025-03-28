package com.plcoding.cryptotracker.core.presentation.util

import com.plcoding.cryptotracker.R

fun getDrawableIdForCoin(symbol: String): Int {
    return when (symbol.uppercase()) {
        "AAVE" -> R.drawable.aave
        "ADA" -> R.drawable.ada
        "ALGO" -> R.drawable.algo
        "ATOM" -> R.drawable.atom
        "AVAX" -> R.drawable.avax
        "BCH" -> R.drawable.bch
        "BNB" -> R.drawable.bnb
        "BTC" -> R.drawable.btc
        "CRV" -> R.drawable.crv
        "DAI" -> R.drawable.dai
        "DOGE" -> R.drawable.doge
        "DOT" -> R.drawable.dot
        "EOS" -> R.drawable.eos
        "ETC" -> R.drawable.etc
        "ETH" -> R.drawable.eth
        "FIL" -> R.drawable.fil
        "GRT" -> R.drawable.grt
        "ICP" -> R.drawable.icp
        "KCS" -> R.drawable.kcs
        "LEO" -> R.drawable.leo
        "LINK" -> R.drawable.link
        "LTC" -> R.drawable.ltc
        "MKR" -> R.drawable.mkr
        "PAXG" -> R.drawable.paxg
        "QNT" -> R.drawable.qnt
        "SAND" -> R.drawable.sand
        "SOL" -> R.drawable.sol
        "STX" -> R.drawable.stx
        "THETA" -> R.drawable.theta
        "TRX" -> R.drawable.trx
        "UNI" -> R.drawable.uni
        "USDC" -> R.drawable.usdc
        "USDT" -> R.drawable.usdt
        "VET" -> R.drawable.vet
        "WBTC" -> R.drawable.wbtc
        "XLM" -> R.drawable.xlm
        "XMR" -> R.drawable.xmr
        "XRP" -> R.drawable.xrp
        "XTZ" -> R.drawable.xtz
        "ZEC" -> R.drawable.zec
        else -> R.drawable.question_sign
    }
}
