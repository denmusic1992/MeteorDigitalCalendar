package helpers

import com.russhwolf.settings.Settings
import com.soywiz.krypto.MD5
import kotlinx.serialization.toUtf8Bytes

/**
 * Здесь собраны методы для создания хеша, используемого в запросах
 */
@Suppress("EXPERIMENTAL_API_USAGE")
object HashEncoder {
    /**
     * Метод добавления подписи для валидного запроса
     * @param params параметры в методе
     * @param
     * @return аргумент, добавляющийся в конец запроса
     */
    internal fun addSign(params: ArrayList<Pair<String, Any>>, settings: Settings): String {
        // Здесь, как я понял собираем в правильном формате параметры запроса
        val paramList = arrayListOf<String>()
        for (entry in params) {
            // Для подписи не нужны значения из массива
            if (!entry.first.contains("[") && !entry.first.contains("%5B"))
                paramList.add("${entry.first}=${entry.second}")
        }
        val strWithKey = "${implode(params = paramList)}${Storage.getPrivateKey(settings)}"
        val signParam = getMD5Hash(strWithKey)
        println("signed parameter = $signParam")
        return signParam
    }

    /**
     * Функция, возвращающая все переменные в одну строку и разделенные разделителем, по умолчанию "&"
     * @param separator разделитель, по умолчанию "&"
     * @param params параметры запроса
     * @return строка, содержащая все параметры с разделителем
     */
    private fun implode(separator: String = "&", params: ArrayList<String>): String {
        val str = StringBuilder()
        for (i in 0 until params.size) {
            str.append(if (i == params.size - 1) params[i] else "${params[i]}$separator")
        }
        return str.toString()
    }

    /**
     * Функция, возвращающая хеш md5 из переданной строки
     * @param stringToHash строка, по которой нужно получить хеш
     * @return md5 хеш
     */
    fun getMD5Hash(stringToHash: String): String {
        val md5 = MD5.create()
        md5.update(stringToHash.toUtf8Bytes())
        val messageDigest = md5.digest()

        // Создаем hex string
        val hexString = StringBuilder()
        for (digest: Byte in messageDigest) {
            var h = (digest.toInt() and 0xFF).toUInt().toString(16)
            while (h.length < 2)
                h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    }
}