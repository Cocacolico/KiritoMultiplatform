package es.kirito.kirito.core.domain.kiritoError

import es.kirito.kirito.core.data.network.models.ResponseKiritoErrorDTO
import es.kirito.kirito.core.data.utils.KiritoException


/**
 * @throws KiritoException e hijos.
 * **/
fun ResponseKiritoErrorDTO.lanzarExcepcion(): Boolean {
    var hayError = 0
    val codigo = this.errorCode.toIntOrNull()
    val descripcion = this.errorDesc

    when (codigo) {
        0 -> hayError = 0
        1 -> throw KiritoException("1")
        2 -> throw KiritoException("2")
        3 -> throw KiritoException("3")
        4 -> throw KiritoException("4")
        5 -> throw KiritoException("5")
        6 -> throw KiritoException("6")
        7 -> throw KiritoException("7")
        8 -> throw KiritoException("8")
        9 -> throw KiritoException("9")
        11 -> throw KiritoException("11")
        12 -> throw KiritoException("12")
        13 -> hayError = 13//En este caso se resuelve de otra manera.
        14 -> throw KiritoException("14")
        15 -> throw KiritoException("15")
        16 -> throw KiritoException("16")
        17 -> throw KiritoException("17")
        18 -> throw KiritoException("18")
        100 -> hayError = 0//throw KiritoNoDataException("No hay datos.")
        150 -> throw KiritoException("150")
        151 -> throw KiritoException("151")
        201 -> throw KiritoException("201")
        301 -> throw KiritoException("301")
        302 -> throw KiritoException("302")
        601 -> throw KiritoException("601")
        602 -> throw KiritoException("602")
        603 -> throw KiritoException("603")
        604 -> throw KiritoException("604")
        701 -> throw KiritoException("701")
        702 -> throw KiritoException("702")
        703 -> throw KiritoException("703")
        704 -> throw KiritoException("704")
        705 -> throw KiritoException("705")
        706 -> throw KiritoException("706")
        707 -> throw KiritoException("707")
        708 -> throw KiritoException("708")
        709 -> throw KiritoException("709")
        710 -> throw KiritoException("710")
        711 -> throw KiritoException("711")
        801 -> throw KiritoException("801")
        901 -> throw KiritoException("901")
        902 -> throw KiritoException("902")
        1001 -> throw KiritoException("1001")
        1002 -> throw KiritoException("1002")
        1003 -> throw KiritoException("1003")
        1004 -> throw KiritoException("1004")
        1005 -> throw KiritoException("1005")
        1301 -> throw KiritoException("1301")
        1302 -> throw KiritoException("1302")
        1303 -> throw KiritoException("1303")
        1304 -> throw KiritoException("1304")
        1305 -> throw KiritoException("1305")
        1306 -> throw KiritoException("1306")
        1307 -> throw KiritoException("1307")
        1308 -> throw KiritoException("1308")
        1309 -> throw KiritoException("1309")
        1310 -> throw KiritoException("1310")
        1311 -> throw KiritoException("1311")
        1312 -> throw KiritoException("1312")
        1313 -> throw KiritoException("1313")
        1314 -> throw KiritoException("1314")
        1315 -> throw KiritoException("1315")
        1321 -> throw KiritoException("1321")
        1322 -> throw KiritoException("1322")
        1323 -> throw KiritoException("1323")
        1324 -> throw KiritoException("1324")
        1325 -> throw KiritoException("1325")
        1326 -> throw KiritoException("1326")
        10001 -> throw KiritoException("10001")
        10011 -> throw KiritoException("10011")
        10012 -> throw KiritoException("10012")
        10013 -> throw KiritoException("10013")
        10014 -> throw KiritoException("10014")
        10021 -> throw KiritoException("10021")
        10031 -> throw KiritoException("10031")
        10032 -> throw KiritoException("10032")
        10033 -> throw KiritoException("10033")
        10034 -> throw KiritoException("10034")
        10101 -> throw KiritoException("10101")
        in 10000..20000 -> return (codigo ?: 0) == 0
        else -> throw KiritoException("Error. Código: $codigo, descripción: $descripcion")
    }
    return hayError == 0
}