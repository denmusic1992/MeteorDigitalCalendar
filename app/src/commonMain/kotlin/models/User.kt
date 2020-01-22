package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Модель, описывающая пользователя
 * TODO: добавить описание переменных
 */
@Serializable
data class User(
    @SerialName("UserID")
    val userId: String,
    @SerialName("Email")
    val email: String,
    @SerialName("Name")
    val name: String,
    @SerialName("UserImage")
    val userImage: String,
    @SerialName("Phone")
    val phone: String,
    @SerialName("Role")
    val role: String,
    @SerialName("WebsiteID")
    val websiteId: String,
    @SerialName("Created")
    val created: String,
    @SerialName("LastLogin")
    val lastLogin: String,
    @SerialName("LastIP")
    val lastIp: String,
    @SerialName("RoleTitle")
    val roleTitle: String,
    @SerialName("PushNew")
    val pushNew: String,
    @SerialName("PushFavourites")
    val pushFavourites: String
){
    override fun toString(): String {
        return "User profile: $name, $phone, website: $websiteId, last logged: $lastLogin"
    }
}