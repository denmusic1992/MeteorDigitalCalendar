package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Модель, описывающая устройство с ключом шифрования
 * @param privateKey - ключ шифрования
 */
@Serializable
data class Device (
    @SerialName("PrivateKey")
    val privateKey: String
) {
    override fun toString(): String {
        return "Device info: privateKey = $privateKey"
    }
}