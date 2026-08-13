package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.PayableBill
import com.example.model.ReceivableInvoice
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.MoneyOutBlue
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun MonthEndReportModal(
    invoices: List<ReceivableInvoice>,
    bills: List<PayableBill>,
    onDismiss: () -> Unit,
    onExportComplete: () -> Unit
) {
    val totalOutputVat = invoices.sumOf { it.vatAmount }
    val totalNetInvoiced = invoices.sumOf { it.netAmount }
    val totalGrossInvoiced = invoices.sumOf { it.grossAmount }

    val totalPayablesAmount = bills.sumOf { it.amount }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            color = SlateSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF Report",
                            tint = BurntOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Month-End Journal Export",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Structured CSV / PDF for Accountant",
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

                Spacer(modifier = Modifier.height(14.dp))

                // Preview Journal Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Funktion Ledger Month-End Summary",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BurntOrange
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Debtors Invoiced:", fontSize = 11.sp, color = TextMuted)
                            Text("R ${String.format("%,.2f", totalGrossInvoiced)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Net Turnover Sales:", fontSize = 11.sp, color = TextMuted)
                            Text("R ${String.format("%,.2f", totalNetInvoiced)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MoneyInGreen)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Output SARS VAT (15%):", fontSize = 11.sp, color = TextMuted)
                            Text("R ${String.format("%,.2f", totalOutputVat)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Creditor Payables & Staff Day-Rates:", fontSize = 11.sp, color = TextMuted)
                            Text("R ${String.format("%,.2f", totalPayablesAmount)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MoneyOutBlue)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // CSV Raw Format Code Box Preview
                Text(
                    text = "Generated CSV Journal Preview",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SlateSurfaceVariant)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "DATE,DOC_TYPE,ENTITY,VAT_NO,NET,VAT_15,GROSS_TOTAL,STATUS\n" +
                                "2026-08-10,INV,Highveldt Marble,4920192831,85000.00,12750.00,97750.00,UNPAID\n" +
                                "2026-08-10,INV,Kruger Stone Works,4810293847,42000.00,6300.00,48300.00,UNPAID\n" +
                                "2026-08-10,BILL,Mamba Cement SA,4109827361,-16043.48,-2406.52,-18450.00,DUE\n" +
                                "2026-08-14,STAFF_PAY,Nomvula Dlamini (Maid),DOMESTIC,-450.00,0.00,-450.00,SETTLED",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextWhite,
                        lineHeight = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onExportComplete,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "CSV",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Download CSV", fontSize = 11.sp)
                    }

                    Button(
                        onClick = onExportComplete,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(42.dp)
                            .testTag("export_month_end_confirm"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BurntOrange)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Email Accountant", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
