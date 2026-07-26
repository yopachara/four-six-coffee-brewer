import SwiftUI
import Shared

/// Hosts the shared Compose UI. Everything the app draws comes from FlowSixRoot() in :shared, so
/// this is the only Swift view in the project.
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        // Compose draws its own insets via WindowInsets.safeDrawing, and the keyboard is handled
        // inside the Compose hierarchy too.
        ComposeView()
            .ignoresSafeArea(.all)
    }
}
