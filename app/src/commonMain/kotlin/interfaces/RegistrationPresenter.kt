package interfaces

import models.RegistrationCredentials

/**
 * Интерфейс для регистрации устройства и пользователя
 */
interface RegistrationPresenter {
    /**
     * Регистрация
     */
    fun registerDevice()

    /**
     * Авторизация пользователя
     */
    fun authorizeUser()

    /**
     * Регистрация пользователя
     * @param credentials данные для регистрации
     */
    fun registerUser(credentials: RegistrationCredentials)

    /**
     * Войти пользователю с данными
     * @param credentials данные для регистрации (здесь используются только поля email и password)
     */
    fun loginUser(credentials: RegistrationCredentials)

    /**
     * Сбросить пароль
     * @param credentials данные для регистрации (здесь используется только поле email)
     */
    fun emailResendData(credentials: RegistrationCredentials)
}