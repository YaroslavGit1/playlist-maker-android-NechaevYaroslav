package com.example.playlist_maker_android_nechaevyaroslav.ui.navigation

enum class Destination(val route: String) {
    Main("main"),
    Search("search"),
    Playlists("playlists"),
    PlaylistDetails("playlist/{playlistId}"),
    Favorites("favorites"),
    NewPlaylist("new_playlist"),
    Settings("settings"),
    Details("details/{trackJson}"),
}
