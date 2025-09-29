package com.example.player

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform