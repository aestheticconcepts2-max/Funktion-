package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.PayableBill
import com.example.ui.theme.MoneyOutBlue
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun FlyingCashModal(
    bill: PayableBill,
    onAnimationComplete: () -> Unit
) {
    // Flying animation parameters
    val flyY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Float upwards and fade out
        flyY.animateTo(
            targetValue = -180f,
            animationSpec = tween(durationMillis = 1100)
        )
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300)
        )
        onAnimationComplete()
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = SlateSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Flying Cash Icon Container
                Box(
                    modifier = Modifier
                        .offset(y = flyY.value.dp)
                        .alpha(alpha.value)
                        .size(96.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MoneyOutBlue.copy(alpha = 0.2f))
                        .testTag("flying_cash_animation"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = "Wad of Cash",
                            tint = MoneyOutBlue,
                            modifier = Modifier.size(48.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = "Wings",
                            tint = MoneyOutBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Settling Supplier Bill 💸",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = bill.supplierName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MoneyOutBlue
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "R ${String.format("%,.2f", bill.amount)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Text(
                    text = "EFT Payment Dispatched via Bank Feed",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }
    }
}
