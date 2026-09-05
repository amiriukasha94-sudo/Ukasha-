package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BotState

/**
 * High-impact, prominent bot picture and HUD display for CleanGold PovertyScalper 0.1.
 * Ensures the robot picture is perfectly big and clearly visible, with futuristic
 * holographic radar sweeps, optic laser visor animations, voice activity, and state management.
 */
@Composable
fun PovertyScalperBotDisplay(
    botState: BotState,
    accentColor: Color,
    isSpeaking: Boolean = false,
    onStartBot: () -> Unit = {},
    onStopBot: () -> Unit = {},
    onTogglePause: () -> Unit = {},
    onSelectBotState: (BotState) -> Unit = {},
    showControls: Boolean = true,
    avatarHeight: Dp = 230.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BotDisplayAnimations")

    // Laser scanline sweeping across the robot's optical visor
    val scanlineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OpticScanY"
    )

    // Holographic aura glow pulsation
    val auraGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraGlow"
    )

    // Speaking audio waveform bounce
    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 160, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Wave1"
    )
    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 26f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 210, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Wave2"
    )
    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 140, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Wave3"
    )

    val stateColor = when (botState) {
        BotState.ANALYZING -> accentColor
        BotState.BUYING -> Color(0xFF00FF88)
        BotState.BLOCKED -> Color(0xFFFF5252)
        BotState.NEWS_PAUSE -> Color(0xFFFFB300)
        BotState.SLEEPING -> Color(0xFF757575)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF101014)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                colors = listOf(
                    stateColor.copy(alpha = 0.6f * auraGlow),
                    Color(0x22FFFFFF),
                    stateColor.copy(alpha = 0.2f)
                )
            ),
            width = 1.5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top HUD Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(stateColor)
                            .shadow(6.dp, CircleShape, spotColor = stateColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "POVERTYSCALPER 0.1",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(stateColor.copy(alpha = 0.15f))
                        .border(1.dp, stateColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = botState.name,
                        color = stateColor,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // HIGH-QUALITY ANIMATED ROBOT GIRL AVATAR (REPLACES PLACEHOLDER)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                RobotGirlAvatar(
                    botState = botState,
                    accentColor = accentColor,
                    isSpeaking = isSpeaking,
                    size = avatarHeight,
                    showStatusBadge = true,
                    showHoloHalo = true,
                    onClick = {
                        onTogglePause()
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bot Voice / Behavior Speech Bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF17171C))
                    .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = when (botState) {
                        BotState.ANALYZING -> "Scanning XAUUSD M5 order flow. Liquidity clusters mapped. Standing by for high-probability sniper entries."
                        BotState.BUYING -> "Bullish Fair Value Gap confirmed. Executing 1% risk scalp long. Stop loss locked strictly below rejection wick."
                        BotState.BLOCKED -> "Drawdown guard engaged: Max risk threshold reached. Engine locked to protect funded capital."
                        BotState.NEWS_PAUSE -> "High impact USD news approaching. Trading paused automatically to shield account from spread spikes."
                        BotState.SLEEPING -> "PovertyScalper 0.1 is in standby mode. Tap 'START BOT' to arm real-time scanning."
                    },
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (showControls) {
                Spacer(modifier = Modifier.height(14.dp))

                // Master Start / Stop / Pause Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Start Bot Button
                    Button(
                        onClick = onStartBot,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start Bot",
                                tint = Color(0xFF0A0A0A),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "START BOT",
                                color = Color(0xFF0A0A0A),
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Pause / Standby Button
                    Button(
                        onClick = onTogglePause,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24242A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (botState == BotState.SLEEPING) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = "Pause",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (botState == BotState.SLEEPING) "RESUME" else "PAUSE",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Stop Bot Button
                    Button(
                        onClick = onStopBot,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A1515)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PowerSettingsNew,
                                contentDescription = "Stop Bot",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "STOP",
                                color = Color(0xFFFF5252),
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Mode Selector Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BotState.entries.forEach { state ->
                        val isSelected = botState == state
                        val pillColor = when (state) {
                            BotState.ANALYZING -> accentColor
                            BotState.BUYING -> Color(0xFF00FF88)
                            BotState.BLOCKED -> Color(0xFFFF5252)
                            BotState.NEWS_PAUSE -> Color(0xFFFFB300)
                            BotState.SLEEPING -> Color(0xFF888888)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) pillColor.copy(alpha = 0.2f) else Color(0xFF151518))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) pillColor else Color(0x14FFFFFF),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectBotState(state) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (state) {
                                    BotState.ANALYZING -> "SCAN"
                                    BotState.BUYING -> "SCALP"
                                    BotState.BLOCKED -> "LOCK"
                                    BotState.NEWS_PAUSE -> "NEWS"
                                    BotState.SLEEPING -> "SLEEP"
                                },
                                color = if (isSelected) pillColor else Color(0xFF888888),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
