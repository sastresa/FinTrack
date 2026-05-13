package com.fintrack.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.domain.model.Category
import com.fintrack.ui.navigation.Routes

data class CategoryPreset(
    val iconName: String,
    val label: String,
    val icon: ImageVector,
)

data class BadgeTone(
    val container: Color,
    val content: Color,
)

private val categoryPresets = listOf(
    CategoryPreset("category", "General", Icons.Filled.Category),
    CategoryPreset("shopping_cart", "Groceries", Icons.Filled.ShoppingCart),
    CategoryPreset("restaurant", "Dining", Icons.Filled.Restaurant),
    CategoryPreset("directions_car", "Transport", Icons.Filled.DirectionsCar),
    CategoryPreset("home", "Rent", Icons.Filled.Home),
    CategoryPreset("payments", "Salary", Icons.Filled.Payments),
    CategoryPreset("work", "Freelance", Icons.Filled.Work),
    CategoryPreset("swap_horiz", "Transfer", Icons.Filled.SwapHoriz),
)

private val navIcons = mapOf(
    Routes.DASHBOARD to Icons.Filled.Dashboard,
    Routes.TRANSACTIONS to Icons.AutoMirrored.Filled.ReceiptLong,
    Routes.BUDGETS to Icons.Filled.Savings,
    Routes.REPORTS to Icons.Filled.Insights,
    Routes.SETTINGS to Icons.Filled.Settings,
)

fun categoryIconPresets(): List<CategoryPreset> = categoryPresets

fun categoryIcon(iconName: String): ImageVector =
    categoryPresets.firstOrNull { it.iconName == iconName }?.icon ?: Icons.Filled.Category

fun financeNavIcon(route: String): ImageVector =
    navIcons[route] ?: Icons.Filled.Category

@Composable
fun categoryTone(colorToken: String): BadgeTone {
    val colors = MaterialTheme.colorScheme
    return when (colorToken) {
        "mint" -> BadgeTone(container = Color(0xFFDDF4EA), content = Color(0xFF145C47))
        "coral" -> BadgeTone(container = Color(0xFFF9E0DA), content = Color(0xFF8A4639))
        "blue" -> BadgeTone(container = Color(0xFFDCEAFB), content = Color(0xFF245F91))
        "violet" -> BadgeTone(container = Color(0xFFE8E0F8), content = Color(0xFF5F4B93))
        "green" -> BadgeTone(container = Color(0xFFDCEFDA), content = Color(0xFF2B6A33))
        "amber" -> BadgeTone(container = Color(0xFFF7E8C8), content = Color(0xFF8A5B08))
        "slate" -> BadgeTone(container = Color(0xFFE0E7EA), content = Color(0xFF45606A))
        else -> BadgeTone(
            container = colors.surfaceVariant,
            content = colors.onSurfaceVariant,
        )
    }
}

@Composable
fun CategoryBadge(
    iconName: String,
    colorToken: String,
    label: String? = null,
    modifier: Modifier = Modifier,
    emphasize: Boolean = false,
) {
    val tone = categoryTone(colorToken)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(if (emphasize) tone.container else tone.container.copy(alpha = 0.8f))
            .border(
                width = 1.dp,
                color = tone.content.copy(alpha = if (emphasize) 0.32f else 0.18f),
                shape = RoundedCornerShape(999.dp),
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(tone.content.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = categoryIcon(iconName),
                contentDescription = label,
                tint = tone.content,
                modifier = Modifier.size(14.dp),
            )
        }
        if (label != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                color = tone.content,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun CategoryBadge(
    category: Category,
    modifier: Modifier = Modifier,
    emphasize: Boolean = false,
) {
    CategoryBadge(
        iconName = category.iconName,
        colorToken = category.colorToken,
        label = category.name,
        modifier = modifier,
        emphasize = emphasize,
    )
}

@Composable
fun CategoryGlyph(
    iconName: String,
    colorToken: String,
    modifier: Modifier = Modifier,
) {
    val tone = categoryTone(colorToken)
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(tone.container),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = categoryIcon(iconName),
            contentDescription = null,
            tint = tone.content,
            modifier = Modifier.size(14.dp),
        )
    }
}

@Composable
fun CategoryInlineLabel(
    iconName: String,
    colorToken: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryGlyph(iconName = iconName, colorToken = colorToken)
        Spacer(Modifier.width(8.dp))
        Text(text = label)
    }
}

@Composable
fun FinanceNavBadge(
    route: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val container = if (selected) colors.primaryContainer else colors.surfaceVariant.copy(alpha = 0.74f)
    val content = if (selected) colors.primary else colors.onSurfaceVariant
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(container)
            .border(
                width = 1.dp,
                color = if (selected) colors.primary.copy(alpha = 0.18f) else colors.outline.copy(alpha = 0.35f),
                shape = RoundedCornerShape(14.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = financeNavIcon(route),
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(18.dp),
        )
    }
}
