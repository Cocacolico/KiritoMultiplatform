package es.kirito.kirito.core.presentation.components

import androidx.compose.runtime.Composable


@Composable
expect fun ShortToast(message: String)

@Composable
expect fun LongToast(message: String?)
