package presenters

import com.russhwolf.settings.Settings
import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import enums.ClientType
import enums.ResponseCode
import helpers.Storage
import interfaces.CommonPresenter
import interfaces.CommonViewInterface
import interfaces.CoroutinePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Filter
import network.CoreRequestAPI
import network.EventsRequestAPI

/**
 * Презентер для работы с главными функциями приложения
 */
class CommonPresenterImpl(
    private val commonView: CommonViewInterface,
    private val settings: Settings
) :
    CommonPresenter, CoroutinePresenter {

    override val job: Job
        get() = Job()

    private var filter: Filter

    /**
     * инициализация фильтра
     * TODO: Перенести в отдельный метод, типа clear
     */
    init {
        val dateFrom = Filter.formatDate(DateTime.now() - (DateTime.now().dayOfMonth.days - 1.days))
        println("dateFrom = $dateFrom")
        val dateTo =
            Filter.formatDate(DateTime.now() + (DateTime.now().endOfMonth.dayOfMonth - DateTime.now().dayOfMonth).days)
        println("dateTo = $dateTo")
        val currentMonth = DateTime.now().month1
        println("dateTo = $currentMonth")
        filter = Filter(
            dateFrom,
            dateTo,
            currentMonth,
            selectedPrice = "",
            isFavourite = false,
            isFree = false,
            selectedCities = Filter.fillCities()
        )
    }

    /**
     * Метод регистрации приложения в системе
     */
    override fun registerDevice() {
        // Если ещё не зарегистрированы, то регистрируемся
        if (Storage.getPrivateKey(settings) == null) {
            val deviceID = Storage.getDeviceID(settings)
            // Отправляем запрос на сервер
            launch {
                val data = CoreRequestAPI.postDeviceID(deviceID, ClientType.Android, settings)
                // Если получили ответ с сервера, разбираем его
                // получили отклик
                if (data != null) {
                    // Если дата есть и все хорошо, то добавляем в хранилище ключ и говорим, что регистрация успешна
                    when (data.code) {
                        ResponseCode.Created.code -> {
                            Storage.setPrivateKey(settings, data.data!!.privateKey)
                            withContext(Dispatchers.Main) {
                                commonView.registerResult(true, data.data.toString())
                            }
                        }
                        // Иначе передаем ошибку в вызывающий класс
                        else -> {
                            withContext(Dispatchers.Main) {
                                commonView.registerResult(false, data.errorList.toString())
                            }
                        }
                    }
                } else
                // Здесь то же самое, передаем ошибку
                    withContext(Dispatchers.Main) {
                        commonView.registerResult(false, "Unable to get data, null!")
                    }
            }
        } else {
            commonView.registerResult(
                true,
                "device already registered, uuid = ${Storage.getPrivateKey(settings)}"
            )
        }
    }

    override fun authorizeUser() {
        // Ищем пользователя на устройстве
        val previousUser = Storage.getCurrentUser(settings)
        // Если есть такой, то говорим серверу, что логинется такой то пользователь
        if (previousUser != null) {
            val deviceID = Storage.getDeviceID(settings)
            launch {
                val user = CoreRequestAPI.getCurrentUser(deviceID, settings)
                if (user?.data != null && user.data.userId == previousUser.userId) {
                    Storage.setCurrentUser(settings, user.data)
                    withContext(Dispatchers.Main) {
                        commonView.authorizationResult(user.data.toString())
                    }
                } else {
                    Storage.removeCurrentUser(settings)
                    withContext(Dispatchers.Main) {
                        commonView.authorizationResult("Пользователь удален!")
                    }
                }
            }
        } else {
            commonView.authorizationResult("Нет пользователя, нет логина")
        }
    }

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
                filter.dateFrom,
                filter.dateTo,
                filter.selectedCities,
                filter.getSelectedCategoryIds(),
                filter.selectedPrice,
                // TODO: Это переделать, на сервере
                if (filter.isFavourite) 1 else 0,
                deviceID,
                settings
            )
            withContext(Dispatchers.Main) {
                commonView.eventsReceived(eventsData?.data)
            }
        }
    }
}