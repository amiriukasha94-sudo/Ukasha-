package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.BotState

@Composable
fun TeslaBotAvatar(
    botState: BotState,
    accentColor: Color,
    isSpeaking: Boolean = false,
    size: Dp = 96.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "TeslaBotAnimations")

    // Blinking animation: blinks every 3 seconds (brief 200ms blink)
    val blinkProgress by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "BlinkLoop"
    )

    // Dedicated blink eye height scale
    val eyeScaleY by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EyeScale"
    )

    // Eye scanning left-to-right for ANALYZING state
    val scanOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EyeScan"
    )

    // Waveform mouth movement when speaking
    val mouthWave by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 180, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "MouthWave"
    )

    // Subtle breathing halo
    val breathingPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingHalo"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFF111111))
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.2f * breathingPulse),
                shape = CircleShape
            )
            .padding(size * 0.06f)
            .clip(CircleShape)
            .background(Color(0xFF1A1A1A))
            .border(
                width = 1.dp,
                color = Color(0x1AFFFFFF),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.78f)) {
            val canvasW = this.size.width
            val canvasH = this.size.height
            val centerX = canvasW / 2f
            val centerY = canvasH / 2f

            // Inner dark glass visor
            drawCircle(
                color = Color(0xFF141414),
                radius = canvasW * 0.48f,
                center = Offset(centerX, centerY)
            )

            // Visor reflection sheen
            val sheenPath = Path().apply {
                moveTo(canvasW * 0.15f, canvasH * 0.35f)
                cubicTo(
                    canvasW * 0.3f, canvasH * 0.15f,
                    canvasW * 0.7f, canvasH * 0.15f,
                    canvasW * 0.85f, canvasH * 0.35f
                )
                cubicTo(
                    canvasW * 0.7f, canvasH * 0.25f,
                    canvasW * 0.3f, canvasH * 0.25f,
                    canvasW * 0.15f, canvasH * 0.35f
                )
            }
            drawPath(
                path = sheenPath,
                color = Color.White.copy(alpha = 0.08f)
            )

            // Draw eyes based on BotState
            drawBotEyes(
                botState = botState,
                accentColor = accentColor,
                eyeScaleY = eyeScaleY,
                scanOffset = scanOffset,
                centerX = centerX,
                centerY = centerY * 0.88f,
                w = canvasW,
                h = canvasH,
                breathingPulse = breathingPulse
            )

            // Draw mouth / voice waveform
            drawBotMouth(
                isSpeaking = isSpeaking,
                mouthWave = mouthWave,
                accentColor = accentColor,
                centerX = centerX,
                centerY = centerY * 1.42f,
                w = canvasW
            )
        }
    }
}

private fun DrawScope.drawBotEyes(
    botState: BotState,
    accentColor: Color,
    eyeScaleY: Float,
    scanOffset: Float,
    centerX: Float,
    centerY: Float,
    w: Float,
    h: Float,
    breathingPulse: Float
) {
    val eyeColor = when (botState) {
        BotState.BLOCKED -> Color(0xFFFF5252) // Warning Red
        BotState.NEWS_PAUSE -> Color(0xFFFFB300) // Amber Alert
        BotState.SLEEPING -> accentColor.copy(alpha = 0.45f)
        else -> accentColor
    }

    val eyeSpacing = w * 0.22f
    val baseEyeWidth = w * 0.24f
    val baseEyeHeight = (h * 0.075f) * eyeScaleY.coerceIn(0.12f, 1f)

    when (botState) {
        BotState.SLEEPING -> {
            // Sleeping: thin horizontal resting curved slits
            val leftEyeCenter = Offset(centerX - eyeSpacing / 1.1f, centerY)
            val rightEyeCenter = Offset(centerX + eyeSpacing / 1.1f, centerY)

            drawLine(
                color = eyeColor,
                start = Offset(leftEyeCenter.x - baseEyeWidth * 0.4f, centerY),
                end = Offset(leftEyeCenter.x + baseEyeWidth * 0.4f, centerY),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = eyeColor,
                start = Offset(rightEyeCenter.x - baseEyeWidth * 0.4f, centerY),
                end = Offset(rightEyeCenter.x + baseEyeWidth * 0.4f, centerY),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        BotState.ANALYZING -> {
            // Analyzing: eyes move left-right with scanner reticle
            val sweepShift = scanOffset * (w * 0.08f)
            val leftCenter = Offset(centerX - eyeSpacing + sweepShift, centerY)
            val rightCenter = Offset(centerX + eyeSpacing + sweepShift, centerY)

            // Visor slits
            drawRoundRect(
                color = eyeColor,
                topLeft = Offset(leftCenter.x - baseEyeWidth * 0.45f, centerY - baseEyeHeight / 2f),
                size = Size(baseEyeWidth * 0.9f, baseEyeHeight),
                cornerRadius = CornerRadius(baseEyeHeight / 2f, baseEyeHeight / 2f)
            )
            drawRoundRect(
                color = eyeColor,
                topLeft = Offset(rightCenter.x - baseEyeWidth * 0.45f, centerY - baseEyeHeight / 2f),
                size = Size(baseEyeWidth * 0.9f, baseEyeHeight),
                cornerRadius = CornerRadius(baseEyeHeight / 2f, baseEyeHeight / 2f)
            )

            // Scanning pupil glow
            drawCircle(
                color = Color.White,
                radius = 3.5f,
                center = leftCenter
            )
            drawCircle(
                color = Color.White,
                radius = 3.5f,
                center = rightCenter
            )
        }

        BotState.BUYING -> {
            // Buying: confident, slightly upward arched happy robotic LED eyes
            val leftPath = Path().apply {
                moveTo(centerX - eyeSpacing - baseEyeWidth * 0.45f, centerY + 3f)
                quadraticTo(
                    centerX - eyeSpacing, centerY - baseEyeHeight * 1.5f,
                    centerX - eyeSpacing + baseEyeWidth * 0.45f, centerY + 1f
                )
            }
            val rightPath = Path().apply {
                moveTo(centerX + eyeSpacing - baseEyeWidth * 0.45f, centerY + 1f)
                quadraticTo(
                    centerX + eyeSpacing, centerY - baseEyeHeight * 1.5f,
                    centerX + eyeSpacing + baseEyeWidth * 0.45f, centerY + 3f
                )
            }

            drawPath(
                path = leftPath,
                color = eyeColor,
                style = Stroke(width = baseEyeHeight * 1.2f, cap = StrokeCap.Round)
            )
            drawPath(
                path = rightPath,
                color = eyeColor,
                style = Stroke(width = baseEyeHeight * 1.2f, cap = StrokeCap.Round)
            )
        }

        BotState.BLOCKED -> {
            // Blocked: Shield-shaped cybernetic center visor
            val shieldPath = Path().apply {
                moveTo(centerX, centerY - h * 0.12f)
                lineTo(centerX + w * 0.18f, centerY - h * 0.05f)
                lineTo(centerX + w * 0.14f, centerY + h * 0.08f)
                lineTo(centerX, centerY + h * 0.14f)
                lineTo(centerX - w * 0.14f, centerY + h * 0.08f)
                lineTo(centerX - w * 0.18f, centerY - h * 0.05f)
                close()
            }

            drawPath(
                path = shieldPath,
                color = eyeColor.copy(alpha = 0.25f)
            )
            drawPath(
                path = shieldPath,
                color = eyeColor,
                style = Stroke(width = 3.5f)
            )

            // Inner lock dot
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(centerX, centerY)
            )
        }

        BotState.NEWS_PAUSE -> {
            // News Pause: dual alert bars + subtle pause indicators
            drawRoundRect(
                color = eyeColor,
                topLeft = Offset(centerX - eyeSpacing - baseEyeWidth * 0.4f, centerY - baseEyeHeight / 2f),
                size = Size(baseEyeWidth * 0.8f, baseEyeHeight * 0.8f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = eyeColor,
                topLeft = Offset(centerX + eyeSpacing - baseEyeWidth * 0.4f, centerY - baseEyeHeight / 2f),
                size = Size(baseEyeWidth * 0.8f, baseEyeHeight * 0.8f),
                cornerRadius = CornerRadius(3f, 3f)
            )

            // News warning icon (hazard triangle contour)
            drawLine(
                color = eyeColor,
                start = Offset(centerX, centerY - h * 0.14f),
                end = Offset(centerX - w * 0.07f, centerY - h * 0.02f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = eyeColor,
                start = Offset(centerX - w * 0.07f, centerY - h * 0.02f),
                end = Offset(centerX + w * 0.07f, centerY - h * 0.02f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = eyeColor,
                start = Offset(centerX + w * 0.07f, centerY - h * 0.02f),
                end = Offset(centerX, centerY - h * 0.14f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun DrawScope.drawBotMouth(
    isSpeaking: Boolean,
    mouthWave: Float,
    accentColor: Color,
    centerX: Float,
    centerY: Float,
    w: Float
) {
    if (isSpeaking) {
        // High-tech Tesla audio visualizer frequency bars
        val barCount = 5
        val barWidth = 3f
        val maxBarH = w * 0.14f
        val spacing = 7f

        val startX = centerX - ((barCount - 1) * spacing) / 2f

        for (i in 0 until barCount) {
            val multiplier = when (i) {
                0, 4 -> 0.45f * mouthWave
                1, 3 -> 0.85f * (1.2f - mouthWave)
                else -> 1.0f * mouthWave
            }
            val barH = maxBarH * multiplier.coerceIn(0.2f, 1f)

            drawLine(
                color = accentColor,
                start = Offset(startX + i * spacing, centerY - barH / 2f),
                end = Offset(startX + i * spacing, centerY + barH / 2f),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    } else {
        // Quiet state: subtle high-tech chin status slit
        val mouthWidth = w * 0.16f
        drawLine(
            color = Color.White.copy(alpha = 0.2f),
            start = Offset(centerX - mouthWidth / 2f, centerY),
            end = Offset(centerX + mouthWidth / 2f, centerY),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
    }
}
