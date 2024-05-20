import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    // Inicializamos DataStore
    init() {
        KoinKt.doInitKoin()// Esto es lo que acabo de meter!!
        MainViewControllerKt.initialize()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}


//Buenas, Héctor:
/*
Esto es lo que he encontrado de cómo se hace por lado de iOS, donde hay que meter el initKoin{}
En este archivo hay algo de Koin, no sé si es lo que buscamos o si te puede ayudar: https://github.com/joreilly/FantasyPremierLeague/blob/main/ios/FantasyPremierLeague/FantasyPremierLeague/FantasyPremierLeagueApp.swift
TEN EN CUENTA que en ese proyecto usa Koin para todo: Viewmodels, repos, internet y también database, así que lo mismo vale para luego y no ahora.

 */