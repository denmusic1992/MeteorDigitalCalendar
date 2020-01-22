package interfaces

import models.CategoryEvent

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

    fun categoriesReceived(categories: ArrayList<CategoryEvent>?)
}