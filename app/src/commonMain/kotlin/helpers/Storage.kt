package helpers

import com.benasher44.uuid.uuid4
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import models.User

/**
 *
 */
object Storage {

    //TODO: Нужно что то придумать с настройками Settings, куда нибудь их перенести, но т.к.
    // реализация для каждой платформы своя, а actual функцию без получения статики getPreferences
    // не получить, оставим до лучших времен

    /**
     * Метод получения пользователя из preferences
     * @param settings параметры preferences (у android - SharedPreferences, у ios - NSUserDefaults
     * @return сохраненный в приложении пользователь
     */
    fun getCurrentUser(settings: Settings): User? {
        // Инициализируем Json, чтобы парсить
        val json = Json(JsonConfiguration.Stable)
        // считываем
        val userJson = settings.getStringOrNull(CURRENT_USER_KEY)
        // если есть, то парсим и возвращаем
        if(userJson != null) {
            val user = json.parse(User.serializer(), userJson)
            println(user.toString())
            return user
        }
        // в противном случае у нас нет пользователя, возвращаем null
        return null
    }

    /**
     * Задать пользователя для приложения
     * @param settings параметры preferences (у android - SharedPreferences, у ios - NSUserDefaults
     */
    fun setCurrentUser(settings: Settings, user: User) {
        // Инициализируем Json, чтобы парсить
        val json = Json(JsonConfiguration.Stable)
        // Конвертим пользователя в json и записываем
        val userJson = json.stringify(User.serializer(), user)
        settings.putString(CURRENT_USER_KEY, userJson)
    }

    /**
     * Удалить пользователя
     * @param settings параметры preferences (у android - SharedPreferences, у ios - NSUserDefaults
     */
    fun removeCurrentUser(settings: Settings) {
        settings.remove(CURRENT_USER_KEY)
    }

    /**
     * Получить значение UUID, присвоенного пользователю
     * @param settings параметры preferences (у android - SharedPreferences, у ios - NSUserDefaults
     * @return id пользователя
     */
    fun getDeviceID(settings: Settings): String {
        // Если в преференсах нет значения, то генерируем его и записываем, и возвращаем
        var deviceID = settings.getStringOrNull(DEVICE_ID_KEY)
        if(deviceID == null) {
            deviceID = uuid4().toString()
            settings.putString(DEVICE_ID_KEY, deviceID)
        }
        return deviceID
    }

    /**
     * Получить ключ для кодирования
     * @param settings параметры preferences (у android - SharedPreferences, у ios - NSUserDefaults
     * @return тот самый ключ или null
     */
    fun getPrivateKey(settings: Settings): String? {
        return settings.getStringOrNull(PRIVATE_KEY)
    }

    /**
     * Задать ключ для кодирования
     * @param settings параметры preferences (у android - SharedPreferences, у ios - NSUserDefaults
     * @param privateKey ключ, полученный с сервера
     */
    fun setPrivateKey(settings: Settings, privateKey: String) {
        settings.putString(PRIVATE_KEY, privateKey)
    }

    //region Store keys
    private const val CURRENT_USER_KEY = "CurrentUser"
    private const val DEVICE_ID_KEY = "deviceID"
    private const val PRIVATE_KEY = "privateKey"
    //endregion
}