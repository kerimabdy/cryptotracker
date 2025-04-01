package com.plcoding.cryptotracker.crypto.presentation.coin_detail.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun InfoCard(
    title: String,
    formattedText: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val defaultTextStyle = LocalTextStyle.current.copy(
        textAlign = TextAlign.Left,
        fontSize = 18.sp,
        color = contentColor
    )
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row {
                Text(
                    text = title.toUpperCase(Locale.current),
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 14.sp,
                    color = contentColor.copy(.5f)
                )
                Icon(
                    painter = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .size(24.dp),
                    tint = contentColor.copy(.5f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedContent(
                targetState = formattedText,
                label = "IconAnimation"
            ) { formattedText ->
                Text(
                    text = formattedText,
                    style = defaultTextStyle,
                )
            }

        }

    }

}

@PreviewLightDark
@Composable
private fun InfoCardPreview() {
    CryptoTrackerTheme {
        InfoCard(
            title = "Price",
            formattedText = "$ 25,234.22",
            icon = painterResource(R.drawable.dollar),
        )
    }
}