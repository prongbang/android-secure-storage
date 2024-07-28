package com.inteniquetic.ass

interface AndroidSecureStorage {
    fun containsKey(key: String): Boolean

    @Throws(Exception::class)
    fun read(key: String): String?

    @Throws(java.lang.Exception::class)
    fun readAll(): HashMap<String, String?>?

    @Throws(java.lang.Exception::class)
    fun write(key: String, value: String)

    fun delete(key: String)

    fun deleteAll()
}