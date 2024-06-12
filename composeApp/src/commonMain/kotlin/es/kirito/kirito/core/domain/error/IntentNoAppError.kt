package es.kirito.kirito.core.domain.error

import es.kirito.kirito.core.domain.error.GenericError

enum class IntentNoAppError: GenericError {
    NO_APPLICATION_AVAILABLE,
    UNKNOWN
}