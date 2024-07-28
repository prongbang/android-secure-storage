package com.inteniquetic.ass.key

import java.security.PrivateKey
import java.security.PublicKey

interface KeyPairManager {
    @Throws(Exception::class)
    fun getPrivateKey(): PrivateKey

    @Throws(java.lang.Exception::class)
    fun getPublicKey(): PublicKey
}