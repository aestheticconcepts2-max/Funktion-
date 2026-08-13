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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.LineItem
import com.example.model.PaymentTerms
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.BurntOrangeDark
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun TaxInvoiceEngineModal(
    onDismiss: () -> Unit,
    onCreateInvoice: (
        clientName: String,
        clientVat: String,
        description: String,
        terms: PaymentTerms,
        netAmount: Double,
        isVatInclusive: Boolean,
        lineItems: List<LineItem>
    ) -> Unit
) {
    var clientName by remember { mutableStateOf("Highveldt Marble & Granite") }
    var clientVat by remember { mutableStateOf("4920192831") }
    var supplierVat by remember { mutableStateOf("4109827361") } // Funktion SME SARS VAT #
    var selectedTerms by remember { mutableStateOf(PaymentTerms.DAYS_30) }
    var isVatInclusive by remember { mutableStateOf(false) } // Dual Mode Toggle
    var attachmentLocked by remember { mutableStateOf(false) }

    // Line items list
    val lineItems = remember {
        mutableStateListOf(
            LineItem("1", "High-Gloss Marble Slabs (20mm Polished)", 10.0, 3500.0),
            LineItem("2", "Polymer Anti-Stain Sealant", 2.0, 1250.0)
        )
    }

    var newItemDesc by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("1") }
    var newItemPrice by remember { mutableStateOf("0") }

    // Computations
    val subTotalNet = lineItems.sumOf { it.total }
    val vatRate = 0.15
    val calculatedNet: Double
    val calculatedVat: Double
    val calculatedGross: Double

    if (isVatInclusive) {
        calculatedGross = subTotalNet
        calculatedNet = calculatedGross / (1.0 + vatRate)
        calculatedVat = calculatedGross - calculatedNet
    } else {
        calculatedNet = subTotalNet
        calculatedVat = calculatedNet * vatRate
        calculatedGross = calculatedNet + calculatedVat
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = SlateSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BurntOrangeDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SARS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SARS Tax Invoice Builder",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Dual Mode VAT & SARS Compliant Engine",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dual Mode Toggle: Exclusive vs Inclusive
                Text(
                    text = "VAT Calculation Mode",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SlateSurfaceVariant)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isVatInclusive) BurntOrange else SlateSurfaceVariant)
                            .clickable { isVatInclusive = false }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "VAT Exclusive (Net + 15%)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isVatInclusive) TextWhite else TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isVatInclusive) BurntOrange else SlateSurfaceVariant)
                            .clickable { isVatInclusive = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "VAT Inclusive (Gross Extract)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isVatInclusive) TextWhite else TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // VAT Numbers & Client Name Inputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = supplierVat,
                        onValueChange = { supplierVat = it },
                        label = { Text("Supplier VAT #", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = clientVat,
                        onValueChange = { clientVat = it },
                        label = { Text("Client VAT #", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Client Business / Entity Name", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurntOrange,
                        unfocusedBorderColor = SlateBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Line Item Builder Section
                Text(
                    text = "Invoice Line Items",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Added Line Items
                lineItems.forEachIndexed { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.description,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Qty: ${item.quantity} x R ${String.format("%,.2f", item.unitPrice)}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "R ${String.format("%,.2f", item.total)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                IconButton(
                                    onClick = { lineItems.removeAt(index) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Add Item Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItemDesc,
                        onValueChange = { newItemDesc = it },
                        placeholder = { Text("Item Description", fontSize = 10.sp) },
                        modifier = Modifier.weight(2f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = newItemQty,
                        onValueChange = { newItemQty = it },
                        placeholder = { Text("Qty", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = newItemPrice,
                        onValueChange = { newItemPrice = it },
                        placeholder = { Text("Price", fontSize = 10.sp) },
                        modifier = Modifier.weight(1.2f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    IconButton(
                        onClick = {
                            val qty = newItemQty.toDoubleOrNull() ?: 1.0
                            val price = newItemPrice.toDoubleOrNull() ?: 0.0
                            if (newItemDesc.isNotBlank() && price > 0) {
                                lineItems.add(LineItem(System.currentTimeMillis().toString(), newItemDesc, qty, price))
                                newItemDesc = ""
                                newItemPrice = "0"
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BurntOrange)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SA Banking Details Block (Absa Bank, Branch 632005)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "South African Banking Settlement Details",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BurntOrange
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bank: Absa Bank SA | Branch Code: 632005\nSWIFT: ABSAZAJJ | Account #: 40-9821-3301\nAuto-Ref: FUNK-INV-${(100..999).random()}",
                            fontSize = 10.sp,
                            color = TextWhite,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Calculation Summary Block
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SlateSurfaceVariant)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Net Subtotal:", fontSize = 12.sp, color = TextMuted)
                        Text("R ${String.format("%,.2f", calculatedNet)}", fontSize = 12.sp, color = TextWhite)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("SA VAT (15%):", fontSize = 12.sp, color = TextMuted)
                        Text("R ${String.format("%,.2f", calculatedVat)}", fontSize = 12.sp, color = TextWhite)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Gross Total Due:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("R ${String.format("%,.2f", calculatedGross)}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = BurntOrange)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Tools & Communication Row (Handcuff / Lock attachment + Wright Brothers Rocket Send)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Handcuff / Attachment Tool Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (attachmentLocked) BurntOrange else SlateSurfaceVariant)
                            .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
                            .clickable { attachmentLocked = !attachmentLocked }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Attachment",
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (attachmentLocked) "Document Locked 🔒" else "Lock Doc 🔒",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite
                            )
                        }
                    }

                    // Send Button with Rocket / Wright Brothers Biplane Icon
                    Button(
                        onClick = {
                            val desc = lineItems.firstOrNull()?.description ?: "Custom Material Order"
                            onCreateInvoice(
                                clientName,
                                clientVat,
                                desc,
                                selectedTerms,
                                subTotalNet,
                                isVatInclusive,
                                lineItems.toList()
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("issue_tax_invoice_submit_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BurntOrange)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "Send",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Issue & Dispatch Tax Invoice",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}
