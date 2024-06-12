package es.kirito.kirito.core.domain.useCases

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import es.kirito.kirito.core.domain.error.IntentNoAppError
import es.kirito.kirito.core.domain.error.Result
import es.kirito.kirito.applicationContext

actual fun abrirWhatsappUseCase(telefono: String): Result<Unit, IntentNoAppError> {

    var url = "https://api.whatsapp.com/send?phone=34"
    url += telefono

    val openWhatsappIntent = Intent(Intent.ACTION_VIEW)
    openWhatsappIntent.data = Uri.parse(url)

    // Checking whether whatsapp is installed or not
    return try {
        applicationContext.startActivity(openWhatsappIntent)
        Result.Success(Unit)
    } catch (ex: ActivityNotFoundException) {
        Result.Error(IntentNoAppError.NO_APPLICATION_AVAILABLE)
    }catch (ex: Exception) {
        Result.Error(IntentNoAppError.UNKNOWN)
    }
}