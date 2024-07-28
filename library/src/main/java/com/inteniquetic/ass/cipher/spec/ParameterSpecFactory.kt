package com.inteniquetic.ass.cipher.spec

import java.security.spec.AlgorithmParameterSpec

interface ParameterSpecFactory {
    fun create(): AlgorithmParameterSpec
}