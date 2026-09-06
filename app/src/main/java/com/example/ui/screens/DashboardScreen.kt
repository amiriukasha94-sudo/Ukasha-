package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppThemeColor
import com.example.data.BotState
import com.example.data.CandleStick
import com.example.data.Mt5VerificationStatus
import com.example.data.NewsEvent
import com.example.data.NewsFilterConfig
import com.example.data.Trade
import com.example.data.TradingAnalytics
import com.example.data.UserProfile
import com.example.ui.components.ChartAnalyser
import com.example.ui.components.NewsSentimentCard
import com.example.ui.components.PovertyScalperBotDisplay
import com.example.ui.components.TeslaBotAvatar
import com.example.ui.components.TradingChart

@Composable
fun DashboardScreen(
    currentTheme: AppThemeColor,
    userProfile: UserProfile,
    botState: BotState,
    isBotSpeaking: Boolean,
    todayProfit: Double,
    currentGoldPrice: Float,
    candles: List<CandleStick>,
    trades: List<Trade>,
    openTrades: List<Trade>,
    analytics: TradingAnalytics,
    newsEvents: List<NewsEvent>,
    newsFilterConfig: NewsFilterConfig,
    onStartBot: () -> Unit,
    onStopBot: () -> Unit,
    onTogglePause: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPayment: () -> Unit,
    onOpenMt5Verification: () -> Unit,
    onToggleNewsFilter: () -> Unit,
    onNewsBufferChanged: (Int) -> Unit,
    onNavigateToAdmin: () -> Unit,
    onLogout: () -> Unit,
    onSelectBotState: (BotState) -> Unit,
    onExecuteScalp: (type: String, entryPrice: Double, sl: Double, tp: Double) -> Unit = { _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val accentColor = currentTheme.primary
    val isAdmin = userProfile.email.equals("ukashaamiri865@gmail.com", ignoreCase = true)

    val infiniteTransition = rememberInfiniteTransition(label = "ScanLightTransition")
    val scanLightAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ScanAlpha"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Live AI Robot Background Image with dark atmospheric overlay
        Image(
            painter = painterResource(id = R.drawable.img_robot_bg),
            contentDescription = "PovertyScalper 0.1 Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Vignette & Geometric Grid Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xF00A0A0A),
                            Color(0xFA0A0A0A),
                            Color(0xFF0A0A0A)
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section: CG Brand + PovertyScalper 0.1 + WhatsApp + Settings
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Brand: CleanGold - PovertyScalper 0.1
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(accentColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PS",
                                color = Color(0xFF0A0A0A),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CleanGold",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.2).sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(accentColor.copy(alpha = 0.2f))
                                        .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "PovertyScalper 0.1",
                                        color = accentColor,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "MT5: ${userProfile.mt5Account} • ${userProfile.mt5Broker}",
                                color = Color(0xFF64748B),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Right Actions: WhatsApp + Upgrade UGX + Settings + Admin
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // WhatsApp Direct Contact Button (0765 014053)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF142018))
                                .border(1.dp, Color(0x3300E676), RoundedCornerShape(8.dp))
                                .clickable {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://wa.me/256765014053?text=Hello%20CleanGold%20PovertyScalper%20Team,%20I%20need%20assistance%20with%20my%20MT5%20account.")
                                    )
                                    context.startActivity(intent)
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💬", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "0765 014053",
                                    color = Color(0xFF00E676),
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // UGX Upgrade / Instant Payment button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(accentColor.copy(alpha = 0.15f))
                                .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .clickable { onOpenPayment() }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "UGX PLAN",
                                color = accentColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        if (isAdmin) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF161616))
                                    .border(1.dp, Color(0x14FFFFFF), CircleShape)
                                    .clickable { onNavigateToAdmin() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Admin Panel",
                                    tint = accentColor,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF161616))
                                .border(1.dp, Color(0x14FFFFFF), CircleShape)
                                .clickable { onOpenSettings() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF161616))
                                .border(1.dp, Color(0x14FFFFFF), CircleShape)
                                .clickable { onLogout() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PowerSettingsNew,
                                contentDescription = "Logout",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }

            // MT5 Verification & License Status Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // MT5 Link & Verification Badge
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF161616))
                            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(14.dp))
                            .clickable { onOpenMt5Verification() }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = if (userProfile.mt5VerificationStatus == Mt5VerificationStatus.VERIFIED) Color(0xFF00FF88) else accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "MT5 #${userProfile.mt5Account}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (userProfile.mt5VerificationStatus == Mt5VerificationStatus.VERIFIED) "VERIFIED OWNERSHIP" else "CLICK TO VERIFY",
                                        color = if (userProfile.mt5VerificationStatus == Mt5VerificationStatus.VERIFIED) Color(0xFF00FF88) else accentColor,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            Text("➔", color = Color(0xFF64748B), fontSize = 11.sp)
                        }
                    }

                    // Expiry countdown
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF161616))
                            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(14.dp))
                            .clickable { onOpenPayment() }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "${userProfile.expiryDaysRemaining}d ${String.format("%02d", userProfile.expiryHoursRemaining)}h",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "RENEW (UGX)",
                                    color = accentColor,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // PROMINENT REAL-TIME ANALYTICS SECTION
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "REAL-TIME ANALYTICS (FIRESTORE SYNC)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = "LIVE TICK",
                            color = Color(0xFF00FF88),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 4 Prominent Real-time Metric Cards
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // 1. Total Trades Executed
                            AnalyticsCard(
                                title = "TOTAL TRADES EXECUTED",
                                value = "${analytics.totalTrades}",
                                subtext = "${analytics.winningTrades} Wins / ${analytics.losingTrades} Losses",
                                subtextColor = Color(0xFF00FF88),
                                modifier = Modifier.weight(1f)
                            )

                            // 2. Win Rate (%)
                            AnalyticsCard(
                                title = "WIN RATE (%)",
                                value = "${analytics.winRate}%",
                                subtext = "Instituitional 90%+ Target",
                                subtextColor = accentColor,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // 3. Average Profit per Trade
                            AnalyticsCard(
                                title = "AVERAGE PROFIT / TRADE",
                                value = "+$${String.format("%.2f", analytics.averageProfit)}",
                                subtext = "Net: +$${String.format("%.2f", analytics.totalProfit)}",
                                subtextColor = Color(0xFF00FF88),
                                modifier = Modifier.weight(1f)
                            )

                            // 4. Maximum Drawdown
                            AnalyticsCard(
                                title = "MAXIMUM DRAWDOWN",
                                value = "${analytics.maxDrawdown}%",
                                subtext = "Prop Challenge Safe (<4%)",
                                subtextColor = if (analytics.maxDrawdown < 3.0) Color(0xFF00D4FF) else Color(0xFFFFB300),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // LIVE AI ROBOT COMMAND CENTER ("PovertyScalper 0.1") - PERFECTLY BIG AND SEEN
            item {
                PovertyScalperBotDisplay(
                    botState = botState,
                    accentColor = accentColor,
                    isSpeaking = isBotSpeaking,
                    onStartBot = onStartBot,
                    onStopBot = onStopBot,
                    onTogglePause = onTogglePause,
                    onSelectBotState = onSelectBotState,
                    showControls = true,
                    avatarHeight = 230.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // NEWS SENTIMENT FILTER & HIGH-IMPACT NEWS EVENTS SECTION
            item {
                NewsSentimentCard(
                    newsEvents = newsEvents,
                    config = newsFilterConfig,
                    botState = botState,
                    accentColor = accentColor,
                    onToggleFilter = onToggleNewsFilter,
                    onBufferChanged = onNewsBufferChanged
                )
            }

            // AI REAL-TIME CHART ANALYSER & SCALPING PREDICTOR
            item {
                ChartAnalyser(
                    currentPrice = currentGoldPrice,
                    candles = candles,
                    accentColor = accentColor,
                    onExecuteScalp = onExecuteScalp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // LIVE CHART SECTION (XAUUSD M5 with EMA20/50)
            item {
                TradingChart(
                    candles = candles,
                    currentPrice = currentGoldPrice,
                    accentColor = accentColor
                )
            }

            // REALTIME TRADE HISTORY LOG
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "REALTIME MT5 TRADE LOG",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Text(
                        text = "EQUITY: $${String.format("%,.2f", userProfile.equity)}",
                        color = Color(0xFF00FF88),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0x14FFFFFF)),
                        width = 1.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("TIME / SYM", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.3f))
                            Text("TYPE / LOT", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.1f))
                            Text("SL / TP", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.3f))
                            Text("PROFIT", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                        }

                        trades.take(6).forEach { trade ->
                            TradeRowItem(trade = trade, accentColor = accentColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun AnalyticsCard(
    title: String,
    value: String,
    subtext: String,
    subtextColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0x14FFFFFF)),
            width = 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                color = Color(0xFF64748B),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                color = subtextColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TradeRowItem(trade: Trade, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.3f)) {
            Text(text = trade.symbol, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = trade.timestamp, color = Color(0xFF64748B), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }

        Column(modifier = Modifier.weight(1.1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (trade.type == "BUY") Color(0xFF00E676) else Color(0xFFFF5252))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(text = trade.type, color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "${trade.lot}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }

        Column(modifier = Modifier.weight(1.3f)) {
            Text(text = "SL ${trade.sl}", color = Color(0xFFFF7A7A), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Text(text = "TP ${trade.tp}", color = Color(0xFF81C784), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }

        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            val isPositive = trade.profit >= 0
            Text(
                text = "${if (isPositive) "+" else ""}$${String.format("%.2f", trade.profit)}",
                color = if (isPositive) Color(0xFF00FF88) else Color(0xFFFF5252),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Closed", color = Color(0xFF64748B), fontSize = 9.sp)
        }
    }
}
