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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ChecklistItem
import com.example.model.DishRecipe
import com.example.model.NotebookType
import com.example.model.PersonalExpense
import com.example.model.RecipeIngredient
import com.example.model.SharedNotebookEntry
import com.example.model.StaffMember
import com.example.model.StaffRole
import com.example.model.UserMode
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.MoneyOutBlue
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun LifestyleSuiteSection(
    userMode: UserMode,
    checklists: List<ChecklistItem>,
    personalExpenses: List<PersonalExpense>,
    dishes: List<DishRecipe>,
    staffMembers: List<StaffMember>,
    notebookEntries: List<SharedNotebookEntry>,
    allowance: Double,
    onSetUserMode: (UserMode) -> Unit,
    onToggleChecklist: (String) -> Unit,
    onAddChecklist: (mode: UserMode, category: String, title: String, dueDate: String) -> Unit,
    onCreateDishRecipe: (dishName: String, category: String, servings: Int, ingredients: List<RecipeIngredient>, sellingPrice: Double, notes: String) -> Unit,
    onAddStaff: (name: String, role: StaffRole, dayRate: Double, shiftDate: String) -> Unit,
    onMarkStaffPaid: (staffId: String) -> Unit,
    onAddNotebookEntry: (title: String, type: NotebookType, content: String, category: String) -> Unit,
    onShareNotebookEntry: (entry: SharedNotebookEntry, channel: String) -> Unit,
    onDeleteNotebookEntry: (entryId: String) -> Unit,
    onOpenTapToPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estate & Culinary Ops Sub-Navigation Tab index
    // 0: Menu & Costing, 1: Staff & Day-Rates, 2: Inventory & Groceries, 3: Master Calendar
    var opsSubTab by remember { mutableStateOf(0) }

    var showRecipeDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. User Mode Toggle Switch
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Consolidated Operations & Lifestyle Suite",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = userMode.displayName,
                        fontSize = 11.sp,
                        color = BurntOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val modes = listOf(UserMode.ESTATE_CULINARY_OPS, UserMode.STUDENT, UserMode.KAM_SME)
                    modes.forEach { mode ->
                        val selected = userMode == mode
                        FilterChip(
                            selected = selected,
                            onClick = { onSetUserMode(mode) },
                            label = { Text(mode.displayName, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f).testTag("mode_${mode.name}"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurntOrange,
                                selectedLabelColor = TextWhite,
                                containerColor = SlateSurfaceVariant,
                                labelColor = TextMuted
                            )
                        )
                    }
                }
            }
        }

        // 2. Render view based on UserMode
        if (userMode == UserMode.ESTATE_CULINARY_OPS) {
            // Sub-Navigation Bar for Estate & Culinary Ops
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val subTabs = listOf(
                        "Menu & Costing" to Icons.Default.RestaurantMenu,
                        "Staff & Day-Rates" to Icons.Default.Group,
                        "Inventory & Groceries" to Icons.Default.ShoppingCart,
                        "Master Calendar" to Icons.Default.CalendarMonth
                    )

                    subTabs.forEachIndexed { index, (label, icon) ->
                        val isSelected = opsSubTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) BurntOrange else Color.Transparent)
                                .clickable { opsSubTab = index }
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                                .testTag("subtab_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) TextWhite else TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = label,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) TextWhite else TextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Render active Sub-Tab Content
            when (opsSubTab) {
                0 -> MenuAndCostingSubTab(
                    dishes = dishes,
                    onOpenRecipeDialog = { showRecipeDialog = true }
                )
                1 -> StaffAndDayRatesSubTab(
                    staffMembers = staffMembers,
                    onAddStaff = onAddStaff,
                    onMarkPaid = onMarkStaffPaid
                )
                2 -> InventoryAndGroceriesSubTab(
                    checklists = checklists,
                    personalExpenses = personalExpenses,
                    allowance = allowance,
                    onToggleChecklist = onToggleChecklist,
                    onAddChecklist = onAddChecklist,
                    onOpenTapToPay = onOpenTapToPay
                )
                3 -> MasterCalendarNotebookSubTab(
                    notebookEntries = notebookEntries,
                    onOpenAddNote = { showAddNoteDialog = true },
                    onShareEntry = onShareNotebookEntry,
                    onDeleteEntry = onDeleteNotebookEntry
                )
            }
        } else {
            // Standard Student or KAM / SME Mode View
            StandardChecklistSuiteView(
                userMode = userMode,
                checklists = checklists,
                personalExpenses = personalExpenses,
                allowance = allowance,
                onToggleChecklist = onToggleChecklist,
                onAddChecklist = onAddChecklist,
                onOpenTapToPay = onOpenTapToPay
            )
        }
    }

    // Modal Dialogs for Recipe Builder & Shared Notebook
    if (showRecipeDialog) {
        RecipeBuilderDialog(
            onDismiss = { showRecipeDialog = false },
            onCreateRecipe = { name, cat, servings, ingredients, price, notes ->
                onCreateDishRecipe(name, cat, servings, ingredients, price, notes)
                showRecipeDialog = false
            }
        )
    }

    if (showAddNoteDialog) {
        AddNotebookEntryDialog(
            onDismiss = { showAddNoteDialog = false },
            onAddEntry = { title, type, content, cat ->
                onAddNotebookEntry(title, type, content, cat)
                showAddNoteDialog = false
            }
        )
    }
}

// --- Sub-Tab 1: Menu & Costing ---
@Composable
private fun MenuAndCostingSubTab(
    dishes: List<DishRecipe>,
    onOpenRecipeDialog: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dish Recipe Builder & Costing Engine",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Raw ingredient costs, gross profit margins & event meal budgets",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = onOpenRecipeDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("add_recipe_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "New Dish", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (dishes.isEmpty()) {
                Text(text = "No dish recipes created yet. Tap '+ New Dish' to build one.", fontSize = 11.sp, color = TextMuted)
            } else {
                dishes.forEach { dish ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = dish.dishName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(text = "${dish.category} • ${dish.servings} Servings", fontSize = 10.sp, color = BurntOrange)
                                }

                                val marginColor = if (dish.grossProfitMargin >= 50.0) MoneyInGreen else BurntOrange
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(marginColor.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${String.format("%.1f", dish.grossProfitMargin)}% Gross Margin",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = marginColor
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Raw Cost / Serving", fontSize = 9.sp, color = TextMuted)
                                    Text("R ${String.format("%.2f", dish.costPerServing)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                                Column {
                                    Text("Target Price / Serving", fontSize = 9.sp, color = TextMuted)
                                    Text("R ${String.format("%.2f", dish.sellingPricePerServing)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MoneyInGreen)
                                }
                                Column {
                                    Text("Total Meal Budget", fontSize = 9.sp, color = TextMuted)
                                    Text("R ${String.format("%.2f", dish.totalMealBudget)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MoneyOutBlue)
                                }
                            }

                            if (dish.ingredients.isNotEmpty()) {
                                Text("Raw Ingredients Breakdown:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                                dish.ingredients.forEach { ing ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("• ${ing.name} (${ing.quantity} ${ing.unit})", fontSize = 10.sp, color = TextWhite)
                                        Text("R ${String.format("%.2f", ing.totalCost)}", fontSize = 10.sp, color = TextMuted)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Sub-Tab 2: Staff & Day-Rate Expenses ---
@Composable
private fun StaffAndDayRatesSubTab(
    staffMembers: List<StaffMember>,
    onAddStaff: (name: String, role: StaffRole, dayRate: Double, shiftDate: String) -> Unit,
    onMarkPaid: (staffId: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(StaffRole.DOMESTIC_MAID) }
    var dayRateText by remember { mutableStateOf("450") }
    var shiftDateText by remember { mutableStateOf("Today") }

    val totalUnpaid = staffMembers.filterNot { it.isPaid }.sumOf { it.dayRate }
    val totalPaid = staffMembers.filter { it.isPaid }.sumOf { it.dayRate }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Staff & Day-Rate Expense Logger",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Domestic workers & temp kitchen staff shifts",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Row {
                    Text(text = "Unpaid: ", fontSize = 10.sp, color = TextMuted)
                    Text(text = "R ${String.format("%,.0f", totalUnpaid)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BurntOrange)
                }
            }

            // Quick Add Form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SlateSurfaceVariant)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Log Domestic / Temp Staff Shift:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BurntOrange)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Staff Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1.2f).testTag("staff_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = dayRateText,
                        onValueChange = { dayRateText = it },
                        placeholder = { Text("Rate (R)") },
                        singleLine = true,
                        modifier = Modifier.weight(0.8f).testTag("staff_rate_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurntOrange,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }

                // Role Selector Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StaffRole.values().take(3).forEach { role ->
                        FilterChip(
                            selected = selectedRole == role,
                            onClick = { selectedRole = role },
                            label = { Text(role.displayName, fontSize = 9.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurntOrange,
                                selectedLabelColor = TextWhite,
                                containerColor = SlateSurface,
                                labelColor = TextMuted
                            )
                        )
                    }
                }

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val rate = dayRateText.toDoubleOrNull() ?: 450.0
                            onAddStaff(name, selectedRole, rate, shiftDateText)
                            name = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("log_staff_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Log Shift Record", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Staff Roster Cards
            staffMembers.forEach { staff ->
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
                        Text(text = staff.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(
                            text = "${staff.role.displayName} • ${staff.shiftDate} • R ${String.format("%.2f", staff.dayRate)}",
                            fontSize = 10.sp,
                            color = BurntOrange
                        )
                    }

                    if (staff.isPaid) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MoneyInGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "PAID (Overheads Logged)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MoneyInGreen)
                        }
                    } else {
                        Button(
                            onClick = { onMarkPaid(staff.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MoneyInGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("mark_paid_${staff.id}")
                        ) {
                            Text(text = "Mark Paid", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// --- Sub-Tab 3: Inventory & Groceries ---
@Composable
private fun InventoryAndGroceriesSubTab(
    checklists: List<ChecklistItem>,
    personalExpenses: List<PersonalExpense>,
    allowance: Double,
    onToggleChecklist: (String) -> Unit,
    onAddChecklist: (mode: UserMode, category: String, title: String, dueDate: String) -> Unit,
    onOpenTapToPay: () -> Unit
) {
    StandardChecklistSuiteView(
        userMode = UserMode.EXECUTIVE_HOUSEWIFE,
        checklists = checklists,
        personalExpenses = personalExpenses,
        allowance = allowance,
        onToggleChecklist = onToggleChecklist,
        onAddChecklist = onAddChecklist,
        onOpenTapToPay = onOpenTapToPay
    )
}

// --- Sub-Tab 4: Master Calendar & Shared Notebook ---
@Composable
private fun MasterCalendarNotebookSubTab(
    notebookEntries: List<SharedNotebookEntry>,
    onOpenAddNote: () -> Unit,
    onShareEntry: (entry: SharedNotebookEntry, channel: String) -> Unit,
    onDeleteEntry: (entryId: String) -> Unit
) {
    var filterType by remember { mutableStateOf<NotebookType?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Shared Notebook & Master Calendar Hub",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Recipe cards, weekly menus & household guides with 1-tap sharing",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = onOpenAddNote,
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("add_note_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "New Note", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilterChip(
                    selected = filterType == null,
                    onClick = { filterType = null },
                    label = { Text("All", fontSize = 9.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BurntOrange,
                        selectedLabelColor = TextWhite,
                        containerColor = SlateSurfaceVariant,
                        labelColor = TextMuted
                    )
                )
                NotebookType.values().forEach { type ->
                    FilterChip(
                        selected = filterType == type,
                        onClick = { filterType = type },
                        label = { Text(type.displayName, fontSize = 9.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurntOrange,
                            selectedLabelColor = TextWhite,
                            containerColor = SlateSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            val filtered = if (filterType == null) notebookEntries else notebookEntries.filter { it.type == filterType }

            if (filtered.isEmpty()) {
                Text(text = "No notebook entries found.", fontSize = 11.sp, color = TextMuted)
            } else {
                filtered.forEach { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = entry.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(text = "${entry.type.displayName} • ${entry.category} • ${entry.createdDate}", fontSize = 9.sp, color = BurntOrange)
                                }

                                IconButton(onClick = { onDeleteEntry(entry.id) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }

                            Text(
                                text = entry.content,
                                fontSize = 11.sp,
                                color = TextWhite,
                                lineHeight = 15.sp
                            )

                            // 1-Tap Share Row (WhatsApp & PDF Export)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onShareEntry(entry, "WhatsApp") },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp).testTag("share_whatsapp_${entry.id}")
                                ) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "WhatsApp", modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("WhatsApp", fontSize = 10.sp)
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                Button(
                                    onClick = { onShareEntry(entry, "PDF Export") },
                                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp).testTag("share_pdf_${entry.id}")
                                ) {
                                    Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = "PDF", modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("PDF Export", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Standard Checklist View (Student / KAM) ---
@Composable
private fun StandardChecklistSuiteView(
    userMode: UserMode,
    checklists: List<ChecklistItem>,
    personalExpenses: List<PersonalExpense>,
    allowance: Double,
    onToggleChecklist: (String) -> Unit,
    onAddChecklist: (mode: UserMode, category: String, title: String, dueDate: String) -> Unit,
    onOpenTapToPay: () -> Unit
) {
    var newTitle by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf(
            when (userMode) {
                UserMode.KAM_SME -> "Follow-up Tasks"
                UserMode.EXECUTIVE_HOUSEWIFE -> "Weekly Groceries"
                UserMode.STUDENT -> "Assignment Deadlines"
                UserMode.ESTATE_CULINARY_OPS -> "Weekly Groceries"
            }
        )
    }

    val totalExpenses = personalExpenses.sumOf { it.amount }
    val remainingBalance = allowance - totalExpenses

    // Personal Balance Tracker Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Live Allowance & Expense Tracker", fontSize = 12.sp, color = TextMuted)
                    Text(
                        text = "R ${String.format("%,.2f", remainingBalance)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (remainingBalance >= 0) MoneyInGreen else BurntOrange
                    )
                }

                Button(
                    onClick = onOpenTapToPay,
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Contactless, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Tap-to-Pay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Checklist Suite
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "${userMode.displayName} Tasks", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)

            val categories = when (userMode) {
                UserMode.KAM_SME -> listOf("Follow-up Tasks", "30-Day Payment Nudges", "Site Visit Logs")
                UserMode.EXECUTIVE_HOUSEWIFE, UserMode.ESTATE_CULINARY_OPS -> listOf("Weekly Groceries", "Household Expenses", "Family Appointments", "Maintenance")
                UserMode.STUDENT -> listOf("Assignment Deadlines", "Exam Timetable", "Study Notes", "Textbooks")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurntOrange,
                            selectedLabelColor = TextWhite,
                            containerColor = SlateSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    placeholder = { Text("Add task...") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurntOrange,
                        unfocusedBorderColor = SlateBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            onAddChecklist(userMode, selectedCategory, newTitle, "Today")
                            newTitle = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            val currentItems = checklists.filter { it.mode == userMode && (selectedCategory.isBlank() || it.category == selectedCategory) }
            currentItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SlateSurfaceVariant)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isCompleted,
                        onCheckedChange = { onToggleChecklist(item.id) },
                        colors = CheckboxDefaults.colors(checkedColor = MoneyInGreen, uncheckedColor = TextMuted)
                    )
                    Column {
                        Text(text = item.title, fontSize = 12.sp, color = TextWhite)
                        Text(text = "${item.category} • Due: ${item.dueDate}", fontSize = 10.sp, color = BurntOrange)
                    }
                }
            }
        }
    }
}

// --- Recipe Builder Modal Dialog ---
@Composable
private fun RecipeBuilderDialog(
    onDismiss: () -> Unit,
    onCreateRecipe: (dishName: String, category: String, servings: Int, ingredients: List<RecipeIngredient>, sellingPrice: Double, notes: String) -> Unit
) {
    var dishName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Main Course") }
    var servingsText by remember { mutableStateOf("10") }
    var sellingPriceText by remember { mutableStateOf("250") }

    var ingName by remember { mutableStateOf("") }
    var ingQtyText by remember { mutableStateOf("1") }
    var ingUnit by remember { mutableStateOf("kg") }
    var ingCostText by remember { mutableStateOf("100") }

    val ingredientsList = remember { mutableStateListOf<RecipeIngredient>() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(20.dp),
            color = SlateSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Build Dish Recipe & Costing", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                OutlinedTextField(
                    value = dishName,
                    onValueChange = { dishName = it },
                    label = { Text("Dish Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("dialog_dish_name"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BurntOrange, unfocusedBorderColor = SlateBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = servingsText,
                        onValueChange = { servingsText = it },
                        label = { Text("Servings") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("dialog_servings"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BurntOrange, unfocusedBorderColor = SlateBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )

                    OutlinedTextField(
                        value = sellingPriceText,
                        onValueChange = { sellingPriceText = it },
                        label = { Text("Target Price/Serving (R)") },
                        singleLine = true,
                        modifier = Modifier.weight(1.2f).testTag("dialog_selling_price"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BurntOrange, unfocusedBorderColor = SlateBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                }

                // Ingredient Adder Box
                Text("Add Raw Ingredient:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BurntOrange)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = ingName,
                        onValueChange = { ingName = it },
                        placeholder = { Text("Ingredient") },
                        singleLine = true,
                        modifier = Modifier.weight(1.2f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BurntOrange, unfocusedBorderColor = SlateBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    OutlinedTextField(
                        value = ingCostText,
                        onValueChange = { ingCostText = it },
                        placeholder = { Text("Cost (R)") },
                        singleLine = true,
                        modifier = Modifier.weight(0.8f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BurntOrange, unfocusedBorderColor = SlateBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Button(
                        onClick = {
                            if (ingName.isNotBlank()) {
                                ingredientsList.add(
                                    RecipeIngredient(
                                        id = "ing_${System.currentTimeMillis()}",
                                        name = ingName,
                                        quantity = ingQtyText.toDoubleOrNull() ?: 1.0,
                                        unit = ingUnit,
                                        costPerUnit = ingCostText.toDoubleOrNull() ?: 50.0
                                    )
                                )
                                ingName = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+", fontSize = 14.sp)
                    }
                }

                if (ingredientsList.isNotEmpty()) {
                    Text("Ingredients added (${ingredientsList.size}):", fontSize = 10.sp, color = TextMuted)
                    ingredientsList.forEach { ing ->
                        Text("• ${ing.name} - R ${String.format("%.2f", ing.totalCost)}", fontSize = 10.sp, color = TextWhite)
                    }
                }

                Button(
                    onClick = {
                        val servings = servingsText.toIntOrNull() ?: 10
                        val price = sellingPriceText.toDoubleOrNull() ?: 250.0
                        onCreateRecipe(dishName, category, servings, ingredientsList.toList(), price, "")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("save_recipe_confirm")
                ) {
                    Text("Save Dish Recipe", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- Shared Notebook Entry Dialog ---
@Composable
private fun AddNotebookEntryDialog(
    onDismiss: () -> Unit,
    onAddEntry: (title: String, type: NotebookType, content: String, category: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(NotebookType.RECIPE_CARD) }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Estate Ops") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(20.dp),
            color = SlateSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Add Shared Notebook Entry", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("dialog_note_title"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BurntOrange, unfocusedBorderColor = SlateBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    NotebookType.values().forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.displayName, fontSize = 9.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurntOrange,
                                selectedLabelColor = TextWhite,
                                containerColor = SlateSurfaceVariant,
                                labelColor = TextMuted
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content / Notes") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("dialog_note_content"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BurntOrange, unfocusedBorderColor = SlateBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                )

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddEntry(title, selectedType, content, category)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("save_note_confirm")
                ) {
                    Text("Save Entry", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
