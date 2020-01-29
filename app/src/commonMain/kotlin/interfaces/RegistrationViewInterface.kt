package interfaces

import enums.RegisterError

/**
 * Интерфейс для взаимдоействия registration presenter и view
 */
interface RegistrationViewInterface {
    /**
     * Результат при регистрации устройства
     * @param success успех или неудача
     * @param message что произошло
     */
    fun registerResult(success: Boolean, message: String? = null)

    /**
     * Результат при авторизации пользователя
     * @param message что произошло, для теста больше
     */
    fun authorizationResult(message: String? = null)

    /**
     * Произошла ошибка при регистрации
     * @param errors Список ошибок
     */
    fun registrationFailed(errors: ArrayList<RegisterError>, messageError: String? = null)

    /**
     * Регистрация прошла успешно
     */
    fun registrationSuccess()
}