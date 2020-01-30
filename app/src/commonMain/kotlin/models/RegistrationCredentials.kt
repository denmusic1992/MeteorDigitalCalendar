package models

import helpers.RegexFabric

/**
 * Модель, описывающая регистрационные параметры
 * @param email почта пользователя
 * @param password пароль пользователя
 * @param confirmPassword подтверждение пароля
 * @param name имя пользователя
 * @param phone телефон пользователя
 */
data class RegistrationCredentials(
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var name: String = "",
    var phone: String = ""
) {

    /**
     * Проверка валидности пароля
     */
    fun isPasswordCorrect(): Boolean {
        return password.isNotEmpty() && password == confirmPassword
    }

    /**
     * Проверка валидности mail
     */
    fun isEmailValid(): Boolean {
        return email.isNotEmpty() && email.matches(RegexFabric.getRegex(RegexFabric.EMAIL))
    }

    /**
     * Проверка телефона на валидность
     */
    fun isPhoneValid(): Boolean {
        return phone.isNotEmpty() && phone.matches(RegexFabric.getRegex(RegexFabric.PHONE))
    }

    /**
     * Проверка валидности имени пользователя
     */
    fun isNameValid(): Boolean {
        return name.isNotEmpty()
    }

    /**
     * Проверка, может ли пользователь логиниться
     */
    fun canTryLogin(): Boolean {
        return isEmailValid() && password.isNotEmpty()
    }
}