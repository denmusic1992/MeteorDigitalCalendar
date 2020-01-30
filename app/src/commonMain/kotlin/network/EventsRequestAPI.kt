package network

import com.russhwolf.settings.Settings
import enums.MethodType
import interfaces.EventsApiInterface
import models.CategoryEvent
import models.CommonResponse
import models.Event
import models.Filter

/**
 * Класс описывающий логику подключения к методам api из Events
 */
@Suppress("EXPERIMENTAL_API_USAGE")
object EventsRequestAPI : EventsApiInterface {

    //region url staff
    // Адреса api url
    private const val GET_CATEGORIES_API = "calendar/categories"
    private const val GET_EVENTS_API = "calendar/events"
    private const val PUT_SET_FAVOURITE = "calendar/events/favourites"

    //endregion

    /**
     * Метод получения типов событий в календаре
     */
    override suspend fun getCategories(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<Array<CategoryEvent>>? {
        // Собираем в кучу параметры
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", authDeviceID))

//        // инициализируем сериализатор под наши нужды
//        val serializer = KotlinxSerializer(Json.nonstrict).apply {
//            // Регистрируем список тегов
//            register(CommonResponse.serializer(CategoryEvent.serializer().list))
//        }

        return RequestAPI.makeRequest(methodType, settings, GET_CATEGORIES_API, params)
    }

    /**
     * Метод получения списка событий в соответствии с выбранными параметрами фильтрации
     */
    override suspend fun getEvents(
        deviceID: String,
        filter: Filter,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<Array<Event>>? {
        // Заполняем переменные для доступа к API
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", deviceID))
        params.add(Pair("DateFrom", filter.dateFrom))
        params.add(Pair("DateTo", filter.dateTo))
        // Добавляем выбранные города
        for (city in filter.selectedCities) {
            params.add(Pair("CityType[]", city))
        }
        // Добавляем выбранные категории
        for (category in filter.getSelectedCategoryIds()) {
            params.add(Pair("CategoryID[]", category))
        }
        params.add(Pair("Favourite", filter.isFavourite))
        params.add(Pair("Price", filter.selectedPrice))

//        // инициализируем сериализатор под наши нужды
//        val serializer = KotlinxSerializer(Json.nonstrict).apply {
//            // Регистрируем список тегов
//            register(CommonResponse.serializer(Event.serializer().list))
//        }

        return RequestAPI.makeRequest(methodType, settings, GET_EVENTS_API, params)
    }

    override suspend fun setFavourite(
        deviceID: String,
        eventID: String,
        favourite: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<Unit>? {
        // Заполняем переменные для доступа к API
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", deviceID))
        params.add(Pair("EventID", eventID))
        params.add(Pair("Favourite", favourite))

        return RequestAPI.makeRequest(methodType, settings, PUT_SET_FAVOURITE, params)
    }
}