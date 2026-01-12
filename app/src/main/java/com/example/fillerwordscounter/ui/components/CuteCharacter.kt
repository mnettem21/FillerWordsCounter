package com.example.fillerwordscounter.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.ui.theme.AppColors
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun CuteCharacter(
    modifier: Modifier = Modifier,
    mood: CharacterMood = CharacterMood.Calm
) {
    val bodyColor = when (mood) {
        CharacterMood.Calm -> AppColors.Orange
        CharacterMood.Listening -> AppColors.SoftBlue
        CharacterMood.Celebrating -> AppColors.Peach
    }

    Box(modifier = modifier.size(140.dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            // Shadow blob
            drawRoundRect(
                color = Color(0x22000000),
                topLeft = Offset(size.width * 0.22f, size.height * 0.78f),
                size = Size(size.width * 0.56f, size.height * 0.10f),
                cornerRadius = CornerRadius(999f, 999f)
            )

            // Head/Body (rounded blob)
            drawRoundRect(
                color = bodyColor,
                topLeft = Offset(size.width * 0.22f, size.height * 0.18f),
                size = Size(size.width * 0.56f, size.height * 0.58f),
                cornerRadius = CornerRadius(60f, 60f)
            )

            // Belly stripe
            drawRoundRect(
                color = Color(0x33FFFFFF),
                topLeft = Offset(size.width * 0.28f, size.height * 0.50f),
                size = Size(size.width * 0.44f, size.height * 0.10f),
                cornerRadius = CornerRadius(999f, 999f)
            )

            // Legs
            val legColor = Color(0xFF2A2A2A)
            drawRoundRect(
                color = legColor,
                topLeft = Offset(size.width * 0.40f, size.height * 0.70f),
                size = Size(size.width * 0.08f, size.height * 0.10f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            drawRoundRect(
                color = legColor,
                topLeft = Offset(size.width * 0.52f, size.height * 0.70f),
                size = Size(size.width * 0.08f, size.height * 0.10f),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Feet
            drawRoundRect(
                color = legColor,
                topLeft = Offset(size.width * 0.36f, size.height * 0.79f),
                size = Size(size.width * 0.14f, size.height * 0.06f),
                cornerRadius = CornerRadius(999f, 999f)
            )
            drawRoundRect(
                color = legColor,
                topLeft = Offset(size.width * 0.50f, size.height * 0.79f),
                size = Size(size.width * 0.14f, size.height * 0.06f),
                cornerRadius = CornerRadius(999f, 999f)
            )

            // Face (eyes + smile)
            val faceColor = Color(0xFF2A2A2A)

            // Closed eyes = curved lines (approx with rounded rects)
            drawRoundRect(
                color = faceColor,
                topLeft = Offset(size.width * 0.36f, size.height * 0.34f),
                size = Size(size.width * 0.10f, size.height * 0.03f),
                cornerRadius = CornerRadius(999f, 999f)
            )
            drawRoundRect(
                color = faceColor,
                topLeft = Offset(size.width * 0.54f, size.height * 0.34f),
                size = Size(size.width * 0.10f, size.height * 0.03f),
                cornerRadius = CornerRadius(999f, 999f)
            )

            // Mouth
            if (mood == CharacterMood.Celebrating) {
                // Bigger curved smile 😊
                val mouthPath = Path().apply {
                    val centerX = size.width * 0.50f
                    val centerY = size.height * 0.44f
                    val width = size.width * 0.20f
                    val height = size.height * 0.07f

                    moveTo(centerX - width, centerY)
                    quadraticBezierTo(
                        centerX,
                        centerY + height,
                        centerX + width,
                        centerY
                    )
                }

                drawPath(
                    path = mouthPath,
                    color = faceColor,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = size.width * 0.02f,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )
            } else {
                // Calm / Listening: subtle flat smile
                drawRoundRect(
                    color = faceColor,
                    topLeft = Offset(size.width * 0.42f, size.height * 0.42f),
                    size = Size(size.width * 0.26f, size.height * 0.035f),
                    cornerRadius = CornerRadius(999f, 999f)
                )
            }

            // Optional “headphones” when listening
            if (mood == CharacterMood.Listening) {
                val bandColor = Color(0xFF4A4A4A)
                val padColor = Color(0xFF2F7EC7)

                // Ear pads (keep these where they are unless you want tighter headphones)
                val padW = size.width * 0.095f
                val padH = size.height * 0.18f
                val leftPadX = size.width * 0.20f
                val rightPadX = size.width * 0.69f
                val padY = size.height * 0.24f // slightly lower than before

                // Headband (make it lower + a bit thicker)
                val bandH = size.height * 0.10f
                val bandY = size.height * 0.13f // LOWER the band (was ~0.12-0.14)
                val bandW = (rightPadX - leftPadX) * 0.80f // shorter top band
                val bandX = (size.width - bandW) / 2f

                drawRoundRect(
                    color = bandColor,
                    topLeft = Offset(bandX, bandY),
                    size = Size(bandW, bandH),
                    cornerRadius = CornerRadius(999f, 999f)
                )

                // Pads
                drawRoundRect(
                    color = padColor,
                    topLeft = Offset(leftPadX, padY),
                    size = Size(padW, padH),
                    cornerRadius = CornerRadius(22f, 22f)
                )
                drawRoundRect(
                    color = padColor,
                    topLeft = Offset(rightPadX, padY),
                    size = Size(padW, padH),
                    cornerRadius = CornerRadius(22f, 22f)
                )

                // Curved connectors (arc-like) from pads up into the band
                val strokeW = size.width * 0.035f
                val stroke = Stroke(width = strokeW, cap = StrokeCap.Round)

                val overlap = size.height * 0.015f

                // Target points on the band (near ends)
                val bandTopY = bandY + bandH * 0.55f
                val leftBandAttach = Offset(bandX + bandW * 0.18f, bandTopY)
                val rightBandAttach = Offset(bandX + bandW * 0.82f, bandTopY)

                // Start points on top of pads
                val leftPadTop = Offset(leftPadX + padW * 0.60f, padY + overlap)
                val rightPadTop = Offset(rightPadX + padW * 0.40f, padY + overlap)

                // Control points (pull outward a bit so it curves nicely)
                val leftCtrl = Offset(leftPadTop.x - size.width * 0.08f, (leftPadTop.y + leftBandAttach.y) / 2f)
                val rightCtrl = Offset(rightPadTop.x + size.width * 0.08f, (rightPadTop.y + rightBandAttach.y) / 2f)

                val leftPath = Path().apply {
                    moveTo(leftPadTop.x, leftPadTop.y)
                    quadraticBezierTo(leftCtrl.x, leftCtrl.y, leftBandAttach.x, leftBandAttach.y)
                }
                val rightPath = Path().apply {
                    moveTo(rightPadTop.x, rightPadTop.y)
                    quadraticBezierTo(rightCtrl.x, rightCtrl.y, rightBandAttach.x, rightBandAttach.y)
                }

                drawPath(path = leftPath, color = bandColor, style = stroke)
                drawPath(path = rightPath, color = bandColor, style = stroke)

                // Optional: small "rings" where connector meets pad (cute detail)
                val ringSize = size.width * 0.04f
                drawRoundRect(
                    color = bandColor,
                    topLeft = Offset(leftPadX + padW * 0.50f, padY + padH * 0.08f),
                    size = Size(ringSize, ringSize),
                    cornerRadius = CornerRadius(999f, 999f)
                )
                drawRoundRect(
                    color = bandColor,
                    topLeft = Offset(rightPadX + padW * 0.12f, padY + padH * 0.08f),
                    size = Size(ringSize, ringSize),
                    cornerRadius = CornerRadius(999f, 999f)
                )
            }
        }
    }
}

enum class CharacterMood { Calm, Listening, Celebrating }