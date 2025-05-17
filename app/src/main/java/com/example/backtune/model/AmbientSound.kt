package com.example.backtune.model

data class AmbientSound(
    val id: String,
    val name: String,
    val resourceName: String // Remove default value to ensure proper resource names
) 