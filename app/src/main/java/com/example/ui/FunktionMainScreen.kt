package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.InvoiceStatus
import com.example.ui.components.AppHeader
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CallLogModal
import com.example.ui.components.ClientCrmHubSection
import com.example.ui.components.ClientOnboardingDrawer
import com.example.ui.components.ClientTimelineSheet
import com.example.ui.components.DebugBankFeedBar
import com.example.ui.components.FlyingCashModal
import com.example.ui.components.LedgerMetricsSummary
import com.example.ui.components.LifestyleSuiteSection
import com.example.ui.components.MonthEndReportModal
import com.example.ui.components.NewDealModal
import com.example.ui.components.NewReminderModal
import com.example.ui.components.NudgeModal
import com.example.ui.components.PayableSection
import com.example.ui.components.PiggyBankModal
import com.example.ui.components.ReceivableSection
import com.example.ui.components.RocketBiplaneLaunchModal
import com.example.ui.components.SalesPipelineSection
import com.example.ui.components.TapToPayModal
import com.example.ui.components.TaxInvoiceEngineModal
import com.example.ui.theme.SlateBackground
import com.example.viewmodel.FunktionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunktionMainScreen(
    viewModel: FunktionViewModel = viewModel()
) {
    val context = LocalContext.current
    val invoices by viewModel.invoices.collectAsState()
    val bills by viewModel.bills.collectAsState()
    val timelineEvents by viewModel.timeline.collectAsState()

    val clients by viewModel.clients.collectAsState()
    val pipelineCards by viewModel.pipelineCards.collectAsState()
    val smartReminders by viewModel.smartReminders.collectAsState()
    val checklists by viewModel.checklists.collectAsState()
    val personalExpenses by viewModel.personalExpenses.collectAsState()
    val userMode by viewModel.userMode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val selectedClientId by viewModel.selectedClientId.collectAsState()
    val personalAllowance by viewModel.personalAllowance.collectAsState()

    val dishes by viewModel.dishes.collectAsState()
    val staffMembers by viewModel.staffMembers.collectAsState()
    val notebookEntries by viewModel.notebookEntries.collectAsState()

    val showNewClientDrawer by viewModel.showNewClientDrawer.collectAsState()
    val showNewDealModal by viewModel.showNewDealModal.collectAsState()
    val showNewReminderModal by viewModel.showNewReminderModal.collectAsState()
    val showTapToPayModal by viewModel.showTapToPayModal.collectAsState()

    val showInvoiceModal by viewModel.showInvoiceModal.collectAsState()
    val piggyBankInvoice by viewModel.piggyBankInvoice.collectAsState()
    val flyingCashBill by viewModel.flyingCashBill.collectAsState()
    val nudgeInvoice by viewModel.nudgeInvoice.collectAsState()
    val activeTimelineClientId by viewModel.activeClientTimelineId.collectAsState()
    val showMonthEndDialog by viewModel.showMonthEndDialog.collectAsState()
    val callLogClientName by viewModel.callLogClientName.collectAsState()
    val toastMsg by viewModel.toastMessage.collectAsState()

    var activeLaunchAttachment by remember { mutableStateOf<Pair<String, String>?>(null) } // clientName, attachmentName
    var activeReminderClient by remember { mutableStateOf<String?>(null) } // clientName

    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Calculate metrics
    val collectedMoneyIn = invoices.filter { it.status == InvoiceStatus.PAID }.sumOf { it.grossAmount }
    val pendingMoneyIn = invoices.filter { it.status == InvoiceStatus.UNPAID }.sumOf { it.grossAmount }

    val paidMoneyOut = bills.filter { it.status == com.example.model.BillStatus.SETTLED }.sumOf { it.amount }
    val pendingMoneyOut = bills.filter { it.status == com.example.model.BillStatus.DUE }.sumOf { it.amount }

    // Toast feedback listener
    LaunchedEffect(toastMsg) {
        toastMsg?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.setActiveTab(it) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SlateBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Global Header with Live Search Bar & + New Client Button
                AppHeader(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onNewClientClick = { viewModel.openNewClientDrawer() },
                    onSendMonthEnd = { viewModel.openMonthEndDialog() }
                )

                when (activeTab) {
                    0 -> {
                        // --- TAB 1: EXECUTIVE LEDGER ---
                        LedgerMetricsSummary(
                            collectedMoneyIn = collectedMoneyIn,
                            pendingMoneyIn = pendingMoneyIn,
                            paidMoneyOut = paidMoneyOut,
                            pendingMoneyOut = pendingMoneyOut
                        )

                        DebugBankFeedBar(
                            onSimulateIncomingEFT = { viewModel.simulateIncomingEFT() },
                            onSimulateOutboundPayment = { viewModel.simulateOutboundPayment() }
                        )

                        ReceivableSection(
                            invoices = invoices,
                            onCreateInvoiceClick = { viewModel.openInvoiceModal() },
                            onConfirmPaymentClick = { invoice -> viewModel.triggerConfirmPayment(invoice) },
                            onSendNudgeClick = { invoice -> viewModel.openNudgeModal(invoice) },
                            onOpenTimelineClick = { clientId -> viewModel.openClientTimeline(clientId) },
                            onDialClientClick = { invoice -> viewModel.dialClientPhone(invoice.clientName) },
                            onAttachmentClick = { invoice -> viewModel.viewInvoiceAttachment(invoice) },
                            onCameraClick = { invoice -> viewModel.openCameraProof(invoice) },
                            onDeleteInvoiceClick = { invoice -> viewModel.deleteInvoice(invoice.id) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PayableSection(
                            bills = bills,
                            onPayBillClick = { bill -> viewModel.triggerPayBill(bill) }
                        )
                    }

                    1 -> {
                        // --- TAB 2: CONSOLIDATED CLIENT HUB (CRM) ---
                        ClientCrmHubSection(
                            clients = clients,
                            invoices = invoices,
                            timelineEvents = timelineEvents,
                            reminders = smartReminders,
                            selectedClientId = selectedClientId,
                            searchQuery = searchQuery,
                            onSelectClient = { viewModel.selectClient(it) },
                            onNewClientClick = { viewModel.openNewClientDrawer() },
                            onOpenCallLog = { clientName -> viewModel.openCallLogDialog(clientName) },
                            onTriggerPayment = { invoice -> viewModel.triggerConfirmPayment(invoice) },
                            onToggleReminder = { id -> viewModel.toggleReminderCompleted(id) },
                            onAddReminderClick = { client ->
                                activeReminderClient = client.name
                                viewModel.openNewReminderModal()
                            },
                            onSaveClientNotes = { clientId, notes -> viewModel.updateClientNotes(clientId, notes) },
                            onLaunchAttachment = { clientName, attName ->
                                activeLaunchAttachment = Pair(clientName, attName)
                            }
                        )
                    }

                    2 -> {
                        // --- TAB 3: SALES PIPELINE MODULE ---
                        SalesPipelineSection(
                            pipelineCards = pipelineCards,
                            onAddDealClick = { viewModel.openNewDealModal() },
                            onMoveStage = { cardId, newStage -> viewModel.movePipelineStage(cardId, newStage) },
                            onReassignKam = { cardId, newKam -> viewModel.reassignKam(cardId, newKam) },
                            onMoveToHousePool = { cardId -> viewModel.moveToHousePool(cardId) }
                        )
                    }

                    3 -> {
                        // --- TAB 4: MULTI-USER LIFESTYLE DIARY & CALENDAR ---
                        LifestyleSuiteSection(
                            userMode = userMode,
                            checklists = checklists,
                            personalExpenses = personalExpenses,
                            dishes = dishes,
                            staffMembers = staffMembers,
                            notebookEntries = notebookEntries,
                            allowance = personalAllowance,
                            onSetUserMode = { viewModel.setUserMode(it) },
                            onToggleChecklist = { viewModel.toggleChecklistItem(it) },
                            onAddChecklist = { mode, cat, title, due -> viewModel.addChecklistItem(mode, cat, title, due) },
                            onCreateDishRecipe = { dishName, cat, servings, ingredients, price, notes ->
                                viewModel.createDishRecipe(dishName, cat, servings, ingredients, price, notes)
                            },
                            onAddStaff = { name, role, dayRate, shiftDate ->
                                viewModel.addStaffMember(name, role, dayRate, shiftDate)
                            },
                            onMarkStaffPaid = { staffId -> viewModel.markStaffPaid(staffId) },
                            onAddNotebookEntry = { title, type, content, cat ->
                                viewModel.addNotebookEntry(title, type, content, cat)
                            },
                            onShareNotebookEntry = { entry, channel ->
                                viewModel.shareNotebookEntry(entry, channel)
                            },
                            onDeleteNotebookEntry = { entryId -> viewModel.deleteNotebookEntry(entryId) },
                            onOpenTapToPay = { viewModel.openTapToPayModal() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- OVERLAY MODALS & SLIDE-OVER SHEETS ---

        // 1. Client Onboarding Drawer
        if (showNewClientDrawer) {
            ClientOnboardingDrawer(
                onDismiss = { viewModel.closeNewClientDrawer() },
                onSaveClient = { name, companyName, vatNumber, mobile, whatsapp, landline, email, billingAddress, jobSiteAddress, bankName, accountNumber, branchCode, terms ->
                    viewModel.createNewClient(
                        name, companyName, vatNumber, mobile, whatsapp, landline, email,
                        billingAddress, jobSiteAddress, bankName, accountNumber, branchCode, terms
                    )
                }
            )
        }

        // 2. Sales Pipeline New Deal Modal
        if (showNewDealModal) {
            NewDealModal(
                onDismiss = { viewModel.closeNewDealModal() },
                onSaveDeal = { clientName, desc, value, stage ->
                    viewModel.createPipelineDeal(clientName, desc, value, stage)
                }
            )
        }

        // 3. Smart Reminder Modal
        if (showNewReminderModal) {
            NewReminderModal(
                clientName = activeReminderClient ?: "Key Account",
                onDismiss = { viewModel.closeNewReminderModal() },
                onSaveReminder = { title, date, time, tag ->
                    viewModel.createSmartReminder(
                        clientId = "cli_${System.currentTimeMillis()}",
                        clientName = activeReminderClient ?: "Key Account",
                        title = title,
                        date = date,
                        time = time,
                        tag = tag
                    )
                }
            )
        }

        // 4. Contactless Tap-To-Pay Modal
        if (showTapToPayModal) {
            TapToPayModal(
                onDismiss = { viewModel.closeTapToPayModal() },
                onConfirmTapPayment = { title, category, amount ->
                    viewModel.processTapToPayExpense(title, category, amount)
                }
            )
        }

        // 5. Rocket / Wright Brothers Biplane Launch Modal
        activeLaunchAttachment?.let { (cName, aName) ->
            RocketBiplaneLaunchModal(
                clientName = cName,
                attachmentName = aName,
                onDismiss = { activeLaunchAttachment = null },
                onConfirmLaunch = { msg, channel ->
                    viewModel.sendCommunicationWithAttachment(
                        clientId = cName,
                        clientName = cName,
                        message = msg,
                        attachmentName = aName,
                        channel = channel
                    )
                    activeLaunchAttachment = null
                }
            )
        }

        // 6. SARS Tax Invoice Engine Modal
        if (showInvoiceModal) {
            TaxInvoiceEngineModal(
                onDismiss = { viewModel.closeInvoiceModal() },
                onCreateInvoice = { clientName, clientVat, description, terms, netAmount, isVatInclusive, lineItems ->
                    viewModel.createTaxInvoice(
                        clientName, clientVat, description, terms, netAmount, isVatInclusive, lineItems
                    )
                }
            )
        }

        // 7. Piggy Bank Confirmation Modal
        piggyBankInvoice?.let { inv ->
            PiggyBankModal(
                invoice = inv,
                onDismiss = { viewModel.dismissPiggyBank() },
                onSwipedAwayToConfirm = { viewModel.finalizePiggyBankPaymentSwipedAway() }
            )
        }

        // 8. Flying Cash Bill Settlement Overlay
        flyingCashBill?.let { bill ->
            FlyingCashModal(
                bill = bill,
                onAnimationComplete = { viewModel.finalizePayBillAnimation() }
            )
        }

        // 9. Overdue Nudge WhatsApp Modal
        nudgeInvoice?.let { inv ->
            NudgeModal(
                invoice = inv,
                onDismiss = { viewModel.closeNudgeModal() },
                onSendWhatsAppNudge = { invoice -> viewModel.sendNudgeWhatsApp(invoice) }
            )
        }

        // 10. Client Timeline Sheet
        activeTimelineClientId?.let { id ->
            val targetInvoice = invoices.firstOrNull { it.id == id }
            val name = targetInvoice?.clientName ?: "Client Activity Hub"
            val filteredEvents = timelineEvents.filter { it.clientId == id || it.clientName == name }

            ClientTimelineSheet(
                clientName = name,
                events = filteredEvents,
                sheetState = sheetState,
                onDismiss = { viewModel.closeClientTimeline() },
                onOpenCallLog = { clientName -> viewModel.openCallLogDialog(clientName) }
            )
        }

        // 11. Month-End Export Dialog
        if (showMonthEndDialog) {
            MonthEndReportModal(
                invoices = invoices,
                bills = bills,
                onDismiss = { viewModel.closeMonthEndDialog() },
                onExportComplete = { viewModel.closeMonthEndDialog() }
            )
        }

        // 12. Call Log Modal
        callLogClientName?.let { clientName ->
            CallLogModal(
                clientName = clientName,
                onDismiss = { viewModel.closeCallLogDialog() },
                onLogCall = { channel, duration -> viewModel.logCall(clientName, channel, duration) }
            )
        }
    }
}

