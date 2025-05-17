package com.example.backtune.model

data class AmbientSound(
    val id: String,
    val name: String,
    val resourceName: String = "rain" // Name of the raw resource file
) 