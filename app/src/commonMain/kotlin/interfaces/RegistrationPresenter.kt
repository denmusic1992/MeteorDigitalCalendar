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
     */
    fun registerUser(credentials: RegistrationCredentials)

    /**
     * Авторизация пользователя
     */
    fun authorizeUser(credentials: RegistrationCredentials)
}