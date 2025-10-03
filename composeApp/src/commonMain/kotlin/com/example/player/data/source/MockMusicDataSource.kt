package com.example.player.data.source

import com.example.player.domain.model.Album
import com.example.player.domain.model.Artist
import com.example.player.domain.model.Playlist
import com.example.player.domain.model.Track
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * A mock data source that provides a structured, interconnected library of
 * royalty-free music and associated metadata.
 *
 * - Music is sourced from FreeSound Music (https://freesound.org/).
 * - Images are sourced from Pexels (https://www.pexels.com/).
 *
 * This class simulates a real-world data source, allowing the app to feature
 * artists, albums, tracks, and playlists with unique artwork for each track.
 */
class MockMusicDataSource {

    // =====================================================================================
    //  BASE DATA: ARTISTS
    // =====================================================================================
    private val artists = listOf(
        Artist(
            id = "artist-luminar",
            name = "Luminar",
            bio = "Ethereal sounds and ambient textures from the cosmos. Luminar creates immersive soundscapes that transport listeners to distant galaxies and inner peace.",
            imageUrl = "https://images.pexels.com/photos/1048033/pexels-photo-1048033.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Artist(
            id = "artist-urban-echoes",
            name = "Urban Echoes",
            bio = "Lo-fi hip hop and chillhop producer capturing the essence of city life after midnight. Known for jazzy samples and rainy day vibes.",
            imageUrl = "https://images.pexels.com/photos/1638324/pexels-photo-1638324.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Artist(
            id = "artist-digital-pulse",
            name = "Digital Pulse",
            bio = "Electronic music pioneer blending synthwave, cyberpunk, and futuristic beats. Creating the soundtrack for tomorrow's world.",
            imageUrl = "https://images.pexels.com/photos/5053840/pexels-photo-5053840.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Artist(
            id = "artist-fireside",
            name = "Fireside Collective",
            bio = "Indie folk ensemble bringing warmth and storytelling through acoustic arrangements. Perfect for cozy evenings and introspective moments.",
            imageUrl = "https://images.pexels.com/photos/1190297/pexels-photo-1190297.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Artist(
            id = "artist-nova-beats",
            name = "Nova Beats",
            bio = "High-energy EDM producer known for festival anthems and progressive house tracks that ignite dance floors worldwide.",
            imageUrl = "https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Artist(
            id = "artist-midnight-strings",
            name = "Midnight Strings",
            bio = "Classical crossover artist reimagining timeless compositions with modern production. Bridging centuries of musical heritage.",
            imageUrl = "https://images.pexels.com/photos/164821/pexels-photo-164821.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        )
    )

    // =====================================================================================
    //  BASE DATA: ALBUMS
    // =====================================================================================
    private val albums = listOf(
        // Luminar Albums
        Album(
            id = "album-dreamscapes",
            title = "Dreamscapes",
            artist = artists[0],
            year = 2023,
            trackCount = 3,
            artworkUrl = "https://images.pexels.com/photos/220182/pexels-photo-220182.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Album(
            id = "album-celestial-voyage",
            title = "Celestial Voyage",
            artist = artists[0],
            year = 2024,
            trackCount = 4,
            artworkUrl = "https://images.pexels.com/photos/2150/sky-space-dark-galaxy.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),

        // Urban Echoes Albums
        Album(
            id = "album-sidewalk-sonnets",
            title = "Sidewalk Sonnets",
            artist = artists[1],
            year = 2024,
            trackCount = 3,
            artworkUrl = "https://images.pexels.com/photos/813011/pexels-photo-813011.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Album(
            id = "album-late-night-tapes",
            title = "Late Night Tapes Vol. 1",
            artist = artists[1],
            year = 2023,
            trackCount = 4,
            artworkUrl = "https://images.pexels.com/photos/1616403/pexels-photo-1616403.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),

        // Digital Pulse Albums
        Album(
            id = "album-megastructure",
            title = "Megastructure",
            artist = artists[2],
            year = 2024,
            trackCount = 3,
            artworkUrl = "https://images.pexels.com/photos/995766/pexels-photo-995766.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Album(
            id = "album-neon-dreams",
            title = "Neon Dreams",
            artist = artists[2],
            year = 2023,
            trackCount = 3,
            artworkUrl = "https://images.pexels.com/photos/1939485/pexels-photo-1939485.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),

        // Fireside Collective Albums
        Album(
            id = "album-whispers-of-the-wood",
            title = "Whispers of the Wood",
            artist = artists[3],
            year = 2022,
            trackCount = 3,
            artworkUrl = "https://images.pexels.com/photos/159711/books-book-pages-read-literature-159711.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Album(
            id = "album-autumn-tales",
            title = "Autumn Tales",
            artist = artists[3],
            year = 2023,
            trackCount = 3,
            artworkUrl = "https://images.pexels.com/photos/1261728/pexels-photo-1261728.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),

        // Nova Beats Albums
        Album(
            id = "album-electric-horizon",
            title = "Electric Horizon",
            artist = artists[4],
            year = 2024,
            trackCount = 4,
            artworkUrl = "https://images.pexels.com/photos/1763075/pexels-photo-1763075.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
    )

    // =====================================================================================
    //  BASE DATA: TRACKS
    // =====================================================================================
    private val tracks = listOf(
        // Album 1: Dreamscapes by Luminar
        Track(
            id = "track-01",
            title = "Starlight Guiding",
            artist = artists[0],
            album = albums[0],
            duration = 2.minutes + 5.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827828_2771755-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1252890/pexels-photo-1252890.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-02",
            title = "Nebula's Embrace",
            artist = artists[0],
            album = albums[0],
            duration = 51.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827506_16463170-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1118873/pexels-photo-1118873.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-03",
            title = "Cosmic Dust",
            artist = artists[0],
            album = albums[0],
            duration = 45.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827182_14312064-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/176851/pexels-photo-176851.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),

        // Album 2: Celestial Voyage by Luminar
        Track(
            id = "track-04",
            title = "Aurora Borealis",
            artist = artists[0],
            album = albums[1],
            duration = 1.minutes + 25.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827125_16857550-lq.mp3 ",
            artworkUrl = "https://images.pexels.com/photos/1933239/pexels-photo-1933239.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-05",
            title = "Interstellar Drift",
            artist = artists[0],
            album = albums[1],
            duration = 1.minutes + 46.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827112_14312064-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/2085998/pexels-photo-2085998.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-06",
            title = "Gravity's Dance",
            artist = artists[0],
            album = albums[1],
            duration = 3.minutes + 36.seconds,
            audioUrl = "https://cdn.freesound.org/previews/826/826337_5187472-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1169754/pexels-photo-1169754.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),
        Track(
            id = "track-07",
            title = "Moonlit Resonance",
            artist = artists[0],
            album = albums[1],
            duration = 1.minutes + 6.seconds,
            audioUrl = "https://cdn.freesound.org/previews/826/826047_14312064-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/792381/pexels-photo-792381.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 4
        ),

        // Album 3: Sidewalk Sonnets by Urban Echoes
        Track(
            id = "track-08",
            title = "Rain on Cobblestone",
            artist = artists[1],
            album = albums[2],
            duration = 2.minutes + 8.seconds,
            audioUrl = "https://cdn.freesound.org/previews/825/825764_17830854-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1624496/pexels-photo-1624496.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-09",
            title = "Midnight Café",
            artist = artists[1],
            album = albums[2],
            duration = 43.seconds,
            audioUrl = "https://cdn.freesound.org/previews/825/825589_11861866-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/733745/pexels-photo-733745.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-10",
            title = "Graffiti Heart",
            artist = artists[1],
            album = albums[2],
            duration = 52.seconds,
            audioUrl = "https://cdn.freesound.org/previews/825/825539_12880153-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/163036/mario-luigi-yoschi-figures-163036.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),

        // Album 4: Late Night Tapes Vol. 1 by Urban Echoes
        Track(
            id = "track-11",
            title = "City Lights Fade",
            artist = artists[1],
            album = albums[3],
            duration = 1.minutes,
            audioUrl = "https://cdn.freesound.org/previews/824/824761_12880153-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1486222/pexels-photo-1486222.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-12",
            title = "Subway Serenade",
            artist = artists[1],
            album = albums[3],
            duration = 2.minutes + 40.seconds,
            audioUrl = "https://cdn.freesound.org/previews/823/823846_12880153-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-13",
            title = "Neon Reflections",
            artist = artists[1],
            album = albums[3],
            duration = 1.minutes + 8.seconds,
            audioUrl = "https://cdn.freesound.org/previews/823/823125_14312064-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/2166711/pexels-photo-2166711.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),
        Track(
            id = "track-14",
            title = "Dawn's First Train",
            artist = artists[1],
            album = albums[3],
            duration = 1.minutes + 21.seconds,
            audioUrl = "https://cdn.freesound.org/previews/822/822521_17781179-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/457881/pexels-photo-457881.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 4
        ),

        // Album 5: Megastructure by Digital Pulse
        Track(
            id = "track-15",
            title = "Data Spire",
            artist = artists[2],
            album = albums[4],
            duration = 1.minutes + 50.seconds,
            audioUrl = "https://cdn.freesound.org/previews/822/822445_17776809-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/933054/pexels-photo-933054.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-16",
            title = "Circuit Overdrive",
            artist = artists[2],
            album = albums[4],
            duration = 2.minutes + 30.seconds,
            audioUrl = "https://cdn.pixabay.com/audio/2023/09/04/audio_35507a2249.mp3",
            artworkUrl = "https://images.pexels.com/photos/2156/sky-earth-space-working.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-17",
            title = "Neon Arteries",
            artist = artists[2],
            album = albums[4],
            duration = 1.minutes + 5.seconds,
            audioUrl = "https://cdn.freesound.org/previews/822/822446_17776809-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/110854/pexels-photo-110854.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),

        // Album 6: Neon Dreams by Digital Pulse
        Track(
            id = "track-18",
            title = "Synthwave Sunset",
            artist = artists[2],
            album = albums[5],
            duration = 2.minutes + 8.seconds,
            audioUrl = "https://cdn.freesound.org/previews/825/825764_17830854-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1563356/pexels-photo-1563356.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-19",
            title = "Digital Horizon",
            artist = artists[2],
            album = albums[5],
            duration = 0.minutes + 43.seconds,
            audioUrl = "https://cdn.freesound.org/previews/825/825589_11861866-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1089438/pexels-photo-1089438.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-20",
            title = "Cyberpunk Nights",
            artist = artists[2],
            album = albums[5],
            duration = 3.minutes + 36.seconds,
            audioUrl = "https://cdn.freesound.org/previews/826/826337_5187472-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/325185/pexels-photo-325185.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),

        // Album 7: Whispers of the Wood by Fireside Collective
        Track(
            id = "track-21",
            title = "Morning Dew",
            artist = artists[3],
            album = albums[6],
            duration = 0.minutes + 45.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827182_14312064-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/673857/pexels-photo-673857.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-22",
            title = "Cinder & Pine",
            artist = artists[3],
            album = albums[6],
            duration = 1.minutes + 50.seconds,
            audioUrl = "https://cdn.freesound.org/previews/822/822445_17776809-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/268791/pexels-photo-268791.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-23",
            title = "Woven Roots",
            artist = artists[3],
            album = albums[6],
            duration = 2.minutes + 40.seconds,
            audioUrl = "https://cdn.freesound.org/previews/823/823846_12880153-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/56832/road-asphalt-space-sky-56832.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),

        // Album 8: Autumn Tales by Fireside Collective
        Track(
            id = "track-24",
            title = "Maple Leaves Falling",
            artist = artists[3],
            album = albums[7],
            duration = 2.minutes + 8.seconds,
            audioUrl = "https://cdn.freesound.org/previews/825/825764_17830854-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1619317/pexels-photo-1619317.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 1
        ),
        Track(
            id = "track-25",
            title = "Harvest Moon",
            artist = artists[3],
            album = albums[7],
            duration = 2.minutes + 5.seconds,
            audioUrl = "https://cdn.freesound.org/previews/827/827828_2771755-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/1275929/pexels-photo-1275929.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 2
        ),
        Track(
            id = "track-26",
            title = "Cabin Stories",
            artist = artists[3],
            album = albums[7],
            duration = 1.minutes + 5.seconds,
            audioUrl = "https://cdn.freesound.org/previews/822/822446_17776809-lq.mp3",
            artworkUrl = "https://images.pexels.com/photos/247600/pexels-photo-247600.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            trackNumber = 3
        ),
    )

    // =====================================================================================
    //  DERIVED DATA: PLAYLISTS
    // =====================================================================================
    private val playlists = listOf(
        Playlist(
            id = "playlist-cosmic-drift",
            name = "Cosmic Drift",
            description = "Ambient tracks for focus and relaxation. Let your mind wander through infinite space.",
            tracks = listOf(
                tracks[0], // Starlight Guiding
                tracks[2], // Cosmic Dust
                tracks[3], // Aurora Borealis
                tracks[5], // Gravity's Dance
                tracks[16] // Neon Arteries
            ),
            coverUrl = "https://images.pexels.com/photos/1274260/pexels-photo-1274260.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Playlist(
            id = "playlist-urban-lofi",
            name = "Urban Lo-Fi",
            description = "Chill beats for studying, relaxing, or a rainy day. Perfect for late-night coding sessions.",
            tracks = listOf(
                tracks[7],  // Rain on Cobblestone
                tracks[8],  // Midnight Café
                tracks[9],  // Graffiti Heart
                tracks[10], // City Lights Fade
                tracks[11], // Subway Serenade
                tracks[13]  // Dawn's First Train
            ),
            coverUrl = "https://images.pexels.com/photos/1547813/pexels-photo-1547813.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Playlist(
            id = "playlist-folk-and-firelight",
            name = "Folk & Firelight",
            description = "Acoustic and warm tracks for a cozy evening by the fire. Storytelling at its finest.",
            tracks = listOf(
                tracks[20], // Morning Dew
                tracks[21], // Cinder & Pine
                tracks[22], // Woven Roots
                tracks[23], // Maple Leaves Falling
                tracks[24], // Harvest Moon
                tracks[25]  // Cabin Stories
            ),
            coverUrl = "https://images.pexels.com/photos/247929/pexels-photo-247929.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Playlist(
            id = "playlist-synthwave-nights",
            name = "Synthwave Nights",
            description = "Retro-futuristic vibes and neon-soaked dreams. Drive into the digital sunset.",
            tracks = listOf(
                tracks[17], // Synthwave Sunset
                tracks[18], // Digital Horizon
                tracks[19], // Cyberpunk Nights
                tracks[14], // Data Spire
                tracks[15]  // Circuit Overdrive
            ),
            coverUrl = "https://images.pexels.com/photos/2246476/pexels-photo-2246476.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Playlist(
            id = "playlist-classical-modern",
            name = "Classical Reimagined",
            description = "Timeless compositions meet modern production. Where heritage meets innovation.",
            tracks = listOf(
                tracks[10], // Nocturne in Blue
                tracks[4], // Velvet Reverie
                tracks[2], // Timeless Waltz
                tracks[1]   // Nebula's Embrace
            ),
            coverUrl = "https://images.pexels.com/photos/210764/pexels-photo-210764.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Playlist(
            id = "playlist-deep-focus",
            name = "Deep Focus",
            description = "Instrumental tracks to enhance concentration and productivity. Zone in and get things done.",
            tracks = listOf(
                tracks[4],  // Interstellar Drift
                tracks[6],  // Moonlit Resonance
                tracks[20], // Nocturne in Blue
                tracks[0],  // Starlight Guiding
                tracks[12]  // Neon Reflections
            ),
            coverUrl = "https://images.pexels.com/photos/267569/pexels-photo-267569.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        Playlist(
            id = "playlist-sunset-chill",
            name = "Sunset Chill",
            description = "Mellow vibes for golden hour. Watch the day fade away with these smooth tracks.",
            tracks = listOf(
                tracks[17], // Synthwave Sunset
                tracks[8],  // Midnight Café
                tracks[24], // Harvest Moon
                tracks[21]  // Cinder & Pine
            ),
            coverUrl = "https://images.pexels.com/photos/158163/clouds-cloudporn-weather-lookup-158163.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        )
    )

    // =====================================================================================
    //  PUBLIC API
    // =====================================================================================
    fun getTracks(): List<Track> = tracks
    fun getAlbums(): List<Album> = albums
    fun getArtists(): List<Artist> = artists
    fun getPlaylists(): List<Playlist> = playlists
}