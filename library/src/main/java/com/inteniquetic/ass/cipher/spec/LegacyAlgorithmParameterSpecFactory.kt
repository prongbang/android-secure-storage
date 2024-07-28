@file:Suppress("DEPRECATION")

package com.inteniquetic.ass.cipher.spec

import android.content.Context
import android.security.KeyPairGeneratorSpec
import com.inteniquetic.ass.key.KeyAliasFactory
import java.math.BigInteger
import java.security.spec.AlgorithmParameterSpec
import java.util.Calendar
import javax.security.auth.x500.X500Principal

class LegacyAlgorithmParameterSpecFactory(
    private val context: Context,
    private val keyAliasFactory: KeyAliasFactory,
) : ParameterSpecFactory {

    @Suppress("deprecation")
    override fun create(): AlgorithmParameterSpec {
        val alias = keyAliasFactory.create()
        val start = Calendar.getInstance()
        val end = Calendar.getInstance().apply {
            add(Calendar.YEAR, 50)
        }

        return KeyPairGeneratorSpec.Builder(context)
            .setAlias(alias)
            .setSubject(X500Principal("CN=$alias"))
            .setStartDate(start.time)
            .setEndDate(end.time)
            .setSerialNumber(BigInteger.valueOf(1))
            .build()
    }
}