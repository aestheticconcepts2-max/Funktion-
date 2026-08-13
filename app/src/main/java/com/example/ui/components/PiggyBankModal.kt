package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.ReceivableInvoice
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber
import kotlin.math.roundToInt

@Composable
fun PiggyBankModal(
    invoice: ReceivableInvoice,
    onDismiss: () -> Unit,
    onSwipedAwayToConfirm: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 300f // pixels to trigger confirm

    // Coin dropping animation state
    val coinOffsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        coinOffsetY.animateTo(
            targetValue = 60f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = SlateSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Text(
                    text = "Confirm Payment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = invoice.clientName,
                    fontSize = 13.sp,
                    color = MoneyInGreen,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Draggable Piggy Container
                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), 0) }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (kotlin.math.abs(offsetX) > swipeThreshold) {
                                        onSwipedAwayToConfirm()
                                    } else {
                                        offsetX = 0f
                                    }
                                },
                                onDragCancel = { offsetX = 0f },
                                onHorizontalDrag = { _, dragAmount ->
                                    offsetX += dragAmount
                                }
                            )
                        }
                        .clip(RoundedCornerShape(20.dp))
                        .background(SlateSurfaceVariant)
                        .border(2.dp, MoneyInGreen.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(24.dp)
                        .testTag("piggy_bank_swipe_card"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Dropping Gold Coin Animation
                        Box(
                            modifier = Modifier
                                .offset(y = coinOffsetY.value.dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(WarningAmber),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "R",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SlateSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Piggy Bank Icon Badge
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(MoneyInGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Savings,
                                contentDescription = "Piggy Bank",
                                tint = MoneyInGreen,
                                modifier = Modifier.size(54.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "R ${String.format("%,.2f", invoice.grossAmount)}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = MoneyInGreen
                        )
                        Text(
                            text = "Invoice #${invoice.invoiceNumber}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Swipe Instruction Prompt Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Swipe,
                            contentDescription = "Swipe Away",
                            tint = MoneyInGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Swipe Piggy away to confirm & post to ledger ↔",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
