package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BotState
import com.example.data.NewsEvent
import com.example.data.NewsFilterConfig
import com.example.data.NewsImpact
import com.example.data.SentimentType

@Composable
fun NewsSentimentCard(
    newsEvents: List<NewsEvent>,
    config: NewsFilterConfig,
    botState: BotState,
    accentColor: Color,
    onToggleFilter: () -> Unit,
    onBufferChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val isNewsPaused = botState == BotState.NEWS_PAUSE

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isNewsPaused) Color(0xFF1E1414) else Color(0xFF161616)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isNewsPaused) Color(0xFFFF5252).copy(alpha = 0.5f) else Color(0x14FFFFFF)
            ),
            width = 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: News Shield + Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isNewsPaused) Color(0xFFFF5252).copy(alpha = 0.2f)
                                else accentColor.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isNewsPaused) Icons.Default.Campaign else Icons.Default.Shield,
                            contentDescription = null,
                            tint = if (isNewsPaused) Color(0xFFFF5252) else accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "NEWS SENTIMENT FILTER",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            if (isNewsPaused) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFFF5252))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("PAUSED FOR NEWS", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                        Text(
                            text = if (config.isEnabled) "Automated 30m buffer active" else "News Shield Disabled",
                            color = if (config.isEnabled) Color(0xFF00FF88) else Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }
                }

                Switch(
                    checked = config.isEnabled,
                    onCheckedChange = { onToggleFilter() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF0A0A0A),
                        checkedTrackColor = accentColor,
                        uncheckedTrackColor = Color(0xFF26262B)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Buffer Time Selection Pills (15m, 30m, 60m)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Buffer:", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                listOf(15, 30, 60).forEach { minutes ->
                    val isSelected = config.bufferMinutes == minutes
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) accentColor else Color(0xFF1E1E24))
                            .border(1.dp, if (isSelected) accentColor else Color(0x14FFFFFF), RoundedCornerShape(6.dp))
                            .clickable { onBufferChanged(minutes) }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "±${minutes}m",
                            color = if (isSelected) Color(0xFF0A0A0A) else Color(0xFF94A3B8),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Upcoming News List
            val displayEvents = if (expanded) newsEvents else newsEvents.take(2)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                displayEvents.forEach { event ->
                    NewsItemRow(event = event, accentColor = accentColor)
                }
            }

            if (newsEvents.size > 2) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (expanded) "Show Less" else "View All Upcoming High-Impact Events (${newsEvents.size})",
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun NewsItemRow(event: NewsEvent, accentColor: Color) {
    val impactBg = when (event.impact) {
        NewsImpact.HIGH -> Color(0xFFFF5252).copy(alpha = 0.2f)
        NewsImpact.MEDIUM -> Color(0xFFFFB300).copy(alpha = 0.2f)
        NewsImpact.LOW -> Color(0xFF64748B).copy(alpha = 0.2f)
    }
    val impactColor = when (event.impact) {
        NewsImpact.HIGH -> Color(0xFFFF5252)
        NewsImpact.MEDIUM -> Color(0xFFFFB300)
        NewsImpact.LOW -> Color(0xFF94A3B8)
    }

    val sentimentColor = when (event.sentiment) {
        SentimentType.BULLISH -> Color(0xFF00FF88)
        SentimentType.BEARISH -> Color(0xFFFF5252)
        SentimentType.NEUTRAL -> Color(0xFF94A3B8)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1E))
            .border(1.dp, Color(0x10FFFFFF), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Impact badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(impactBg)
                            .border(1.dp, impactColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "${event.currency} ${event.impact.name}",
                            color = impactColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Sentiment Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(sentimentColor.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "${event.sentiment.name} (${if (event.sentimentScore >= 0) "+" else ""}${String.format("%.2f", event.sentimentScore)})",
                            color = sentimentColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Countdown timer
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (event.minutesUntil <= 30) Color(0xFFFF5252) else Color(0xFF94A3B8),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "in ${event.minutesUntil}m",
                        color = if (event.minutesUntil <= 30) Color(0xFFFF5252) else Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = event.headline,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (event.forecast.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Forecast: ${event.forecast} • Prev: ${event.previous} • Scheduled: ${event.scheduledTime}",
                    color = Color(0xFF64748B),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
