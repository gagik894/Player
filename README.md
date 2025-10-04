# Music Player

A modern, cross-platform music player application built with Kotlin Multiplatform and Compose
Multiplatform. The application delivers a consistent user experience across Android, iOS, and Web
platforms while leveraging platform-specific capabilities for optimal performance.

## Overview

This music player demonstrates the capabilities of Kotlin Multiplatform (KMP) by sharing business
logic, data models, and UI components across multiple platforms. The application features a clean
architecture with clear separation of concerns, implementing the Model-View-Intent (MVI) pattern for
predictable state management.

## Features

### Core Functionality

- **Playback Controls**: Play, pause, skip forward/backward with smooth transitions
- **Queue Management**: Dynamic playback queue
- **Shuffle & Repeat**: Support for shuffle mode and multiple repeat modes (off, one, all)
- **Seek Functionality**: Precise seeking within tracks with visual progress indication
- **Favorites Management**: Mark tracks as favorites for quick access
- **Search**: Search across tracks, albums, artists, and playlists

### Content Organization

- **Browse by Category**: Navigate through tracks, albums, artists, and playlists
- **Album Details**: View complete album information with track listings
- **Artist Profiles**: Explore artist discographies with albums and tracks
- **Playlist Collections**: Curated playlists with thematic music groupings
- **Home Screen**: Featured content with all and favorite tracks

### User Interface

- **Material Design 3**: Modern UI following Material You design principles
- **Adaptive Layout**: Responsive design that adapts to different screen sizes
- **Smooth Animations**: Polished transitions and micro-interactions
- **Mini Player**: Persistent playback controls accessible from all screens
- **Full Player View**: Immersive full-screen player with queue visualization

### Platform Support

- **Android** (API 24+)
- **iOS** (Arm64, Simulator Arm64)
- **Web** (JavaScript & WebAssembly)

## Architecture

### Project Structure

```
composeApp/
├── src/
│   ├── commonMain/          # Shared code across all platforms
│   │   ├── kotlin/
│   │   │   └── com/example/player/
│   │   │       ├── data/              # Data layer
│   │   │       │   ├── repository/    # Repository implementations
│   │   │       │   └── source/        # Data sources
│   │   │       ├── domain/            # Business logic
│   │   │       │   ├── model/         # Domain models
│   │   │       │   ├── repository/    # Repository interfaces
│   │   │       │   ├── usecase/       # Use cases
│   │   │       │   └── player/        # Platform player abstraction
│   │   │       ├── presentation/      # Presentation layer
│   │   │       │   ├── mvi/           # ViewModels (MVI pattern)
│   │   │       │   ├── ui/            # Compose UI components
│   │   │       │   └── theme/         # Theming
│   │   │       ├── navigation/        # Navigation logic
│   │   │       └── di/                # Dependency injection
│   │   └── composeResources/   # Shared resources
│   ├── androidMain/         # Android-specific code
│   ├── iosMain/            # iOS-specific code
│   ├── jsMain/             # JavaScript-specific code
│   └── wasmJsMain/         # WebAssembly-specific code
```

### Architectural Patterns

#### Clean Architecture

The application follows Clean Architecture principles with three distinct layers:

1. **Presentation Layer**: Compose UI components and ViewModels managing UI state
2. **Domain Layer**: Business logic, use cases, and domain models
3. **Data Layer**: Repository implementations and data sources

#### Model-View-Intent (MVI)

State management follows the MVI pattern:

- **Model**: Immutable view states representing UI at any point in time
- **View**: Compose functions observing and rendering state
- **Intent**: User actions and events triggering state changes

#### Repository Pattern

Data access is abstracted through repository interfaces:

- `MusicRepository`: Manages music library data (tracks, albums, artists, playlists)
- `PlayerRepository`: Handles playback state and controls

### Platform Abstraction

Platform-specific functionality is abstracted using Kotlin's `expect`/`actual` mechanism:

#### Media Playback

- **Android**: ExoPlayer for robust media playback
- **iOS**: AVPlayer for native iOS integration
- **Web**: HTML5 Audio API for browser-based playback

```kotlin
expect class PlatformPlayer(platformContext: PlatformContext) {
    val playerState: StateFlow<PlayerState>
    fun prepare(url: String)
    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Duration)
    fun release()
}
```

Each platform provides an `actual` implementation optimized for its ecosystem.

## Music Data Source

### Data Acquisition Strategy

The application uses a **bundled mock data source** approach for demonstration purposes:

#### Implementation Details

- **Source**: `MockMusicDataSource` class in `data/source/`
- **Content**: Curated collection of royalty-free music and artwork
- **Music**: Sourced from [FreeSound](https://freesound.org/) - Royality free audio
- **Artwork**: Sourced from [Pexels](https://www.pexels.com/) - Free-to-use images

#### Data Structure

The mock data source provides:

- **6 Artists**: Luminar, Urban Echoes, Digital Pulse, Fireside Collective, Nova Beats, Midnight
  Strings
- **8 Albums**: Each with 3-4 tracks
- **27 Tracks**: Complete with metadata, artwork, and audio URLs
- **6 Playlists**: Thematic collections spanning different genres

#### Rationale for Bundled Approach

1. **Demonstration Focus**: Emphasizes application architecture and UI/UX rather than backend
   integration
2. **Consistent Experience**: Provides reliable data for showcasing features

#### Alternative Approaches

The architecture supports seamless transition to other data sources:

**Remote API Integration**:

```kotlin
class RemoteMusicDataSource(private val apiClient: HttpClient) {
    suspend fun fetchTracks(): List<Track> {
        return apiClient.get("https://api.example.com/tracks")
    }
}
```

**Local Device Library** (Android example):

```kotlin
class LocalMusicDataSource(private val contentResolver: ContentResolver) {
    suspend fun scanDeviceMusic(): List<Track> {
        // Query MediaStore for local audio files
    }
}
```

**Hybrid Approach**:
Combine multiple data sources with a unified repository interface, allowing both streaming and local
playback.

### Audio Streaming

Tracks are streamed directly from CDN URLs:

- No local file storage required
- Adaptive buffering based on network conditions
- Platform-native media players handle caching automatically

## State Management

### Playback State

The `PlaybackState` model encapsulates all playback-related information:

```kotlin
data class PlaybackState(
    val currentTrack: Track? = null,
    val queue: List<Track> = emptyList(),
    val currentTrackIndex: Int = 0,
    val isPlaying: Boolean = false,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val currentPosition: Duration = Duration.ZERO,
    val totalDuration: Duration = Duration.ZERO
)
```

### State Flow

1. User interacts with UI (e.g., clicks play button)
2. UI dispatches an `Intent` to the ViewModel
3. ViewModel processes intent, calls appropriate use case
4. Use case executes business logic via repository
5. Repository updates state and emits new state via Flow
6. ViewModel transforms state into `ViewState`
7. Compose UI recomposes with new state

## Getting Started

### Prerequisites

- **JDK**: 17 or higher
- **Android Studio**: Ladybug (2024.2.1) or later
- **Xcode**: 15+ (for iOS development)
- **Kotlin Plugin**: Latest version

### Building the Project

#### Android

```bash
./gradlew :composeApp:assembleDebug
```

Run on device/emulator:

```bash
./gradlew :composeApp:installDebug
```

#### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and build, or use Kotlin Multiplatform Mobile plugin in
Android Studio.

#### Web (JavaScript)

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

#### Web (WebAssembly)

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## Known Issues

### Favorites State Inconsistency

**Description**: Under certain conditions, the favorite status of a track may become inconsistent
across different views.

## Technical Highlights

### Multiplatform Media Player

The `PlatformPlayer` abstraction enables platform-specific optimizations:

- Android's ExoPlayer provides advanced features like gapless playback
- iOS's AVPlayer integrates with system media controls and lock screen
- Web implementation uses HTML5 Audio with fallback support

### Image Loading & Caching

Coil 3 provides efficient image loading across all platforms:

- Memory caching with configurable size limits
- Disk caching for offline access
- Automatic placeholder and error handling
- Crossfade animations for smooth loading

### Type-Safe Navigation

Navigation uses Kotlin serialization for type-safe argument passing:

```kotlin
@Serializable
data class AlbumDetail(val albumId: String) : PlayerDestination

// Usage
navController.navigate(PlayerDestination.AlbumDetail("album-id"))
```

### UI Responsiveness

- State updates on main thread
- Heavy operations offloaded to IO dispatcher
- Debounced search queries to reduce overhead

## Future Enhancements

Potential improvements for production deployment:

1. **Backend Integration**: Replace mock data source with REST API client
2. **Offline Mode**: Download tracks for offline playback
3. **User Authentication**: Personal libraries and cross-device sync
4. **Lyrics Display**: Time-synced lyrics integration
5. **Equalizer**: Platform-specific audio processing
6. **Analytics**: Track user engagement and playback patterns

## Acknowledgments

- **JetBrains**: Kotlin Multiplatform and Compose Multiplatform frameworks
- **FreeSound Community**: Royalty-free music content
- **Pexels Contributors**: High-quality photography