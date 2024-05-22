@file:OptIn(KoinExperimentalAPI::class)


package es.kirito.kirito.menu.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun MenuScreen(navController: NavHostController) {
    val viewModel = koinViewModel<MenuViewModel>()
}