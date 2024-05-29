@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.turnos.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.constants.ConstantsNormativaLaboral
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.colorDeFondoTurnos
import es.kirito.kirito.core.domain.util.colorTextoTurnos
import es.kirito.kirito.core.domain.util.enFormatoDeSalida
import es.kirito.kirito.core.domain.util.esTipoTurnoCambiable
import es.kirito.kirito.core.domain.util.esTurnoConDias
import es.kirito.kirito.core.domain.util.genNombreTextView
import es.kirito.kirito.core.domain.util.isNotNullNorBlank
import es.kirito.kirito.core.domain.util.nombreTurnosConTipo
import es.kirito.kirito.core.domain.util.toComposeColor
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.domain.util.toTurnoPrxTr
import es.kirito.kirito.core.presentation.components.HeaderWithPrevNext
import es.kirito.kirito.core.presentation.components.MyDialogInformation
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.ParagraphSubtitle
import es.kirito.kirito.core.presentation.components.detectSwipe
import es.kirito.kirito.core.presentation.theme.azulKirito
import es.kirito.kirito.core.presentation.utils.Orientation
import es.kirito.kirito.core.presentation.utils.getScreenSizeInfo
import es.kirito.kirito.core.presentation.utils.orientation
import es.kirito.kirito.turnos.domain.models.ErroresHoy
import es.kirito.kirito.turnos.domain.models.Season
import es.kirito.kirito.turnos.presentation.components.DateString
import es.kirito.kirito.turnos.presentation.components.DialogClimaTarea
import es.kirito.kirito.turnos.presentation.components.DialogEditShift
import es.kirito.kirito.turnos.presentation.components.ErroresHoy
import es.kirito.kirito.turnos.presentation.components.ExcesosYMermas
import es.kirito.kirito.turnos.presentation.components.LabelComj
import es.kirito.kirito.turnos.presentation.components.LabelLibra
import es.kirito.kirito.turnos.presentation.components.TareaConClima
import es.kirito.kirito.turnos.presentation.components.TurnoProxTarHeader
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources._cambiado_con_
import kirito.composeapp.generated.resources.antes_
import kirito.composeapp.generated.resources.autumn1
import kirito.composeapp.generated.resources.cancelar
import kirito.composeapp.generated.resources.empty_24
import kirito.composeapp.generated.resources.excediste_x_horas_y_generaste_x
import kirito.composeapp.generated.resources.generar_cuadro_anual_con_claves_vac_as
import kirito.composeapp.generated.resources.historial_del_turno
import kirito.composeapp.generated.resources.localizador
import kirito.composeapp.generated.resources.modificado_el_dia_xxx
import kirito.composeapp.generated.resources.notas_del_turno_
import kirito.composeapp.generated.resources.otras_opciones
import kirito.composeapp.generated.resources.seleccionar
import kirito.composeapp.generated.resources.spring1
import kirito.composeapp.generated.resources.summer1
import kirito.composeapp.generated.resources.teleindicadores_
import kirito.composeapp.generated.resources.tu_descanso_se_vio_mermado_en_x_horas_y_generaste_x
import kirito.composeapp.generated.resources.tus_notas
import kirito.composeapp.generated.resources.winter1
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HoyScreen(navController: NavHostController) {
    val viewModel = koinViewModel<HoyViewModel>()

    Surface(Modifier.fillMaxSize()) {
        //TODO: Imagen ega.
       // val dialogImageEga by viewModel.imagenEgaDialog.collectAsState(initial = null)
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
        val orientation = getScreenSizeInfo().orientation()
        val novedades by viewModel.novedades.collectAsState(initial = false)
        val showLocalizadorDialog by viewModel.showLocalizadorDialog.collectAsState(initial = false)
        val localizador by viewModel.localizador.collectAsState(initial = null)

        LaunchedEffect(Unit) {
            while (true) {
                nowTimeString = Clock.System.now().toLocalTime().toString()
                delay(1.seconds)
            }
        }
        LaunchedEffect(Unit) {
            viewModel.toastFestivo.collect { show ->
                //TODO: Mostrar este toast.
//                if (show)
//                    context.showLongToast(context.getString(R.string.advertencia_festivos))
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
                Orientation.LANDSCAPE ->
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
                            imageVector = Icons.Outlined.Add,
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
                        resource =
                        when (estacion) {
                            Season.Spring -> Res.drawable.spring1
                            Season.Summer -> Res.drawable.summer1
                            Season.Autumn -> Res.drawable.autumn1
                            Season.Winter -> Res.drawable.winter1
                            Season.None -> Res.drawable.empty_24
                            null -> Res.drawable.empty_24
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
            //TODO: Crear el diálogo novedades, para cuando las haya
//            NovedadesDialog {
//                viewModel.onNovedadShown()
//            }
        }

       // if (showDialogImageEga && dialogImageEga?.imagenesEga != null) {
            //TODO: IMAGEN EGAAA
//            ImageEgaDialog(dialogImageEga ?: return@Surface) {
//                showDialogImageEga = false
//            }
   //     }

        MyDialogInformation(show = showDialogExcesos, text = textExcesos,
            onDismiss = { showDialogExcesos = false })
        { showDialogExcesos = false }

        if (showDatePicker) {
            DatePickerDialog(onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        viewModel.onDateSelected(
                            ((datePickerState.selectedDateMillis ?: 1000) / 1000).toInstant()
                                .toLocalDate()
                        )
                    }, enabled = datePickerConfirmEnabled.value) {
                        Text(text = stringResource( Res.string.seleccionar))
                    }
                }, dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(text = stringResource( Res.string.cancelar))
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

        //TODO: Mostrar este toast
//        LaunchedEffect(Unit) {
//            viewModel.toastString.collect {
//                if (it != null)
//                    context.showLongToast(it)
//            }
//        }
    }
}


@Composable
fun HoyHeader(viewModel: HoyViewModel, onDateClicked: () -> Unit, onImageClicked: () -> Unit) {

    val date by viewModel.date.collectAsState()
    val cuDetalle by viewModel.cuDetalleDelTurno.collectAsState(initial = null)
    val turnoPrxTr by viewModel.turnoPrxTr.collectAsState(initial = null)
    //TODO: Imagen ega
  //  val imagenEga by viewModel.imagenEga.collectAsState(initial = null)
    val festivo by viewModel.festivo.collectAsState(initial = "")

    HeaderWithPrevNext(
        title = DateString(date),
        festivo = festivo,
        onDateClick = { onDateClicked() },
        onPrevClick = { viewModel.onPreviousDayClick(1) },
        onNextClick = { viewModel.onNextDayClick(1) },
        onPrevLongClick = { viewModel.onPreviousDayClick(7) },
        onNextLongClick = { viewModel.onNextDayClick(7) },
        onFestivoClick = {
            //TODO: Mostrar este toast.
         ///   context.showLongToast(festivo)
        }
    )
    if (cuDetalle != null)
        Text(
            text = cuDetalle?.tipoYTurnoText(turnoPrxTr)
                ?: "",
            Modifier
                .background(color = colorDeFondoTurnos(cuDetalle?.tipo ?: "").toComposeColor())
                .fillMaxWidth(),
            color = colorTextoTurnos(cuDetalle?.tipo ?: ""),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    if (cuDetalle?.esTurnoConDias() == true)
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.onComjYLibraClick()
                }) {
            val COMJs = cuDetalle?.comj ?: 0
            val LIBRas = cuDetalle?.libra ?: 0
            if (COMJs > 0)
                LabelComj(COMJs)
            if (LIBRas > 0)
                LabelLibra(LIBRas)
        }
    if (turnoPrxTr?.sitioOrigen != null || cuDetalle?.tipo == "7000")
        TurnoProxTarHeader(turno = turnoPrxTr, cuDetalle)

//    //TODO: Imagen ega
//    if (imagenEga != null) {
//        val scrollState = rememberScrollState()
//        //TODO: Imagen del egaaaa
////        Image(
////            bitmap = imagenEga?.genImage(context = context)?.asImageBitmap() ?: return,
////            contentDescription = "",
////            contentScale = ContentScale.FillHeight,
////            modifier = Modifier
////                .horizontalScroll(scrollState)
////                .padding(4.dp)
////                .pointerInput(Unit) {
////                    detectTapGestures { offset ->
////                        viewModel.onImageClicked(
////                            scrollState.value,
////                            offset.x,
////                            imagenEga ?: return@detectTapGestures,
////                            context
////                        )
////                        onImageClicked()
////                    }
////                }
////
////        )
//    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HoyBody(
    viewModel: HoyViewModel,
    onTareaClick: (GrTarea) -> Unit,
    onExcesosClick: (String) -> Unit,
    onGenerarCuadroVacioClick: () -> Unit,
    onGenerarCuadroClick: () -> Unit,
) {

    val tareas by viewModel.tareas.collectAsState(initial = emptyList())
    val teleindicadores by viewModel.teleindicadores
        .collectAsState(initial = emptyList())
    val notasUsuario by viewModel.notasUsuario.collectAsState(initial = buildSpannedString { })
    val notasTurno by viewModel.notasTurno.collectAsState(initial = buildSpannedString { })
    val advMermasTurnosLaterales by viewModel.advMermasTurnosLaterales
        .collectAsState(initial = HoyViewModel.AdvmermasTurnosLaterales(false, null, null))
    val cuDetalle by viewModel.cuDetalleDelTurno.collectAsState(initial = null)
    val historial by viewModel.historial.collectAsState(initial = emptyList())
    val errores by viewModel.erroresHoy.collectAsState(initial = ErroresHoy())
    val localizador by viewModel.localizador.collectAsState(initial = null)
    val generarCuadroVacioClicked by rememberSaveable { mutableStateOf(false) }
    val selectedDate by viewModel.date.collectAsState(initial = null)
    var showErrors by remember { mutableStateOf(false) }
    val orientation = getScreenSizeInfo().orientation()


    //Un pequeño delay, para que no se vean por instantes los errores.
    LaunchedEffect(selectedDate) {
        showErrors = false
        delay(300)
        showErrors = true
    }

    LazyColumn(Modifier.padding(horizontal = 16.dp)) {

        if (errores.hayErrores && showErrors)
            item {ErroresHoy(errores, cuDetalle, Modifier.animateItemPlacement()) }

        if (cuDetalle == null && showErrors) {
            item {
                Button(onClick = {
                    generarCuadroVacioClicked != generarCuadroVacioClicked
                    onGenerarCuadroVacioClick()
                }, enabled = !generarCuadroVacioClicked) {
                    Text(text = stringResource(Res.string.generar_cuadro_anual_con_claves_vac_as))
                }
                OutlinedButton(onClick = { onGenerarCuadroClick() }) {
                    Text(text = stringResource(Res.string.otras_opciones))
                }
            }
        }

        //Solo mostramos aquí las tareas si estamos en vertical.
        if (tareas.isNotEmpty() && orientation == Orientation.PORTRAIT)
            items(tareas) { tarea ->
                TareaConClima(tarea = tarea, Modifier.animateItemPlacement()) { estaTarea ->
                    onTareaClick(estaTarea)
                }
            }
        if (cuDetalle?.tipo?.esTipoTurnoCambiable() == true && cuDetalle?.nombreDebe.isNotNullNorBlank())
            item {
                HorizontalDivider(Modifier.padding(vertical = 4.dp))
                MyTextStd(
                    text = genNombreTextView(
                        cuDetalle?.toTurnoPrxTr() ?: TurnoPrxTr()//Prefiero algo vacío que los !!.
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        if (teleindicadores.size > 1) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                ParagraphSubtitle(text = stringResource( Res.string.teleindicadores_))
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
                ParagraphSubtitle(text = stringResource(Res.string.tus_notas))
                MyTextStd(text = notasUsuario.toSpanned().toAnnotatedString())
            }
        }
        if (localizador != null) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                ParagraphSubtitle(text = stringResource(Res.string.localizador),
                    modifier = Modifier.clickable { viewModel.onLocalizadorClick() }.fillMaxWidth())
                MyTextStd(text = localizador?.localizador ?: "",
                    modifier = Modifier.clickable { viewModel.onLocalizadorClick() }.fillMaxWidth())
            }
        }

        if (notasTurno.isNotBlank()) {
            item {
                HorizontalDivider(Modifier.padding(top = 4.dp))
                ParagraphSubtitle(text = stringResource(Res.string.notas_del_turno_))
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
                        Res.string.excediste_x_horas_y_generaste_x,
                        LocalTime.fromSecondOfDay((excesos / ConstantsNormativaLaboral.EXCESO_JORNADA).toInt()),
                        LocalTime.fromSecondOfDay(excesos)
                    ) + "\n"
                if (mermas != null && mermas != 0)
                    texto += stringResource(
                        Res.string.tu_descanso_se_vio_mermado_en_x_horas_y_generaste_x,
                        LocalTime.fromSecondOfDay((mermas / ConstantsNormativaLaboral.MERMA_DESCANSO).toInt()),
                        LocalTime.fromSecondOfDay(mermas)
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
                    ParagraphSubtitle(text = stringResource(Res.string.historial_del_turno))
                }

                MyTextStd(
                    text = stringResource(
                        Res.string.antes_,
                        nombreTurnosConTipo(historia.tipo, historia.turno)
                    ) +
                            if (historia.nombreDebe.isNotNullNorBlank()) {
                                stringResource(
                                    Res.string._cambiado_con_,
                                    historia.nombreDebe ?: ""
                                )
                            } else {
                                ""
                            } +
                            stringResource(
                                Res.string.modificado_el_dia_xxx,
                                historia.updated.toInstant().enFormatoDeSalida() ?: ""
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

@Composable
private fun CuDetalleConFestivoDBModel.tipoYTurnoText(turnoPrxTr: TurnoPrxTr?): String {
    var texto = nombreTurnosConTipo(this.tipo, this.turno)
    if (turnoPrxTr?.equivalencia != null) {
        texto = "$texto - ${turnoPrxTr.equivalencia}"
    }
    return texto
}
