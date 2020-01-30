package interfaces

/**
 * Интерфейс для взаимодействия CommonPresenter и View
 * TODO: Необходимо сделать интерфейсы для взаимодействия с данными моделей
 */
interface CommonViewInterface {
    /**
     * Результат при получении тегов
     * @param success успех или неудача
     * @param reason текст ошибки при неудаче
     */
    fun categoriesReceivedResult(success: Boolean, reason: String? = null)

    /**
     * Результат при получении мероприятий с сервера
     * @param success успех или неудача
     * @param reason текст ошибки при неудаче
     */
    fun eventsReceivedResult(success: Boolean, reason: String? = null)

    /**
     * @param success успех или неудача
     * @param reason текст ошибки при неудаче
     */
    fun favouriteResult(success: Boolean, reason: String? = null)
}