package com.rutchem.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform