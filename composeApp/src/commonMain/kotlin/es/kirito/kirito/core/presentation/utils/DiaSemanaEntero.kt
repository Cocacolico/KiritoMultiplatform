package es.kirito.kirito.core.presentation.utils

import androidx.compose.runtime.Composable
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.full_days
import org.jetbrains.compose.resources.stringArrayResource

@Composable
fun String?.diaSemanaEntero(): String {
    return when (this) {
        "L" -> stringArrayResource(Res.array.full_days)[0]
        "M" -> stringArrayResource(Res.array.full_days)[1]
        "X" -> stringArrayResource(Res.array.full_days)[2]
        "J" -> stringArrayResource(Res.array.full_days)[3]
        "V" -> stringArrayResource(Res.array.full_days)[4]
        "S" -> stringArrayResource(Res.array.full_days)[5]
        "D" -> stringArrayResource(Res.array.full_days)[6]
        "F" -> stringArrayResource(Res.array.full_days)[7]
        else -> ""
    }

}