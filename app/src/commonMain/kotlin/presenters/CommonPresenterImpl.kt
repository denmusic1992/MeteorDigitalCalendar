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
import models.CategoryEvent
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
        launch {
            val deviceID = Storage.getDeviceID(settings)

            val response = EventsRequestAPI.getCategories(deviceID, settings)
            if (response != null && response.code == ResponseCode.Ok.code) {
                val categories = response.data as ArrayList<CategoryEvent>?
                if (categories != null)
                    withContext(Dispatchers.Main) {
                        // Добавляем теги в фильтр, по умолчанию все выбраны изначально
                        filter.categories = categories
                        commonView.categoriesReceivedResult(true)
                    }
            } else {
                withContext(Dispatchers.Main) {
                    commonView.categoriesReceivedResult(
                        false,
                        "Не удалось получить данные по категориям с сервера"
                    )
                }
            }
        }
    }

    override fun setEvents() {
        launch {
            val deviceID = Storage.getDeviceID(settings)

            val eventsData = EventsRequestAPI.getEvents(
                deviceID,
                filter,
                settings
            )
            // Пишем события в переменную
            if (eventsData != null) {
                when (eventsData.code) {
                    ResponseCode.Ok.code -> {
                        events = eventsData.data as ArrayList<Event>?
                        withContext(Dispatchers.Main) {
                            commonView.eventsReceivedResult(true)
                        }
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            commonView.eventsReceivedResult(false, eventsData.errorList.toString())
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                commonView.eventsReceivedResult(false, "Не удалось получить события с сервера!")
            }
        }
    }

    override fun setFavourite(selectedEvent: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        launch {
            val deviceID = Storage.getDeviceID(settings)
            val event = events?.get(selectedEvent)
            if(event != null) {
                val favourite =
                    EventsRequestAPI.setFavourite(
                        deviceID,
                        event.eventId.toString(),
                        event.getEditedFavourite(),
                        settings
                    )
                when (favourite?.code) {
                    ResponseCode.Created.code -> {
                        withContext(Dispatchers.Main) {
                            event.setFavourite()
                            commonView.favouriteResult(true)
                        }
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            commonView.favouriteResult(
                                false,
                                "Не удалось добавить в избранное!"
                            )
                        }
                    }
                }
            }
        }
    }
}