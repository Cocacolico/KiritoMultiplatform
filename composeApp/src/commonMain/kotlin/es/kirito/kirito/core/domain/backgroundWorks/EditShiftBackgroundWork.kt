@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package es.kirito.kirito.core.domain.backgroundWorks

import es.kirito.kirito.core.data.database.CuDetalle

expect fun enqueueEditShiftBackgroundWork(turno: CuDetalle)
