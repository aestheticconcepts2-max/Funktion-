package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BillStatus
import com.example.model.PayableBill
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.MoneyOutBlue
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun PayableSection(
    bills: List<PayableBill>,
    onPayBillClick: (PayableBill) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Section Header
        Column {
            Text(
                text = "Accounts Payable",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Money Out / Supplier Expenses",
                fontSize = 11.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bill Cards List
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            bills.forEach { bill ->
                PayableBillCard(
                    bill = bill,
                    onPayBill = { onPayBillClick(bill) }
                )
            }
        }
    }
}

@Composable
fun PayableBillCard(
    bill: PayableBill,
    onPayBill: () -> Unit
) {
    val isSettled = bill.status == BillStatus.SETTLED

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Bill # & Due Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bill.billNumber,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Text(
                    text = if (isSettled) "SETTLED" else bill.dueDate,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSettled) MoneyInGreen else MoneyOutBlue
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Supplier Name & VAT
            Text(
                text = bill.supplierName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Supplier VAT #: ${bill.supplierVat}",
                fontSize = 10.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = bill.description,
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount & Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SlateSurfaceVariant)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Bill Total Due",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "R ${String.format("%,.2f", bill.amount)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isSettled) MoneyInGreen else TextWhite
                    )
                }

                if (isSettled) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Paid",
                            tint = MoneyInGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Paid",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoneyInGreen
                        )
                    }
                } else {
                    Button(
                        onClick = onPayBill,
                        colors = ButtonDefaults.buttonColors(containerColor = MoneyOutBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("pay_bill_button_${bill.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = "Pay Bill",
                            modifier = Modifier.size(16.dp),
                            tint = TextWhite
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pay Bill 💸",
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
