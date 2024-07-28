package com.inteniquetic.ass.key

import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey

class AndroidKeyPairManager(
    private val keyAliasFactory: KeyAliasFactory,
) : KeyPairManager {

    override fun getPrivateKey(): PrivateKey {
        val keyAlias = keyAliasFactory.create()

        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)

        val key = ks.getKey(keyAlias, null)
            ?: throw Exception("No key found under alias: $keyAlias in AndroidKeyStore")

        if (key !is PrivateKey) {
            throw Exception("Key found under alias: $keyAlias is not an instance of PrivateKey")
        }

        return key
    }

    override fun getPublicKey(): PublicKey {
        val keyAlias = keyAliasFactory.create()

        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)

        val cert = ks.getCertificate(keyAlias)
            ?: throw Exception("No certificate found under alias: $keyAlias in AndroidKeyStore")

        val key = cert.publicKey
            ?: throw Exception("No public key found in certificate under alias: $keyAlias")

        return key
    }

}