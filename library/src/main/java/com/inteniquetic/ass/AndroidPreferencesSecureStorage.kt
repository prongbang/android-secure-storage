package com.inteniquetic.ass

import android.content.Context
import android.os.Build
import android.util.Base64
import android.util.Log
import com.inteniquetic.ass.cipher.KeyAlgorithmsCipher
import com.inteniquetic.ass.cipher.RSAOAEPKeyCipher
import com.inteniquetic.ass.cipher.RSAPKCS1KeyCipher
import com.inteniquetic.ass.cipher.StorageAlgorithmsCipher
import com.inteniquetic.ass.cipher.spec.LegacyAlgorithmParameterSpecFactory
import com.inteniquetic.ass.cipher.spec.OAEPAlgorithmParameterSpecFactory
import com.inteniquetic.ass.cipher.spec.PKCS1AlgorithmParameterSpecFactory
import com.inteniquetic.ass.crypto.CBCAESCryptography
import com.inteniquetic.ass.crypto.GCMAESCryptography
import com.inteniquetic.ass.key.AndroidKeyPairManager
import com.inteniquetic.ass.key.AndroidKeyStoreFactory
import com.inteniquetic.ass.key.DefaultKeyAliasFactory
import com.inteniquetic.ass.key.KeyAliasFactory
import com.inteniquetic.ass.storage.PreferencesManager
import com.inteniquetic.ass.storage.SharedPreferencesManager
import com.inteniquetic.ass.storage.DefaultPreferencesName
import com.inteniquetic.ass.storage.PreferencesName
import java.nio.charset.StandardCharsets

class AndroidPreferencesSecureStorage(
    private val context: Context,
    private val preferencesPrefixKey: String = PREFERENCES_KEY_PREFIX,
    private val preferencesManager: PreferencesManager = SharedPreferencesManager(
        context,
        DefaultPreferencesName()
    ),
    private val keyAlgorithmsCipher: KeyAlgorithmsCipher = KeyAlgorithmsCipher.RSA_ECB_OAEP_WITH_SHA_256_AND_MGF1_PADDING,
    private val storageAlgorithmsCipher: StorageAlgorithmsCipher = StorageAlgorithmsCipher.AES_GCM_NO_PADDING,
    private val keyAliasFactory: KeyAliasFactory = DefaultKeyAliasFactory(
        context,
        keyAlgorithmsCipher
    ),
) : AndroidSecureStorage {

    companion object {
        private const val PREFERENCES_KEY_PREFIX = "0190f890f88e786da25977f608943dcd"

        fun newInstance(context: Context): AndroidSecureStorage =
            AndroidPreferencesSecureStorage(context)
    }

    private val isSupportEncryptedPreferences = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M).not()

    private val preferences by lazy {
        if (isSupportEncryptedPreferences) {
            preferencesManager.secure()
        } else {
            preferencesManager.insecure()
        }
    }

    private val aesCryptography by lazy {
        val keyPairManager = AndroidKeyPairManager(keyAliasFactory)
        val legacyAlgorithmParameterSpecFactory =
            LegacyAlgorithmParameterSpecFactory(context, keyAliasFactory)

        val keyCipher = when (keyAlgorithmsCipher) {
            KeyAlgorithmsCipher.RSA_ECB_PKCS1_PADDING -> {
                RSAPKCS1KeyCipher(
                    AndroidKeyStoreFactory(
                        keyAliasFactory,
                        legacyAlgorithmParameterSpecFactory,
                        PKCS1AlgorithmParameterSpecFactory(keyAliasFactory)
                    ),
                    keyPairManager
                )
            }

            KeyAlgorithmsCipher.RSA_ECB_OAEP_WITH_SHA_256_AND_MGF1_PADDING -> {
                RSAOAEPKeyCipher(
                    AndroidKeyStoreFactory(
                        keyAliasFactory,
                        legacyAlgorithmParameterSpecFactory,
                        OAEPAlgorithmParameterSpecFactory(keyAliasFactory)
                    ),
                    keyPairManager
                )
            }
        }
        when (storageAlgorithmsCipher) {
            StorageAlgorithmsCipher.AES_GCM_NO_PADDING -> {
                GCMAESCryptography(keyCipher, preferencesManager)
            }

            StorageAlgorithmsCipher.AES_CBC_PKCS7_PADDING -> {
                CBCAESCryptography(keyCipher, preferencesManager)
            }
        }
    }

    override fun containsKey(key: String): Boolean = preferences.contains(key)

    override fun read(key: String): String? {
        val value: String? = preferences.getString(key, null)
        if (isSupportEncryptedPreferences) {
            return value
        }
        return decryptValue(value)
    }

    override fun readAll(): HashMap<String, String?>? {
        val raw = preferences.all as Map<String, Any?>? ?: return null

        val all: HashMap<String, String?> = HashMap()
        for ((keyWithPrefix, rawValue) in raw) {
            val value = rawValue?.toString()
            if (keyWithPrefix.contains(preferencesPrefixKey)) {
                val key: String =
                    keyWithPrefix.replaceFirst((preferencesPrefixKey + '_').toRegex(), "")
                if (isSupportEncryptedPreferences) {
                    all[key] = value
                } else {
                    all[key] = decryptValue(value)
                }
            }
        }

        return all
    }

    override fun write(key: String, value: String) {
        val editor = preferences.edit()

        if (isSupportEncryptedPreferences) {
            editor.putString(key, value)
        } else {
            val result: ByteArray? =
                aesCryptography.encrypt(value.toByteArray(StandardCharsets.UTF_8))
            editor.putString(key, Base64.encodeToString(result, Base64.DEFAULT))
        }
        editor.apply()
    }

    override fun delete(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }

    override fun deleteAll() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    @Throws(Exception::class)
    private fun decryptValue(value: String?): String? {
        if (value == null) {
            return null
        }
        val data = Base64.decode(value, Base64.DEFAULT)
        val result: ByteArray = aesCryptography.decrypt(data) ?: return null

        return String(result, StandardCharsets.UTF_8)
    }
}