package es.kirito.kirito.core.domain.kiritoError

class KiritoException( message: String? = null, cause: Throwable? = null) : Exception(message, cause){
    constructor(cause: Throwable) : this( null, cause)
}

