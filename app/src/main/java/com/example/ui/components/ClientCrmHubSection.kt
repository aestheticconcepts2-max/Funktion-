package com.example.ui.components

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Client
import com.example.model.InvoiceStatus
import com.example.model.PaymentTerms
import com.example.model.ReceivableInvoice
import com.example.model.SmartReminder
import com.example.model.TimelineEvent
import com.example.model.TimelineEventType
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.MoneyOutBlue
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun ClientCrmHubSection(
    clients: List<Client>,
    invoices: List<ReceivableInvoice>,
    timelineEvents: List<TimelineEvent>,
    reminders: List<SmartReminder>,
    selectedClientId: String?,
    searchQuery: String,
    onSelectClient: (String?) -> Unit,
    onNewClientClick: () -> Unit,
    onOpenCallLog: (String) -> Unit,
    onTriggerPayment: (ReceivableInvoice) -> Unit,
    onToggleReminder: (String) -> Unit,
    onAddReminderClick: (Client) -> Unit,
    onSaveClientNotes: (String, String) -> Unit,
    onLaunchAttachment: (clientName: String, attachmentName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredClients = remember(clients, searchQuery) {
        if (searchQuery.isBlank()) clients
        else clients.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.companyName.contains(searchQuery, ignoreCase = true) ||
                    it.vatNumber.contains(searchQuery)
        }
    }

    val selectedClient = remember(clients, selectedClientId) {
        clients.firstOrNull { it.id == selectedClientId }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (selectedClient == null) {
            // --- CLIENT HUB LIST VIEW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Client Hub (CRM & Accounts)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "${filteredClients.size} Key Accounts",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = onNewClientClick,
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Client",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+ New Client", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            filteredClients.forEach { client ->
                ClientCardItem(
                    client = client,
                    onClick = { onSelectClient(client.id) }
                )
            }
        } else {
            // --- ALL-IN-ONE CLIENT PROFILE PAGE ---
            ClientProfileView(
                client = selectedClient,
                invoices = invoices.filter { it.clientName.contains(selectedClient.name, ignoreCase = true) },
                timelineEvents = timelineEvents.filter { it.clientId == selectedClient.id || it.clientName == selectedClient.name },
                reminders = reminders.filter { it.clientId == selectedClient.id || it.clientName == selectedClient.name },
                onBack = { onSelectClient(null) },
                onOpenCallLog = { onOpenCallLog(selectedClient.name) },
                onTriggerPayment = onTriggerPayment,
                onToggleReminder = onToggleReminder,
                onAddReminderClick = { onAddReminderClick(selectedClient) },
                onSaveClientNotes = { notes -> onSaveClientNotes(selectedClient.id, notes) },
                onLaunchAttachment = { attName -> onLaunchAttachment(selectedClient.name, attName) }
            )
        }
    }
}

@Composable
fun ClientCardItem(
    client: Client,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("client_item_${client.id}"),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(BurntOrange.copy(alpha = 0.2f))
                        .border(1.dp, BurntOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = client.name.take(1).uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BurntOrange
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = client.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = client.companyName,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "VAT: ${client.vatNumber}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SlateSurfaceVariant)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = client.defaultTerms.displayName,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = BurntOrange
                            )
                        }
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Profile",
                tint = TextMuted
            )
        }
    }
}

@Composable
fun ClientProfileView(
    client: Client,
    invoices: List<ReceivableInvoice>,
    timelineEvents: List<TimelineEvent>,
    reminders: List<SmartReminder>,
    onBack: () -> Unit,
    onOpenCallLog: () -> Unit,
    onTriggerPayment: (ReceivableInvoice) -> Unit,
    onToggleReminder: (String) -> Unit,
    onAddReminderClick: () -> Unit,
    onSaveClientNotes: (String) -> Unit,
    onLaunchAttachment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    var selectedAttachment by remember { mutableStateOf("TaxInvoice_${client.name.replace(" ", "")}.pdf") }
    var clientNotes by remember { mutableStateOf(client.notes) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Back Navigation
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onBack() }
                .padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = BurntOrange,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Back to Client Hub",
                fontSize = 12.sp,
                color = BurntOrange,
                fontWeight = FontWeight.Bold
            )
        }

        // 1. Client Header Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = client.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = client.companyName,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "VAT Registration #${client.vatNumber}",
                            fontSize = 11.sp,
                            color = BurntOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BurntOrange.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = client.defaultTerms.displayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BurntOrange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Addresses & Banking Info
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "📍 Head Office: ${client.billingAddress}", fontSize = 11.sp, color = TextMuted)
                    Text(text = "🏗️ Job Site Delivery: ${client.jobSiteAddress}", fontSize = 11.sp, color = TextMuted)
                    Text(text = "🏦 Bank: ${client.bankName} | Acc: ${client.accountNumber} | Branch: ${client.branchCode}", fontSize = 11.sp, color = TextMuted)
                    Text(text = "📞 Contact: ${client.mobile} | ✉️ ${client.email}", fontSize = 11.sp, color = TextMuted)
                }
            }
        }

        // 2. Communication Suite
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Communication & Document Lock Suite",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BurntOrange
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Integrated Messaging Text Field
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type message, proforma note or call memo...") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("comm_message_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = SlateSurfaceVariant,
                            unfocusedContainerColor = SlateSurfaceVariant
                        )
                    )

                    // Phone Receiver Button (Direct Dialer)
                    IconButton(
                        onClick = onOpenCallLog,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BurntOrange)
                            .testTag("phone_dialer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone Dialer",
                            tint = TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Handcuff Icon Button (Locks Tax Invoice PDF / Proforma / Slab Photo)
                    IconButton(
                        onClick = {
                            selectedAttachment = "SlabPhoto_Calacatta_${client.name.take(4)}.pdf"
                            onLaunchAttachment(selectedAttachment)
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SlateSurfaceVariant)
                            .border(1.dp, BurntOrange, RoundedCornerShape(10.dp))
                            .testTag("handcuff_attachment_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Tax Invoice PDF or Slab Photo",
                            tint = BurntOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Wright Brothers Biplane / Rocket Icon Button (Swift Launch Trigger)
                    IconButton(
                        onClick = { onLaunchAttachment(selectedAttachment) },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BurntOrange)
                            .testTag("rocket_launch_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "Send via Wright Brothers Biplane / Rocket",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // 3. Smart Follow-Up & Reminder Field
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = BurntOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Smart Follow-Up & Reminders",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Button(
                        onClick = onAddReminderClick,
                        colors = ButtonDefaults.buttonColors(containerColor = SlateSurfaceVariant),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                    ) {
                        Text(text = "+ Set Reminder", fontSize = 10.sp, color = BurntOrange, fontWeight = FontWeight.Bold)
                    }
                }

                reminders.forEach { rem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateSurfaceVariant)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = rem.isCompleted,
                                onCheckedChange = { onToggleReminder(rem.id) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MoneyInGreen,
                                    uncheckedColor = TextMuted
                                )
                            )
                            Column {
                                Text(
                                    text = rem.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (rem.isCompleted) TextMuted else TextWhite
                                )
                                Text(
                                    text = "${rem.tag.displayName} • ${rem.reminderDate} at ${rem.reminderTime}",
                                    fontSize = 10.sp,
                                    color = BurntOrange
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. In-Profile Payment Processing Field
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "In-Profile Payment Settlement",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BurntOrange
                )

                if (invoices.isEmpty()) {
                    Text(
                        text = "No active unpaid invoices for this client.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                } else {
                    invoices.forEach { inv ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SlateSurfaceVariant)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = inv.invoiceNumber,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Gross: R ${String.format("%.2f", inv.grossAmount)}",
                                    fontSize = 11.sp,
                                    color = MoneyInGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            if (inv.status == InvoiceStatus.UNPAID) {
                                Button(
                                    onClick = { onTriggerPayment(inv) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CreditCard,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Piggy Bank Swipe", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Paid",
                                        tint = MoneyInGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "SETTLED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MoneyInGreen)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Client Notes & Chronological Timeline Feed
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Interactive Client Notes",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BurntOrange
                )

                OutlinedTextField(
                    value = clientNotes,
                    onValueChange = {
                        clientNotes = it
                        onSaveClientNotes(it)
                    },
                    placeholder = { Text("Add ongoing job site specs, slab preferences, or special terms...") },
                    modifier = Modifier.fillMaxWidth().testTag("client_notes_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurntOrange,
                        unfocusedBorderColor = SlateBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SlateSurfaceVariant,
                        unfocusedContainerColor = SlateSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Chronological Activity Feed & Timeline",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BurntOrange
                )

                if (timelineEvents.isEmpty()) {
                    Text(
                        text = "No timeline events logged yet.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                } else {
                    timelineEvents.forEach { ev ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SlateSurfaceVariant)
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(BurntOrange.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (ev.type) {
                                        TimelineEventType.QUOTE -> Icons.Default.Receipt
                                        TimelineEventType.INVOICE -> Icons.Default.Receipt
                                        TimelineEventType.EFT_PAYMENT -> Icons.Default.Check
                                        TimelineEventType.CALL_LOG -> Icons.Default.Phone
                                        TimelineEventType.REMINDER_NUDGE -> Icons.Default.Alarm
                                        TimelineEventType.FILE_ATTACHMENT -> Icons.Default.AttachFile
                                    },
                                    contentDescription = null,
                                    tint = BurntOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = ev.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = ev.description,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = ev.timestamp,
                                    fontSize = 9.sp,
                                    color = BurntOrange
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
