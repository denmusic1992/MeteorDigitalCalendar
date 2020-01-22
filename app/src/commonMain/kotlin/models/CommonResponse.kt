package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Модель, поисывающая ответ с сервера в общем виде
 * @param status Статус ответа
 * @param code Код ответа
 * @param errorList список ошибок с сервера
 * @param messageList список сообщений с сервера
 * @param data данные с сервера
 */
@Serializable
data class CommonResponse <T> (
    @SerialName("Status")
    val status: String,
    @SerialName("Code")
    val code: Int,
    @SerialName("ErrorList")
    val errorList: List<String>?,
    @SerialName("MessageList")
    val messageList: List<String>?,
    @SerialName("Data")
    val data: T?
)