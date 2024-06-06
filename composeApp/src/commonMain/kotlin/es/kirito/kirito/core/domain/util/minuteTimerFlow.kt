package es.kirito.kirito.core.domain.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.asTimeSource

/**
 * Devuelve un flow que lanza un tic al invocarlo y luego cada vez que cambia el minuto. */
fun minuteTimerFlow(): Flow<Int> = flow {
    // Emit the current time (seconds) initially
    val currentSecs = (Clock.System.now().epochSeconds % 60).toInt()
    val initialDelay = 60 - currentSecs
    emit(initialDelay)

    // Delay for the remaining seconds of the current minute
    delay(initialDelay * 1000L)

    // Emit every 60 seconds thereafter
    while (true) {
        emit(60)
        delay(60000L) // 60 seconds in milliseconds
    }
}