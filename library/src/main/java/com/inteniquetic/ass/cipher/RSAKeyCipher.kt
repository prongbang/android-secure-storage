package com.inteniquetic.ass.cipher

import com.inteniquetic.ass.key.KeyPairManager
import com.inteniquetic.ass.key.KeyStoreFactory
import java.security.Key
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher

abstract class RSAKeyCipher(
    keyStoreFactory: KeyStoreFactory,
    private val keyPairManager: KeyPairManager,
) : KeyCipher {

    init {
        keyStoreFactory.create()
    }

    @Throws(Exception::class)
    abstract fun getCipher(): Cipher

    abstract fun getAlgorithmParameterSpec(): AlgorithmParameterSpec?

    override fun wrap(key: Key): ByteArray {
        val publicKey: PublicKey = keyPairManager.getPublicKey()
        val cipher: Cipher = getCipher()
        cipher.init(Cipher.WRAP_MODE, publicKey, getAlgorithmParameterSpec())

        return cipher.wrap(key)
    }

    override fun unwrap(wrappedKey: ByteArray, algorithm: String): Key {
        val privateKey: PrivateKey = keyPairManager.getPrivateKey()
        val cipher: Cipher = getCipher()
        cipher.init(Cipher.UNWRAP_MODE, privateKey, getAlgorithmParameterSpec())

        return cipher.unwrap(wrappedKey, algorithm, Cipher.SECRET_KEY)
    }

}