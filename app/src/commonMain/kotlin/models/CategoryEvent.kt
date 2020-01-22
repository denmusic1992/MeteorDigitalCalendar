package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Модель категории события
 * @param categoryId идентификатор категории
 * @param title название категории
 * @param color соответствующий цвет этой категории
 * @param order порядок в списке (Transient - может отсутствовать в json)
 */
@Serializable
data class CategoryEvent(
    @SerialName("CategoryID")
    val categoryId: Int,
    @SerialName("Title")
    val title: String,
    @SerialName("Color")
    val color: String,
    @SerialName("SortOrder")
    @Transient
    val order: Int = 0
)