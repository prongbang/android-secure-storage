package com.inteniquetic.ass.key

import java.security.spec.AlgorithmParameterSpec

interface KeyStoreFactory {
    fun createSpec(): AlgorithmParameterSpec

    @Throws(Exception::class)
    fun create()
}