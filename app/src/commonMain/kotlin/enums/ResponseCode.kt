package enums

/**
 * Перечисление возможных откликов с сервера
 * @param code код с сервера
 */
enum class ResponseCode(val code: Int) {
    Ok(200),
    Created(201),
    Accepted(202),
    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404),
    UnprocessableEntity(422),
    InternalServerError(500)
}