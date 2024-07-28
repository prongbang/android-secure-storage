package com.inteniquetic.ass.crypto

interface Cryptography {
    @Throws(Exception::class)
    fun encrypt(input: ByteArray): ByteArray?

    @Throws(Exception::class)
    fun decrypt(input: ByteArray): ByteArray?
}