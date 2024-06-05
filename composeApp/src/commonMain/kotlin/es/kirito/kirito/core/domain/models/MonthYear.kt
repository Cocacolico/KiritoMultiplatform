package es.kirito.kirito.core.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

data class MonthYear(
    val month: Month,
    val year: Int
) {
    fun toLocalDate(): LocalDate {
        return LocalDate(this.year,this.month,1)
    }
}
