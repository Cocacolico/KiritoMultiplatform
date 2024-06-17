@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.menu.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material.icons.outlined.ViewModule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.constants.FlagLogout
import es.kirito.kirito.core.domain.util.colorDeFondoTurnos
import es.kirito.kirito.core.domain.util.enMiFormato
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.presentation.components.ButtonMenuPrincipal
import es.kirito.kirito.core.presentation.components.ButtonMenuPrincipalBadge
import es.kirito.kirito.core.presentation.components.ButtonMenuPrincipalBadgeDiasIniciales
import es.kirito.kirito.core.presentation.components.DiasEspecialesCard
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.components.dialogs.MyDialogConfirmation
import es.kirito.kirito.core.presentation.components.dialogs.MyDialogInformation
import es.kirito.kirito.core.presentation.navigation.Graph
import es.kirito.kirito.core.presentation.theme.Orange
import es.kirito.kirito.menu.domain.MenuState
import es.kirito.kirito.menu.navigation.MenuPrincipalNav
import kirito.composeapp.generated.resources.Desconectarme
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.actualiza_tu_app
import kirito.composeapp.generated.resources.actualizar
import kirito.composeapp.generated.resources.admin
import kirito.composeapp.generated.resources.ajustes
import kirito.composeapp.generated.resources.ayuda
import kirito.composeapp.generated.resources.bienvenido_a
import kirito.composeapp.generated.resources.bienvenido_a__
import kirito.composeapp.generated.resources.borrar_cuadro
import kirito.composeapp.generated.resources.comj
import kirito.composeapp.generated.resources.configurar_google_calendar
import kirito.composeapp.generated.resources.contacto_con_un_admin
import kirito.composeapp.generated.resources.d_as_especiales
import kirito.composeapp.generated.resources.dd
import kirito.composeapp.generated.resources.dj
import kirito.composeapp.generated.resources.dja
import kirito.composeapp.generated.resources.entra_el
import kirito.composeapp.generated.resources.estadisticas
import kirito.composeapp.generated.resources.excesos_de_jornada
import kirito.composeapp.generated.resources.gestiones
import kirito.composeapp.generated.resources.gr_fico_en_vigor
import kirito.composeapp.generated.resources.gr_ficos
import kirito.composeapp.generated.resources.hasta_el
import kirito.composeapp.generated.resources.haz_click_aqu_para_actualizar_tu_app_pronto_dejar_de_funcionar_si_no
import kirito.composeapp.generated.resources.libra
import kirito.composeapp.generated.resources.list_n_telef_nico
import kirito.composeapp.generated.resources.lz
import kirito.composeapp.generated.resources.lza
import kirito.composeapp.generated.resources.mensajes
import kirito.composeapp.generated.resources.mi_perfil
import kirito.composeapp.generated.resources.mis_cambios
import kirito.composeapp.generated.resources.mis_compa_eros
import kirito.composeapp.generated.resources.mis_turnos
import kirito.composeapp.generated.resources.no_hay_grafico_en_vigor
import kirito.composeapp.generated.resources.notificaciones
import kirito.composeapp.generated.resources.peticiones_de_cambios
import kirito.composeapp.generated.resources.pr_ximo_gr_fico
import kirito.composeapp.generated.resources.quieres_salir_de_tu_cuenta
import kirito.composeapp.generated.resources.salir
import kirito.composeapp.generated.resources.salir_de_la_aplicacion
import kirito.composeapp.generated.resources.subir_cuadro_anual
import kirito.composeapp.generated.resources.subir_gr_fico_nuevo
import kirito.composeapp.generated.resources.subir_localizadores
import kirito.composeapp.generated.resources.subir_mensual
import kirito.composeapp.generated.resources.tabl_n_de_anuncios
import kirito.composeapp.generated.resources.te_hemos_forzado_el_logout
import kirito.composeapp.generated.resources.tratamiento_de_datos
import kirito.composeapp.generated.resources.turnos
import kirito.composeapp.generated.resources.turnos_con_notas
import kirito.composeapp.generated.resources.utilidades
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuScreen(navController: NavHostController) {
    val viewModel = koinViewModel<MenuViewModel>()
    val state by viewModel.menuState.collectAsState(MenuState())

    if(state.allDataErased) {
        navController.popBackStack()
        navController.navigate(Graph.RootNavGraph)
    }

    Surface(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(MenuPrincipalNav.Perfil.route)
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        if (state.userData.username == "")
                            Text(
                                text = stringResource(Res.string.bienvenido_a)
                            )
                        else
                            ClickableText(
                                text = AnnotatedString(
                                    stringResource(
                                        Res.string.bienvenido_a__,
                                        state.userData.name
                                    )
                                ),
                                onClick = {
                                    viewModel.onPerfilClick()
                                }
                            )
                        ClickableText(
                            text = AnnotatedString(stringResource(Res.string.mi_perfil)),
                            onClick = {
                                viewModel.onPerfilClick()
                            }
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { viewModel.onLogoutClick() }
                                .size(24.dp)
                        )
                        Text(
                            text = stringResource(Res.string.salir)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        ) {
                            if (state.graficoDeHoy?.descripcion != null) {
                                Text(
                                    text = stringResource(
                                        Res.string.gr_fico_en_vigor,
                                        state.graficoDeHoy!!.descripcion!!
                                    ),
                                )
                                Text(
                                    text = stringResource(
                                        Res.string.hasta_el,
                                        state.graficoDeHoy!!.fechaFinal!!.toLocalDate()
                                            .enMiFormato()
                                    )
                                )
                            } else
                                Text(
                                    text = stringResource(Res.string.no_hay_grafico_en_vigor)
                                )
                            if (state.graficoActualYprox.isNotEmpty()) {
                                Text(
                                    text = stringResource(
                                        Res.string.pr_ximo_gr_fico,
                                        state.graficoActualYprox.first().descripcion!!
                                    )
                                )
                                Text(
                                    text = stringResource(
                                        Res.string.entra_el,
                                        state.graficoActualYprox.first().fechaInicio!!.toLocalDate()
                                            .enMiFormato()
                                    )
                                )
                            }
                        }

                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (state.diasEspeciales.lz != 0)
                                DiasEspecialesCard(
                                    color = colorDeFondoTurnos("LZ"),
                                    text = Res.string.lz,
                                    cantidad = state.diasEspeciales.lz.toString()
                                )
                            if (state.diasEspeciales.lza != 0)
                                DiasEspecialesCard(
                                    color = colorDeFondoTurnos("LZA"),
                                    text = Res.string.lza,
                                    cantidad = state.diasEspeciales.lza.toString()
                                )
                            if (state.diasEspeciales.comj != 0)
                                DiasEspecialesCard(
                                    color = colorDeFondoTurnos("COMJ"),
                                    text = Res.string.comj,
                                    cantidad = state.diasEspeciales.comj.toString()
                                )
                            if (state.diasEspeciales.libra != 0)
                                DiasEspecialesCard(
                                    color = colorDeFondoTurnos("LIBRa"),
                                    text = Res.string.libra,
                                    cantidad = state.diasEspeciales.libra.toString()
                                )
                            if (state.diasEspeciales.dd != 0)
                                DiasEspecialesCard(
                                    color = colorDeFondoTurnos("DD"),
                                    text = Res.string.dd,
                                    cantidad = state.diasEspeciales.dd.toString()
                                )
                            if (state.diasEspeciales.dj != 0)
                                DiasEspecialesCard(
                                    color = colorDeFondoTurnos("DJ"),
                                    text = Res.string.dj,
                                    cantidad = state.diasEspeciales.dj.toString()
                                )
                            if (state.diasEspeciales.libra != 0)
                                DiasEspecialesCard(
                                    color = colorDeFondoTurnos("DJA"),
                                    text = Res.string.dja,
                                    cantidad = state.diasEspeciales.dja.toString()
                                )
                        }

                    }
                    when (state.flagLogout) {
                        FlagLogout.SHOULD_UPD ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Orange,
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                ClickableText(
                                    text = AnnotatedString(stringResource(Res.string.haz_click_aqu_para_actualizar_tu_app_pronto_dejar_de_funcionar_si_no)),
                                    onClick = {
                                        viewModel.showUpdateDialog(mandatory = false)
                                    },
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }

                        FlagLogout.MUST_UPD ->
                            viewModel.showUpdateDialog(mandatory = true)

                        FlagLogout.WRONG_TOKEN ->
                            MyDialogInformation(
                                show = true,
                                onDismiss = {
                                    viewModel.onWrongTokenDismiss()
                                },
                                onConfirm = {
                                    viewModel.onConfirmWrongToken()
                                },
                                text = stringResource(Res.string.te_hemos_forzado_el_logout)
                            )
                    }

                    MyTextSubTitle(text = stringResource(Res.string.mis_turnos))
                    FlowRow(
                        Modifier.fillMaxWidth()
                    ) {
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.Handshake,
                            text = Res.string.mis_cambios,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipalBadgeDiasIniciales(
                            icon = Icons.Outlined.Celebration,
                            text = Res.string.d_as_especiales,
                            onClick = {

                            },
                            numNotificaciones = state.diasInicialesChecked
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.AutoMirrored.Outlined.Notes,
                            text = Res.string.turnos_con_notas,
                            onClick = {

                            }
                        )
                    }
                    MyTextSubTitle(text = stringResource(Res.string.mis_compa_eros))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.userData.mostrarCuadros == "1")
                            ButtonMenuPrincipal(
                                icon = Icons.Outlined.ViewModule,
                                text = Res.string.turnos,
                                onClick = {

                                }
                            )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.Call,
                            text = Res.string.list_n_telef_nico,
                            onClick = {

                            }
                        )
                        if (state.userData.cambiosActivados == "1")
                            ButtonMenuPrincipalBadge(
                                icon = Icons.Outlined.Handshake,
                                text = Res.string.peticiones_de_cambios,
                                onClick = {

                                },
                                numNotificaciones = state.cambiosNuevos
                            )
                        ButtonMenuPrincipalBadge(
                            icon = Icons.AutoMirrored.Outlined.Comment,
                            text = Res.string.tabl_n_de_anuncios,
                            numNotificaciones = 0, // TODO implementar la lógica para contar los anuncios nuevos
                            onClick = {

                            }
                        )


                    }
                    MyTextSubTitle(text = stringResource(Res.string.utilidades))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.TableChart,
                            text = Res.string.gr_ficos,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.Notifications,
                            text = Res.string.notificaciones,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.QueryStats,
                            text = Res.string.estadisticas,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.AccessTime,
                            text = Res.string.excesos_de_jornada,
                            onClick = {

                            }
                        )
                    }
                    MyTextSubTitle(text = stringResource(Res.string.gestiones))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.UploadFile,
                            text = Res.string.subir_cuadro_anual,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.Delete,
                            text = Res.string.borrar_cuadro,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.CalendarMonth,
                            text = Res.string.configurar_google_calendar,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.UploadFile,
                            text = Res.string.subir_mensual,
                            onClick = {

                            }
                        )
                    }
                    if (state.userData.admin == "1")
                        MyTextSubTitle(text = stringResource(Res.string.admin))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.UploadFile,
                            text = Res.string.subir_gr_fico_nuevo,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.UploadFile,
                            text = Res.string.subir_localizadores,
                            onClick = {

                            }
                        )
                    }
                    MyTextSubTitle(text = stringResource(Res.string.ayuda))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ButtonMenuPrincipal(
                            icon = Icons.AutoMirrored.Outlined.ContactSupport,
                            text = Res.string.contacto_con_un_admin,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipalBadge(
                            icon = Icons.AutoMirrored.Outlined.Message,
                            text = Res.string.mensajes,
                            onClick = {

                            },
                            numNotificaciones = state.mensajesAdminNuevos
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.AutoMirrored.Outlined.Help,
                            text = Res.string.ayuda,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.Settings,
                            text = Res.string.ajustes,
                            onClick = {

                            }
                        )
                        ButtonMenuPrincipal(
                            icon = Icons.Outlined.Policy,
                            text = Res.string.tratamiento_de_datos,
                            onClick = {

                            }
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(32.dp)
                    )
                }
            }
            FloatingActionButton(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        // Dialogs para actualizar la app
        MyDialogConfirmation(
            show = state.showUpdateDialog,
            onDismiss = {
                viewModel.onUpdDismiss()
            },
            onConfirm = {
                viewModel.onConfirmUpd()
            },
            title = "",
            text = stringResource(Res.string.actualiza_tu_app),
            okText = stringResource(Res.string.actualizar)
        )
        MyDialogInformation(
            show = state.showUpdateDialog,
            onDismiss = {
                viewModel.onMustUpdDismiss()
            },
            onConfirm = {
                viewModel.onConfirmUpd()
            },
            title = "",
            text = stringResource(Res.string.actualiza_tu_app),
            okText = stringResource(Res.string.actualizar)
        )
        // Dialog para confirmar logout
        MyDialogConfirmation(
            show = state.showLogoutDialog,
            text = stringResource(Res.string.quieres_salir_de_tu_cuenta),
            title = stringResource(Res.string.salir_de_la_aplicacion),
            onDismiss = {
                viewModel.onLogoutDialogDismiss()
            },
            onConfirm = {
                viewModel.onLogoutDialogConfirm()
            },
            okText = stringResource(Res.string.Desconectarme)
        )
    }
}