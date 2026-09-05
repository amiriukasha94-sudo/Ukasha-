package com.example.ui.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.CandleStick

@Composable
fun TradingChart(
    candles: List<CandleStick>,
    currentPrice: Float,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf("M5") }
    var useWebViewWidget by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0x14FFFFFF)),
            width = 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Chart Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Chart",
                            tint = accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "XAUUSD",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GOLD SPOT",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "$${String.format("%.2f", currentPrice)}",
                            color = accentColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Timeframe & View Mode buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("M1", "M5", "M15", "H1").forEach { tf ->
                        val isSelected = selectedTimeframe == tf
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) accentColor else Color(0xFF1A1A22))
                                .clickable { selectedTimeframe = tf }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = tf,
                                color = if (isSelected) Color.Black else Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Toggle native / TradingView webview
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (useWebViewWidget) accentColor else Color(0xFF22222E))
                            .clickable { useWebViewWidget = !useWebViewWidget }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Web,
                                contentDescription = "Toggle TV Widget",
                                tint = if (useWebViewWidget) Color.Black else Color.LightGray,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "TV",
                                color = if (useWebViewWidget) Color.Black else Color.LightGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Indicators legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("EMA 20 (Fast)", color = Color.LightGray, fontSize = 10.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00B0FF))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("EMA 50 (Trend)", color = Color.LightGray, fontSize = 10.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF9E9E9E))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("RSI: 58.2", color = Color.LightGray, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chart area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0A0A0E))
                    .border(1.dp, Color(0xFF1E1E26), RoundedCornerShape(10.dp))
            ) {
                if (useWebViewWidget) {
                    // TradingView Widget embed via WebView
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                webViewClient = WebViewClient()
                                val htmlData = """
                                    <!DOCTYPE html>
                                    <html>
                                    <head>
                                        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
                                        <style>
                                            body { margin: 0; background-color: #0a0a0e; overflow: hidden; }
                                            .tradingview-widget-container { height: 100vh; width: 100vw; }
                                        </style>
                                    </head>
                                    <body>
                                        <div class="tradingview-widget-container">
                                            <div id="tradingview_chart"></div>
                                            <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
                                            <script type="text/javascript">
                                                new TradingView.widget({
                                                    "autosize": true,
                                                    "symbol": "OANDA:XAUUSD",
                                                    "interval": "5",
                                                    "timezone": "Etc/UTC",
                                                    "theme": "dark",
                                                    "style": "1",
                                                    "locale": "en",
                                                    "toolbar_bg": "#0a0a0e",
                                                    "enable_publishing": false,
                                                    "hide_top_toolbar": true,
                                                    "save_image": false,
                                                    "container_id": "tradingview_chart"
                                                });
                                            </script>
                                        </div>
                                    </body>
                                    </html>
                                """.trimIndent()
                                loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // High-performance Native Canvas candlestick chart with EMA20 and EMA50
                    Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 12.dp)) {
                        val w = size.width
                        val h = size.height
                        val count = candles.size.coerceAtLeast(1)

                        val minPrice = candles.minOfOrNull { it.low } ?: (currentPrice - 5f)
                        val maxPrice = (candles.maxOfOrNull { it.high } ?: (currentPrice + 5f)).coerceAtLeast(minPrice + 2f)
                        val priceRange = maxPrice - minPrice

                        fun priceToY(p: Float): Float {
                            return h - ((p - minPrice) / priceRange) * h
                        }

                        // Grid lines
                        val gridSteps = 4
                        for (i in 0..gridSteps) {
                            val y = (h / gridSteps) * i
                            drawLine(
                                color = Color(0xFF1E1E24),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                            )
                        }

                        val candleStep = w / count
                        val candleBarWidth = (candleStep * 0.62f).coerceIn(4f, 16f)

                        val ema20Path = Path()
                        val ema50Path = Path()

                        candles.forEachIndexed { idx, candle ->
                            val x = (idx * candleStep) + candleStep / 2f
                            val isBullish = candle.close >= candle.open
                            val candleColor = if (isBullish) Color(0xFF00E676) else Color(0xFFFF5252)

                            val openY = priceToY(candle.open)
                            val closeY = priceToY(candle.close)
                            val highY = priceToY(candle.high)
                            val lowY = priceToY(candle.low)

                            // Wick line
                            drawLine(
                                color = candleColor,
                                start = Offset(x, highY),
                                end = Offset(x, lowY),
                                strokeWidth = 1.5f
                            )

                            // Candle body
                            val topBody = minOf(openY, closeY)
                            val bodyH = kotlin.math.abs(openY - closeY).coerceAtLeast(2f)
                            drawRect(
                                color = candleColor,
                                topLeft = Offset(x - candleBarWidth / 2f, topBody),
                                size = Size(candleBarWidth, bodyH)
                            )

                            // Add to EMA paths
                            val ema20Y = priceToY(candle.ema20)
                            val ema50Y = priceToY(candle.ema50)

                            if (idx == 0) {
                                ema20Path.moveTo(x, ema20Y)
                                ema50Path.moveTo(x, ema50Y)
                            } else {
                                ema20Path.lineTo(x, ema20Y)
                                ema50Path.lineTo(x, ema50Y)
                            }
                        }

                        // Draw EMA 50 line (Cyan/Blue)
                        drawPath(
                            path = ema50Path,
                            color = Color(0xFF00B0FF),
                            style = Stroke(width = 2.2f)
                        )

                        // Draw EMA 20 line (Accent)
                        drawPath(
                            path = ema20Path,
                            color = accentColor,
                            style = Stroke(width = 2.5f)
                        )

                        // Draw live current price dashed line
                        val currentY = priceToY(currentPrice)
                        drawLine(
                            color = accentColor.copy(alpha = 0.8f),
                            start = Offset(0f, currentY),
                            end = Offset(w, currentY),
                            strokeWidth = 1.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                        )
                    }
                }
            }
        }
    }
}
