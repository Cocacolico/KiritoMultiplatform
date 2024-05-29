@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.turnos.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import es.kirito.kirito.core.domain.models.GrTarea
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoyScreen(navController: NavHostController) {
    val viewModel = koinViewModel<HoyViewModel>()

    Surface(Modifier.fillMaxSize()) {
        val dialogImageEga by viewModel.imagenEgaDialog.collectAsState(initial = null)
        var showDialogImageEga by rememberSaveable { mutableStateOf(false) }
        var showDialogExcesos by rememberSaveable { mutableStateOf(false) }
        var showDialogTarea by remember { mutableStateOf(false) } //NO saveable.
        var showEditDialog by rememberSaveable { mutableStateOf(false) }
        var showDatePicker by rememberSaveable { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()
        val datePickerConfirmEnabled = remember {
            derivedStateOf {
                datePickerState.selectedDateMillis != null
            }
        }
        var textExcesos by rememberSaveable { mutableStateOf("") }
        var tarea by remember { mutableStateOf(GrTarea()) }
        val iHaveShiftsShared by viewModel.iHaveShiftsShared.collectAsState(initial = false)
        var nowTimeString by remember { mutableStateOf("") }
        val cuDetalle by viewModel.cuDetalleDelTurno.collectAsState(initial = null)
        val tareas by viewModel.tareas.collectAsState(initial = emptyList())
        val colorMatrix = floatArrayOf( //Es la matriz necesaria para invertir los colores.
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        )
        val orientation = LocalConfiguration.current.orientation
        val novedades by viewModel.novedades.collectAsStateWithLifecycle(initialValue = false)
        val showLocalizadorDialog by viewModel.showLocalizadorDialog.collectAsState(initial = false)
        val localizador by viewModel.localizador.collectAsState(initial = null)

        LaunchedEffect(Unit) {
            while (true) {
                nowTimeString = LocalTime.now().toStringWS()
                delay(1.seconds)
            }
        }
        LaunchedEffect(Unit) {
            viewModel.toastFestivo.collect { show ->
                if (show)
                    context.showLongToast(context.getString(R.string.advertencia_festivos))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectSwipe(
                        onSwipeLeft = {
                            viewModel.onNextDayClick(1)
                        },
                        onSwipeRight = {
                            viewModel.onPreviousDayClick(1)
                        }
                    )
                }, contentAlignment = Alignment.BottomEnd
        ) {
            when (orientation) {
                Configuration.ORIENTATION_LANDSCAPE ->
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            HoyHeader(viewModel, onDateClicked = {
                                showDatePicker = true
                            }) {
                                showDialogImageEga = true
                            }
                            HoyBody(viewModel, onTareaClick = { tareaClicada ->
                                tarea = tareaClicada
                                showDialogTarea = true
                            }, onExcesosClick = { texto ->
                                textExcesos = texto
                                showDialogExcesos = true
                            }, onGenerarCuadroClick = {
                                viewModel.onGenerarCuadroClick()
                            },
                                onGenerarCuadroVacioClick = {
                                    viewModel.onGenerarCuadroVacioClick()
                                })
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(16.dp)
                        ) {
                            if (tareas.isNotEmpty())
                                LazyColumn {
                                    items(tareas) { thisTarea ->
                                        TareaConClima(
                                            tarea = thisTarea,
                                            Modifier.animateItemPlacement()
                                        ) { estaTarea ->
                                            tarea = estaTarea
                                            showDialogTarea = true

                                        }
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(vertical = 32.dp))
                                    }
                                }
                        }
                    }

                else ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        HoyHeader(viewModel, onDateClicked = {
                            showDatePicker = true
                        }) {
                            showDialogImageEga = true
                        }
                        HoyBody(viewModel, onTareaClick = { tareaClicada ->
                            tarea = tareaClicada
                            showDialogTarea = true
                        }, onExcesosClick = { texto ->
                            textExcesos = texto
                            showDialogExcesos = true
                        }, onGenerarCuadroClick = {
                            viewModel.onGenerarCuadroClick()
                        },
                            onGenerarCuadroVacioClick = {
                                viewModel.onGenerarCuadroVacioClick()
                            })
                    }
            }

            Box(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyTextStd(
                        text = nowTimeString,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .shadow(2.dp, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.onPrimary)
                            .padding(horizontal = 8.dp, vertical = 2.dp)

                    )
                    FloatingActionButton(
                        onClick = { showEditDialog = true },
                        contentColor = MaterialTheme.colorScheme.background,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = ""
                        )
                    }
                }
            }

            //Dibujo de las vacaciones.
            if (cuDetalle?.tipo == "VL" || cuDetalle?.tipo == "VB") {
                val estacion = cuDetalle?.fecha?.toLocalDate()?.getSeason()
                Image(
                    painter = painterResource(
                        id =
                        when (estacion) {
                            Season.Spring -> R.drawable.spring1
                            Season.Summer -> R.drawable.summer1
                            Season.Autumn -> R.drawable.autumn1
                            Season.Winter -> R.drawable.winter1
                            Season.None -> R.drawable.empty_24
                            null -> R.drawable.empty_24
                        }
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter =
                    if (isSystemInDarkTheme())
                        ColorFilter.colorMatrix(
                            ColorMatrix(colorMatrix)
                        )
                    else
                        ColorFilter.colorMatrix(ColorMatrix())
                )
            }
        }

        if (novedades) {
            NovedadesDialog {
                viewModel.onNovedadShown()
            }
        }

        if (showDialogImageEga && dialogImageEga?.imagenesEga != null) {
            ImageEgaDialog(dialogImageEga ?: return@Surface) {
                showDialogImageEga = false
            }
        }

        MyDialogInformation(show = showDialogExcesos, text = textExcesos,
            onDismiss = { showDialogExcesos = false })
        { showDialogExcesos = false }

        if (showDatePicker) {
            DatePickerDialog(onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        viewModel.onDateSelected(
                            ((datePickerState.selectedDateMillis ?: 1000) / 1000).toLocalDateTime()
                                .toLocalDate()
                        )
                    }, enabled = datePickerConfirmEnabled.value) {
                        Text(text = stringResource(id = R.string.seleccionar))
                    }
                }, dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(text = stringResource(id = R.string.cancelar))
                    }
                }) {
                DatePicker(state = datePickerState)
            }
        }

        if (showDialogTarea)
            DialogClimaTarea(
                tarea,
                onDismiss = { showDialogTarea = false },
                onSearch = {
                    showDialogTarea = false
                    viewModel.onSearchClick(tarea)
                })

        if (showEditDialog)
            DialogEditShift(
                hasShiftsShared = iHaveShiftsShared,
                onDismiss = { showEditDialog = false },
                onBulkEditClick = {
                    showEditDialog = false
                    viewModel.onBulkEditClick()
                },
                onExcessClick = {
                    showEditDialog = false
                    viewModel.onExcessClick()
                },
                onEditClick = {
                    showEditDialog = false
                    viewModel.onEditClick()
                },
                onExchangeClick = {
                    showEditDialog = false
                    viewModel.onExchangeClick()
                })



        MyDialogInformation(
            show = showLocalizadorDialog,
            text = localizador?.localizador ?: "",
            fontSize = 26.sp,
            onDismiss = {
                viewModel.hideLocalizadorDialog()
            }) {
            viewModel.hideLocalizadorDialog()
        }

        LaunchedEffect(Unit) {
            viewModel.toastString.collect {
                if (it != null)
                    context.showLongToast(it)
            }
        }
    }
}


@Composable
fun HoyHeader(viewModel: HoyViewModel, onDateClicked: () -> Unit, onImageClicked: () -> Unit) {
    val context = LocalContext.current
    val dateString by viewModel.dateString.collectAsState()
    val cuDetalle by viewModel.cuDetalleDelTurno.collectAsState(initial = null)
    val turnoPrxTr by viewModel.turnoPrxTr.collectAsState(initial = null)
    val imagenEga by viewModel.imagenEga.collectAsState(initial = null)
    val festivo by viewModel.festivo.collectAsState(initial = "")

    HeaderWithPrevNext(
        title = dateString ?: "",
        festivo = festivo,
        onDateClick = { onDateClicked() },
        onPrevClick = { viewModel.onPreviousDayClick(1) },
        onNextClick = { viewModel.onNextDayClick(1) },
        onPrevLongClick = { viewModel.onPreviousDayClick(7) },
        onNextLongClick = { viewModel.onNextDayClick(7) },
        onFestivoClick = {
            context.showLongToast(festivo)
        }
    )
    if (cuDetalle != null)
        Text(
            text = cuDetalle?.tipoYTurnoText(turnoPrxTr, context)
                ?: "",
            Modifier
                .background(color = colorDeFondoTurnos(cuDetalle?.tipo ?: "").toComposeColor())
                .fillMaxWidth(),
            color = colorTextoTurnos(cuDetalle?.tipo ?: "").toComposeColor(),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    if (cuDetalle?.esTurnoConDias() == true)
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.onComjYLibraClick(context)
                }) {
            val COMJs = cuDetalle?.comj ?: 0
            val LIBRas = cuDetalle?.libra ?: 0
            if (COMJs > 0)
                LabelComj(COMJs)
            if (LIBRas > 0)
                LabelLibra(LIBRas)
        }
    if (turnoPrxTr?.sitio_origen != null || cuDetalle?.tipo == "7000")
        TurnoProxTarHeader(turno = turnoPrxTr, cuDetalle)

    if (imagenEga != null) {
        val scrollState = rememberScrollState()
        Image(
            bitmap = imagenEga?.genImage(context = context)?.asImageBitmap() ?: return,
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(4.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        viewModel.onImageClicked(
                            scrollState.value,
                            offset.x,
                            imagenEga ?: return@detectTapGestures,
                            context
                        )
                        onImageClicked()
                    }
                }

        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HoyBody(
    viewModel: HoyViewModel,
    onTareaClick: (GrTareasDomain) -> Unit,
    onExcesosClick: (String) -> Unit,
    onGenerarCuadroVacioClick: () -> Unit,
    onGenerarCuadroClick: () -> Unit,
) {
    val context = LocalContext.current
    val tareas by viewModel.tareas.collectAsState(initial = emptyList())
    val teleindicadores by viewModel.teleindicadores
        .collectAsState(initial = emptyList())
    val notasUsuario by viewModel.notasUsuario.collectAsState(initial = buildSpannedString { })
    val notasTurno by viewModel.notasTurno.collectAsState(initial = buildSpannedString { })
    val advMermasTurnosLaterales by viewModel.advMermasTurnosLaterales
        .collectAsState(initial = HoyViewModel.AdvmermasTurnosLaterales(false, null, null))
    val cuDetalle by viewModel.cuDetalleDelTurno.collectAsState(initial = null)
    val historial by viewModel.historial.collectAsState(initial = emptyList())
    val errores by viewModel.erroresHoy.collectAsState(initial = HoyViewModel.ErroresHoy())
    val localizador by viewModel.localizador.collectAsState(initial = null)
    val generarCuadroVacioClicked by rememberSaveable { mutableStateOf(false) }
    val selectedDate by viewModel.date.collectAsState(initial = null)
    var showErrors by remember { mutableStateOf(false) }
    val orientation = LocalConfiguration.current.orientation


    //Un pequeño delay, para que no se vean por instantes los errores.
    LaunchedEffect(selectedDate) {
        showErrors = false
        delay(300)
        showErrors = true
    }

    LazyColumn(Modifier.padding(horizontal = 16.dp)) {

        if (errores.hayErrores && showErrors)
            item { ErroresHoy(errores, cuDetalle, Modifier.animateItemPlacement()) }

        if (cuDetalle == null && showErrors) {
            item {
                Button(onClick = {
                    generarCuadroVacioClicked != generarCuadroVacioClicked
                    onGenerarCuadroVacioClick()
                }, enabled = !generarCuadroVacioClicked) {
                    Text(text = stringResource(R.string.generar_cuadro_anual_con_claves_vac_as))
                }
                OutlinedButton(onClick = { onGenerarCuadroClick() }) {
                    Text(text = stringResource(R.string.otras_opciones))
                }
            }
        }

        //Solo mostramos aquí las tareas si estamos en vertical.
        if (tareas.isNotEmpty() && orientation == Configuration.ORIENTATION_PORTRAIT)
            items(tareas) { tarea ->
                TareaConClima(tarea = tarea, Modifier.animateItemPlacement()) { estaTarea ->
                    onTareaClick(estaTarea)
                }
            }
        if (cuDetalle?.tipo?.esTipoTurnoCambiable(context) == true && cuDetalle?.nombre_debe.isNotNullNorBlank())
            item {
                HorizontalDivider(Modifier.padding(vertical = 4.dp))
                MyTextStd(
                    text = genNombreTextView(
                        cuDetalle?.toTurnoPrxTr() ?: TurnosPrxTr(),//Prefiero algo vacío que los !!.
                        context
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        if (teleindicadores.size > 1) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                ParagraphSubtitle(text = stringResource(id = R.string.teleindicadores_))
            }

            itemsIndexed(teleindicadores) { index, teleindicadoresDeTren ->
                val gridWidth = with(LocalDensity.current) {
                    (teleindicadoresDeTren.stringLength * (8.sp).toDp()) + 8.dp
                }
                Row(
                    modifier = Modifier
                        .background(
                            color = if (index % 2 == 0)
                                Color.Transparent
                            else
                                azulKirito.copy(alpha = 0.1f)
                        )
                        .padding(vertical = 2.dp)
                ) {
                    MyTextStd(
                        text = teleindicadoresDeTren.tren,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(gridWidth)
                    )
                    teleindicadoresDeTren.teleindicadores.forEach { teleindicador ->
                        MyTextStd(
                            text = teleindicador,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(gridWidth)
                        )
                    }
                }
            }
        }
        if (notasUsuario.isNotBlank()) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                ParagraphSubtitle(text = stringResource(id = R.string.tus_notas))
                MyTextStd(text = notasUsuario.toSpanned().toAnnotatedString())
            }
        }
        if (localizador != null) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                ParagraphSubtitle(text = stringResource(id = R.string.localizador),
                    modifier = Modifier.clickable { viewModel.onLocalizadorClick() }.fillMaxWidth())
                MyTextStd(text = localizador?.localizador ?: "",
                    modifier = Modifier.clickable { viewModel.onLocalizadorClick() }.fillMaxWidth())
            }
        }

        if (notasTurno.isNotBlank()) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                ParagraphSubtitle(text = stringResource(id = R.string.notas_del_turno_))
                MyTextStd(text = notasTurno.toSpanned().toAnnotatedString())
            }
        }
        if (advMermasTurnosLaterales.show) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                Row {
                    if (advMermasTurnosLaterales.fraseAntes != null)
                        MyTextStd(
                            text = advMermasTurnosLaterales.fraseAntes?.toAnnotatedString()
                                ?: AnnotatedString(""),
                            Modifier
                                .weight(1f)
                                .padding(end = 8.dp)

                        )
                    if (advMermasTurnosLaterales.fraseDespues != null)
                        MyTextStd(
                            text = advMermasTurnosLaterales.fraseDespues?.toAnnotatedString()
                                ?: AnnotatedString(""),
                            Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                }
            }
        }

        if ((cuDetalle?.mermas ?: 0) > 0 || (cuDetalle?.excesos ?: 0) > 0) {
            item {
                val excesos = cuDetalle?.excesos
                val mermas = cuDetalle?.mermas
                var texto = ""
                if (excesos != null && excesos != 0)
                    texto = stringResource(
                        id = R.string.excediste_x_horas_y_generaste_x,
                        LocalTime.ofSecondOfDay((excesos / ConstantsNormativaLaboral.EXCESO_JORNADA).toLong()),
                        LocalTime.ofSecondOfDay(excesos.toLong())
                    ) + "\n"
                if (mermas != null && mermas != 0)
                    texto += stringResource(
                        id = R.string.tu_descanso_se_vio_mermado_en_x_horas_y_generaste_x,
                        LocalTime.ofSecondOfDay((mermas / ConstantsNormativaLaboral.MERMA_DESCANSO).toLong()),
                        LocalTime.ofSecondOfDay(mermas.toLong())
                    )



                ExcesosYMermas(cuDetalle) {
                    onExcesosClick(texto)
                }
            }
        }
        if (historial.isNotEmpty()) {
            itemsIndexed(historial) { index, historia ->

                if (index == 0) {
                    HorizontalDivider(Modifier.padding(top = 4.dp))
                    ParagraphSubtitle(text = stringResource(id = R.string.historial_del_turno))
                }

                MyTextStd(
                    text = stringResource(
                        id = R.string.antes_,
                        nombreTurnosConTipo(historia.tipo, historia.turno, context)
                    ) +
                            if (historia.nombre_debe.isNotNullNorBlank()) {
                                stringResource(
                                    id = R.string._cambiado_con_,
                                    historia.nombre_debe ?: ""
                                )
                            } else {
                                ""
                            } +
                            stringResource(
                                id = R.string.modificado_el_dia_xxx,
                                historia.updated.toLocalDateTime().enFormatoDeSalida() ?: ""
                            )
                )
            }
        }

        item {
            //La altura necesaria para que el FAB no tape nada que haya detrás.
            Spacer(modifier = Modifier.padding(vertical = 32.dp))
        }


    }
}


private fun LocalDate.getSeason(): Season {
    return when (this.month) {
        Month.DECEMBER, Month.JANUARY, Month.FEBRUARY -> Season.Winter
        Month.MARCH, Month.APRIL, Month.MAY -> Season.Spring
        Month.JUNE, Month.JULY, Month.AUGUST -> Season.Summer
        Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER -> Season.Autumn
        else -> Season.None
    }
}