package es.kirito.kirito.core.domain.useCases

import es.kirito.kirito.core.domain.error.IntentNoAppError
import es.kirito.kirito.core.domain.error.Result


expect fun abrirWhatsappUseCase(telefono: String): Result<Unit, IntentNoAppError>
