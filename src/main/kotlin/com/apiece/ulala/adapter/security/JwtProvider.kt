package com.apiece.ulala.adapter.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    private val jwtProperty: JwtProperty
) {

    private val accessSecretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperty.accessSecret))
    }

    private val refreshSecretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperty.refreshSecret))
    }

    fun generateAccessToken(memberId: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperty.accessExpirationTime)

        return Jwts.builder()
            .subject(memberId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(accessSecretKey)
            .compact()
    }

    fun generateRefreshToken(memberId: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperty.refreshExpirationTime)

        return Jwts.builder()
            .subject(memberId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(refreshSecretKey)
            .compact()
    }

    fun validateAccessToken(accessToken: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(accessSecretKey)
                .build()
                .parseSignedClaims(accessToken)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(refreshSecretKey)
                .build()
                .parseSignedClaims(refreshToken)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getMemberIdFromAccessToken(accessToken: String): String {
        return Jwts.parser()
            .verifyWith(accessSecretKey)
            .build()
            .parseSignedClaims(accessToken)
            .payload
            .subject
    }

    fun getMemberIdFromRefreshToken(refreshToken: String): String {
        return Jwts.parser()
            .verifyWith(refreshSecretKey)
            .build()
            .parseSignedClaims(refreshToken)
            .payload
            .subject
    }
}

@ConfigurationProperties(prefix = "jwt")
class JwtProperty(
    val accessSecret: String,
    val accessExpirationTime: Long,
    val refreshSecret: String,
    val refreshExpirationTime: Long,
)
