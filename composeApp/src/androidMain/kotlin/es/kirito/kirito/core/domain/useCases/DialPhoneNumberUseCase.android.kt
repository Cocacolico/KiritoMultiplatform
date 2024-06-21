package es.kirito.kirito.core.domain.useCases

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import es.kirito.kirito.core.domain.error.IntentNoAppError
import es.kirito.kirito.core.domain.error.Result
import es.kirito.kirito.applicationContext

actual fun dialPhoneNumberUseCase(phoneNumber: String): Result<Unit, IntentNoAppError> {

    val intent = Intent(Intent.ACTION_DIAL).apply {
        flags = FLAG_ACTIVITY_NEW_TASK
        data = Uri.parse("tel:$phoneNumber")
    }
    //AQUÍ PUEDE DARME UNA EXCEPCIÓN SI NO HAY APP DE TELÉFONO INSTALADA.
    return try {
        applicationContext.startActivity(intent)
        Result.Success(Unit)
    } catch (ex: ActivityNotFoundException) {
        Result.Error(IntentNoAppError.NO_APPLICATION_AVAILABLE)
    }catch (e: Exception){
        Result.Error(IntentNoAppError.UNKNOWN)
    }
}