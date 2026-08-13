package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.model.InvoiceStatus
import com.example.model.PaymentTerms
import com.example.model.ReceivableInvoice
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.MoneyOutBlue
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber

@Composable
fun ReceivableSection(
    invoices: List<ReceivableInvoice>,
    onCreateInvoiceClick: () -> Unit,
    onConfirmPaymentClick: (ReceivableInvoice) -> Unit,
    onSendNudgeClick: (ReceivableInvoice) -> Unit,
    onOpenTimelineClick: (String) -> Unit,
    onDialClientClick: ((ReceivableInvoice) -> Unit)? = null,
    onAttachmentClick: ((ReceivableInvoice) -> Unit)? = null,
    onCameraClick: ((ReceivableInvoice) -> Unit)? = null,
    onDeleteInvoiceClick: ((ReceivableInvoice) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedFilterTab by remember { mutableStateOf<PaymentTerms?>(null) } // null = All Terms

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Accounts Receivable",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "Money In / Debtors Ledger",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Button(
                onClick = onCreateInvoiceClick,
                colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .height(36.dp)
                    .testTag("create_tax_invoice_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Invoice",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tax Invoice",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Terms Filter Tabs: [All], [COD / Immediate], [7-Day], [30-Day], [Overdue]
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            item {
                TermFilterChip(
                    label = "All Terms (${invoices.size})",
                    isSelected = selectedFilterTab == null,
                    onClick = { selectedFilterTab = null }
                )
            }

            items(PaymentTerms.values()) { term ->
                val count = invoices.count { it.terms == term }
                TermFilterChip(
                    label = "${term.displayName} ($count)",
                    isSelected = selectedFilterTab == term,
                    isOverdue = term == PaymentTerms.OVERDUE,
                    onClick = { selectedFilterTab = term }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Invoice Cards List
        val filteredInvoices = if (selectedFilterTab == null) {
            invoices
        } else {
            invoices.filter { it.terms == selectedFilterTab }
        }

        if (filteredInvoices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SlateSurface)
                    .border(1.dp, SlateBorder),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No invoices found for this term category.",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                filteredInvoices.forEach { invoice ->
                    ReceivableCard(
                        invoice = invoice,
                        onConfirmPayment = { onConfirmPaymentClick(invoice) },
                        onSendNudge = { onSendNudgeClick(invoice) },
                        onOpenTimeline = { onOpenTimelineClick(invoice.id) },
                        onDialClient = { onDialClientClick?.invoke(invoice) },
                        onAttachment = { onAttachmentClick?.invoke(invoice) },
                        onCamera = { onCameraClick?.invoke(invoice) },
                        onDeleteInvoice = { onDeleteInvoiceClick?.invoke(invoice) }
                    )
                }
            }
        }
    }
}

@Composable
fun TermFilterChip(
    label: String,
    isSelected: Boolean,
    isOverdue: Boolean = false,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected -> if (isOverdue) OverdueRed else BurntOrange
        else -> SlateSurface
    }
    val textColor = if (isSelected) TextWhite else TextMuted
    val borderColor = if (isSelected) Color.Transparent else SlateBorder

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun ReceivableCard(
    invoice: ReceivableInvoice,
    onConfirmPayment: () -> Unit,
    onSendNudge: () -> Unit,
    onOpenTimeline: () -> Unit,
    onDialClient: () -> Unit = {},
    onAttachment: () -> Unit = {},
    onCamera: () -> Unit = {},
    onDeleteInvoice: () -> Unit = {}
) {
    val isPaid = invoice.status == InvoiceStatus.PAID
    val isOverdue = invoice.terms == PaymentTerms.OVERDUE && !isPaid

    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    // Confirmation prompt for Delete/Archive button
    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = {
                Text(
                    text = "Archive & Delete Invoice?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete and archive invoice ${invoice.invoiceNumber} (${invoice.clientName})? This action will remove it from the active Debtors ledger.",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmationDialog = false
                        onDeleteInvoice()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OverdueRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("confirm_delete_invoice_dialog_btn")
                ) {
                    Text("Confirm Delete", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmationDialog = false },
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Text("Cancel", fontSize = 11.sp, color = TextMuted)
                }
            },
            containerColor = SlateSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SlateSurface
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isOverdue) OverdueRed.copy(alpha = 0.6f) else SlateBorder
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Card Top Row: Invoice # & Terms Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SlateSurfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = invoice.invoiceNumber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when {
                                    isPaid -> MoneyInGreen.copy(alpha = 0.2f)
                                    isOverdue -> OverdueRed.copy(alpha = 0.2f)
                                    invoice.terms == PaymentTerms.COD -> WarningAmber.copy(alpha = 0.2f)
                                    else -> BurntOrange.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isPaid) "PAID / SETTLED" else invoice.terms.displayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isPaid -> MoneyInGreen
                                isOverdue -> OverdueRed
                                invoice.terms == PaymentTerms.COD -> WarningAmber
                                else -> BurntOrange
                            }
                        )
                    }
                }

                // Timeline button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SlateSurfaceVariant)
                        .clickable { onOpenTimeline() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Timeline",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Client Name & VAT
            Text(
                text = invoice.clientName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Client VAT #: ${invoice.clientVat}",
                fontSize = 10.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = invoice.description,
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount Breakdown Row
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
                        text = "Net: R ${String.format("%,.2f", invoice.netAmount)} + 15% VAT",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Gross Total",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }

                Text(
                    text = "R ${String.format("%,.2f", invoice.grossAmount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isPaid) MoneyInGreen else TextWhite
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Icon-Only Action Buttons Row (Phone, Handcuff/Attachment, Camera, Delete) - Completely Replaces Send Nudge & Confirm Payment
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = SlateSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Phone Dialer icon
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp) // Minimum 48dp thumb-friendly touch target
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onDialClient() }
                            .testTag("quick_dial_${invoice.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone Dialer",
                            tint = MoneyInGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(SlateBorder.copy(alpha = 0.5f))
                    )

                    // 2. Handcuff / Attachment icon
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp) // Minimum 48dp thumb-friendly touch target
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onAttachment() }
                            .testTag("quick_attach_${invoice.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Handcuff Attachment",
                            tint = BurntOrange,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(SlateBorder.copy(alpha = 0.5f))
                    )

                    // 3. Camera icon
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp) // Minimum 48dp thumb-friendly touch target
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onCamera() }
                            .testTag("quick_camera_${invoice.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "POD Camera",
                            tint = MoneyOutBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(SlateBorder.copy(alpha = 0.5f))
                    )

                    // 4. Delete / Archive icon
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp) // Minimum 48dp thumb-friendly touch target
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showDeleteConfirmationDialog = true }
                            .testTag("quick_delete_${invoice.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete or Archive Invoice",
                            tint = OverdueRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}
