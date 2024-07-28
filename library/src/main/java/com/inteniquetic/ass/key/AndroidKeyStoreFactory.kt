package com.inteniquetic.ass.key

import android.os.Build
import com.inteniquetic.ass.cipher.spec.ParameterSpecFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.AlgorithmParameterSpec

class AndroidKeyStoreFactory(
    private val keyAliasFactory: KeyAliasFactory,
    private val legacyAlgorithmParameterSpecFactory: ParameterSpecFactory,
    private val algorithmParameterSpecFactory: ParameterSpecFactory,
) : KeyStoreFactory {

    override fun createSpec(): AlgorithmParameterSpec {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            legacyAlgorithmParameterSpecFactory.create()
        } else {
            algorithmParameterSpecFactory.create()
        }
    }

    override fun create() {
        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)

        val privateKey = ks.getKey(keyAliasFactory.create(), null)
        if (privateKey == null) {
            val spec = createSpec()
            val kpGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
            kpGenerator.initialize(spec)
            kpGenerator.generateKeyPair()
        }
    }

}