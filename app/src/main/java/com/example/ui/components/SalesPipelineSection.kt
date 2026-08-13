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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import com.example.model.PipelineCard
import com.example.model.PipelineStage
import com.example.ui.theme.BurntOrange
import com.example.ui.theme.MoneyInGreen
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

enum class AgeingFilter(val displayName: String) {
    ALL("All Deals"),
    ACTIVE("Active (0-30d)"),
    CAUTION("Caution (31-60d)"),
    STAGNANT("Stagnant (61-89d)"),
    NO_GO_90D("Ageing / Stagnant (90d+)")
}

@Composable
fun SalesPipelineSection(
    pipelineCards: List<PipelineCard>,
    onAddDealClick: () -> Unit,
    onMoveStage: (cardId: String, newStage: PipelineStage) -> Unit,
    onReassignKam: ((cardId: String, newKam: String) -> Unit)? = null,
    onMoveToHousePool: ((cardId: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedStageFilter by remember { mutableStateOf<PipelineStage?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedAgeFilter by remember { mutableStateOf(AgeingFilter.ALL) }
    var isTeamViewActive by remember { mutableStateOf(true) }

    // Dialog state for KAM reassignment
    var cardToReassign by remember { mutableStateOf<PipelineCard?>(null) }

    val activeCount = pipelineCards.count { it.inactivityDays in 0..30 }
    val cautionCount = pipelineCards.count { it.inactivityDays in 31..60 }
    val stagnantCount = pipelineCards.count { it.inactivityDays in 61..89 }
    val noGoCount = pipelineCards.count { it.inactivityDays >= 90 }

    // Filter cards by search, ageing filter
    val filteredCards = pipelineCards.filter { card ->
        val matchesSearch = searchQuery.isBlank() ||
                card.clientName.contains(searchQuery, ignoreCase = true) ||
                card.materialDescription.contains(searchQuery, ignoreCase = true) ||
                card.kamName.contains(searchQuery, ignoreCase = true)

        val matchesAgeing = when (selectedAgeFilter) {
            AgeingFilter.ALL -> true
            AgeingFilter.ACTIVE -> card.inactivityDays in 0..30
            AgeingFilter.CAUTION -> card.inactivityDays in 31..60
            AgeingFilter.STAGNANT -> card.inactivityDays in 61..89
            AgeingFilter.NO_GO_90D -> card.inactivityDays >= 90
        }

        matchesSearch && matchesAgeing
    }

    val totalPipelineValue = filteredCards.sumOf { it.projectedValue }

    // Modal Dialog for KAM Reassignment
    cardToReassign?.let { card ->
        var chosenKam by remember { mutableStateOf("John Doe") }
        val kamOptions = listOf("John Doe", "Sarah Jenkins", "Mike Peters", "Executive Pool")

        AlertDialog(
            onDismissRequest = { cardToReassign = null },
            title = {
                Text(
                    text = "Reassign KAM for ${card.clientName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Current KAM: ${card.kamName}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Select New Key Account Manager:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    kamOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { chosenKam = option }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = chosenKam == option,
                                onClick = { chosenKam = option },
                                colors = RadioButtonDefaults.colors(selectedColor = BurntOrange)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = option, fontSize = 13.sp, color = TextWhite)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReassignKam?.invoke(card.id, chosenKam)
                        cardToReassign = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Reassign KAM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { cardToReassign = null },
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Text("Cancel", fontSize = 12.sp, color = TextMuted)
                }
            },
            containerColor = SlateSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header Card with Forecast, Search & KAM Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Row: Title, Value & + New Deal Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Analytics,
                                contentDescription = null,
                                tint = BurntOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sales Pipeline & Cash Flow Forecast",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "R ${String.format("%,.2f", totalPipelineValue)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MoneyInGreen
                        )
                        Text(
                            text = "${filteredCards.size} Active Deals | Team View: All KAM Pipelines",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = onAddDealClick,
                        modifier = Modifier.testTag("add_deal_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Deal",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "+ New", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("pipeline_search_input"),
                    placeholder = { Text("Search client, material, or KAM...", fontSize = 11.sp, color = TextMuted) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = BurntOrange,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SlateSurfaceVariant,
                        unfocusedContainerColor = SlateSurfaceVariant,
                        focusedBorderColor = BurntOrange,
                        unfocusedBorderColor = SlateBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                // Dynamic Color-Coded Filter Pills Row (Green, Orange, Red, NO-GO Limit)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Surface(
                            onClick = { selectedAgeFilter = AgeingFilter.ALL },
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedAgeFilter == AgeingFilter.ALL) BurntOrange else SlateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedAgeFilter == AgeingFilter.ALL) BurntOrange else SlateBorder),
                            modifier = Modifier.testTag("filter_pill_all")
                        ) {
                            Text(
                                text = "All (${pipelineCards.size})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    item {
                        // Green: Active (0-30 days)
                        Surface(
                            onClick = { selectedAgeFilter = AgeingFilter.ACTIVE },
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedAgeFilter == AgeingFilter.ACTIVE) Color(0xFF2E7D32) else Color(0xFF1B4D21).copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50)),
                            modifier = Modifier.testTag("filter_pill_active")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF4CAF50), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Active (0–30d) [$activeCount]",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    item {
                        // Orange: Caution (31-60 days)
                        Surface(
                            onClick = { selectedAgeFilter = AgeingFilter.CAUTION },
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedAgeFilter == AgeingFilter.CAUTION) Color(0xFFE65100) else Color(0xFF7A2D00).copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9800)),
                            modifier = Modifier.testTag("filter_pill_caution")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("⚠️", fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Caution (31–60d) [$cautionCount]",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    item {
                        // Red/Warning: Stagnant (61-89 days)
                        Surface(
                            onClick = { selectedAgeFilter = AgeingFilter.STAGNANT },
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedAgeFilter == AgeingFilter.STAGNANT) OverdueRed else Color(0xFF5C0B0B).copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, OverdueRed),
                            modifier = Modifier.testTag("filter_pill_stagnant")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("⚠️", fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Stagnant (61–89d) [$stagnantCount]",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    item {
                        // Alert Badge: Ageing / Stagnant (90d+) NO-GO Limit
                        Surface(
                            onClick = { selectedAgeFilter = AgeingFilter.NO_GO_90D },
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedAgeFilter == AgeingFilter.NO_GO_90D) Color(0xFFB71C1C) else Color(0xFF3E0000),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF5252)),
                            modifier = Modifier.testTag("filter_pill_nogo")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("🚨", fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Ageing / Stagnant (90d) [$noGoCount]",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFF8A80)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Kanban Stages Filter / Tab Row
        ScrollableTabRow(
            selectedTabIndex = if (selectedStageFilter == null) 0 else selectedStageFilter!!.ordinal + 1,
            containerColor = SlateSurface,
            contentColor = BurntOrange,
            edgePadding = 0.dp
        ) {
            Tab(
                selected = selectedStageFilter == null,
                onClick = { selectedStageFilter = null },
                text = { Text("All Stages", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            PipelineStage.values().forEach { stage ->
                val stageCards = filteredCards.filter { it.stage == stage }
                val stageSum = stageCards.sumOf { it.projectedValue }
                Tab(
                    selected = selectedStageFilter == stage,
                    onClick = { selectedStageFilter = stage },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stage.displayName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("R ${String.format("%,.0f", stageSum)}", fontSize = 9.sp, color = BurntOrange)
                        }
                    }
                )
            }
        }

        // Kanban Column Cards Feed
        val stagesToDisplay = if (selectedStageFilter != null) listOf(selectedStageFilter!!) else PipelineStage.values().toList()

        stagesToDisplay.forEach { stage ->
            val cardsInStage = filteredCards.filter { it.stage == stage }
            val stageTotal = cardsInStage.sumOf { it.projectedValue }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Column Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (stage) {
                                    PipelineStage.IN_TALKS -> Icons.Default.QuestionAnswer
                                    PipelineStage.PROFORMA_SENT -> Icons.Default.Description
                                    PipelineStage.PAYMENT_PENDING -> Icons.Default.MonetizationOn
                                    PipelineStage.DISPATCHED -> Icons.Default.LocalShipping
                                },
                                contentDescription = null,
                                tint = BurntOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "[${stage.displayName}]",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        Text(
                            text = "R ${String.format("%,.2f", stageTotal)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoneyInGreen
                        )
                    }

                    if (cardsInStage.isEmpty()) {
                        Text(
                            text = "No deals matching criteria in this column.",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    } else {
                        cardsInStage.forEach { card ->
                            PipelineCardItem(
                                card = card,
                                onMoveStage = { nextStage -> onMoveStage(card.id, nextStage) },
                                onReassignClick = { cardToReassign = card },
                                onHousePoolClick = { onMoveToHousePool?.invoke(card.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PipelineCardItem(
    card: PipelineCard,
    onMoveStage: (PipelineStage) -> Unit,
    onReassignClick: () -> Unit,
    onHousePoolClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    val nextStage = when (card.stage) {
        PipelineStage.IN_TALKS -> PipelineStage.PROFORMA_SENT
        PipelineStage.PROFORMA_SENT -> PipelineStage.PAYMENT_PENDING
        PipelineStage.PAYMENT_PENDING -> PipelineStage.DISPATCHED
        PipelineStage.DISPATCHED -> null
    }

    // Ageing Badge calculation
    val (badgeText, badgeBgColor, badgeTextColor) = when {
        card.inactivityDays >= 90 -> Triple("🚨 ${card.inactivityDays}d NO-GO LIMIT", Color(0xFFB71C1C), Color(0xFFFFCDD2))
        card.inactivityDays in 61..89 -> Triple("⚠️ ${card.inactivityDays} Days Inactive", Color(0xFFC62828), Color.White)
        card.inactivityDays in 31..60 -> Triple("⚠️ ${card.inactivityDays} Days Caution", Color(0xFFE65100), Color.White)
        else -> Triple("🟢 ${card.inactivityDays}d Active", Color(0xFF2E7D32), Color.White)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (card.inactivityDays >= 90) Color(0xFFFF5252) else SlateBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Header Row: Client Name, Projected Value & Manager 3-Dots Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = card.clientName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "R ${String.format("%,.2f", card.projectedValue)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoneyInGreen
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("manager_menu_${card.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Manager Options",
                            tint = TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(SlateSurface)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = BurntOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Reassign KAM", fontSize = 12.sp, color = TextWhite)
                                }
                            },
                            onClick = {
                                showMenu = false
                                onReassignClick()
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.HomeWork,
                                        contentDescription = null,
                                        tint = MoneyInGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Move to House Pool", fontSize = 12.sp, color = TextWhite)
                                }
                            },
                            onClick = {
                                showMenu = false
                                onHousePoolClick()
                            }
                        )
                    }
                }
            }

            Text(
                text = card.materialDescription,
                fontSize = 11.sp,
                color = TextMuted
            )

            // KAM Info & Ageing Status Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // KAM Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SlateSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Text(
                        text = if (card.isHousePool) "🏠 House Pool" else "👤 KAM: ${card.kamName}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Ageing Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeBgColor
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Action Row: Advance Stage Button or Dispatched indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Created: ${card.createdDate}",
                    fontSize = 10.sp,
                    color = TextMuted
                )

                if (nextStage != null) {
                    Button(
                        onClick = { onMoveStage(nextStage) },
                        modifier = Modifier.height(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BurntOrange),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Move to ${nextStage.displayName}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = TextWhite
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MoneyInGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Released / Dispatched",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoneyInGreen
                        )
                    }
                }
            }
        }
    }
}
