package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyDTO(
    @SerialName("time") var time: ArrayList<String> = arrayListOf(),
    @SerialName("temperature_2m") var temperature2m: ArrayList<Float> = arrayListOf(),
    @SerialName("precipitation_probability") var precipitationProbability: ArrayList<Int> = arrayListOf(),
    @SerialName("rain") var rain: ArrayList<Float> = arrayListOf(),
    @SerialName("snowfall") var snowfall: ArrayList<Float> = arrayListOf(),
    @SerialName("cloud_cover") var cloudCover: ArrayList<Int> = arrayListOf(),
    @SerialName("visibility") var visibility: ArrayList<Int> = arrayListOf(),
    @SerialName("wind_speed_10m") var windSpeed: ArrayList<Float> = arrayListOf(),
)
