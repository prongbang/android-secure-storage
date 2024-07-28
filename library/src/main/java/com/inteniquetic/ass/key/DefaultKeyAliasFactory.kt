package com.inteniquetic.ass.key

import android.content.Context
import com.inteniquetic.ass.cipher.KeyAlgorithmsCipher

class DefaultKeyAliasFactory(
    private val context: Context,
    private val keyAlgorithmsCipher: KeyAlgorithmsCipher
) : KeyAliasFactory {

    override fun create(): String {
        return when (keyAlgorithmsCipher) {
            KeyAlgorithmsCipher.RSA_ECB_PKCS1_PADDING -> "${context.packageName}.PKCS1Key"
            KeyAlgorithmsCipher.RSA_ECB_OAEP_WITH_SHA_256_AND_MGF1_PADDING -> "${context.packageName}.OAEPKey"
        }
    }
}