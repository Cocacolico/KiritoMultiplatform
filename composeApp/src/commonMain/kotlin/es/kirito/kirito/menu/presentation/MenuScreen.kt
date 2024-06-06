@file:OptIn(KoinExperimentalAPI::class)


package es.kirito.kirito.menu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.kirito.kirito.core.presentation.components.ButtonMenuPrincipal
import es.kirito.kirito.core.presentation.components.ButtonMenuPrincipalBadge
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.theme.amarilloKirito
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.admin
import kirito.composeapp.generated.resources.ajustes
import kirito.composeapp.generated.resources.ayuda
import kirito.composeapp.generated.resources.bienvenido_a
import kirito.composeapp.generated.resources.borrar_cuadro
import kirito.composeapp.generated.resources.configurar_google_calendar
import kirito.composeapp.generated.resources.contacto_con_un_admin
import kirito.composeapp.generated.resources.d_as_especiales
import kirito.composeapp.generated.resources.estadisticas
import kirito.composeapp.generated.resources.excesos_de_jornada
import kirito.composeapp.generated.resources.gestiones
import kirito.composeapp.generated.resources.gr_fico_en_vigor
import kirito.composeapp.generated.resources.gr_ficos
import kirito.composeapp.generated.resources.list_n_telef_nico
import kirito.composeapp.generated.resources.mensajes
import kirito.composeapp.generated.resources.mi_perfil
import kirito.composeapp.generated.resources.mis_cambios
import kirito.composeapp.generated.resources.mis_compa_eros
import kirito.composeapp.generated.resources.mis_turnos
import kirito.composeapp.generated.resources.notificaciones
import kirito.composeapp.generated.resources.peticiones_de_cambios
import kirito.composeapp.generated.resources.subir_cuadro_anual
import kirito.composeapp.generated.resources.subir_gr_fico_nuevo
import kirito.composeapp.generated.resources.subir_localizadores
import kirito.composeapp.generated.resources.subir_mensual
import kirito.composeapp.generated.resources.tabl_n_de_anuncios
import kirito.composeapp.generated.resources.tratamiento_de_datos
import kirito.composeapp.generated.resources.turnos
import kirito.composeapp.generated.resources.turnos_con_notas
import kirito.composeapp.generated.resources.utilidades
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalResourceApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun MenuScreen(navController: NavHostController) {
    val viewModel = koinViewModel<MenuViewModel>()
    Surface(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            viewModel.onPerfilClick()
                        }
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null)
                    }
                    Column {
                        ClickableText(
                            text = AnnotatedString(stringResource(Res.string.bienvenido_a)),
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
                    IconButton(
                        onClick = {
                            viewModel.onLogoutClick()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
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
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.gr_fico_en_vigor),
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = "LZ: 6"
                        )
                        Text(
                            text = "DD: 4"
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
                        ButtonMenuPrincipalBadge(
                            icon = Icons.Outlined.Celebration,
                            text = Res.string.d_as_especiales,
                            onClick = {

                            },
                            numNotificaciones = 2
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
                        ButtonMenuPrincipalBadge(
                            icon = Icons.Outlined.Handshake,
                            text = Res.string.peticiones_de_cambios,
                            onClick = {

                            },
                            numNotificaciones = 2
                        )
                        ButtonMenuPrincipalBadge(
                                icon = Icons.AutoMirrored.Outlined.Comment,
                                text = Res.string.tabl_n_de_anuncios,
                            numNotificaciones = 8,
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
                        ButtonMenuPrincipal(
                            icon = Icons.AutoMirrored.Outlined.Message,
                            text = Res.string.mensajes,
                            onClick = {

                            }
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
                }

            }
            FloatingActionButton(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}