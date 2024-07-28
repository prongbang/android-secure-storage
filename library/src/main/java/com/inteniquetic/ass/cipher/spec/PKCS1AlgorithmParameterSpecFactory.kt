package com.inteniquetic.ass.cipher.spec

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.inteniquetic.ass.key.KeyAliasFactory
import java.math.BigInteger
import java.security.spec.AlgorithmParameterSpec
import java.util.Calendar
import javax.security.auth.x500.X500Principal

class PKCS1AlgorithmParameterSpecFactory(
    private val keyAliasFactory: KeyAliasFactory,
) : ParameterSpecFactory {

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun create(): AlgorithmParameterSpec {
        val alias = keyAliasFactory.create()
        val start = Calendar.getInstance()
        val end = Calendar.getInstance().apply {
            add(Calendar.YEAR, 50)
        }

        val builder = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT
        )
            .setCertificateSubject(X500Principal("CN=$alias"))
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .setCertificateSerialNumber(BigInteger.valueOf(1))
            .setCertificateNotBefore(start.time)
            .setCertificateNotAfter(end.time)

        return builder.build()
    }
}