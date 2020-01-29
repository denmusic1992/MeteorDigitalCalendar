package interfaces

import com.russhwolf.settings.Settings
import enums.MethodType
import models.CategoryEvent
import models.CommonResponse
import models.Event

interface EventsApiInterface {
    /**
     * Метод получения категорий мероприятий
     * @param authDeviceID идентификатор устройства
     * @param settings доступ к хранилищу
     * @param methodType Тип метода
     */
    suspend fun getCategories(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType = MethodType.GET
    ): CommonResponse<ArrayList<CategoryEvent>>?

    /**
     * Метод получения event
     * @param dateFrom дата начала мероприятий
     * @param dateTo дата окончания мероприятий
     * @param cities город, в котором будут проводиться мероприятия
     * @param categories тэги события
     * @param price цена
     * @param favourite
     * @param deviceID идентификатор устройства
     * @param settings доступ к хранилищу
     * @param methodType Тип метода
     */
    suspend fun getEvents(
        dateFrom: String,
        dateTo: String,
        cities: ArrayList<String>,
        categories: ArrayList<Int>,
        price: String,
        favourite: Int,
        deviceID: String,
        settings: Settings,
        methodType: MethodType = MethodType.GET
    ): CommonResponse<ArrayList<Event>>?
}