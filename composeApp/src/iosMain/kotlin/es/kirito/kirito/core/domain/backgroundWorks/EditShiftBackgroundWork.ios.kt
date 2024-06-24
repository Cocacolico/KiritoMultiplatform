package es.kirito.kirito.core.domain.backgroundWorks

import es.kirito.kirito.core.data.database.CuDetalle


actual fun enqueueEditShiftBackgroundWork(turno: CuDetalle) {
    TODO("Héctor, te toca hacer esto por detrás!" +
            "Ánimo!")
    /**
     * Te explico lo que tienes que hacer:
     *  Llama a esta función del core Repo.
     *    coreRepo.editarTurno(turnoEditado)
     *
     * Para ello necesitarás transformar el CuDetalle en un TurnoPrxTr, en Android lo hago así:
     *   val turnoEditado = TurnoPrxTr(
     *             idDetalle = inputData.getLong("id_detalle", 0),
     *             idUsuario = 0,
     *             fecha = inputData.getLong("fecha", 0),
     *             turno = inputData.getString("turno")!!,
     *             tipo = inputData.getString("tipo")!!,
     *             notas = inputData.getString("notas"),
     *             nombreDebe = inputData.getString("nombre_debe")?.trim(),
     *             idGrafico = 0,
     *             sitioOrigen =null,
     *             horaOrigen = null,
     *             sitioFin = null,
     *             horaFin = null,
     *             diaSemana = "",
     *             libra = inputData.getInt("LIBRa", 0),
     *             comj = inputData.getInt("COMJ", 0),
     *             indicador = 0,
     *             equivalencia = "",
     *             color = 0,
     *         )
     *
     * Tiene las cosas esas raras de "inputData.getLong" que son para pasar al worker los diferentes datos.
     * Imagino que tú tendrás que hacer algo parecido, no lo sé.
     * Recuerda que para inyectar el coreRepo, la clase en la que estés debe heredar de KoinComponent o si no
     * el by inject() no funciona.
     *
     * **/
}