package interfaces

import models.CategoryEvent

/**
 * Интерфейс основного поведения presenter
 */
interface CommonPresenter {
    /**
     * Регистрация
     */
    fun registerDevice()

    /**
     * Авторизация пользователя
     */
    fun authorizeUser()

    /**
     * Добавить категории в приложение
     * @return список категорий
     */
    fun setCategories()

    /**
     * Получить список мероприятий
     */
    fun setEvents()
}