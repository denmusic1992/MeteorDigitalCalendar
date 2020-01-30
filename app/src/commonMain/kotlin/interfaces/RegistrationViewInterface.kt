package interfaces

import enums.RegisterError

/**
 * Интерфейс для взаимдоействия registration presenter и view
 */
interface RegistrationViewInterface {
    /**
     * Результат при регистрации устройства
     * @param success успех или неудача
     * @param reason что произошло
     */
    fun registerResult(success: Boolean, reason: String? = null)

    /**
     * Результат при авторизации пользователя
     * @param success успех или неудача
     * @param reason что произошло, для теста больше
     * TODO: Быть может, лучше ещё передавать тип ошибки здесь, чтобы тот кто будет делать UI, мог понять что произошло, а не только ошибку словами
     */
    fun authorizationResult(success: Boolean, reason: String? = null)

    /**
     * Результат регистрации
     * @param success успех или неудача
     * @param errors Список ошибок
     * @param messageError ошибка с сервера, если есть
     */
    fun registrationResult(
        success: Boolean,
        errors: ArrayList<RegisterError>? = null,
        messageError: String? = null
    )

    /**
     * Результат сброса пароля
     * @param success успех или неудача
     * @param message что произошло, для теста больше
     */
    fun resendEmailResult(success: Boolean, message: String? = null)
}