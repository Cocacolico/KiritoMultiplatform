package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseWeatherInfoDTO(
    @SerialName("latitude") var latitude: Double? = null,
    @SerialName("longitude") var longitude: Double? = null,
    @SerialName("generationtime_ms") var generationtimeMs: Double? = null,
    @SerialName("utc_offset_seconds") var utcOffsetSeconds: Int? = null,
    @SerialName("timezone") var timezone: String? = null,
    @SerialName("timezone_abbreviation") var timezoneAbbreviation: String? = null,
    @SerialName("elevation") var elevation: Double? = null,
    @SerialName("hourly") var hourly: HourlyDTO? = HourlyDTO()
)
