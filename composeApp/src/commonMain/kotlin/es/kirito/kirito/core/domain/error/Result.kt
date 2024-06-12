package es.kirito.kirito.core.domain.error


sealed interface Result<out D, out E: GenericError> {
    data class Success<out D, out E: GenericError>(val data: D): Result<D, E>
    data class Error<out D, out E: GenericError>(val error: E): Result<D, E>
}