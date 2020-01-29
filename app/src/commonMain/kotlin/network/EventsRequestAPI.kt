package network

import com.russhwolf.settings.Settings
import enums.MethodType
import interfaces.EventsApiInterface
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import models.CategoryEvent
import models.CommonResponse
import models.Event

/**
 * Класс описывающий логику подключения к методам api из Events
 */
@Suppress("EXPERIMENTAL_API_USAGE")
object EventsRequestAPI : EventsApiInterface {

    //region url staff
    // Адреса api url
    private const val GET_CATEGORIES_API = "calendar/categories"
    private const val GET_EVENTS_API = "calendar/events"

    //endregion

    /**
     * Метод получения типов событий в календаре
     * @param authDeviceID идентификатор устройства
     * @param settings доступ к key-value хранилищу
     * @param methodType тип метода
     * @return списко категорий CategoryEvent
     */
    override suspend fun getCategories(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<ArrayList<CategoryEvent>>? {
        // Собираем в кучу параметры
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", authDeviceID))

        // инициализируем сериализатор под наши нужды
        val serializer = KotlinxSerializer(Json.nonstrict).apply {
            // Регистрируем список тегов
            register(CommonResponse.serializer(CategoryEvent.serializer().list))
        }

        return RequestAPI.makeRequest(methodType, settings, GET_CATEGORIES_API, params, serializer)
    }

    /**
     * Метод получения списка событий в соответствии с выбранными параметрами фильтрации
     * @param dateFrom с какой даты мероприятия
     * @param dateTo по какую дату мероприятия
     * @param cities список городов
     * @param categories теги
     * @param price цена (категории цен)
     * @param favourite смотреть только в избранных
     * @param deviceID идентификатор устройства
     * @param settings доступ к хранилищу
     * @param methodType тип метода
     * @return
     */
    override suspend fun getEvents(
        dateFrom: String,
        dateTo: String,
        cities: ArrayList<String>,
        categories: ArrayList<Int>,
        price: String,
        favourite: Int,
        deviceID: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<ArrayList<Event>>? {
        // Заполняем переменные для доступа к API
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", deviceID))
        params.add(Pair("DateFrom", dateFrom))
        params.add(Pair("DateTo", dateTo))
        for (city in cities) {
            params.add(Pair("CityType[]", city))
        }
//        for (category in categories) {
//            params.add(Pair("CategoryID", category))
//        }
        params.add(Pair("Favourite", favourite))
        params.add(Pair("Price", price))

        // инициализируем сериализатор под наши нужды
        val serializer = KotlinxSerializer(Json.nonstrict).apply {
            // Регистрируем список тегов
            register(CommonResponse.serializer(Event.serializer().list))
        }

        return RequestAPI.makeRequest(methodType, settings, GET_EVENTS_API, params, serializer)
    }
}