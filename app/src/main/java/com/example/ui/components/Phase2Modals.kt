package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PipelineStage
import com.example.model.ReminderTag
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDealModal(
    onDismiss: () -> Unit,
    onSaveDeal: (clientName: String, description: String, projectedValue: Double, stage: PipelineStage) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var clientName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var valueStr by remember { mutableStateOf("") }
    var selectedStage by remember { mutableStateOf(PipelineStage.IN_TALKS) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BurntOrange,
        unfocusedBorderColor = SlateBorder,
        focusedLabelColor = BurntOrange,
        unfocusedLabelColor = TextMuted,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedContainerColor = SlateSurfaceVariant,
        unfocusedContainerColor = SlateSurfaceVariant
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = "New Deal",
                        tint = BurntOrange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "New Pipeline Deal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextMuted
                    )
                }
            }

            OutlinedTextField(
                value = clientName,
                onValueChange = { clientName = it },
                label = { Text("Client Name") },
                placeholder = { Text("e.g. Highveldt Marble & Granite") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("deal_client_input"),
                colors = fieldColors
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Material Description / Order Scope") },
                placeholder = { Text("e.g. Polished Carrara Marble Slabs (20mm)") },
                modifier = Modifier.fillMaxWidth().testTag("deal_desc_input"),
                colors = fieldColors
            )

            OutlinedTextField(
                value = valueStr,
                onValueChange = { valueStr = it },
                label = { Text("Projected Value (ZAR R)") },
                placeholder = { Text("125000.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("deal_value_input"),
                colors = fieldColors
            )

            Text("Initial Stage:", fontSize = 12.sp, color = TextMuted)

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PipelineStage.values().forEach { stage ->
                    val selected = selectedStage == stage
                    FilterChip(
                        selected = selected,
                        onClick = { selectedStage = stage },
                        label = { Text(stage.displayName, fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurntOrange,
                            selectedLabelColor = TextWhite,
                            containerColor = SlateSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val value = valueStr.toDoubleOrNull() ?: 0.0
                    onSaveDeal(clientName, description, value, selectedStage)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_deal_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Add to Sales Pipeline",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReminderModal(
    clientName: String,
    onDismiss: () -> Unit,
    onSaveReminder: (title: String, date: String, time: String, tag: ReminderTag) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Today") }
    var time by remember { mutableStateOf("10:00 AM") }
    var selectedTag by remember { mutableStateOf(ReminderTag.PAYMENT_NUDGE) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BurntOrange,
        unfocusedBorderColor = SlateBorder,
        focusedLabelColor = BurntOrange,
        unfocusedLabelColor = TextMuted,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedContainerColor = SlateSurfaceVariant,
        unfocusedContainerColor = SlateSurfaceVariant
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = "Reminder",
                        tint = BurntOrange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Set Smart Reminder: $clientName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextMuted
                    )
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Reminder Task Description") },
                placeholder = { Text("e.g. Call accounts re 30-day payment nudge") },
                modifier = Modifier.fillMaxWidth().testTag("reminder_title_input"),
                colors = fieldColors
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
            }

            Text("Select Tag:", fontSize = 12.sp, color = TextMuted)

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ReminderTag.values().forEach { tag ->
                    val selected = selectedTag == tag
                    FilterChip(
                        selected = selected,
                        onClick = { selectedTag = tag },
                        label = { Text(tag.displayName, fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurntOrange,
                            selectedLabelColor = TextWhite,
                            containerColor = SlateSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onSaveReminder(title, date, time, selectedTag) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_reminder_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Save Reminder & Schedule Nudge",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TapToPayModal(
    onDismiss: () -> Unit,
    onConfirmTapPayment: (title: String, category: String, amount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by remember { mutableStateOf("Contactless Merchant Purchase") }
    var category by remember { mutableStateOf("Groceries") }
    var amountStr by remember { mutableStateOf("850.00") }

    val animY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animY.animateTo(
            targetValue = -15f,
            animationSpec = tween(durationMillis = 600)
        )
        animY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 600)
        )
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BurntOrange,
        unfocusedBorderColor = SlateBorder,
        focusedLabelColor = BurntOrange,
        unfocusedLabelColor = TextMuted,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedContainerColor = SlateSurfaceVariant,
        unfocusedContainerColor = SlateSurfaceVariant
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap-to-Pay Mobile Contactless",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(BurntOrange.copy(alpha = 0.2f))
                    .border(2.dp, BurntOrange, CircleShape)
                    .offset(y = animY.value.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Contactless,
                    contentDescription = "Tap to pay NFC",
                    tint = BurntOrange,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = "Hold phone near terminal to pay",
                fontSize = 13.sp,
                color = MoneyInGreen,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Merchant / Store Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value = amountStr,
                onValueChange = { amountStr = it },
                label = { Text("Expense Amount (ZAR R)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("tap_amount_input"),
                colors = fieldColors
            )

            Text("Category Budget:", fontSize = 12.sp, color = TextMuted)

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Groceries", "Fuel", "Tuition", "Bills").forEach { cat ->
                    val selected = category == cat
                    FilterChip(
                        selected = selected,
                        onClick = { category = cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurntOrange,
                            selectedLabelColor = TextWhite,
                            containerColor = SlateSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            Button(
                onClick = {
                    val amount = amountStr.toDoubleOrNull() ?: 0.0
                    onConfirmTapPayment(title, category, amount)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("confirm_tap_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Contactless,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Approve Tap Payment & Flying Cash Wad",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketBiplaneLaunchModal(
    clientName: String,
    attachmentName: String,
    onDismiss: () -> Unit,
    onConfirmLaunch: (message: String, channel: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var message by remember { mutableStateOf("Please see attached $attachmentName. Document is locked & verified.") }
    var selectedChannel by remember { mutableStateOf("WhatsApp") }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BurntOrange,
        unfocusedBorderColor = SlateBorder,
        focusedLabelColor = BurntOrange,
        unfocusedLabelColor = TextMuted,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedContainerColor = SlateSurfaceVariant,
        unfocusedContainerColor = SlateSurfaceVariant
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = "Rocket Launch",
                        tint = BurntOrange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Wright Brothers Biplane Launch",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextMuted
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked File",
                        tint = BurntOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Locked Attachment File:",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = attachmentName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Transmission Message") },
                modifier = Modifier.fillMaxWidth().testTag("launch_message_input"),
                colors = fieldColors
            )

            Text("Launch Transmission Channel:", fontSize = 12.sp, color = TextMuted)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("WhatsApp", "Email").forEach { channel ->
                    val selected = selectedChannel == channel
                    FilterChip(
                        selected = selected,
                        onClick = { selectedChannel = channel },
                        label = { Text(channel, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurntOrange,
                            selectedLabelColor = TextWhite,
                            containerColor = SlateSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            Button(
                onClick = { onConfirmLaunch(message, selectedChannel) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("confirm_launch_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Launch & Transmit Document",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
