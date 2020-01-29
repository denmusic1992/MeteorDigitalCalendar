package interfaces

import com.russhwolf.settings.Settings
import enums.ClientType
import enums.MethodType
import models.CommonResponse
import models.Device
import models.User

/**
 * Интерфейс взаимодействия с серверной частью
 */
interface CoreApiInterface {

    /**
     * Метод POST регистрации устройства по UUID
     * @param authDeviceID UUID устройства
     * @param client тип платформы
     * @param settings доступ к хранилищу
     * @param methodType POST по умолчанию
     */
    suspend fun postDeviceID(
        authDeviceID: String,
        client: ClientType,
        settings: Settings,
        methodType: MethodType = MethodType.POST
    ): CommonResponse<Device>?

    /**
     * Метод получения пользователя с сервера
     * @param authDeviceID UUID устройства
     * @param settings доступ к хранилищу
     * @param methodType GET по умолчанию
     */
    suspend fun getCurrentUser(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType = MethodType.GET
    ): CommonResponse<User>?
}