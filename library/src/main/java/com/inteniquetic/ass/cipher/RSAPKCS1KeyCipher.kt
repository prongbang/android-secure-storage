package com.inteniquetic.ass.cipher

import android.os.Build
import com.inteniquetic.ass.key.KeyPairManager
import com.inteniquetic.ass.key.KeyStoreFactory
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher

class RSAPKCS1KeyCipher(
    keyStoreFactory: KeyStoreFactory,
    keyPairManager: KeyPairManager,
) : RSAKeyCipher(keyStoreFactory, keyPairManager) {

    override fun getCipher(): Cipher = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL")
    } else {
        Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround")
    }

    override fun getAlgorithmParameterSpec(): AlgorithmParameterSpec? = null

}