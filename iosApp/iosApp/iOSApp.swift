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


/*
//La famosa app de ejemplo de la que nos hemos copiado.
https://github.com/joreilly/FantasyPremierLeague/blob/main/ios/FantasyPremierLeague/FantasyPremierLeague/FantasyPremierLeagueApp.swift
 */