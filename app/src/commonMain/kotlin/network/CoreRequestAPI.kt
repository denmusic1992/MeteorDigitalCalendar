package network

import com.russhwolf.settings.Settings
import enums.ClientType
import enums.MethodType
import interfaces.CoreApiInterface
import models.CommonResponse
import models.Device
import models.RegistrationCredentials
import models.User

/**
 * Класс описывающий логику подключения к методам api из Core
 */
@Suppress("EXPERIMENTAL_API_USAGE")
object CoreRequestAPI : CoreApiInterface {

    //region url staff
    // Адреса api url
    private const val POST_DEVICE_API = "core/devices"
    private const val GET_CURRENT_USER_API = "core/users/current"
    private const val POST_REGISTRATION_DATA = "core/users/register"
    private const val PUT_AUTHORIZATION_DATA = "core/users/login"
    private const val POST_EMAIL_RESEND_DATA = "core/users/reset"

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

        return RequestAPI.makeRequest(methodType, settings, POST_DEVICE_API, paramsMap)
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

        return RequestAPI.makeRequest(methodType, settings, GET_CURRENT_USER_API, paramsMap)
    }


    override suspend fun postRegistrationData(
        deviceID: String,
        credentials: RegistrationCredentials,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<User>? {
        // передаем используемые параметры
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", deviceID))
        params.add(Pair("Email", credentials.email))
        params.add(Pair("Name", credentials.name))
        params.add(Pair("Password1", credentials.password))
        params.add(Pair("Password2", credentials.confirmPassword))
        params.add(Pair("Phone", credentials.phone))

//        // инициализируем сериализатор под наши нужды
//        val serializer = KotlinxSerializer(Json.nonstrict).apply {
//            // Перечисляем, какие именно варианты у нас могут быть с generic data параметр
//            register(CommonResponse.serializer(User.serializer()))
//        }
        //val serializer = RequestAPI.getKotlinxSerializer<>()

        return RequestAPI.makeRequest(methodType, settings, POST_REGISTRATION_DATA, params)
    }

    override suspend fun postAuthorizationData(
        deviceID: String,
        credentials: RegistrationCredentials,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<User>? {
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", deviceID))
        params.add(Pair("Login", credentials.email))
        params.add(Pair("Password", credentials.password))

        return RequestAPI.makeRequest(methodType, settings, PUT_AUTHORIZATION_DATA, params)

    }

    override suspend fun postEmailResendData(
        deviceID: String,
        email: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<Device>? {
        val params = ArrayList<Pair<String, Any>>()
        params.add(Pair("AuthDeviceID", deviceID))
        params.add(Pair("Email", email))

        return RequestAPI.makeRequest(methodType, settings, POST_EMAIL_RESEND_DATA, params)
    }
}