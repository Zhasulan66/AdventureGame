package com.example.analysisgame.presentation

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class LanguageManager(private val context: Context) {
    /*init {
        setLanguage()
    }*/

    fun setLanguage(languageCode: String = "kk") {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun getCurrentLanguage(): String {
        return context.resources.configuration.locale.language
    }


    fun restartActivity() {
        (context as? Activity)?.recreate()
    }

}