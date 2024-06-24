package es.kirito.kirito.core.domain.kiritoError

import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.el_destinatario_no_tiene_turno_para_la_fecha_indicada
import kirito.composeapp.generated.resources.el_estado_indicado_no_es_v_lido
import kirito.composeapp.generated.resources.el_fichero_adjunto_supera_los_20mb_no_se_guarda
import kirito.composeapp.generated.resources.el_id_de_anuncio_no_corresponde_con_el_usuario
import kirito.composeapp.generated.resources.el_margen_de_fecha_no_es_v_lido
import kirito.composeapp.generated.resources.el_mensaje_no_existe
import kirito.composeapp.generated.resources.el_receptor_no_tiene_activos_los_cambios
import kirito.composeapp.generated.resources.el_t_tulo_indicado_no_es_v_lido
import kirito.composeapp.generated.resources.el_usuario_indicado_ya_existe
import kirito.composeapp.generated.resources.el_usuario_no_es_administrador
import kirito.composeapp.generated.resources.el_usuario_no_participa_en_el_sistema_de_cambios
import kirito.composeapp.generated.resources.el_usuario_no_tiene_turno_para_la_fecha_indicada
import kirito.composeapp.generated.resources.error_al_decodificar_la_respuesta_json
import kirito.composeapp.generated.resources.error_al_enviar_el_correo_electr_nico
import kirito.composeapp.generated.resources.error_al_hacer_la_consulta_https_a_google
import kirito.composeapp.generated.resources.error_al_obtener_datos
import kirito.composeapp.generated.resources.error_con_el_id_detalle_o_fecha_no_corresponden
import kirito.composeapp.generated.resources.error_con_el_tipo_de_turno_recibido_no_es_valido
import kirito.composeapp.generated.resources.error_con_el_turno_recibido_no_tiene_valor
import kirito.composeapp.generated.resources.error_con_la_actualizaci_n_de_datos
import kirito.composeapp.generated.resources.error_con_la_fecha_recibida_no_es_valida
import kirito.composeapp.generated.resources.error_desconocido
import kirito.composeapp.generated.resources.error_en_el_identificador_del_receptor_del_cambio
import kirito.composeapp.generated.resources.ese_mensaje_no_es_tuyo
import kirito.composeapp.generated.resources.esta_petici_n_est_en_un_estado_final_y_no_se_puede_modificar
import kirito.composeapp.generated.resources.fecha_del_cambio_anterior_a_hoy
import kirito.composeapp.generated.resources.fichero_demasiado_grande
import kirito.composeapp.generated.resources.identificador_de_cambio_erroneo
import kirito.composeapp.generated.resources.la_contrase_a_antigua_no_coincide
import kirito.composeapp.generated.resources.la_contrase_a_debe_tener_5_caracteres
import kirito.composeapp.generated.resources.la_explicaci_n_indicada_no_es_v_lida
import kirito.composeapp.generated.resources.la_fecha_del_anuncio_no_puede_ser_anterior_a_hoy
import kirito.composeapp.generated.resources.la_fecha_del_cambio_es_erronea
import kirito.composeapp.generated.resources.la_fecha_indicada_para_el_anuncio_es_erronea
import kirito.composeapp.generated.resources.la_fecha_recibida_no_tiene_el_formato_m_y_ej_10_2022
import kirito.composeapp.generated.resources.la_fecha_recibida_no_tiene_el_formato_y_ej_2022
import kirito.composeapp.generated.resources.la_fecha_recibida_no_tiene_el_formato_y_m_d_ej_2022_12_1
import kirito.composeapp.generated.resources.la_fecha_recibida_no_tiene_el_formato_y_m_d_h_i_s_ej_2022_12_1_14_33_12
import kirito.composeapp.generated.resources.los_datos_de_la_petici_n_de_cambio_no_coindicen_con_los_almacenados_en_las_tablas_de_detalles_de_turnos
import kirito.composeapp.generated.resources.no_compartes_tu_cuadro_anual
import kirito.composeapp.generated.resources.no_encuentro_el_fichero_en_el_directorio
import kirito.composeapp.generated.resources.no_hay_datos_de_colores_de_turnos
import kirito.composeapp.generated.resources.no_hay_datos_de_turnos_ganables
import kirito.composeapp.generated.resources.no_hay_imagen_para_el_turno_y_fecha_recibidos
import kirito.composeapp.generated.resources.no_hay_nombre_de_fichero_para_el_turno_y_fecha_recibidos
import kirito.composeapp.generated.resources.no_he_obtenido_datos_v_lidos
import kirito.composeapp.generated.resources.no_he_podido_aceptar_el_cambio_contacta_con_el_administrador
import kirito.composeapp.generated.resources.no_he_podido_cargar_la_clase_php_de_trabajo
import kirito.composeapp.generated.resources.no_he_podido_procesar_tu_solicutud
import kirito.composeapp.generated.resources.no_puedes_pedirte_un_cambio_a_ti_mismo
import kirito.composeapp.generated.resources.no_puedo_devolver_imagen_ya_que_no_hay_gr_fico_para_la_fecha_indicada
import kirito.composeapp.generated.resources.no_se_ha_podido_analizar_el_contenido_del_fichero_pdf
import kirito.composeapp.generated.resources.no_viene_el_campo_con_el_identificador_de_turno
import kirito.composeapp.generated.resources.no_viene_el_campo_con_el_nombre_del_cambio
import kirito.composeapp.generated.resources.no_viene_el_campo_con_el_tipo_de_turno
import kirito.composeapp.generated.resources.no_viene_el_campo_con_el_turno
import kirito.composeapp.generated.resources.no_viene_el_campo_con_la_fecha_del_turno
import kirito.composeapp.generated.resources.no_viene_el_campo_con_las_notas_del_turno
import kirito.composeapp.generated.resources.no_viene_el_fichero_pdf
import kirito.composeapp.generated.resources.no_viene_nombre_de_la_app
import kirito.composeapp.generated.resources.no_viene_petici_n_de_acci_n
import kirito.composeapp.generated.resources.no_viene_token_de_usuario
import kirito.composeapp.generated.resources.no_viene_versi_n_de_la_app
import kirito.composeapp.generated.resources.nombre_de_la_app_desconocido
import kirito.composeapp.generated.resources.petici_n_de_acci_n_no_existente
import kirito.composeapp.generated.resources.petici_n_incorrecta
import kirito.composeapp.generated.resources.problemas_de_login_de_usuario
import kirito.composeapp.generated.resources.sin_error
import kirito.composeapp.generated.resources.tienes_que_responder_a_la_petici_n_del_usuario_al_que_pides_el_cambio_antes_de_hacerle_una_nueva_petici_n
import kirito.composeapp.generated.resources.token_incorrecto
import kirito.composeapp.generated.resources.usuario_bloqueado
import kirito.composeapp.generated.resources.usuario_desactivado
import kirito.composeapp.generated.resources.usuario_o_contrase_a_incorrectos
import kirito.composeapp.generated.resources.versi_n_de_la_app_a_punto_de_caducar
import kirito.composeapp.generated.resources.versi_n_de_la_app_obsoleta
import kirito.composeapp.generated.resources.ya_tiene_anuncio_previo_para_la_fecha_indicada
import kirito.composeapp.generated.resources.ya_tienes_una_petici_n_de_cambio_para_ese_usuario_y_dia
import org.jetbrains.compose.resources.StringResource


fun kiritoErrorStringResource( error: String?) : StringResource {
    val errorCode = error?.toIntOrNull()
    return when (errorCode)  {
        0 ->  Res.string.sin_error
        1 ->  Res.string.no_viene_token_de_usuario
        2 ->  Res.string.token_incorrecto
        3 ->  Res.string.petici_n_incorrecta
        4 ->  Res.string.no_viene_petici_n_de_acci_n
        5 ->  Res.string.petici_n_de_acci_n_no_existente
        6 ->  Res.string.versi_n_de_la_app_obsoleta
        7 ->  Res.string.no_viene_nombre_de_la_app
        8 ->  Res.string.no_viene_versi_n_de_la_app
        9 ->  Res.string.nombre_de_la_app_desconocido
        11 ->  Res.string.usuario_o_contrase_a_incorrectos
        12 ->  Res.string.usuario_bloqueado
        14 ->  Res.string.usuario_desactivado
        15 ->  Res.string.problemas_de_login_de_usuario
        16 ->  Res.string.el_usuario_indicado_ya_existe
        17 ->  Res.string.la_contrase_a_antigua_no_coincide
        18 ->  Res.string.la_contrase_a_debe_tener_5_caracteres
        150 ->  Res.string.no_hay_datos_de_turnos_ganables
        151 ->  Res.string.no_hay_datos_de_colores_de_turnos
        201 ->  Res.string.el_usuario_no_es_administrador
        301 ->  Res.string.el_mensaje_no_existe
        302 ->  Res.string.ese_mensaje_no_es_tuyo
        601 ->  Res.string.no_se_ha_podido_analizar_el_contenido_del_fichero_pdf
        602 ->  Res.string.no_viene_el_fichero_pdf
        603 ->  Res.string.fichero_demasiado_grande
        604 ->  Res.string.error_desconocido
        701 ->  Res.string.no_viene_el_campo_con_el_identificador_de_turno
        702 ->  Res.string.no_viene_el_campo_con_la_fecha_del_turno
        703 ->  Res.string.no_viene_el_campo_con_el_turno
        704 ->  Res.string.no_viene_el_campo_con_el_tipo_de_turno
        705 ->  Res.string.no_viene_el_campo_con_las_notas_del_turno
        706 ->  Res.string.no_viene_el_campo_con_el_nombre_del_cambio
        707 ->  Res.string.error_con_el_id_detalle_o_fecha_no_corresponden
        708 ->  Res.string.error_con_la_fecha_recibida_no_es_valida
        709 ->  Res.string.error_con_el_turno_recibido_no_tiene_valor
        710 ->  Res.string.error_con_el_tipo_de_turno_recibido_no_es_valido
        711 ->  Res.string.error_con_la_actualizaci_n_de_datos
        801 ->  Res.string.no_compartes_tu_cuadro_anual
        901 ->  Res.string.error_al_hacer_la_consulta_https_a_google
        902 ->  Res.string.error_al_decodificar_la_respuesta_json
        1001 ->  Res.string.no_he_podido_procesar_tu_solicutud
        1002 ->  Res.string.no_he_podido_cargar_la_clase_php_de_trabajo
        1003 ->  Res.string.no_he_obtenido_datos_v_lidos
        1004 ->  Res.string.error_al_obtener_datos
        1005 ->  Res.string.error_al_enviar_el_correo_electr_nico
        1301 ->  Res.string.error_en_el_identificador_del_receptor_del_cambio
        1302 ->  Res.string.el_usuario_no_participa_en_el_sistema_de_cambios
        1303 ->  Res.string.el_receptor_no_tiene_activos_los_cambios
        1304 ->  Res.string.no_puedes_pedirte_un_cambio_a_ti_mismo
        1305 ->  Res.string.no_he_podido_aceptar_el_cambio_contacta_con_el_administrador
        1306 ->  Res.string.la_fecha_del_cambio_es_erronea
        1307 ->  Res.string.fecha_del_cambio_anterior_a_hoy
        1308 ->  Res.string.el_usuario_no_tiene_turno_para_la_fecha_indicada
        1309 ->  Res.string.el_destinatario_no_tiene_turno_para_la_fecha_indicada
        1310 ->  Res.string.ya_tienes_una_petici_n_de_cambio_para_ese_usuario_y_dia
        1311 ->  Res.string.identificador_de_cambio_erroneo
        1312 ->  Res.string.el_estado_indicado_no_es_v_lido
        1313 ->  Res.string.esta_petici_n_est_en_un_estado_final_y_no_se_puede_modificar
        1314 ->  Res.string.tienes_que_responder_a_la_petici_n_del_usuario_al_que_pides_el_cambio_antes_de_hacerle_una_nueva_petici_n
        1315 ->  Res.string.los_datos_de_la_petici_n_de_cambio_no_coindicen_con_los_almacenados_en_las_tablas_de_detalles_de_turnos
        1321 ->  Res.string.la_fecha_indicada_para_el_anuncio_es_erronea
        1322 ->  Res.string.el_t_tulo_indicado_no_es_v_lido
        1323 ->  Res.string.la_explicaci_n_indicada_no_es_v_lida
        1324 ->  Res.string.el_id_de_anuncio_no_corresponde_con_el_usuario
        1325 ->  Res.string.ya_tiene_anuncio_previo_para_la_fecha_indicada
        1326 ->  Res.string.la_fecha_del_anuncio_no_puede_ser_anterior_a_hoy
        10001 ->  Res.string.versi_n_de_la_app_a_punto_de_caducar
        10011 ->  Res.string.la_fecha_recibida_no_tiene_el_formato_y_m_d_ej_2022_12_1
        10012 ->  Res.string.la_fecha_recibida_no_tiene_el_formato_y_m_d_h_i_s_ej_2022_12_1_14_33_12
        10013 ->  Res.string.la_fecha_recibida_no_tiene_el_formato_m_y_ej_10_2022
        10014 ->  Res.string.la_fecha_recibida_no_tiene_el_formato_y_ej_2022
        10021 ->  Res.string.el_margen_de_fecha_no_es_v_lido
        10031 ->  Res.string.no_puedo_devolver_imagen_ya_que_no_hay_gr_fico_para_la_fecha_indicada
        10032 ->  Res.string.no_hay_imagen_para_el_turno_y_fecha_recibidos
        10033 ->  Res.string.no_hay_nombre_de_fichero_para_el_turno_y_fecha_recibidos
        10034 ->  Res.string.no_encuentro_el_fichero_en_el_directorio
        10101 ->  Res.string.el_fichero_adjunto_supera_los_20mb_no_se_guarda

        else -> Res.string.error_desconocido
    }
}