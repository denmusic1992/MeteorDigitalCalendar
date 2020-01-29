package network

import com.russhwolf.settings.Settings
import enums.MethodType
import helpers.HashEncoder
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
import models.CommonResponse

/**
 * Класс, работающий с подключением к серверу
 */
// TODO: Не вижу смысла в обращении по интерфейсу, но пока оставим так
object RequestAPI {

    //region url staff
    // Адрес URL
    private const val BASE_ADDRESS = "http://mdc.fokgroup.com/api/"
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
     * @param params список параметров запроса (пришлось переделать в list pair, т.к. передаем массивы)
     * @param kotlinxSerializer json парсер данных с сервера
     * @param T тип объекта, который ждем при отклике с сервера
     */
    internal suspend fun <T> makeRequest(
        method: MethodType,
        settings: Settings,
        url: String,
        params: ArrayList<Pair<String, Any>>,
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
                            parameter(set.first, set.second)
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
                            parameter(set.first, set.second)
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
                            parameter(set.first, set.second)
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
}