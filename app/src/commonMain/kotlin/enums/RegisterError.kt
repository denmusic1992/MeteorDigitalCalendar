package enums

/**
 * Возможные ошибки при попытке регистрации
 */
enum class RegisterError(val error: String) {
    EmailError("Неправильно введен e-mail!"),
    PasswordError("Неправильно введен пароль!"),
    NameError("Имя пользователя пустое!"),
    PhoneError("Телефон введен неправильно!"),
    RegistrationError("Ошибка при регистрации!")
}