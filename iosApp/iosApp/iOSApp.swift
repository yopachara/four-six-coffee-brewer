import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        // Must run before any Composable resolves a ViewModel from the graph, which mirrors
        // FlowSixApplication.onCreate() on Android.
        MainViewControllerKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.all)
        }
    }
}
