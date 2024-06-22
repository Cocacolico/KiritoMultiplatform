package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun HeaderWithBack(title: String, onBackClick: ()->Unit) {
    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center) {
        TitleText(title)
        FloatingActionButton(
            modifier = Modifier.align(Alignment.CenterStart),
            content = { Icon(Icons.Outlined.ArrowBackIosNew, "") },
            onClick = { onBackClick() }
        )
    }
}