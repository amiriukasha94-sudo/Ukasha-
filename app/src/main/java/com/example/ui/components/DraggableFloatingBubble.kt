package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.BotState
import com.example.data.UserProfile
import kotlin.math.roundToInt

@Composable
fun DraggableFloatingBubble(
    botState: BotState,
    accentColor: Color,
    isSpeaking: Boolean,
    openTradesCount: Int,
    todayProfit: Double,
    userProfile: UserProfile,
    onTogglePause: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onEnterPip: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMiniDashboard by remember { mutableStateOf(false) }

    val config = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) { config.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { config.screenHeightDp.dp.toPx() }

    // Initial position: bottom right (with padding)
    var offsetX by remember { mutableFloatStateOf(screenWidthPx - with(density) { 84.dp.toPx() }) }
    var offsetY by remember { mutableFloatStateOf(screenHeightPx - with(density) { 160.dp.toPx() }) }

    // Pulse animation when trades are open
    val hasOpenTrade = openTradesCount > 0 || botState == BotState.BUYING
    val infiniteTransition = rememberInfiniteTransition(label = "BubblePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        // Floating Draggable Head
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(10f, screenWidthPx - 220f)
                        offsetY = (offsetY + dragAmount.y).coerceIn(80f, screenHeightPx - 260f)
                    }
                }
                .clickable { showMiniDashboard = true }
        ) {
            // Pulsing aura when trade is open
            if (hasOpenTrade) {
                Box(
                    modifier = Modifier
                        .size(68.dp * pulseScale)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = pulseAlpha))
                )
            }

            // Bot Head circle (56dp) - Geometric Balance style
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.Center)
                    .shadow(16.dp, CircleShape, spotColor = accentColor)
                    .clip(CircleShape)
                    .background(Color(0xFF161616))
                    .border(2.dp, accentColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                TeslaBotAvatar(
                    botState = botState,
                    accentColor = accentColor,
                    isSpeaking = isSpeaking,
                    size = 48.dp
                )
            }

            // Notification / Alert Badge at top right
            if (botState == BotState.BLOCKED || botState == BotState.NEWS_PAUSE) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF5252))
                        .border(2.dp, Color(0xFF0A0A0A), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("!", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Live P&L Badge at bottom center of bubble
            val profitFormatted = if (todayProfit >= 0) "+$${String.format("%.1f", todayProfit)}" else "-$${String.format("%.1f", -todayProfit)}"
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF161616))
                    .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(6.dp))
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(
                    text = profitFormatted,
                    color = if (todayProfit >= 0) Color(0xFF00FF88) else Color(0xFFFF5252),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Mini Dashboard Modal
        if (showMiniDashboard) {
            Dialog(onDismissRequest = { showMiniDashboard = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(
                            colors = listOf(accentColor.copy(alpha = 0.5f), Color(0x14FFFFFF))
                        ),
                        width = 1.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TeslaBotAvatar(
                                    botState = botState,
                                    accentColor = accentColor,
                                    isSpeaking = isSpeaking,
                                    size = 40.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "PovertyScalper 0.1",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = botState.title,
                                        color = accentColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            IconButton(
                                onClick = { showMiniDashboard = false },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Mini Dashboard",
                                    tint = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Status pill
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1E1E26))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "\"${botState.statusMessage}\"",
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Row: Equity & Today's Profit
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B22)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Equity", color = Color.Gray, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$${String.format("%,.2f", userProfile.equity)}",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B22)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Today P&L", color = Color.Gray, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "+$${String.format("%.2f", todayProfit)}",
                                        color = Color(0xFF00FF88),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Pause / Resume Bot Button
                        Button(
                            onClick = { onTogglePause() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (userProfile.botPaused) Color(0xFF00E676) else Color(0xFFFF3D00)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = if (userProfile.botPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (userProfile.botPaused) "Resume EA" else "Pause EA",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (userProfile.botPaused) "Resume Safe Scalper" else "Pause Bot Immediately",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Row: Picture-in-Picture & Go to full dashboard
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    showMiniDashboard = false
                                    onEnterPip()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PictureInPicture,
                                    contentDescription = "Picture in Picture",
                                    tint = accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("PiP Mode", color = Color.White, fontSize = 11.sp)
                            }

                            Button(
                                onClick = {
                                    showMiniDashboard = false
                                    onNavigateToDashboard()
                                },
                                modifier = Modifier.weight(1.2f),
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Dashboard,
                                    contentDescription = "Full Dashboard",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Full View", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
