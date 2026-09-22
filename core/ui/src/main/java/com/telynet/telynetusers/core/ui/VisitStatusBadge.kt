package com.telynet.telynetusers.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val StatusPendingBackground = Color(0xFFFFEDD5)
val StatusPendingText = Color(0xFFC2410C)
val StatusPendingDot = Color(0xFFF97316)

val StatusVisitedBackground = Color(0xFFD0F8EC)
val StatusVisitedText = Color(0xFF0F766E)
val StatusVisitedDot = Color(0xFF10B981)

@Composable
fun VisitStatusBadge(
    isVisited: Boolean,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (isVisited) StatusVisitedBackground else StatusPendingBackground
    val textColor = if (isVisited) StatusVisitedText else StatusPendingText
    val dotColor = if (isVisited) StatusVisitedDot else StatusPendingDot
    val label =
        if (isVisited) {
            "✓ Visited"
        } else {
            "Pending Visit"
        }

    Surface(
        shape = RoundedCornerShape(100.dp),
        color = bgColor,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        ) {
            if (!isVisited) {
                Box(
                    modifier =
                        Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(dotColor),
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                color = textColor,
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                    ),
            )
        }
    }
}
