package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Модель, описывающая мероприятие
 * @param eventId идентификатор мероприятия
 * @param title тема мероприятия
 * @param organizer организатор мероприятия
 * @param speakers спикеры, кто выступает
 * @param cityID идентификатор города
 * @param cityTitle город
 * @param location место проведения
 * @param address адрес места проведения
 * @param dateFrom дата начала мероприятия
 * @param dateTo дата окончания мероприятия
 * @param url ссылка на мероприятие
 * @param registrationURL ссылка на регистрацию
 * @param programURL ссылка на программу мероприятия
 * @param priceFrom начальная цена
 * @param priceTo максимальная цена
 * @param favourite является ли избранным
 */
@Serializable
data class Event(
    @SerialName("EventID")
    val eventId: Int,
    @SerialName("Title")
    val title: String,
    @SerialName("Organizer")
    val organizer: String,
    @SerialName("Speakers")
    val speakers: String,
    @SerialName("CityID")
    val cityID: Int,
    @SerialName("CityTitle")
    val cityTitle: String,
    @SerialName("Location")
    val location: String,
    @SerialName("Address")
    val address: String,
    @SerialName("DateFrom")
    val dateFrom: String,
    @SerialName("DateTo")
    val dateTo: String,
    @SerialName("URL")
    val url: String,
    @SerialName("RegistrationURL")
    val registrationURL: String,
    @SerialName("ProgramURL")
    val programURL: String,
    @SerialName("PriceFrom")
    val priceFrom: Int,
    @SerialName("PriceTo")
    val priceTo: Int,
    @SerialName("Favourite")
    val favourite: String,
    @SerialName("FirstEvent")
    val isFirstElement: Boolean,
    @SerialName("LastEvent")
    val isLastElement: Boolean,
    @SerialName("CategoryList")
    val categoryEvents: ArrayList<CategoryEvent>
)