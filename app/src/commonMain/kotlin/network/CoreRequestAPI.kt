package network

import com.russhwolf.settings.Settings
import enums.ClientType
import enums.MethodType
import interfaces.CoreApiInterface
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import models.CommonResponse
import models.Device
import models.User

/**
 * Класс описывающий логику подключения к методам api из Core
 */
@Suppress("EXPERIMENTAL_API_USAGE")
object CoreRequestAPI: CoreApiInterface {

    //region url staff
    // Адреса api url
    private const val POST_DEVICE_API = "core/devices"
    private const val GET_CURRENT_USER_API = "core/users/register"

    //endregion

    /**
     * Метод регистрации и получения privateKey
     * @param authDeviceID идентификатор устройства, записан в виде uuid в хранилище приложения
     * @param client тип устройства
     * @param settings доступ к хранилищу типа key-value
     * @param methodType тип метода
     * @return данные об устройстве и его private key
     */
    override suspend fun postDeviceID(
        authDeviceID: String,
        client: ClientType,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<Device>? {
        // Задаем набор параметров
        val paramsMap = ArrayList<Pair<String, Any>>()
        paramsMap.add(Pair("AuthDeviceID", authDeviceID))
        paramsMap.add(Pair("Client", client.type))

        //инициализируем сериализатор под наши нужды
        val serializer = KotlinxSerializer(Json.nonstrict).apply {
            // Перечисляем, какие именно варианты у нас могут быть с generic data параметр
            register(CommonResponse.serializer(Device.serializer()))
        }
        return RequestAPI.makeRequest(methodType, settings, POST_DEVICE_API, paramsMap, serializer)
    }

    /**
     * Получаем пользователя
     * @param authDeviceID идентификатор устройства
     * @param settings доступ к хранилищу
     * @param methodType тип метода
     * @return пользовательские учетные данные
     */
    override suspend fun getCurrentUser(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<User>? {
        val paramsMap = ArrayList<Pair<String, Any>>()
        paramsMap.add(Pair("AuthDeviceID", authDeviceID))

        // инициализируем сериализатор под наши нужды
        val serializer = KotlinxSerializer(Json.nonstrict).apply {
            // Перечисляем, какие именно варианты у нас могут быть с generic data параметр
            register(CommonResponse.serializer(User.serializer()))
        }
        return RequestAPI.makeRequest(methodType, settings, GET_CURRENT_USER_API, paramsMap, serializer)
    }
}