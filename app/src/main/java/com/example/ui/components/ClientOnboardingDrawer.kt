package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PaymentTerms
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientOnboardingDrawer(
    onDismiss: () -> Unit,
    onSaveClient: (
        name: String,
        companyName: String,
        vatNumber: String,
        mobile: String,
        whatsapp: String,
        landline: String,
        email: String,
        billingAddress: String,
        jobSiteAddress: String,
        bankName: String,
        accountNumber: String,
        branchCode: String,
        terms: PaymentTerms
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var vatNumber by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var landline by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var billingAddress by remember { mutableStateOf("") }
    var jobSiteAddress by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var branchCode by remember { mutableStateOf("") }
    var selectedTerms by remember { mutableStateOf(PaymentTerms.DAYS_30) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SlateSurface,
        scrimColor = Color.Black.copy(alpha = 0.7f)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "New Client",
                        tint = BurntOrange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "+ New Client Onboarding",
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

            Text(
                text = "Capture client profile, SARS VAT details, job site addresses, and banking info.",
                fontSize = 12.sp,
                color = TextMuted
            )

            // Section 1: Business Identification
            Text(
                text = "1. Business Identity & VAT",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BurntOrange
            )

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

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Client Trade Name") },
                placeholder = { Text("e.g. Highveldt Marble & Granite") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("client_name_input"),
                colors = fieldColors
            )

            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                label = { Text("Registered Company Legal Name") },
                placeholder = { Text("e.g. Highveldt Stone Works SA (Pty) Ltd") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("client_company_input"),
                colors = fieldColors
            )

            OutlinedTextField(
                value = vatNumber,
                onValueChange = { vatNumber = it },
                label = { Text("SARS VAT Number") },
                placeholder = { Text("e.g. 4920192831") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("client_vat_input"),
                colors = fieldColors
            )

            // Section 2: Contact Numbers
            Text(
                text = "2. Communication Suite Contact Info",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BurntOrange
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("Mobile Number") },
                    placeholder = { Text("+27 82 000 0000") },
                    singleLine = true,
                    modifier = Modifier.weight(1f).testTag("client_mobile_input"),
                    colors = fieldColors
                )
                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("WhatsApp Number") },
                    placeholder = { Text("+27 82 000 0000") },
                    singleLine = true,
                    modifier = Modifier.weight(1f).testTag("client_whatsapp_input"),
                    colors = fieldColors
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = landline,
                    onValueChange = { landline = it },
                    label = { Text("Landline") },
                    placeholder = { Text("+27 11 000 0000") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    placeholder = { Text("accounts@client.co.za") },
                    singleLine = true,
                    modifier = Modifier.weight(1f).testTag("client_email_input"),
                    colors = fieldColors
                )
            }

            // Section 3: Addresses
            Text(
                text = "3. Billing & Job Site Delivery Address",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BurntOrange
            )

            OutlinedTextField(
                value = billingAddress,
                onValueChange = { billingAddress = it },
                label = { Text("Head Office / Billing Address") },
                placeholder = { Text("14 Granite Crescent, Midrand, Johannesburg") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value = jobSiteAddress,
                onValueChange = { jobSiteAddress = it },
                label = { Text("Job Site Delivery Address") },
                placeholder = { Text("Site 42, Waterfall City Commercial Estate") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            // Section 4: Banking Details & Terms
            Text(
                text = "4. Client Banking Details & Payment Terms",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BurntOrange
            )

            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("Bank Name") },
                placeholder = { Text("e.g. First National Bank / Absa") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("Account Number") },
                    placeholder = { Text("6281029384") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
                OutlinedTextField(
                    value = branchCode,
                    onValueChange = { branchCode = it },
                    label = { Text("Branch Code") },
                    placeholder = { Text("250655") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
            }

            Text(
                text = "Approved Payment Terms:",
                fontSize = 12.sp,
                color = TextMuted
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(PaymentTerms.COD, PaymentTerms.DAYS_7, PaymentTerms.DAYS_30).forEach { term ->
                    val selected = selectedTerms == term
                    FilterChip(
                        selected = selected,
                        onClick = { selectedTerms = term },
                        label = { Text(term.displayName, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurntOrange,
                            selectedLabelColor = TextWhite,
                            containerColor = SlateSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Save Action Button
            Button(
                onClick = {
                    onSaveClient(
                        name, companyName, vatNumber, mobile, whatsapp, landline, email,
                        billingAddress, jobSiteAddress, bankName, accountNumber, branchCode, selectedTerms
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_client_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Confirm & Onboard Client",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
