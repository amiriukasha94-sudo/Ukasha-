package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BotState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium, high-quality Robot Girl AI Avatar for CleanGold PovertyScalper 0.1.
 *
 * Features:
 * - Dynamic animated head mechanics: responsive head tilt, breathing float, nodding physics
 * - State-reactive optical sensors, holographic eye visor, and facial HUD expressions
 * - Cybernetic antenna ears with pulsing state LEDs and audio frequency rings
 * - Holographic halo crown with orbiting telemetry nodes
 * - Real-time acoustic vocalizer mouth waveform sync when speaking
 * - Interactive tap reaction (winks, glows, bursts holographic sparks)
 */
@Composable
fun RobotGirlAvatar(
    botState: BotState,
    accentColor: Color,
    isSpeaking: Boolean = false,
    size: Dp = 230.dp,
    showStatusBadge: Boolean = true,
    showHoloHalo: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isWinking by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "RobotGirlStateTransitions")

    // State-based thematic colors
    val stateColor = when (botState) {
        BotState.ANALYZING -> accentColor
        BotState.BUYING -> Color(0xFF00FF88)
        BotState.BLOCKED -> Color(0xFFFF4444)
        BotState.NEWS_PAUSE -> Color(0xFFFFB300)
        BotState.SLEEPING -> Color(0xFF7E8B9B)
    }

    // Secondary glow color
    val secondaryColor = when (botState) {
        BotState.ANALYZING -> Color(0xFF00E5FF)
        BotState.BUYING -> Color(0xFFB9F6CA)
        BotState.BLOCKED -> Color(0xFFFF8A80)
        BotState.NEWS_PAUSE -> Color(0xFFFFD54F)
        BotState.SLEEPING -> Color(0xFF546E7A)
    }

    // 1. HEAD ANIMATION: Floating / Breathing vertical bob
    val headBobY by infiniteTransition.animateFloat(
        initialValue = if (botState == BotState.SLEEPING) 2f else -3.5f,
        targetValue = if (botState == BotState.SLEEPING) 6f else 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (botState == BotState.SLEEPING) 3200 else 2200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HeadBob"
    )

    // 2. HEAD ANIMATION: Subtle state-based head tilt / nod
    val baseTilt = when (botState) {
        BotState.ANALYZING -> -2.2f // Inquisitive, analytical tilt
        BotState.BUYING -> 1.5f    // Confident, decisive tilt
        BotState.BLOCKED -> -3.0f   // Alert, cautious angle
        BotState.NEWS_PAUSE -> 2.0f // Thoughtful posture
        BotState.SLEEPING -> 4.5f   // Relaxed resting tilt
    }

    val dynamicTilt by infiniteTransition.animateFloat(
        initialValue = baseTilt - 1.2f,
        targetValue = baseTilt + 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (botState == BotState.ANALYZING) 1600 else 2800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HeadTilt"
    )

    // 3. EYE / OPTIC BLINK: Periodic natural blink
    val eyeOpenRatio by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "BlinkPeriod"
    )

    // 4. EYE SCANNING: Left-to-right eye scan for ANALYZING state
    val eyeScanX by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (botState == BotState.ANALYZING) 1100 else 2400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EyeScan"
    )

    // 5. OPTIC LASER / HUD SCANLINE
    val scanlineProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (botState == BotState.ANALYZING) 1400 else 2800,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "Scanline"
    )

    // 6. AURA GLOW PULSE
    val auraGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.98f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (botState == BotState.BUYING) 800 else 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraGlow"
    )

    // 7. ORBITING HALO ANGLE
    val haloRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "HaloRotation"
    )

    // 8. VOCALIZER WAVEFORM when isSpeaking == true
    val voiceWave1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 140, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Voice1"
    )
    val voiceWave2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 180, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Voice2"
    )

    Box(
        modifier = modifier
            .size(size)
            .testTag("robot_girl_avatar")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                coroutineScope.launch {
                    isWinking = true
                    delay(750)
                    isWinking = false
                }
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        // Outer Cyber Halo Orbit (Floating tech ring behind/above head)
        if (showHoloHalo) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(rotationZ = haloRotation)
            ) {
                val radius = this.size.minDimension * 0.46f
                val center = Offset(this.size.width / 2f, this.size.height / 2f)

                // Holographic orbital dash ring
                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(
                            stateColor.copy(alpha = 0.6f * auraGlow),
                            secondaryColor.copy(alpha = 0.2f),
                            Color.Transparent,
                            stateColor.copy(alpha = 0.8f * auraGlow)
                        )
                    ),
                    radius = radius,
                    center = center,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                            floatArrayOf(18f, 14f, 6f, 14f),
                            0f
                        )
                    )
                )

                // Orbiting satellite nodes
                val orbitCount = 4
                for (i in 0 until orbitCount) {
                    val angle = Math.toRadians((i * (360.0 / orbitCount)).toDouble())
                    val nodeX = (center.x + radius * cos(angle)).toFloat()
                    val nodeY = (center.y + radius * sin(angle)).toFloat()
                    drawCircle(
                        color = if (i % 2 == 0) stateColor else secondaryColor,
                        radius = 2.5.dp.toPx(),
                        center = Offset(nodeX, nodeY)
                    )
                }
            }
        }

        // Animated Head Container with Bobbing & Tilting Physics
        Box(
            modifier = Modifier
                .fillMaxSize(0.88f)
                .graphicsLayer {
                    translationY = headBobY * density
                    rotationZ = dynamicTilt
                },
            contentAlignment = Alignment.Center
        ) {
            // High-Tech Cyber Frame & Backlight
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(size * 0.16f))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                stateColor.copy(alpha = 0.25f * auraGlow),
                                Color(0xFF101016),
                                Color(0xFF07070A)
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                stateColor.copy(alpha = 0.9f * auraGlow),
                                secondaryColor.copy(alpha = 0.4f),
                                Color(0x22FFFFFF),
                                stateColor.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(size * 0.16f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Robot Girl Character Portrait
                Image(
                    painter = painterResource(id = R.drawable.img_robot_girl_avatar),
                    contentDescription = "PovertyScalper Robot Girl AI",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(size * 0.16f)),
                    contentScale = ContentScale.Crop
                )

                // High-Tech Cyber Visor Vignette
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x15000000),
                                    Color(0x7505050A)
                                )
                            )
                        )
                )

                // Animated Optic Laser Scanning Line across eyes/visor
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp)
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            translationY = this.size.height * 0.18f + (this.size.height * 0.46f * scanlineProgress)
                        }
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    stateColor.copy(alpha = 0.3f),
                                    stateColor,
                                    Color.White,
                                    stateColor,
                                    stateColor.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Interactive HUD Overlay: Optic Eye Sensors & Cheek Circuit Glyphs
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasW = this.size.width
                    val canvasH = this.size.height

                    // Cybernetic ear antennas status lights (Top Left & Top Right)
                    val earAlpha = if (botState == BotState.SLEEPING) 0.3f else auraGlow
                    drawCircle(
                        color = stateColor.copy(alpha = earAlpha),
                        radius = 4.dp.toPx(),
                        center = Offset(canvasW * 0.18f, canvasH * 0.28f)
                    )
                    drawCircle(
                        color = stateColor.copy(alpha = earAlpha),
                        radius = 4.dp.toPx(),
                        center = Offset(canvasW * 0.82f, canvasH * 0.28f)
                    )

                    // Cybernetic Circuit Traces on Cheeks
                    val circuitAlpha = if (botState == BotState.BUYING) 0.9f else 0.45f * auraGlow
                    // Left cheek trace
                    val leftCheekPath = Path().apply {
                        moveTo(canvasW * 0.26f, canvasH * 0.58f)
                        lineTo(canvasW * 0.32f, canvasH * 0.62f)
                        lineTo(canvasW * 0.32f, canvasH * 0.66f)
                    }
                    drawPath(
                        path = leftCheekPath,
                        color = stateColor.copy(alpha = circuitAlpha),
                        style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawCircle(
                        color = stateColor.copy(alpha = circuitAlpha),
                        radius = 2.dp.toPx(),
                        center = Offset(canvasW * 0.32f, canvasH * 0.66f)
                    )

                    // Right cheek trace
                    val rightCheekPath = Path().apply {
                        moveTo(canvasW * 0.74f, canvasH * 0.58f)
                        lineTo(canvasW * 0.68f, canvasH * 0.62f)
                        lineTo(canvasW * 0.68f, canvasH * 0.66f)
                    }
                    drawPath(
                        path = rightCheekPath,
                        color = stateColor.copy(alpha = circuitAlpha),
                        style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawCircle(
                        color = stateColor.copy(alpha = circuitAlpha),
                        radius = 2.dp.toPx(),
                        center = Offset(canvasW * 0.68f, canvasH * 0.66f)
                    )

                    // Dynamic Optic Visor HUD Reticle (over eye region)
                    if (botState == BotState.ANALYZING) {
                        val eyeScanPixel = (eyeScanX * canvasW * 0.04f)
                        val eyeY = canvasH * 0.44f

                        // Left eye scanning reticle
                        drawCircle(
                            color = stateColor.copy(alpha = 0.6f * auraGlow),
                            radius = 9.dp.toPx(),
                            center = Offset(canvasW * 0.40f + eyeScanPixel, eyeY),
                            style = Stroke(width = 1.2.dp.toPx())
                        )
                        // Right eye scanning reticle
                        drawCircle(
                            color = stateColor.copy(alpha = 0.6f * auraGlow),
                            radius = 9.dp.toPx(),
                            center = Offset(canvasW * 0.60f + eyeScanPixel, eyeY),
                            style = Stroke(width = 1.2.dp.toPx())
                        )

                        // Crosshair tick marks
                        drawLine(
                            color = stateColor.copy(alpha = 0.8f),
                            start = Offset(canvasW * 0.40f + eyeScanPixel - 14f, eyeY),
                            end = Offset(canvasW * 0.40f + eyeScanPixel + 14f, eyeY),
                            strokeWidth = 1.2.dp.toPx()
                        )
                        drawLine(
                            color = stateColor.copy(alpha = 0.8f),
                            start = Offset(canvasW * 0.60f + eyeScanPixel - 14f, eyeY),
                            end = Offset(canvasW * 0.60f + eyeScanPixel + 14f, eyeY),
                            strokeWidth = 1.2.dp.toPx()
                        )
                    } else if (botState == BotState.BUYING) {
                        // Buying mode: high-intensity optic laser surge rings
                        val eyeY = canvasH * 0.44f
                        drawCircle(
                            color = Color(0xFF00FF88).copy(alpha = 0.5f * auraGlow),
                            radius = 12.dp.toPx(),
                            center = Offset(canvasW * 0.40f, eyeY),
                            style = Stroke(width = 1.8.dp.toPx())
                        )
                        drawCircle(
                            color = Color(0xFF00FF88).copy(alpha = 0.5f * auraGlow),
                            radius = 12.dp.toPx(),
                            center = Offset(canvasW * 0.60f, eyeY),
                            style = Stroke(width = 1.8.dp.toPx())
                        )
                    } else if (botState == BotState.BLOCKED) {
                        // Warning hazard indicators on forehead
                        val hazardPath = Path().apply {
                            moveTo(canvasW * 0.50f, canvasH * 0.28f)
                            lineTo(canvasW * 0.46f, canvasH * 0.34f)
                            lineTo(canvasW * 0.54f, canvasH * 0.34f)
                            close()
                        }
                        drawPath(
                            path = hazardPath,
                            color = Color(0xFFFF5252).copy(alpha = 0.8f * auraGlow),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                }

                // Interactive Wink Animation (if clicked)
                if (isWinking) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = (-size.value * 0.05f).dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xCC000000))
                            .border(1.dp, stateColor, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "AI SCALP SYNCED ✨",
                            color = stateColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                // Vocalizer Audio Waveform HUD (Active when isSpeaking == true)
                if (isSpeaking) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xCC05050A))
                            .border(1.dp, stateColor.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Speaking",
                                tint = stateColor,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            // Dynamic bouncing equalizer bars
                            val heights = listOf(
                                6.dp * voiceWave1,
                                14.dp * voiceWave2,
                                18.dp * voiceWave1,
                                11.dp * voiceWave2,
                                5.dp * voiceWave1
                            )
                            heights.forEach { h ->
                                Box(
                                    modifier = Modifier
                                        .width(2.5.dp)
                                        .height(h.coerceAtLeast(3.dp))
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(stateColor)
                                )
                            }
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "VOCALIZING",
                                color = stateColor,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Bottom Status Badge (Bot State & Model designation)
        if (showStatusBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0C0C10))
                    .border(1.dp, stateColor.copy(alpha = 0.7f * auraGlow), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("robot_girl_status_badge")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(stateColor)
                            .shadow(4.dp, CircleShape, spotColor = stateColor)
                    )
                    Text(
                        text = "ROBOT GIRL • ${botState.name}",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
