package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Модель, описывающая пользователя
 * TODO: добавить описание переменных
 */
@Serializable
data class User(
    @SerialName("UserID")
    val userId: String?,
    @SerialName("Email")
    val email: String?,
    @SerialName("Name")
    val name: String?,
    @SerialName("UserImage")
    val userImage: String?,
    @SerialName("Phone")
    val phone: String?,
    @SerialName("Role")
    val role: String?,
    @SerialName("WebsiteID")
    val websiteId: String?,
    @SerialName("Created")
    @Transient
    val created: String? = null,
    @SerialName("LastLogin")
    @Transient
    val lastLogin: String? = null,
    @SerialName("LastIP")
    @Transient
    val lastIp: String? = null,
    @SerialName("RoleTitle")
    @Transient
    val roleTitle: String? = null,
    @SerialName("PushNew")
    val pushNew: String?,
    @SerialName("PushFavourites")
    val pushFavourites: String?
){
    override fun toString(): String {
        return "User profile: $name, $phone, website: $websiteId, last logged: $lastLogin"
    }
}