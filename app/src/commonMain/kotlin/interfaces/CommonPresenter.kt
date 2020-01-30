package interfaces

/**
 * Интерфейс основного поведения presenter
 */
interface CommonPresenter {

    /**
     * Добавить категории в приложение
     * @return список категорий
     */
    fun setCategories()

    /**
     * Получить список мероприятий
     */
    fun setEvents()

    /**
     * Сделать событие с id избранным
     * @param selectedEvent id события
     */
    fun setFavourite(selectedEvent: Int)
}