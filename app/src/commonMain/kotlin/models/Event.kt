package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.Transient

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
    val eventId: Int?,
    @SerialName("Title")
    val title: String?,
    @SerialName("Organizer")
    val organizer: String?,
    @SerialName("Speakers")
    val speakers: String?,
    @SerialName("CityID")
    val cityID: Int?,
    @SerialName("CityTitle")
    val cityTitle: String?,
    @SerialName("Location")
    val location: String?,
    @SerialName("Address")
    val address: String?,
    @SerialName("DateFrom")
    val dateFrom: String?,
    @SerialName("DateTo")
    val dateTo: String?,
    @SerialName("URL")
    val url: String?,
    @SerialName("RegistrationURL")
    val registrationURL: String?,
    @SerialName("ProgramURL")
    val programURL: String?,
    @SerialName("PriceFrom")
    val priceFrom: Int?,
    @SerialName("PriceTo")
    val priceTo: Int?,
    @SerialName("Favourite")
    val favourite: String?,
    @SerialName("FirstEvent")
    @Transient
    val isFirstElement: Boolean? = false,
    @SerialName("LastEvent")
    @Transient
    val isLastElement: Boolean? = false,
    @SerialName("CategoryList")
    val categoryEvents: Array<CategoryEvent>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Event

        if (eventId != other.eventId) return false
        if (title != other.title) return false
        if (organizer != other.organizer) return false
        if (speakers != other.speakers) return false
        if (cityID != other.cityID) return false
        if (cityTitle != other.cityTitle) return false
        if (location != other.location) return false
        if (address != other.address) return false
        if (dateFrom != other.dateFrom) return false
        if (dateTo != other.dateTo) return false
        if (url != other.url) return false
        if (registrationURL != other.registrationURL) return false
        if (programURL != other.programURL) return false
        if (priceFrom != other.priceFrom) return false
        if (priceTo != other.priceTo) return false
        if (favourite != other.favourite) return false
        if (isFirstElement != other.isFirstElement) return false
        if (isLastElement != other.isLastElement) return false
        if (categoryEvents != null) {
            if (other.categoryEvents == null) return false
            if (!categoryEvents.contentEquals(other.categoryEvents)) return false
        } else if (other.categoryEvents != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = eventId ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (organizer?.hashCode() ?: 0)
        result = 31 * result + (speakers?.hashCode() ?: 0)
        result = 31 * result + (cityID ?: 0)
        result = 31 * result + (cityTitle?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (dateFrom?.hashCode() ?: 0)
        result = 31 * result + (dateTo?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (registrationURL?.hashCode() ?: 0)
        result = 31 * result + (programURL?.hashCode() ?: 0)
        result = 31 * result + (priceFrom ?: 0)
        result = 31 * result + (priceTo ?: 0)
        result = 31 * result + (favourite?.hashCode() ?: 0)
        result = 31 * result + (isFirstElement?.hashCode() ?: 0)
        result = 31 * result + (isLastElement?.hashCode() ?: 0)
        result = 31 * result + (categoryEvents?.contentHashCode() ?: 0)
        return result
    }
}