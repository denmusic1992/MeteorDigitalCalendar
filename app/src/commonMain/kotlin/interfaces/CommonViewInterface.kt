package interfaces

import models.CategoryEvent

/**
 * Интерфейс для взаимодействия CommonPresenter и View
 */
interface CommonViewInterface {
    /**
     * Результат при получении тегов
     * @param categories полученные теги с сервера
     */
    fun categoriesReceived(categories: ArrayList<CategoryEvent>?)

    /**
     * Результат при получении мероприятий с сервера
     */
    fun eventsReceived()
}