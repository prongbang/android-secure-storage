package com.inteniquetic.ass.crypto

import com.inteniquetic.ass.cipher.KeyCipher
import com.inteniquetic.ass.storage.PreferencesManager
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

class CBCAESCryptography(
    keyCipher: KeyCipher,
    preferencesManager: PreferencesManager,
) : AESCryptography(keyCipher, preferencesManager) {

    override fun getPreferencesKey(): String =
        "172ddba7f888041a1b6c4ef211e3c975c384761eff99d07d494bcdb4898d7800"

    @Throws(Exception::class)
    override fun getCipher(): Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

    override fun getIvSize(): Int = 16

    override fun getParameterSpec(iv: ByteArray): AlgorithmParameterSpec = IvParameterSpec(iv)
}