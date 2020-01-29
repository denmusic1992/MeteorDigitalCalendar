package interfaces

import models.CategoryEvent
import models.Event

/**
 * Интерфейс для взаимодействия CommonPresenter и View
 */
interface CommonViewInterface {
    /**
     * Результат при регистрации устройства
     * @param success успех или неудача
     * @param message что произошло
     */
    fun registerResult(success: Boolean, message: String? = null)

    /**
     * Результат при авторизации пользователя
     * @param message что произошло, для теста больше
     */
    fun authorizationResult(message: String? = null)

    /**
     * Результат при получении тегов
     * @param categories полученные теги с сервера
     */
    fun categoriesReceived(categories: ArrayList<CategoryEvent>?)

    /**
     * Результат при получении мероприятий с сервера
     * @param events полученные мероприятия
     */
    fun eventsReceived(events: ArrayList<Event>?)
}