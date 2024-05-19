package es.kirito.kirito.core.data.utils

open class KiritoException(message: String? = null, cause: Throwable? = null) : Exception(message, cause){
    constructor(cause: Throwable) : this(null, cause)
}
open class KiritoUserException(message: String? = null) : KiritoException(message)

class KiritoNoTokenException(message: String? = null) : KiritoException(message)
class KiritoWrongTokenException(message: String? = null) : KiritoException(message)
class KiritoOldVersionException(message: String? = null) : KiritoException(message)
class KiritoRegisterException(message: String? = null) : KiritoException(message)
class KiritoUserBlockedException(message: String? = null) : KiritoUserException(message)
class KiritoWarningException(message: String? = null) : KiritoException(message)
class KiritoCambiosException(message: String? = null): KiritoException(message)

class KiritoFileProcessingException(message: String? = null): KiritoException(message)