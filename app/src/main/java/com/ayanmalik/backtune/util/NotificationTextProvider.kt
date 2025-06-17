package com.ayanmalik.backtune.util

/**
 * Provides random notification texts for relaxation reminders
 */
object NotificationTextProvider {
    private val notificationTexts = listOf(
        NotificationText(
            title = "Time for Your Daily Relaxation! 🌟",
            content = "Your perfect relaxation session awaits with BackTune",
            expandedText = "Take a moment to unwind with your favorite ambient sounds. Let BackTune create your perfect relaxation atmosphere! 🎧"
        ),
        NotificationText(
            title = "Relaxation Time! 🎵",
            content = "BackTune is ready to enhance your mood",
            expandedText = "Transform your space into a peaceful sanctuary with BackTune's ambient sounds. Your perfect relaxation moment is just a tap away! 🌿"
        ),
        NotificationText(
            title = "Your Relaxation Reminder ⭐",
            content = "Time to create your perfect soundscape",
            expandedText = "Mix your favorite YouTube content with soothing ambient sounds. Let BackTune help you find your perfect relaxation balance! 🎶"
        ),
        NotificationText(
            title = "Ready for Relaxation? 🌙",
            content = "BackTune is here to enhance your experience",
            expandedText = "Create your ideal relaxation environment with BackTune. Combine your favorite videos with calming ambient sounds! 🎧"
        ),
        NotificationText(
            title = "Relaxation Awaits! ✨",
            content = "Your perfect sound therapy session is ready",
            expandedText = "Take a break and let BackTune create your perfect relaxation atmosphere. Your favorite content with ambient sounds awaits! 🌟"
        ),
        NotificationText(
            title = "Time to Unwind! 🎶",
            content = "BackTune is here to enhance your relaxation",
            expandedText = "Transform your space into a peaceful haven with BackTune. Your perfect relaxation session is just a tap away! 🌿"
        ),
        NotificationText(
            title = "Your Relaxation Moment! 🌟",
            content = "Create your perfect soundscape with BackTune",
            expandedText = "Mix your favorite content with soothing ambient sounds. Let BackTune help you find your perfect relaxation balance! 🎧"
        ),
        NotificationText(
            title = "Ready for Your Sound Therapy? 🎵",
            content = "BackTune is waiting to enhance your experience",
            expandedText = "Take a moment to relax with BackTune. Your perfect combination of content and ambient sounds awaits! ✨"
        )
    )

    fun getRandomNotificationText(): NotificationText {
        return notificationTexts.random()
    }
}

data class NotificationText(
    val title: String,
    val content: String,
    val expandedText: String
) 