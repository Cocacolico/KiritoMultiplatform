package es.kirito.kirito.precarga.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultsDTO(
    @SerialName("address_components") var addressComponents: ArrayList<AddressComponentsDTO> = arrayListOf(),
    @SerialName("formatted_address") var formattedAddress: String? = null,
    @SerialName("geometry") var geometry: GeometryDTO? = GeometryDTO(),
    @SerialName("partial_match") var partialMatch: Boolean? = null,
    @SerialName("place_id") var placeId: String? = null,
    @SerialName("types") var types: ArrayList<String> = arrayListOf()
)
