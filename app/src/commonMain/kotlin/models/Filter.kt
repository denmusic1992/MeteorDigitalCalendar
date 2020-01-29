package models

import com.soywiz.klock.DateTime
import kotlin.jvm.Synchronized

/**
 * Модель, описывающая фильтр для мероприятий
 * @param dateFrom Начиная с какой даты показывать события
 * @param dateTo По какую дату включительно показывать события
 * @param selectedMonth Выбранный месяц для показа
 * @param categories теги
 * @param isFavourite флаг "только избранные"
 * @param isFree флаг "только бесплатные"
 * @param selectedCities выбранные гоорода
 */
data class Filter(
    var dateFrom: String,
    var dateTo: String,
    var selectedMonth: Int,
    var categories: ArrayList<CategoryEvent> = arrayListOf(),
    var isFavourite: Boolean,
    var isFree: Boolean,
    var selectedPrice: String,
    var selectedCities: ArrayList<String>
) {

    fun getSelectedCategoryIds(): ArrayList<Int> {
        return ArrayList(categories.filter { category -> category.selected }.map { it.categoryId })
    }

    companion object {
        @Synchronized
        internal fun fillCities(): ArrayList<String> {
            return arrayListOf("russia", "moscow", "spb")
        }

        @Synchronized
        internal fun formatDate(date: DateTime): String {
            return date.toString(DATE_FMT)
        }

        private const val DATE_FMT = "YYYY-MM-dd"
    }
}