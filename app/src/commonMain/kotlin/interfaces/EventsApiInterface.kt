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
    ): CommonResponse<ArrayList<CategoryEvent>>?

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
    ): CommonResponse<ArrayList<Event>>?
}