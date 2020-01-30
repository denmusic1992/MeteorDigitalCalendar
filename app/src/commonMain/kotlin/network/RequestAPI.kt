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
import kotlinx.serialization.internal.UnitSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import models.*

/**
 * Класс, работающий с подключением к серверу
 */
// TODO: Не вижу смысла в обращении по интерфейсу, но пока оставим так
@Suppress("EXPERIMENTAL_API_USAGE")
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
     * @param T тип объекта, который ждем при отклике с сервера
     */
    internal suspend inline fun <reified T> makeRequest(
        method: MethodType,
        settings: Settings,
        url: String,
        params: ArrayList<Pair<String, Any>>
    ): CommonResponse<T>? {
        // Здесь выбираем тип запроса
        var data: CommonResponse<T>? = null

        // здесь умный метод получения сериализатора
        // решился на это, потому что надоело в каждом методе запроса писать одно и то же
        // по добавлению сериализатора
        val client = getHttpClient(getKotlinxSerializer<T>())
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
     * Получить сериализатор для http клиента
     * @param T json, описанный в моделях для запроса
     * TODO: надо подумать, оставить так или же лучше переделать
     */
    internal inline fun <reified T> getKotlinxSerializer(): KotlinxSerializer {
        // инициализируем сериализатор под наши нужды
        return KotlinxSerializer(Json.nonstrict).apply {
            // Перечисляем, какие именно варианты у нас могут быть с generic data параметр
            when (T::class) {
                User::class -> register(CommonResponse.serializer(User.serializer()))
                Device::class -> register(CommonResponse.serializer(Device.serializer()))
                CategoryEvent::class -> register(CommonResponse.serializer(CategoryEvent.serializer()))
                Array<CategoryEvent>::class -> register(CommonResponse.serializer(CategoryEvent.serializer().list))
                Event::class -> register(CommonResponse.serializer(Event.serializer()))
                Array<Event>::class -> register(CommonResponse.serializer(Event.serializer().list))
                Unit::class -> register(CommonResponse.serializer(UnitSerializer))
            }
        }
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