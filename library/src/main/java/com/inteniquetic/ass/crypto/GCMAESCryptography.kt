package com.inteniquetic.ass.crypto

import com.inteniquetic.ass.cipher.KeyCipher
import com.inteniquetic.ass.storage.PreferencesManager
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

class GCMAESCryptography(
    keyCipher: KeyCipher,
    preferencesManager: PreferencesManager,
) : AESCryptography(keyCipher, preferencesManager) {

    override fun getPreferencesKey(): String =
        "be6df0945d5186cd094a880895822218379e648c30e6f178949a1b5802e55485"

    override fun getCipher(): Cipher = Cipher.getInstance("AES/GCM/NoPadding")

    override fun getIvSize(): Int = 12

    override fun getParameterSpec(iv: ByteArray): AlgorithmParameterSpec =
        GCMParameterSpec(128, iv)
}