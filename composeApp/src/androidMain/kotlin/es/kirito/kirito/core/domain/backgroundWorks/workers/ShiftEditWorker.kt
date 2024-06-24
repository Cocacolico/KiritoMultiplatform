package es.kirito.kirito.core.domain.backgroundWorks.workers


import android.content.Context
import android.util.MalformedJsonException
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import es.kirito.kirito.core.data.utils.KiritoException
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.turnos.domain.TurnosRepository
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class ShiftEditWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {
    companion object {
        const val Progress = "Progress"
        const val WORK_SHIFT_EDIT = "es.kirito.kirito.workers.ShiftEditWorker"
    }
    override suspend fun doWork(): Result {
        val turnosRepo: TurnosRepository by inject()
        val coreRepo: CoreRepository by inject()

        val firstUpdate = workDataOf(Progress to 5)
        val retryUpdate = workDataOf(Progress to 99)///RETRY
        val errorUpdate = workDataOf(Progress to 98)//FAILURE
        val lastUpdate = workDataOf(Progress to 100)//SUCCESS
        setProgress(firstUpdate)//Inicio el progress, al 0%.

        val turnoEditado = TurnoPrxTr(
            idDetalle = inputData.getLong("id_detalle", 0),
            idUsuario = 0,
            fecha = inputData.getLong("fecha", 0),
            turno = inputData.getString("turno")!!,
            tipo = inputData.getString("tipo")!!,
            notas = inputData.getString("notas"),
            nombreDebe = inputData.getString("nombre_debe")?.trim(),
            idGrafico = 0,
            sitioOrigen =null,
            horaOrigen = null,
            sitioFin = null,
            horaFin = null,
            diaSemana = "",
            libra = inputData.getInt("LIBRa", 0),
            comj = inputData.getInt("COMJ", 0),
            indicador = 0,
            equivalencia = "",
            color = 0,
        )
        println("Turno: $turnoEditado, ${turnoEditado.fecha.toLocalDate()}")

        try {
            println("Iniciando workRequest.")
            coreRepo.editarTurno(turnoEditado)
        }
        catch (e: Exception){
            when(e){
                is MalformedJsonException ->{
                    return if (runAttemptCount > 3)
                        Result.failure()
                    else
                        Result.retry()
                }
                /** MALFORMED JSON lo dejo como retry, para que puedan enviarse varias veces. Comprobar que no da mil errores GROTESCOS.*/
                is KiritoException ->{
                   // FirebaseCrashlytics.getInstance().recordException(e)
                    //TODO: Crashlytics y firebase
                    setProgress(errorUpdate)
                    println("El error es ${e.message}")

                    return Result.failure()
                }
                else ->{//Otros tipos de excepciones.
                    setProgress(retryUpdate)//Termino el progress al 100%.
                    println(e.cause.toString())
                    println("El error es ${e.message}")
                    return Result.retry()
                }
            }
        }

        setProgress(lastUpdate)
        return Result.success()
    }
}