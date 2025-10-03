import SwiftUI

@main
struct iOSApp: App {
    init() {
        configureAudioSession()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

    private func configureAudioSession() {
            let audioSession = AVAudioSession.sharedInstance()
            do {
                try audioSession.setCategory(.playback, mode: .default)
                try audioSession.setActive(true)
            } catch {
                print("Failed to set up audio session: \(error)")
            }
        }
}