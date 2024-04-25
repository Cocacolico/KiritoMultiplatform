package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.presentation.theme.azulKirito
import es.kirito.kirito.core.presentation.theme.azulKiritoClaro
import es.kirito.kirito.core.presentation.theme.customColors
import es.kirito.kirito.core.presentation.theme.kiritoTypography


@Composable
fun MyTextStdWithPadding(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 14.sp
) {
    MyTextStd(
        text = text,
        modifier = modifier.padding(horizontal = 16.dp),
        textAlign = textAlign,
        fontSize = fontSize
    )
}

@Composable
fun MyTextStdWithPadding(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 14.sp
) {
    MyTextStd(
        text = text,
        modifier = modifier.padding(horizontal = 16.dp),
        textAlign = textAlign,
        fontSize = fontSize
    )
}

@Composable
fun MyTextStd(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 14.sp,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = kiritoTypography.bodyMedium,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines
    )
}

@Composable
fun MyTextStd(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 14.sp,
    color: Color
) {
    Text(
        text = text,
        modifier = modifier,
        style = kiritoTypography.bodyMedium,
        textAlign = textAlign,
        fontSize = fontSize,
        color = color
    )
}

@Composable
fun MyTextStd(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 14.sp
) {
    Text(
        text = text,
        modifier = modifier,
        style = kiritoTypography.bodyMedium,
        textAlign = textAlign,
        fontSize = fontSize
    )
}

@Composable
fun MyTextTareas(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 13.sp
) {
    Text(
        text = text,
        modifier = modifier,
        style = kiritoTypography.bodyMedium.copy(lineHeight = 15.sp),
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun MyTextSubTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 16.dp),
        style = kiritoTypography.bodyMedium,
        textAlign = textAlign,
        color = azulKiritoClaro,
        fontSize = 18.sp
    )
}


@Composable
fun ParagraphTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text, modifier = modifier,
        style = kiritoTypography.bodyMedium,
        fontSize = 18.sp,
    )
}

@Composable
fun ParagraphSubtitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text, modifier = modifier,
        style = kiritoTypography.bodyMedium,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun MyTextPocoImportante(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 11.sp,
        color = MaterialTheme.customColors.textoPocoImportante,
        modifier = modifier,
        style = kiritoTypography.bodySmall
    )
}

@Composable
fun TextClicable(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = modifier.clickable { onClick() },
        color = azulKiritoClaro,
        fontSize = 16.sp,
    )
}


@Composable
fun AutoResizeText(
    text: String,
    fontSizeRange: FontSizeRange,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
) {
    var fontSizeValue by remember { mutableFloatStateOf(fontSizeRange.max.value) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        style = style,
        fontSize = fontSizeValue.sp,
        onTextLayout = {
            if (it.didOverflowHeight && !readyToDraw) {
                val nextFontSizeValue = fontSizeValue - fontSizeRange.step.value
                if (nextFontSizeValue <= fontSizeRange.min.value) {
                    // Reached minimum, set minimum font size and it's readToDraw
                    fontSizeValue = fontSizeRange.min.value
                    readyToDraw = true
                } else {
                    // Text doesn't fit yet and haven't reached minimum text range, keep decreasing
                    fontSizeValue = nextFontSizeValue
                }
            } else {
                // Text fits before reaching the minimum, it's readyToDraw
                readyToDraw = true
            }
        },
        modifier = modifier.drawWithContent { if (readyToDraw) drawContent() }
    )
}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_STEP,
) {
    init {
        require(min < max) { "min should be less than max, $this" }
        require(step.value > 0) { "step should be greater than 0, $this" }
    }

    companion object {
        private val DEFAULT_TEXT_STEP = 2.sp
    }
}


@Composable
fun TitleText(titulo: String) {
    Box(
        modifier = Modifier
            .padding(PaddingValues(horizontal = 16.dp, vertical = 16.dp))
            .fillMaxWidth()
    ) {
        Text(
            text = titulo,
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            style = MaterialTheme.typography.titleMedium,
            color = azulKirito,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DialogTitleText(titulo: String) {
    Box(
        modifier = Modifier
            .padding(PaddingValues(horizontal = 16.dp))
            .fillMaxWidth()
    ) {
        Text(
            text = titulo,
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            style = MaterialTheme.typography.titleMedium,
            color = azulKirito,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * @param tipo si es null, se calcula del [turno]
 * **/
//TODO; arreglar.
//@Composable
//fun MyTextTurno(tipo: String?, turno: String?, modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    Box(
//        modifier = modifier
//            .clip(CircleShape)
//            .background(
//                color = Color.Companion.fromHex(
//                    colorDeFondoTurnos(
//                        tipo ?: turno.calcularTipoDelTurno()
//                    )
//                ), shape = CircleShape
//            )
//    ) {
//
//        Text(
//            text = nombreTurnosConTipo(
//                tipo ?: turno.calcularTipoDelTurno(),
//                turno ?: "-",
//                context
//            ),
//            color = Color.Companion.fromHex(colorTextoTurnos(tipo ?: turno.calcularTipoDelTurno())),
//            style = TextStyle(
//                background = Color.Companion.fromHex(
//                    colorDeFondoTurnos(
//                        tipo ?: turno.calcularTipoDelTurno()
//                    )
//                )
//            ),
//            modifier = Modifier
//                .padding(horizontal = 8.dp, vertical = 4.dp)
//        )
//    }
//}

//@Composable
//fun TextEtiqueta(tipo: String, valor: Int, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier,
//
//        ) {
//        Image(
//           painter = painterResource(id = R.drawable.ic_label_24),
//            colorFilter = ColorFilter.tint(Color.Companion.fromHex(colorDeFondoTurnos(tipo))),
//            contentDescription = "",
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier.matchParentSize()
//        )
//        Text(
//            text = "$valor $tipo",
//            modifier = Modifier
//                .align(Alignment.Center)
//                .padding(horizontal = 16.dp, vertical = 1.dp),
//         //TODO   color = Color.Companion.fromHex(colorTextoTurnos(tipo))
//        )
//    }
//}


@Composable
fun BigTextWarning(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
      //TODO  color = colorResource(id = Res.color.DarkOrange),
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun BigTextInfo(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun MyTextError(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun TextTituloTelefono(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = kiritoTypography.headlineMedium
    )
}

@Composable
fun TextSubtituloTelefono(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = kiritoTypography.titleMedium,
        fontStyle = FontStyle.Italic
    )
}