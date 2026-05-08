package com.fintrack.data.local.mapper

import com.fintrack.data.local.entity.CategoryEntity
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    iconName = iconName,
    colorToken = colorToken,
    type = CategoryType.valueOf(type),
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    iconName = iconName,
    colorToken = colorToken,
    type = type.name,
)
