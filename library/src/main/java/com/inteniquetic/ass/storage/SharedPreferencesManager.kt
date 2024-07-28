package com.inteniquetic.ass.storage

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPreferencesManager(
    private val context: Context,
    private val preferencesName: PreferencesName,
) : PreferencesManager {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun secure(): SharedPreferences {
        val key = MasterKey.Builder(context)
            .setKeyGenParameterSpec(
                KeyGenParameterSpec.Builder(
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setKeySize(256)
                    .build()
            )
            .build()

        return EncryptedSharedPreferences.create(
            context,
            preferencesName.name(),
            key,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun insecure(): SharedPreferences {
        val sharedPreferences = context.applicationContext.getSharedPreferences(
            preferencesName.name(),
            Context.MODE_PRIVATE
        )
        return sharedPreferences
    }
}