package com.example.neighborsis.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*


class Pkce {
    //base64 secure code create
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(UnsupportedEncodingException::class)
    fun generateCodeVerifier(): String? {
        val secureRandom = SecureRandom()
        val codeVerifier = ByteArray(32)
        secureRandom.nextBytes(codeVerifier)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier)
    }

    //maded
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    fun generateCodeChallange(codeVerifier: String): String? {
        val bytes = codeVerifier.toByteArray(charset("US-ASCII"))
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest: ByteArray = messageDigest.digest()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
}