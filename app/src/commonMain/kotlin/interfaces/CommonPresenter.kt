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
}