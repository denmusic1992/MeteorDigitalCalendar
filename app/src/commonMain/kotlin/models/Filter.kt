package models

import com.soywiz.klock.DateTime
import com.soywiz.klock.days
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
    // TODO: Исправить в дальнейшем
    var isFavourite: Int,
    var isFree: Boolean,
    var selectedPrice: String,
    var selectedCities: ArrayList<String>
) {

    /**
     * Получить id выбранных категорий
     * @return список id
     */
    fun getSelectedCategoryIds(): ArrayList<Int> {
        return ArrayList(categories.filter { category -> category.selected }.map { it.categoryId })
    }

    companion object {

        /**
         * Заполнить стандартными городами
         */
        @Synchronized
        internal fun fillCities(): ArrayList<String> {
            return arrayListOf("russia", "moscow", "spb")
        }

        /**
         * Форматировать дату
         */
        @Synchronized
        internal fun formatDate(date: DateTime): String {
            return date.toString(DATE_FMT)
        }

        @Synchronized
        fun getFilter(): Filter {
            val dateFrom = formatDate(DateTime.now() - (DateTime.now().dayOfMonth.days - 1.days))
            println("dateFrom = $dateFrom")
            val dateTo =
                formatDate(DateTime.now() + (DateTime.now().endOfMonth.dayOfMonth - DateTime.now().dayOfMonth).days)
            println("dateTo = $dateTo")
            val currentMonth = DateTime.now().month1
            println("dateTo = $currentMonth")
            return Filter(
                dateFrom,
                dateTo,
                currentMonth,
                selectedPrice = "",
                isFavourite = 0,
                isFree = false,
                selectedCities = fillCities()
            )
        }

        private const val DATE_FMT = "YYYY-MM-dd"
    }
}