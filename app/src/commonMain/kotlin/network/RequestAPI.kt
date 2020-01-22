package network

import com.russhwolf.settings.Settings
import enums.ClientType
import enums.MethodType
import helpers.HashEncoder
import interfaces.ApiCoreInterface
import interfaces.ApiEventsInterface
import io.ktor.client.HttpClient
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import kotlinx.io.core.use
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import models.CategoryEvent
import models.CommonResponse
import models.Device
import models.User

/**
 * Класс, работающий с подключением к серверу
 */
// TODO: Не вижу смысла в обращении по интерфейсу, но пока оставим так
@Suppress("EXPERIMENTAL_API_USAGE")
object RequestAPI : ApiCoreInterface, ApiEventsInterface {

    //region url staff
    // Адрес URL
    private const val BASE_ADDRESS = "http://mdc.fokgroup.com/api/"
    // Методы api
    // TODO: надо их перенести куда нибудь подальше
    private const val POST_DEVICE_API = "core/devices"
    private const val GET_CURRENT_USER_API = "core/users/register"
    private const val GET_CATEGORIES_API = "calendar/categories"
    //endregion

//    // инициализация http клиента
//    private var client = HttpClient {
//        install(DefaultRequest) {
//            headers.append("Accept", "application/json")
//        }
//
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.ALL
//        }
////        install(JsonFeature) {
////            //serializer =
////            serializer = KotlinxSerializer(Json.nonstrict).apply {
////                // Перечисляем, какие именно варианты у нас могут быть с generic data параметр
////                //register<Device>()
////                register(CommonResponse.serializer(User.serializer()))
////                register(CommonResponse.serializer(Device.serializer()))
////            }
////        }
//    }

    /**
     * Метод запроса к серверу
     * @param method тип запроса
     * @param settings настройки для данных key-value
     * @param url адрес запроса к API (без указания базового адреса)
     * @param params список параметров запроса
     * @param kotlinxSerializer json парсер данных с сервера
     * @param T тип объекта, который ждем при отклике с сервера
     */
    private suspend fun <T> makeRequest(
        method: MethodType,
        settings: Settings,
        url: String,
        params: HashMap<String, String>,
        kotlinxSerializer: KotlinxSerializer
    ): CommonResponse<T>? {
        // Здесь выбираем тип запроса
        var data: CommonResponse<T>? = null

        val client = getHttpClient(kotlinxSerializer)
        // TODO: Здесь надо дождаться ответа с GitHub, как правильно сделать подключение сериализации
        when (method) {
            MethodType.GET -> {
                // Используем use, тобы однократно воспользоваться и закрыть
                client.use {
                    data = it.get("$BASE_ADDRESS$url") {
                        // Добавляем параметры
                        for (set in params) {
                            parameter(set.key, set.value)
                        }
                        // добавляем подпись параметра
                        parameter("Sign", HashEncoder.addSign(params, settings))
                    }
                }

            }
            MethodType.POST -> {
                client.use {
                    data = it.post("$BASE_ADDRESS$url") {
                        // Добавляем параметры
                        for (set in params) {
                            parameter(set.key, set.value)
                        }
                        // добавляем подпись параметра
                        parameter("Sign", HashEncoder.addSign(params, settings))
                    }
                }
            }
            MethodType.PUT -> {
                client.use {
                    data = it.put("$BASE_ADDRESS$url") {
                        // Добавляем параметры
                        for (set in params) {
                            parameter(set.key, set.value)
                        }
                        // добавляем подпись параметра
                        parameter("Sign", HashEncoder.addSign(params, settings))
                    }
                }
            }
        }
        return data
    }

    /**
     * Метод получения HttpClient, живет он в пределах одного использования
     * @param kotlinxSerializer json парсер данных с сервера
     */
    private fun getHttpClient(kotlinxSerializer: KotlinxSerializer): HttpClient {
        return HttpClient {
            install(DefaultRequest) {
                headers.append("Accept", "application/json")
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = kotlinxSerializer
            }
        }
    }

    /**
     * Метод регистрации и получения privateKey
     * @param authDeviceID идентификатор устройства, записан в виде uuid в хранилище приложения
     * @param client тип устройства
     * @param settings доступ к хранилищу типа key-value
     */
    override suspend fun postDeviceID(
        authDeviceID: String,
        client: ClientType,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<Device>? {
        // Задаем набор параметров
        val paramsMap = HashMap<String, String>()
        paramsMap["AuthDeviceID"] = authDeviceID
        paramsMap["Client"] = client.type

        //инициализируем сериализатор под наши нужды
        val serializer = KotlinxSerializer(Json.nonstrict).apply {
            // Перечисляем, какие именно варианты у нас могут быть с generic data параметр
            register(CommonResponse.serializer(Device.serializer()))
        }

        // Вызываем новоиспеченный метод
        return makeRequest(methodType, settings, POST_DEVICE_API, paramsMap, serializer)
    }

    override fun getEvents(
        dateFrom: String,
        dateTo: String,
        city: ArrayList<String>,
        categories: ArrayList<Int>,
        price: String,
        favourite: Int,
        deviceID: String,
        settings: Settings,
        methodType: MethodType
    ) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getCategories(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<ArrayList<CategoryEvent>>? {

        // Собираем в кучу параметры
        val params = HashMap<String, String>()
        params["AuthDeviceID"] = authDeviceID

        // инициализируем сериализатор под наши нужды
        val serializer = KotlinxSerializer(Json.nonstrict).apply {
            // Регистрируем список тегов
            register(CommonResponse.serializer(CategoryEvent.serializer().list))
        }
        return makeRequest(methodType, settings, GET_CATEGORIES_API, params, serializer)
    }

    /**
     * Получаем пользователя
     */
    override suspend fun getCurrentUser(
        authDeviceID: String,
        settings: Settings,
        methodType: MethodType
    ): CommonResponse<User>? {
        val paramsMap = HashMap<String, String>()
        paramsMap["AuthDeviceID"] = authDeviceID

        // инициализируем сериализатор под наши нужды
        val serializer = KotlinxSerializer(Json.nonstrict).apply {
            // Перечисляем, какие именно варианты у нас могут быть с generic data параметр
            register(CommonResponse.serializer(User.serializer()))
        }

        return makeRequest(methodType, settings, GET_CURRENT_USER_API, paramsMap, serializer)
    }
}