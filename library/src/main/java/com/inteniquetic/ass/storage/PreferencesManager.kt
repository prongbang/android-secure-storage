package com.inteniquetic.ass.storage

import android.content.SharedPreferences

interface PreferencesManager {
    fun secure(): SharedPreferences
    fun insecure(): SharedPreferences
}