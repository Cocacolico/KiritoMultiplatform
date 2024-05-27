package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressComponentsDTO(
    @SerialName("long_name") var longName: String? = null,
    @SerialName("short_name") var shortName: String? = null,
    @SerialName("types") var types: ArrayList<String> = arrayListOf()
)