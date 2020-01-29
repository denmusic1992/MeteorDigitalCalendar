package helpers

import kotlin.jvm.Synchronized

/**
 * Фабрика regex, по которой можно проверять валидность введенных данных
 * @sample RegexFabric.getRegex(RegexFabric.EMAIL)
 */
object RegexFabric {

    //region type of regex

    const val EMAIL: String = "emailRegex"
    const val URL: String = "urlRegex"
    const val PHONE: String = "phone"
    const val WORD_SPLIT: String = "wordRegex"

    //endregion

    /**
     * Метод получения regex
     * @param name тип regex
     * @return искомый regex
     */
    fun getRegex(name: String): Regex {
        return when (name) {
            WORD_SPLIT -> "[\\s ,;]+".toRegex()
//                EMAIL-> "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$".toRegex()
            EMAIL -> ".+@.{2,}\\..{2,}".toRegex()
            URL -> "(http://|https://|www.).{3,}".toRegex()
            PHONE -> "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b".toRegex()
            else -> "".toRegex()
        }
    }

}