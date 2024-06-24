@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package es.kirito.kirito.core.domain.backgroundWorks

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import es.kirito.kirito.applicationContext
import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.domain.backgroundWorks.workers.ShiftEditWorker
import es.kirito.kirito.core.domain.backgroundWorks.workers.ShiftEditWorker.Companion.WORK_SHIFT_EDIT

actual fun enqueueEditShiftBackgroundWork(turno: CuDetalle) {

        val workManager = WorkManager.getInstance(applicationContext)
        val worker = OneTimeWorkRequest.Builder(ShiftEditWorker::class.java)

        val data = Data.Builder()
        with(turno) {
            data.putLong("id_detalle", idDetalle)
            data.putLong("fecha", fecha)
            data.putString("turno", turno.turno)
            data.putString("tipo", tipo)
            data.putString("notas", notas)
            data.putString("nombre_debe", nombreDebe)
            data.putInt("LIBRa", libra ?: 0)
            data.putInt("COMJ", comj ?: 0)

        }
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        worker
            .addTag(WORK_SHIFT_EDIT)
            .setInputData(data.build())
            .setConstraints(constraints)
            .build()
        workManager.enqueue(worker.build())

    }
