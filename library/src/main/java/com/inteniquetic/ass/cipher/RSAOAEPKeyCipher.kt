package com.inteniquetic.ass.cipher

import com.inteniquetic.ass.key.KeyPairManager
import com.inteniquetic.ass.key.KeyStoreFactory
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

class RSAOAEPKeyCipher(
    keyStoreFactory: KeyStoreFactory,
    keyPairManager: KeyPairManager,
) : RSAKeyCipher(keyStoreFactory, keyPairManager) {

    @Throws(Exception::class)
    override fun getCipher(): Cipher =
        Cipher.getInstance("RSA/ECB/OAEPPadding", "AndroidKeyStoreBCWorkaround")

    override fun getAlgorithmParameterSpec(): AlgorithmParameterSpec = OAEPParameterSpec(
        "SHA-256",
        "MGF1",
        MGF1ParameterSpec.SHA1,
        PSource.PSpecified.DEFAULT
    )
}