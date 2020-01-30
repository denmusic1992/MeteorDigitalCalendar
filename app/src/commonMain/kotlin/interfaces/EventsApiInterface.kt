package interfaces

import com.russhwolf.settings.Settings
import enums.MethodType
import models.CategoryEvent
import models.CommonResponse
import models.Event
import models.Filter

interface EventsApiInterface {
    /**
     * Метод получения категорий мероприятий
     * @param authDeviceID идентификатор устройства
     * @param settings доступ к хранилищу
     * @param methodType Тип метода
     * @return список категорий CategoryEvent
     */
    suspend fun getCategories(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType = MethodType.GET
    ): CommonResponse<Array<CategoryEvent>>?

    /**
     * Метод получения event
     * @param deviceID идентификатор устройства
     * @param filter параметры поиска
     * @param settings доступ к хранилищу
     * @param methodType Тип метода
     * @return список событий по фильтру
     */
    suspend fun getEvents(
        deviceID: String,
        filter: Filter,
        settings: Settings,
        methodType: MethodType = MethodType.GET
    ): CommonResponse<Array<Event>>?

    /**
     * Метод, добавляющий событие в избранные
     * @param deviceID идентификатор устройства
     * @param eventID номер события
     * @param favourite является ли избранным
     * @param settings доступ к хранилищу
     * @param methodType Тип метода
     * @return ответ, получилось или нет
     */
    suspend fun setFavourite(
        deviceID: String,
        eventID: String,
        favourite: String,
        settings: Settings,
        methodType: MethodType = MethodType.PUT
    ): CommonResponse<Unit>?
}