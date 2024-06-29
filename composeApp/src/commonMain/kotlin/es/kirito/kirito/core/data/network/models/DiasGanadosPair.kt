package es.kirito.kirito.core.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class DiasGanadosPair(
    val tipo: String,
    val ganados: String
)
