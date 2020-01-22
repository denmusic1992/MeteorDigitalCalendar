package enums

/**
 * Перечисление платформ, которые используются в приложении
 * @param type возвращаемый тип платформы
 */
enum class ClientType(val type: String) {
    Android("android"),
    iOS("iOS")
}