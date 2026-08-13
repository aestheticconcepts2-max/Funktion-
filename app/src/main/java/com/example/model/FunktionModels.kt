package com.example.model

enum class PaymentTerms(val displayName: String) {
    COD("COD / Immediate"),
    DAYS_7("7-Day Terms"),
    DAYS_30("30-Day Terms"),
    OVERDUE("Overdue")
}

enum class InvoiceStatus {
    UNPAID,
    PAID
}

data class LineItem(
    val id: String,
    val description: String,
    val quantity: Double,
    val unitPrice: Double
) {
    val total: Double get() = quantity * unitPrice
}

data class ReceivableInvoice(
    val id: String,
    val invoiceNumber: String,
    val clientName: String,
    val clientVat: String,
    val description: String,
    val terms: PaymentTerms,
    val netAmount: Double,
    val vatAmount: Double,
    val grossAmount: Double,
    val dueDate: String,
    val status: InvoiceStatus = InvoiceStatus.UNPAID,
    val isVatInclusive: Boolean = false,
    val lineItems: List<LineItem> = emptyList(),
    val createdTimestamp: String = "2026-08-10"
)

enum class BillStatus {
    DUE,
    SETTLED
}

data class PayableBill(
    val id: String,
    val supplierName: String,
    val supplierVat: String,
    val billNumber: String,
    val description: String,
    val amount: Double,
    val dueDate: String,
    val status: BillStatus = BillStatus.DUE
)

enum class TimelineEventType {
    QUOTE,
    INVOICE,
    EFT_PAYMENT,
    CALL_LOG,
    REMINDER_NUDGE,
    FILE_ATTACHMENT
}

data class TimelineEvent(
    val id: String,
    val clientId: String,
    val clientName: String,
    val type: TimelineEventType,
    val title: String,
    val description: String,
    val timestamp: String,
    val attachmentName: String? = null,
    val callDurationSec: Int? = null
)

data class Client(
    val id: String,
    val name: String,
    val companyName: String,
    val vatNumber: String,
    val mobile: String,
    val whatsapp: String,
    val landline: String,
    val email: String,
    val billingAddress: String,
    val jobSiteAddress: String,
    val bankName: String,
    val accountNumber: String,
    val branchCode: String,
    val defaultTerms: PaymentTerms,
    val notes: String = ""
)

enum class PipelineStage(val displayName: String) {
    IN_TALKS("In Talks"),
    PROFORMA_SENT("Proforma Sent"),
    PAYMENT_PENDING("Payment Pending"),
    DISPATCHED("Released / Dispatched")
}

data class PipelineCard(
    val id: String,
    val clientId: String,
    val clientName: String,
    val materialDescription: String,
    val projectedValue: Double,
    val stage: PipelineStage,
    val createdDate: String,
    val inactivityDays: Int = 15,
    val kamName: String = "John Doe",
    val isHousePool: Boolean = false
)

enum class ReminderTag(val displayName: String) {
    PAYMENT_NUDGE("Payment Nudge"),
    PROBING_NEW_BUSINESS("Probing New Business"),
    SITE_MEETING("Site Meeting"),
    GENERAL("General Follow-Up")
}

data class SmartReminder(
    val id: String,
    val clientId: String,
    val clientName: String,
    val title: String,
    val reminderDate: String,
    val reminderTime: String,
    val tag: ReminderTag,
    val isCompleted: Boolean = false
)

enum class UserMode(val displayName: String) {
    ESTATE_CULINARY_OPS("Estate & Culinary Ops"),
    STUDENT("Student"),
    KAM_SME("KAM / SME"),
    EXECUTIVE_HOUSEWIFE("Executive Housewife")
}

data class ChecklistItem(
    val id: String,
    val mode: UserMode,
    val category: String, // e.g. "Weekly Groceries", "Household Expenses", "Family Appointments", "Maintenance", "Assignment Deadlines", "Exam Timetable", "Study Notes", "Textbooks", "Follow-up Tasks"
    val title: String,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val amount: Double? = null
)

data class PersonalExpense(
    val id: String,
    val title: String,
    val category: String, // "Groceries", "Fuel", "Tuition", "Bills", "Other"
    val amount: Double,
    val timestamp: String
)

// --- Estate & Culinary Ops Models ---

data class RecipeIngredient(
    val id: String,
    val name: String,
    val quantity: Double,
    val unit: String, // "kg", "g", "L", "ml", "units"
    val costPerUnit: Double
) {
    val totalCost: Double get() = quantity * costPerUnit
}

data class DishRecipe(
    val id: String,
    val dishName: String,
    val category: String, // "Starter", "Main Course", "Dessert", "Catering Platter"
    val servings: Int,
    val ingredients: List<RecipeIngredient>,
    val sellingPricePerServing: Double,
    val notes: String = ""
) {
    val totalCost: Double get() = ingredients.sumOf { it.totalCost }
    val costPerServing: Double get() = if (servings > 0) totalCost / servings else 0.0
    val grossProfitMargin: Double get() = if (sellingPricePerServing > 0) {
        ((sellingPricePerServing - costPerServing) / sellingPricePerServing) * 100.0
    } else 0.0
    val totalMealBudget: Double get() = sellingPricePerServing * servings
}

enum class StaffRole(val displayName: String) {
    DOMESTIC_MAID("Domestic Maid"),
    GARDENER("Gardener"),
    TEMP_SOUS_CHEF("Temp Sous Chef"),
    KITCHEN_PREP("Kitchen Prep"),
    WAITER_SERVER("Waiter / Server"),
    ESTATE_DRIVER("Estate Driver")
}

data class StaffMember(
    val id: String,
    val name: String,
    val role: StaffRole,
    val dayRate: Double,
    val shiftDate: String,
    val isPaid: Boolean = false,
    val paidDate: String? = null
)

enum class NotebookType(val displayName: String) {
    RECIPE_CARD("Recipe Card"),
    WEEKLY_MENU("Weekly Menu"),
    HOUSEHOLD_GUIDE("Household Guide"),
    PROJECT_NOTE("Project Note")
}

data class SharedNotebookEntry(
    val id: String,
    val title: String,
    val type: NotebookType,
    val content: String,
    val category: String,
    val createdDate: String,
    val isPinned: Boolean = false
)

