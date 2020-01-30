package interfaces

import com.russhwolf.settings.Settings
import enums.ClientType
import enums.MethodType
import models.CommonResponse
import models.Device
import models.RegistrationCredentials
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

    /**
     * Метод регистрации пользователя в приложении
     * @param deviceID идентификатор устройства
     * @param credentials данные для регистрации
     * @param settings доступ к key-value хранилищу
     * @param methodType тип метода
     * @return пользовательскую информацию
     */
    suspend fun postRegistrationData(
        deviceID: String,
        credentials: RegistrationCredentials,
        settings: Settings,
        methodType: MethodType = MethodType.POST
    ): CommonResponse<User>?

    /**
     * Метод авторизации в приложении
     * @param deviceID идентификатор устройства
     * @param credentials данные для регистрации
     * @param settings доступ к key-value хранилищу
     * @param methodType тип метода
     * @return пользовательскую информацию
     */
    suspend fun postAuthorizationData(
        deviceID: String,
        credentials: RegistrationCredentials,
        settings: Settings,
        methodType: MethodType = MethodType.PUT
    ): CommonResponse<User>?
}