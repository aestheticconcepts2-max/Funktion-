package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.BillStatus
import com.example.model.ChecklistItem
import com.example.model.Client
import com.example.model.InvoiceStatus
import com.example.model.LineItem
import com.example.model.PayableBill
import com.example.model.PaymentTerms
import com.example.model.PersonalExpense
import com.example.model.PipelineCard
import com.example.model.PipelineStage
import com.example.model.ReceivableInvoice
import com.example.model.ReminderTag
import com.example.model.SmartReminder
import com.example.model.TimelineEvent
import com.example.model.TimelineEventType
import com.example.model.UserMode
import com.example.sound.SoundEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.example.model.DishRecipe
import com.example.model.NotebookType
import com.example.model.RecipeIngredient
import com.example.model.SharedNotebookEntry
import com.example.model.StaffMember
import com.example.model.StaffRole

class FunktionViewModel : ViewModel() {

    private val _invoices = MutableStateFlow<List<ReceivableInvoice>>(emptyList())
    val invoices: StateFlow<List<ReceivableInvoice>> = _invoices.asStateFlow()

    private val _bills = MutableStateFlow<List<PayableBill>>(emptyList())
    val bills: StateFlow<List<PayableBill>> = _bills.asStateFlow()

    private val _timeline = MutableStateFlow<List<TimelineEvent>>(emptyList())
    val timeline: StateFlow<List<TimelineEvent>> = _timeline.asStateFlow()

    // Phase 2 State
    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    private val _pipelineCards = MutableStateFlow<List<PipelineCard>>(emptyList())
    val pipelineCards: StateFlow<List<PipelineCard>> = _pipelineCards.asStateFlow()

    private val _smartReminders = MutableStateFlow<List<SmartReminder>>(emptyList())
    val smartReminders: StateFlow<List<SmartReminder>> = _smartReminders.asStateFlow()

    private val _checklists = MutableStateFlow<List<ChecklistItem>>(emptyList())
    val checklists: StateFlow<List<ChecklistItem>> = _checklists.asStateFlow()

    private val _personalExpenses = MutableStateFlow<List<PersonalExpense>>(emptyList())
    val personalExpenses: StateFlow<List<PersonalExpense>> = _personalExpenses.asStateFlow()

    // Estate & Culinary Ops State
    private val _dishes = MutableStateFlow<List<DishRecipe>>(emptyList())
    val dishes: StateFlow<List<DishRecipe>> = _dishes.asStateFlow()

    private val _staffMembers = MutableStateFlow<List<StaffMember>>(emptyList())
    val staffMembers: StateFlow<List<StaffMember>> = _staffMembers.asStateFlow()

    private val _notebookEntries = MutableStateFlow<List<SharedNotebookEntry>>(emptyList())
    val notebookEntries: StateFlow<List<SharedNotebookEntry>> = _notebookEntries.asStateFlow()

    private val _userMode = MutableStateFlow(UserMode.ESTATE_CULINARY_OPS)
    val userMode: StateFlow<UserMode> = _userMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeTab = MutableStateFlow(0) // 0: Ledger, 1: Client Hub, 2: Pipeline, 3: Lifestyle
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _selectedClientId = MutableStateFlow<String?>(null)
    val selectedClientId: StateFlow<String?> = _selectedClientId.asStateFlow()

    private val _showNewClientDrawer = MutableStateFlow(false)
    val showNewClientDrawer: StateFlow<Boolean> = _showNewClientDrawer.asStateFlow()

    private val _showNewDealModal = MutableStateFlow(false)
    val showNewDealModal: StateFlow<Boolean> = _showNewDealModal.asStateFlow()

    private val _showNewReminderModal = MutableStateFlow(false)
    val showNewReminderModal: StateFlow<Boolean> = _showNewReminderModal.asStateFlow()

    private val _showTapToPayModal = MutableStateFlow(false)
    val showTapToPayModal: StateFlow<Boolean> = _showTapToPayModal.asStateFlow()

    // Modals & Active UI States
    private val _showInvoiceModal = MutableStateFlow(false)
    val showInvoiceModal: StateFlow<Boolean> = _showInvoiceModal.asStateFlow()

    private val _piggyBankInvoice = MutableStateFlow<ReceivableInvoice?>(null)
    val piggyBankInvoice: StateFlow<ReceivableInvoice?> = _piggyBankInvoice.asStateFlow()

    private val _flyingCashBill = MutableStateFlow<PayableBill?>(null)
    val flyingCashBill: StateFlow<PayableBill?> = _flyingCashBill.asStateFlow()

    private val _nudgeInvoice = MutableStateFlow<ReceivableInvoice?>(null)
    val nudgeInvoice: StateFlow<ReceivableInvoice?> = _nudgeInvoice.asStateFlow()

    private val _activeClientTimelineId = MutableStateFlow<String?>(null)
    val activeClientTimelineId: StateFlow<String?> = _activeClientTimelineId.asStateFlow()

    private val _showMonthEndDialog = MutableStateFlow(false)
    val showMonthEndDialog: StateFlow<Boolean> = _showMonthEndDialog.asStateFlow()

    private val _callLogClientName = MutableStateFlow<String?>(null)
    val callLogClientName: StateFlow<String?> = _callLogClientName.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Personal Budget
    private val _personalAllowance = MutableStateFlow(45000.0)
    val personalAllowance: StateFlow<Double> = _personalAllowance.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        val initialClients = listOf(
            Client(
                id = "cli_1",
                name = "Highveldt Marble & Granite",
                companyName = "Highveldt Stone Works SA",
                vatNumber = "4920192831",
                mobile = "+27 82 491 0293",
                whatsapp = "+27 82 491 0293",
                landline = "+27 11 802 1100",
                email = "accounts@highveldtmarble.co.za",
                billingAddress = "14 Granite Crescent, Midrand, Johannesburg, 1685",
                jobSiteAddress = "Site 42, Waterfall City Commercial Estate",
                bankName = "First National Bank",
                accountNumber = "6281029384",
                branchCode = "250655",
                defaultTerms = PaymentTerms.DAYS_30,
                notes = "Key Account. Prefers Calacatta Gold 20mm & Nero Marquina. Always requires proforma PDF prior to site offload."
            ),
            Client(
                id = "cli_2",
                name = "Kruger Stone & Tiling Works",
                companyName = "Kruger Tiling (Pty) Ltd",
                vatNumber = "4810293847",
                mobile = "+27 71 829 3019",
                whatsapp = "+27 71 829 3019",
                landline = "+27 13 752 4000",
                email = "orders@krugertiling.co.za",
                billingAddress = "88 Paul Kruger St, Nelspruit, 1200",
                jobSiteAddress = "Skukuza Safari Lodge Extension Site B",
                bankName = "Absa Bank",
                accountNumber = "4092837162",
                branchCode = "632005",
                defaultTerms = PaymentTerms.DAYS_30,
                notes = "Requires anti-slip quartzite pavers with non-porous polymer sealant."
            ),
            Client(
                id = "cli_3",
                name = "Cape Architecture Studio",
                companyName = "Cape Design Architecture Studio",
                vatNumber = "4720194820",
                mobile = "+27 83 291 0022",
                whatsapp = "+27 83 291 0022",
                landline = "+27 21 424 9900",
                email = "projects@capearchitecture.co.za",
                billingAddress = "102 Bree Street, Cape Town CBD, 8001",
                jobSiteAddress = "Camps Bay Luxury Villa Project",
                bankName = "Standard Bank",
                accountNumber = "021928374",
                branchCode = "051001",
                defaultTerms = PaymentTerms.DAYS_7,
                notes = "High-end bespoke terrazzo custom curved mouldings."
            ),
            Client(
                id = "cli_4",
                name = "Zambezi Luxury Living",
                companyName = "Zambezi Interiors (Pty) Ltd",
                vatNumber = "4630192834",
                mobile = "+27 84 901 2288",
                whatsapp = "+27 84 901 2288",
                landline = "+27 12 346 8810",
                email = "finance@zambeziliving.co.za",
                billingAddress = "15 Menlyn Maine Towers, Pretoria, 0181",
                jobSiteAddress = "Pretoria East Executive Estate",
                bankName = "Nedbank",
                accountNumber = "1982039485",
                branchCode = "198765",
                defaultTerms = PaymentTerms.COD,
                notes = "COD terms strictly enforced for nano stone coating treatments."
            ),
            Client(
                id = "cli_5",
                name = "Catarata Stones",
                companyName = "Catarata Natural Stone Suppliers",
                vatNumber = "4510293812",
                mobile = "+27 72 381 9022",
                whatsapp = "+27 72 381 9022",
                landline = "+27 31 566 2000",
                email = "info@cataratastones.co.za",
                billingAddress = "45 Umhlanga Ridge Blvd, Durban, 4319",
                jobSiteAddress = "Ballito Coastal Estate Phase 3",
                bankName = "First National Bank",
                accountNumber = "6290192831",
                branchCode = "250655",
                defaultTerms = PaymentTerms.DAYS_30,
                notes = "Inquiring about premium Brazilian Quartzite slabs and onyx backlighting panels."
            ),
            Client(
                id = "cli_6",
                name = "Kitchen & Bath Craft",
                companyName = "KB Craft Renovations",
                vatNumber = "4410928374",
                mobile = "+27 82 102 9384",
                whatsapp = "+27 82 102 9384",
                landline = "+27 11 465 1920",
                email = "billing@kbcraft.co.za",
                billingAddress = "12 Fourways Crossing, Sandton, 2055",
                jobSiteAddress = "Dainfern Golf Estate Villa 110",
                bankName = "Absa Bank",
                accountNumber = "4019283741",
                branchCode = "632005",
                defaultTerms = PaymentTerms.DAYS_7,
                notes = "Residential granite countertop cutting and mitred edge fabrication."
            )
        )

        val initialInvoices = listOf(
            ReceivableInvoice(
                id = "cli_1",
                invoiceNumber = "INV-2026-104",
                clientName = "Highveldt Marble & Granite",
                clientVat = "4920192831",
                description = "Polished Calacatta Gold Marble Slabs (120m²)",
                terms = PaymentTerms.OVERDUE,
                netAmount = 85000.0,
                vatAmount = 12750.0,
                grossAmount = 97750.0,
                dueDate = "15 July 2026",
                status = InvoiceStatus.UNPAID,
                lineItems = listOf(
                    LineItem("1", "Polished Calacatta Gold Marble Slabs 20mm", 120.0, 708.33)
                )
            ),
            ReceivableInvoice(
                id = "cli_2",
                invoiceNumber = "INV-2026-118",
                clientName = "Kruger Stone & Tiling Works",
                clientVat = "4810293847",
                description = "Natural Quartzite Exterior Pavers & Sealant",
                terms = PaymentTerms.DAYS_30,
                netAmount = 42000.0,
                vatAmount = 6300.0,
                grossAmount = 48300.0,
                dueDate = "30 August 2026",
                status = InvoiceStatus.UNPAID,
                lineItems = listOf(
                    LineItem("1", "Natural Quartzite Exterior Pavers 600x600", 350.0, 100.0),
                    LineItem("2", "Polymer Anti-Stain Sealant (25L Drums)", 4.0, 1750.0)
                )
            ),
            ReceivableInvoice(
                id = "cli_3",
                invoiceNumber = "INV-2026-122",
                clientName = "Cape Architecture Studio",
                clientVat = "4720194820",
                description = "Custom Terrazzo Reception Desk Fabrication",
                terms = PaymentTerms.DAYS_7,
                netAmount = 28500.0,
                vatAmount = 4275.0,
                grossAmount = 32775.0,
                dueDate = "18 August 2026",
                status = InvoiceStatus.UNPAID,
                lineItems = listOf(
                    LineItem("1", "Custom Terrazzo Curved Desk Mould", 1.0, 28500.0)
                )
            ),
            ReceivableInvoice(
                id = "cli_4",
                invoiceNumber = "INV-2026-125",
                clientName = "Zambezi Luxury Living",
                clientVat = "4630192834",
                description = "Anti-Slip Stone Coating & Proforma Delivery",
                terms = PaymentTerms.COD,
                netAmount = 15000.0,
                vatAmount = 2250.0,
                grossAmount = 17250.0,
                dueDate = "Immediate COD",
                status = InvoiceStatus.UNPAID,
                lineItems = listOf(
                    LineItem("1", "Nano Anti-Slip Stone Treatment Solution", 50.0, 300.0)
                )
            )
        )

        val initialBills = listOf(
            PayableBill(
                id = "sup_1",
                supplierName = "Mamba Cement SA (Pty) Ltd",
                supplierVat = "4109827361",
                billNumber = "SUP-8821",
                description = "Cem-Bond Rapid Hardening Cement (150 Bags)",
                amount = 18450.0,
                dueDate = "Due Today",
                status = BillStatus.DUE
            ),
            PayableBill(
                id = "sup_2",
                supplierName = "Midrand Diamond Tooling",
                supplierVat = "4019283746",
                billNumber = "MDT-4019",
                description = "Bridge Saw Segmented Diamond Blades (x4)",
                amount = 9200.0,
                dueDate = "In 3 Days",
                status = BillStatus.DUE
            )
        )

        val initialTimeline = listOf(
            TimelineEvent(
                id = "tl_1",
                clientId = "cli_1",
                clientName = "Highveldt Marble & Granite",
                type = TimelineEventType.QUOTE,
                title = "Proforma Quote Issued",
                description = "Generated & locked proforma #PF-2026-092 for Calacatta Marble Slabs.",
                timestamp = "12 July 2026, 10:30 AM",
                attachmentName = "Proforma_PF-2026-092.pdf"
            ),
            TimelineEvent(
                id = "tl_2",
                clientId = "cli_1",
                clientName = "Highveldt Marble & Granite",
                type = TimelineEventType.INVOICE,
                title = "SARS Tax Invoice #INV-2026-104",
                description = "SARS Tax Invoice issued. Total gross value: R 97,750.00.",
                timestamp = "15 July 2026, 14:15 PM",
                attachmentName = "TaxInvoice_INV-2026-104.pdf"
            ),
            TimelineEvent(
                id = "tl_3",
                clientId = "cli_2",
                clientName = "Kruger Stone & Tiling Works",
                type = TimelineEventType.CALL_LOG,
                title = "Site Delivery Call",
                description = "Confirmed 30-day terms and delivery slot for Kruger National Park site.",
                timestamp = "01 August 2026, 11:00 AM",
                callDurationSec = 380
            )
        )

        val initialPipeline = listOf(
            PipelineCard(
                id = "pipe_1",
                clientId = "cli_5",
                clientName = "Catarata Stones",
                materialDescription = "Polished Carrara Marble Slabs 20mm & Onyx Panels",
                projectedValue = 125000.0,
                stage = PipelineStage.IN_TALKS,
                createdDate = "10 August 2026",
                inactivityDays = 12,
                kamName = "Sarah Jenkins"
            ),
            PipelineCard(
                id = "pipe_2",
                clientId = "cli_6",
                clientName = "Kitchen & Bath Craft",
                materialDescription = "Granite Kitchen Countertops & Mitred Edging",
                projectedValue = 84000.0,
                stage = PipelineStage.PROFORMA_SENT,
                createdDate = "11 July 2026",
                inactivityDays = 45,
                kamName = "John Doe"
            ),
            PipelineCard(
                id = "pipe_3",
                clientId = "cli_1",
                clientName = "Highveldt Marble & Granite",
                materialDescription = "Calacatta Gold Marble Slabs (120m²)",
                projectedValue = 97750.0,
                stage = PipelineStage.PAYMENT_PENDING,
                createdDate = "15 May 2026",
                inactivityDays = 88,
                kamName = "Mike Peters"
            ),
            PipelineCard(
                id = "pipe_4",
                clientId = "cli_2",
                clientName = "Kruger Stone & Tiling Works",
                materialDescription = "Natural Quartzite Exterior Pavers (350m²)",
                projectedValue = 48300.0,
                stage = PipelineStage.DISPATCHED,
                createdDate = "01 August 2026",
                inactivityDays = 10,
                kamName = "John Doe"
            ),
            PipelineCard(
                id = "pipe_5",
                clientId = "cli_7",
                clientName = "Apex Mining & Commercial",
                materialDescription = "Heavy Duty Basalt Pavers & Retaining Walls",
                projectedValue = 215000.0,
                stage = PipelineStage.IN_TALKS,
                createdDate = "10 May 2026",
                inactivityDays = 94,
                kamName = "Mike Peters"
            )
        )

        val initialReminders = listOf(
            SmartReminder(
                id = "rem_1",
                clientId = "cli_1",
                clientName = "Highveldt Marble & Granite",
                title = "Call accounts re: Overdue Tax Invoice #104 settlement",
                reminderDate = "Today",
                reminderTime = "09:00 AM",
                tag = ReminderTag.PAYMENT_NUDGE
            ),
            SmartReminder(
                id = "rem_2",
                clientId = "cli_5",
                clientName = "Catarata Stones",
                title = "Send slab photos & proforma for Durban estate project",
                reminderDate = "Tomorrow",
                reminderTime = "10:30 AM",
                tag = ReminderTag.PROBING_NEW_BUSINESS
            ),
            SmartReminder(
                id = "rem_3",
                clientId = "cli_3",
                clientName = "Cape Architecture Studio",
                title = "Camps Bay site meeting & curved terrazzo mock-up inspection",
                reminderDate = "18 August",
                reminderTime = "14:00 PM",
                tag = ReminderTag.SITE_MEETING
            )
        )

        val initialChecklists = listOf(
            // KAM / SME
            ChecklistItem("chk_1", UserMode.KAM_SME, "Follow-up Tasks", "Site visit log: Measure Sandton penthouse kitchen", "15 Aug 2026"),
            ChecklistItem("chk_2", UserMode.KAM_SME, "30-Day Payment Nudges", "Nudge Catarata Stones account for pending proforma approval", "16 Aug 2026"),
            ChecklistItem("chk_3", UserMode.KAM_SME, "Site Visit Logs", "Confirm truck dispatch slot for Kruger Stone site", "17 Aug 2026"),

            // Executive Housewife Suite
            ChecklistItem("chk_4", UserMode.EXECUTIVE_HOUSEWIFE, "Weekly Groceries", "Organic produce, artisanal coffee & almond milk", "This Week", false, 1850.0),
            ChecklistItem("chk_5", UserMode.EXECUTIVE_HOUSEWIFE, "Household Expenses", "Settle monthly estate levy & fiber internet bill", "15 Aug 2026", false, 4200.0),
            ChecklistItem("chk_6", UserMode.EXECUTIVE_HOUSEWIFE, "Family Appointments", "Pediatrician annual wellness checkup (Dr. Nel)", "20 Aug 2026", false, 950.0),
            ChecklistItem("chk_7", UserMode.EXECUTIVE_HOUSEWIFE, "Maintenance", "HVAC annual aircon service & pool pump filter replacement", "22 Aug 2026", false, 2500.0),

            // Student Suite
            ChecklistItem("chk_8", UserMode.STUDENT, "Assignment Deadlines", "Materials Engineering Case Study - Natural Stone Stress Limits", "15 Aug 2026"),
            ChecklistItem("chk_9", UserMode.STUDENT, "Exam Timetable", "Advanced CAD & Architectural Drafting Final Exam", "22 Aug 2026"),
            ChecklistItem("chk_10", UserMode.STUDENT, "Study Notes", "Summarize Structural Load Calculations Chapter 4 & 5", "18 Aug 2026"),
            ChecklistItem("chk_11", UserMode.STUDENT, "Textbooks", "Acquire South African National Building Standards (SANS 10400) Handbook", "14 Aug 2026", false, 650.0)
        )

        val initialExpenses = listOf(
            PersonalExpense("exp_1", "Fuel at Shell Sandton", "Fuel", 1200.0, "12 Aug, 08:30"),
            PersonalExpense("exp_2", "Woolworths Food Store", "Groceries", 2450.0, "11 Aug, 16:15"),
            PersonalExpense("exp_3", "Engineering Course Fees", "Tuition", 3500.0, "05 Aug, 10:00")
        )

        val initialDishes = listOf(
            DishRecipe(
                id = "dish_1",
                dishName = "Aged Ribeye with Garlic Herb Butter",
                category = "Main Course",
                servings = 10,
                ingredients = listOf(
                    RecipeIngredient("ing_1", "Prime Ribeye Beef", 3.5, "kg", 220.0),
                    RecipeIngredient("ing_2", "Artisanal Butter & Herbs", 0.5, "kg", 95.0),
                    RecipeIngredient("ing_3", "Organic Vegetables Side", 2.0, "kg", 45.0)
                ),
                sellingPricePerServing = 280.0,
                notes = "Pre-sear on cast iron, baste with thyme butter. High gross margin for estate dinners."
            ),
            DishRecipe(
                id = "dish_2",
                dishName = "Seafood Paella & Artisanal Tapas Platter",
                category = "Catering Platter",
                servings = 25,
                ingredients = listOf(
                    RecipeIngredient("ing_4", "Tiger Prawns & Calamari", 5.0, "kg", 240.0),
                    RecipeIngredient("ing_5", "Bomba Rice & Saffron Thread", 2.0, "kg", 180.0),
                    RecipeIngredient("ing_6", "Chorizo & Spanish Paprika", 1.5, "kg", 160.0)
                ),
                sellingPricePerServing = 350.0,
                notes = "Catering favorite. Gross profit margin ~ 78% for VIP client functions."
            )
        )

        val initialStaff = listOf(
            StaffMember(
                id = "staff_1",
                name = "Nomvula Dlamini",
                role = StaffRole.DOMESTIC_MAID,
                dayRate = 450.0,
                shiftDate = "14 Aug 2026",
                isPaid = false
            ),
            StaffMember(
                id = "staff_2",
                name = "Sipho Mokoena",
                role = StaffRole.GARDENER,
                dayRate = 500.0,
                shiftDate = "15 Aug 2026",
                isPaid = false
            ),
            StaffMember(
                id = "staff_3",
                name = "Chef Marco Venter",
                role = StaffRole.TEMP_SOUS_CHEF,
                dayRate = 1800.0,
                shiftDate = "16 Aug 2026",
                isPaid = true,
                paidDate = "10 Aug 2026"
            )
        )

        val initialNotebook = listOf(
            SharedNotebookEntry(
                id = "note_1",
                title = "Weekly Gourmet Estate Menu",
                type = NotebookType.WEEKLY_MENU,
                content = "Mon: Truffle Risotto • Tue: Braised Lamb Shanks • Wed: Grilled Kingklip • Thu: Wagyu Burgers • Fri: Tapas & Paella Night",
                category = "Culinary Ops",
                createdDate = "12 Aug 2026",
                isPinned = true
            ),
            SharedNotebookEntry(
                id = "note_2",
                title = "Estate Access & Guest Guide",
                type = NotebookType.HOUSEHOLD_GUIDE,
                content = "Security Gate Code: *9021# • High-Speed Fiber Wi-Fi: EstateGuest_5G (Pass: Platinum#2026) • Solar Inverter Override in Garage West.",
                category = "Estate Ops",
                createdDate = "10 Aug 2026",
                isPinned = true
            ),
            SharedNotebookEntry(
                id = "note_3",
                title = "Gourmet Paella Recipe Card",
                type = NotebookType.RECIPE_CARD,
                content = "Servings: 25 | Saffron infusion 30 min before rice. Maintain medium flame. Cost per serving: R 72.00 | Menu Price: R 350.00 | Margin: 79.4%",
                category = "Catering",
                createdDate = "11 Aug 2026",
                isPinned = false
            )
        )

        _clients.value = initialClients
        _invoices.value = initialInvoices
        _bills.value = initialBills
        _timeline.value = initialTimeline
        _pipelineCards.value = initialPipeline
        _smartReminders.value = initialReminders
        _checklists.value = initialChecklists
        _personalExpenses.value = initialExpenses
        _dishes.value = initialDishes
        _staffMembers.value = initialStaff
        _notebookEntries.value = initialNotebook
    }

    // Navigation & Tabs
    fun setActiveTab(index: Int) {
        _activeTab.value = index
    }

    fun setUserMode(mode: UserMode) {
        _userMode.value = mode
        showToast("Switched to ${mode.displayName} Mode")
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectClient(clientId: String?) {
        _selectedClientId.value = clientId
    }

    // Client Onboarding
    fun openNewClientDrawer() {
        _showNewClientDrawer.value = true
    }

    fun closeNewClientDrawer() {
        _showNewClientDrawer.value = false
    }

    fun createNewClient(
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
    ) {
        val newId = "cli_${System.currentTimeMillis()}"
        val newClient = Client(
            id = newId,
            name = name.ifBlank { "New Client" },
            companyName = companyName.ifBlank { name },
            vatNumber = vatNumber.ifBlank { "4000000000" },
            mobile = mobile,
            whatsapp = whatsapp.ifBlank { mobile },
            landline = landline,
            email = email,
            billingAddress = billingAddress,
            jobSiteAddress = jobSiteAddress,
            bankName = bankName.ifBlank { "Standard Bank" },
            accountNumber = accountNumber.ifBlank { "100000000" },
            branchCode = branchCode.ifBlank { "051001" },
            defaultTerms = terms,
            notes = "Onboarded on ${System.currentTimeMillis()}"
        )

        _clients.update { listOf(newClient) + it }
        _showNewClientDrawer.value = false
        _selectedClientId.value = newId
        _activeTab.value = 1 // Go to CRM Hub
        showToast("New client ${newClient.name} onboarded successfully! 📋")

        addTimelineEvent(
            clientId = newId,
            clientName = newClient.name,
            type = TimelineEventType.CALL_LOG,
            title = "Account Onboarded",
            description = "Captured banking, VAT #${newClient.vatNumber}, and payment terms (${terms.displayName})."
        )
    }

    fun updateClientNotes(clientId: String, newNotes: String) {
        _clients.update { list ->
            list.map { if (it.id == clientId) it.copy(notes = newNotes) else it }
        }
        showToast("Client notes saved!")
    }

    // Sales Pipeline Actions
    fun openNewDealModal() {
        _showNewDealModal.value = true
    }

    fun closeNewDealModal() {
        _showNewDealModal.value = false
    }

    fun createPipelineDeal(
        clientName: String,
        materialDescription: String,
        projectedValue: Double,
        stage: PipelineStage
    ) {
        val newDeal = PipelineCard(
            id = "pipe_${System.currentTimeMillis()}",
            clientId = "cli_${System.currentTimeMillis()}",
            clientName = clientName.ifBlank { "Target Account" },
            materialDescription = materialDescription.ifBlank { "Custom Material Slabs" },
            projectedValue = projectedValue,
            stage = stage,
            createdDate = "Today"
        )
        _pipelineCards.update { listOf(newDeal) + it }
        _showNewDealModal.value = false
        showToast("Added deal to ${stage.displayName}! 📈")
    }

    fun movePipelineStage(cardId: String, newStage: PipelineStage) {
        _pipelineCards.update { list ->
            list.map { if (it.id == cardId) it.copy(stage = newStage) else it }
        }
        val targetCard = _pipelineCards.value.firstOrNull { it.id == cardId }
        showToast("Moved deal for ${targetCard?.clientName ?: "Client"} to ${newStage.displayName}! 💼")

        targetCard?.let { card ->
            addTimelineEvent(
                clientId = card.clientId,
                clientName = card.clientName,
                type = TimelineEventType.QUOTE,
                title = "Pipeline Updated: ${newStage.displayName}",
                description = "Moved deal (${card.materialDescription}) worth R ${String.format("%.2f", card.projectedValue)} to ${newStage.displayName}."
            )
        }
    }

    fun reassignKam(cardId: String, newKamName: String) {
        _pipelineCards.update { list ->
            list.map { if (it.id == cardId) it.copy(kamName = newKamName, isHousePool = false) else it }
        }
        val targetCard = _pipelineCards.value.firstOrNull { it.id == cardId }
        showToast("Reassigned ${targetCard?.clientName ?: "deal"} to KAM: $newKamName! 👤")
        SoundEngine.playCashPing()
    }

    fun moveToHousePool(cardId: String) {
        _pipelineCards.update { list ->
            list.map { if (it.id == cardId) it.copy(kamName = "House Pool", isHousePool = true) else it }
        }
        val targetCard = _pipelineCards.value.firstOrNull { it.id == cardId }
        showToast("Moved ${targetCard?.clientName ?: "deal"} to House Pool! 🏠")
        SoundEngine.playCashPing()
    }

    // Smart Reminders
    fun openNewReminderModal() {
        _showNewReminderModal.value = true
    }

    fun closeNewReminderModal() {
        _showNewReminderModal.value = false
    }

    fun createSmartReminder(
        clientId: String,
        clientName: String,
        title: String,
        date: String,
        time: String,
        tag: ReminderTag
    ) {
        val newRem = SmartReminder(
            id = "rem_${System.currentTimeMillis()}",
            clientId = clientId,
            clientName = clientName.ifBlank { "General Client" },
            title = title.ifBlank { "Follow-up task" },
            reminderDate = date.ifBlank { "Today" },
            reminderTime = time.ifBlank { "12:00 PM" },
            tag = tag,
            isCompleted = false
        )
        _smartReminders.update { listOf(newRem) + it }
        _showNewReminderModal.value = false
        showToast("Set reminder for ${newRem.clientName}! ⏰")
    }

    fun toggleReminderCompleted(reminderId: String) {
        _smartReminders.update { list ->
            list.map {
                if (it.id == reminderId) {
                    val updated = it.copy(isCompleted = !it.isCompleted)
                    if (updated.isCompleted) {
                        showToast("Reminder marked complete! Logged to timeline. ✅")
                        addTimelineEvent(
                            clientId = updated.clientId,
                            clientName = updated.clientName,
                            type = TimelineEventType.REMINDER_NUDGE,
                            title = "Task Completed: ${updated.tag.displayName}",
                            description = updated.title
                        )
                    }
                    updated
                } else it
            }
        }
    }

    // Communication & File Attachment Suite
    fun sendCommunicationWithAttachment(
        clientId: String,
        clientName: String,
        message: String,
        attachmentName: String,
        channel: String // "WhatsApp" or "Email"
    ) {
        showToast("Sent $attachmentName via $channel to $clientName! 🚀")
        SoundEngine.playCashPing()

        addTimelineEvent(
            clientId = clientId,
            clientName = clientName,
            type = TimelineEventType.FILE_ATTACHMENT,
            title = "$attachmentName Sent via $channel",
            description = message.ifBlank { "Attached document/photo locked and transmitted to client." },
            attachmentName = attachmentName
        )
    }

    // Lifestyle & Checklists
    fun toggleChecklistItem(id: String) {
        _checklists.update { list ->
            list.map {
                if (it.id == id) {
                    val next = it.copy(isCompleted = !it.isCompleted)
                    if (next.isCompleted) {
                        SoundEngine.playCoinDrop()
                    }
                    next
                } else it
            }
        }
    }

    fun addChecklistItem(mode: UserMode, category: String, title: String, dueDate: String, amount: Double? = null) {
        val newItem = ChecklistItem(
            id = "chk_${System.currentTimeMillis()}",
            mode = mode,
            category = category,
            title = title.ifBlank { "New Task" },
            dueDate = dueDate.ifBlank { "Today" },
            isCompleted = false,
            amount = amount
        )
        _checklists.update { listOf(newItem) + it }
        showToast("Added item to ${mode.displayName} suite!")
    }

    // Tap-to-Pay & Expense Logging
    fun openTapToPayModal() {
        _showTapToPayModal.value = true
    }

    fun closeTapToPayModal() {
        _showTapToPayModal.value = false
    }

    fun processTapToPayExpense(title: String, category: String, amount: Double) {
        val newExp = PersonalExpense(
            id = "exp_${System.currentTimeMillis()}",
            title = title.ifBlank { "Mobile Tap Payment" },
            category = category,
            amount = amount,
            timestamp = "Today, 14:00"
        )
        _personalExpenses.update { listOf(newExp) + it }
        _showTapToPayModal.value = false

        SoundEngine.playCashPing()
        showToast("Contactless payment of R ${String.format("%.2f", amount)} approved! 📲💳")
    }

    // Actions
    fun openInvoiceModal() {
        _showInvoiceModal.value = true
    }

    fun closeInvoiceModal() {
        _showInvoiceModal.value = false
    }

    fun createTaxInvoice(
        clientName: String,
        clientVat: String,
        description: String,
        terms: PaymentTerms,
        netAmount: Double,
        isVatInclusive: Boolean,
        lineItems: List<LineItem>
    ) {
        val vatRate = 0.15
        val net: Double
        val vat: Double
        val gross: Double

        if (isVatInclusive) {
            gross = netAmount
            net = gross / (1.0 + vatRate)
            vat = gross - net
        } else {
            net = netAmount
            vat = net * vatRate
            gross = net + vat
        }

        val newInvNum = "INV-2026-${(130..999).random()}"
        val newInvoice = ReceivableInvoice(
            id = "cli_${System.currentTimeMillis()}",
            invoiceNumber = newInvNum,
            clientName = clientName.ifBlank { "Client Business" },
            clientVat = clientVat.ifBlank { "4000000000" },
            description = description.ifBlank { "Custom Material Order" },
            terms = terms,
            netAmount = net,
            vatAmount = vat,
            grossAmount = gross,
            dueDate = when (terms) {
                PaymentTerms.COD -> "Immediate COD"
                PaymentTerms.DAYS_7 -> "In 7 Days"
                PaymentTerms.DAYS_30 -> "In 30 Days"
                PaymentTerms.OVERDUE -> "Overdue"
            },
            status = InvoiceStatus.UNPAID,
            isVatInclusive = isVatInclusive,
            lineItems = lineItems
        )

        _invoices.update { listOf(newInvoice) + it }
        _showInvoiceModal.value = false
        showToast("Created Tax Invoice $newInvNum successfully!")

        // Log to timeline
        addTimelineEvent(
            clientId = newInvoice.id,
            clientName = newInvoice.clientName,
            type = TimelineEventType.INVOICE,
            title = "SARS Tax Invoice $newInvNum Created",
            description = "Issued tax invoice for R ${String.format("%.2f", gross)} (${if (isVatInclusive) "VAT Incl." else "VAT Excl."})",
            attachmentName = "$newInvNum.pdf"
        )
    }

    fun triggerConfirmPayment(invoice: ReceivableInvoice) {
        _piggyBankInvoice.value = invoice
        SoundEngine.playCoinDrop()
    }

    fun finalizePiggyBankPaymentSwipedAway() {
        val inv = _piggyBankInvoice.value ?: return
        _invoices.update { list ->
            list.map { if (it.id == inv.id) it.copy(status = InvoiceStatus.PAID) else it }
        }
        _piggyBankInvoice.value = null
        showToast("Payment for ${inv.clientName} confirmed & ledger updated! 🪙")

        // Add timeline payment record
        addTimelineEvent(
            clientId = inv.id,
            clientName = inv.clientName,
            type = TimelineEventType.EFT_PAYMENT,
            title = "EFT Payment Received - R ${String.format("%.2f", inv.grossAmount)}",
            description = "EFT payment settled and confirmed in Absa business account.",
            attachmentName = "Bank_EFT_POP_${inv.invoiceNumber}.pdf"
        )
    }

    fun dismissPiggyBank() {
        _piggyBankInvoice.value = null
    }

    fun triggerPayBill(bill: PayableBill) {
        _flyingCashBill.value = bill
        SoundEngine.playCashPing()
    }

    fun finalizePayBillAnimation() {
        val bill = _flyingCashBill.value ?: return
        _bills.update { list ->
            list.map { if (it.id == bill.id) it.copy(status = BillStatus.SETTLED) else it }
        }
        _flyingCashBill.value = null
        showToast("Settled bill for ${bill.supplierName}! 💸")
    }

    fun dismissFlyingCash() {
        _flyingCashBill.value = null
    }

    fun deleteInvoice(invoiceId: String) {
        val target = _invoices.value.firstOrNull { it.id == invoiceId }
        _invoices.update { list -> list.filterNot { it.id == invoiceId } }
        showToast("Archived & deleted ${target?.invoiceNumber ?: "invoice"} from Debtors ledger. 🗑️")
    }

    fun dialClientPhone(clientName: String) {
        showToast("Opening dialer for $clientName... 📞")
        SoundEngine.playCashPing()
    }

    fun viewInvoiceAttachment(invoice: ReceivableInvoice) {
        showToast("Opening handcuff PDF attachment for ${invoice.invoiceNumber}... 📎🔒")
        SoundEngine.playCashPing()
    }

    fun openCameraProof(invoice: ReceivableInvoice) {
        showToast("Opening camera for Proof of Delivery (POD) on ${invoice.invoiceNumber}... 📷")
        SoundEngine.playCashPing()
    }

    fun openNudgeModal(invoice: ReceivableInvoice) {
        _nudgeInvoice.value = invoice
    }

    fun closeNudgeModal() {
        _nudgeInvoice.value = null
    }

    fun sendNudgeWhatsApp(invoice: ReceivableInvoice) {
        _nudgeInvoice.value = null
        showToast("Polite WhatsApp reminder sent to ${invoice.clientName}! 📱")
        addTimelineEvent(
            clientId = invoice.id,
            clientName = invoice.clientName,
            type = TimelineEventType.REMINDER_NUDGE,
            title = "WhatsApp Pre-Formatted Nudge Sent",
            description = "Sent WhatsApp payment reminder for ${invoice.invoiceNumber} (Overdue R ${String.format("%.2f", invoice.grossAmount)})."
        )
    }

    fun openClientTimeline(clientId: String) {
        _activeClientTimelineId.value = clientId
    }

    fun closeClientTimeline() {
        _activeClientTimelineId.value = null
    }

    fun openMonthEndDialog() {
        _showMonthEndDialog.value = true
    }

    fun closeMonthEndDialog() {
        _showMonthEndDialog.value = false
    }

    fun openCallLogDialog(clientName: String) {
        _callLogClientName.value = clientName
    }

    fun closeCallLogDialog() {
        _callLogClientName.value = null
    }

    fun logCall(clientName: String, callType: String, durationMin: Int) {
        _callLogClientName.value = null
        showToast("Logged $callType call ($durationMin min) with $clientName 📞")
        addTimelineEvent(
            clientId = clientName,
            clientName = clientName,
            type = TimelineEventType.CALL_LOG,
            title = "$callType Call Logged",
            description = "Executive discussion regarding active orders & account settlement terms.",
            callDurationSec = durationMin * 60
        )
    }

    // Debug Simulators
    fun simulateIncomingEFT() {
        val unpaid = _invoices.value.firstOrNull { it.status == InvoiceStatus.UNPAID }
        if (unpaid != null) {
            triggerConfirmPayment(unpaid)
        } else {
            val mockInv = ReceivableInvoice(
                id = "cli_sim_${System.currentTimeMillis()}",
                invoiceNumber = "EFT-LIVE-902",
                clientName = "EFT Simulation Client",
                clientVat = "4901928371",
                description = "Simulated Live Incoming Bank Transfer",
                terms = PaymentTerms.COD,
                netAmount = 12500.0,
                vatAmount = 1875.0,
                grossAmount = 14375.0,
                dueDate = "Immediate COD",
                status = InvoiceStatus.UNPAID
            )
            _invoices.update { listOf(mockInv) + it }
            triggerConfirmPayment(mockInv)
        }
    }

    fun simulateOutboundPayment() {
        val unpaidBill = _bills.value.firstOrNull { it.status == BillStatus.DUE }
        if (unpaidBill != null) {
            triggerPayBill(unpaidBill)
        } else {
            val mockBill = PayableBill(
                id = "sup_sim_${System.currentTimeMillis()}",
                supplierName = "Simulated Supplier SA",
                supplierVat = "4102938475",
                billNumber = "BILL-SIM-501",
                description = "Simulated Live Bank Outbound Wire",
                amount = 8900.0,
                dueDate = "Immediate",
                status = BillStatus.DUE
            )
            _bills.update { listOf(mockBill) + it }
            triggerPayBill(mockBill)
        }
    }

    fun addTimelineEvent(
        clientId: String,
        clientName: String,
        type: TimelineEventType,
        title: String,
        description: String,
        attachmentName: String? = null,
        callDurationSec: Int? = null
    ) {
        val newEvent = TimelineEvent(
            id = "tl_${System.currentTimeMillis()}",
            clientId = clientId,
            clientName = clientName,
            type = type,
            title = title,
            description = description,
            timestamp = "Today, 14:30",
            attachmentName = attachmentName,
            callDurationSec = callDurationSec
        )
        _timeline.update { listOf(newEvent) + it }
    }

    // --- Estate & Culinary Ops Functions ---

    fun createDishRecipe(
        dishName: String,
        category: String,
        servings: Int,
        ingredients: List<RecipeIngredient>,
        sellingPricePerServing: Double,
        notes: String = ""
    ) {
        val newDish = DishRecipe(
            id = "dish_${System.currentTimeMillis()}",
            dishName = dishName.ifBlank { "Gourmet Dish" },
            category = category.ifBlank { "Main Course" },
            servings = if (servings > 0) servings else 1,
            ingredients = ingredients,
            sellingPricePerServing = sellingPricePerServing,
            notes = notes
        )
        _dishes.update { listOf(newDish) + it }
        showToast("Created dish recipe for ${newDish.dishName}! 🍲")

        // Auto-create a recipe card in Shared Notebook Hub
        addNotebookEntry(
            title = "${newDish.dishName} Recipe Card",
            type = NotebookType.RECIPE_CARD,
            content = "Servings: ${newDish.servings} | Cost per serving: R ${String.format("%.2f", newDish.costPerServing)} | Target Price: R ${String.format("%.2f", newDish.sellingPricePerServing)} | Gross Margin: ${String.format("%.1f", newDish.grossProfitMargin)}%",
            category = newDish.category
        )
    }

    fun addStaffMember(name: String, role: StaffRole, dayRate: Double, shiftDate: String) {
        val newStaff = StaffMember(
            id = "staff_${System.currentTimeMillis()}",
            name = name.ifBlank { "Domestic / Temp Staff" },
            role = role,
            dayRate = dayRate,
            shiftDate = shiftDate.ifBlank { "Today" },
            isPaid = false
        )
        _staffMembers.update { listOf(newStaff) + it }
        showToast("Logged staff shift for ${newStaff.name} (${role.displayName})! 👤")
    }

    fun markStaffPaid(staffId: String) {
        val staff = _staffMembers.value.firstOrNull { it.id == staffId } ?: return
        if (staff.isPaid) return

        _staffMembers.update { list ->
            list.map {
                if (it.id == staffId) it.copy(isPaid = true, paidDate = "Today") else it
            }
        }

        // Auto-feed straight into Phase 1 Overhead Expenses (Settled Payable Bill)
        val newBill = PayableBill(
            id = "sup_staff_${System.currentTimeMillis()}",
            supplierName = "${staff.name} (${staff.role.displayName})",
            supplierVat = "Domestic Expense",
            billNumber = "PAY-${(1000..9999).random()}",
            description = "Staff Day-Rate Payout for shift ${staff.shiftDate}",
            amount = staff.dayRate,
            dueDate = "Paid Today",
            status = BillStatus.SETTLED
        )
        _bills.update { listOf(newBill) + it }

        SoundEngine.playCashPing()
        showToast("Paid R ${String.format("%.2f", staff.dayRate)} to ${staff.name}! Auto-logged to Phase 1 Overhead Expenses. 💸")
    }

    fun addNotebookEntry(title: String, type: NotebookType, content: String, category: String) {
        val newEntry = SharedNotebookEntry(
            id = "note_${System.currentTimeMillis()}",
            title = title.ifBlank { "Notebook Entry" },
            type = type,
            content = content,
            category = category.ifBlank { "Estate Ops" },
            createdDate = "Today",
            isPinned = false
        )
        _notebookEntries.update { listOf(newEntry) + it }
        showToast("Saved entry to Shared Notebook! 📓")
    }

    fun shareNotebookEntry(entry: SharedNotebookEntry, channel: String) {
        SoundEngine.playCashPing()
        showToast("Transmitted '${entry.title}' via $channel! 🚀")
    }

    fun deleteNotebookEntry(entryId: String) {
        _notebookEntries.update { list -> list.filterNot { it.id == entryId } }
        showToast("Notebook entry removed.")
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    private fun showToast(msg: String) {
        _toastMessage.value = msg
    }
}

