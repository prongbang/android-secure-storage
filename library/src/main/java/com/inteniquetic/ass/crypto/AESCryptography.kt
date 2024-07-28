package com.inteniquetic.ass.crypto

import android.util.Base64
import android.util.Log
import com.inteniquetic.ass.cipher.KeyCipher
import com.inteniquetic.ass.storage.PreferencesManager
import java.security.Key
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

abstract class AESCryptography(
    private val keyCipher: KeyCipher,
    private val preferencesManager: PreferencesManager,
) : Cryptography {

    private val secureRandom = SecureRandom()
    private val keyPreferencesKey by lazy { getPreferencesKey() }
    private val preferences by lazy { preferencesManager.insecure() }
    private lateinit var secretKey: Key

    init {
        initial()
    }

    abstract fun getCipher(): Cipher
    abstract fun getIvSize(): Int
    abstract fun getPreferencesKey(): String
    abstract fun getParameterSpec(iv: ByteArray): AlgorithmParameterSpec

    private fun initial() {
        val editor = preferences.edit()
        val secret = preferences.getString(keyPreferencesKey, null)

        // Get secret key is exist
        if (secret != null) {
            try {
                val encrypted = Base64.decode(secret, Base64.DEFAULT)
                secretKey = keyCipher.unwrap(encrypted, "AES")
                return
            } catch (e: Exception) {
                Log.e("AESCryptography", "Failed to unwrap secret key during decryption process", e)
            }
        }

        // Generate new secret key
        val key = ByteArray(16)
        secureRandom.nextBytes(key)
        secretKey = SecretKeySpec(key, "AES")

        val encryptedKey = keyCipher.wrap(secretKey)
        editor.putString(keyPreferencesKey, Base64.encodeToString(encryptedKey, Base64.DEFAULT))
        editor.apply()
    }

    override fun encrypt(input: ByteArray): ByteArray? {
        val iv = ByteArray(getIvSize())
        secureRandom.nextBytes(iv)

        val ivParameterSpec = getParameterSpec(iv)

        val cipher = getCipher()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)

        val payload = cipher.doFinal(input)
        val combined = ByteArray(iv.size + payload.size)

        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(payload, 0, combined, iv.size, payload.size)

        return combined
    }

    override fun decrypt(input: ByteArray): ByteArray? {
        val iv = ByteArray(getIvSize())
        System.arraycopy(input, 0, iv, 0, iv.size)
        val ivParameterSpec = getParameterSpec(iv)

        val payloadSize = input.size - getIvSize()
        val payload = ByteArray(payloadSize)
        System.arraycopy(input, iv.size, payload, 0, payloadSize)

        val cipher = getCipher()
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)

        return cipher.doFinal(payload)
    }
}