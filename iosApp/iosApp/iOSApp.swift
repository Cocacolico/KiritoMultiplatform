import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    // Inicializamos DataStore
    init() { MainViewControllerKt.initialize() }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
