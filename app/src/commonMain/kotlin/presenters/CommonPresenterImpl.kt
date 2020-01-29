package presenters

import com.russhwolf.settings.Settings
import enums.ResponseCode
import helpers.Storage
import interfaces.CommonPresenter
import interfaces.CommonViewInterface
import interfaces.CoroutinePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Event
import models.Filter
import network.EventsRequestAPI

/**
 * Презентер для работы с главными функциями приложения
 * @param commonView интерфейс для обращения по главным событиям
 * @param settings доступ к key-value хранилищу
 */
class CommonPresenterImpl(
    private val commonView: CommonViewInterface,
    private val settings: Settings
) :
    CommonPresenter, CoroutinePresenter {

    override val job: Job
        get() = Job()

    // Фильтр для поиска
    private var filter: Filter = Filter.getFilter()

    // Мероприятия
    private var events: ArrayList<Event>? = null


    override fun setCategories() {
        val deviceID = Storage.getDeviceID(settings)
        launch {
            val response = EventsRequestAPI.getCategories(deviceID, settings)
            if (response?.data != null) {
                withContext(Dispatchers.Main) {
                    // Добавляем теги в фильтр, по умолчанию все выбраны изначально
                    filter.categories = response.data
                    commonView.categoriesReceived(response.data)
                }
            }
        }
    }

    override fun setEvents() {
        val deviceID = Storage.getDeviceID(settings)
        launch {
            val eventsData = EventsRequestAPI.getEvents(
                deviceID,
                filter,
                settings
            )
            // Пишем события в переменную
            if(eventsData != null) {
                when(eventsData.code) {
                    ResponseCode.Ok.code -> {
                        events = eventsData.data
                    }
                }
            }
            withContext(Dispatchers.Main) {
                commonView.eventsReceived()
            }
        }
    }
}