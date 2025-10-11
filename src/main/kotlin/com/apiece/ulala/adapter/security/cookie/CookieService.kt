package com.apiece.ulala.adapter.security.cookie

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Service

@Service
class CookieService(
    private val cookieProperty: CookieProperty,
) {

    fun createRefreshTokenCookie(value: String): Cookie {
        return Cookie("refreshToken", value).apply {
            isHttpOnly = true
            secure = cookieProperty.refreshTokenSecure
            path = "/"
            maxAge = 7 * 24 * 60 * 60
        }
    }

    fun deleteRefreshTokenCookie(): Cookie {
        return Cookie("refreshToken", "").apply {
            isHttpOnly = true
            secure = cookieProperty.refreshTokenSecure
            path = "/"
            maxAge = 0
        }
    }

    fun deleteJsessionidCookie(): Cookie {
        return Cookie("JSESSIONID", "").apply {
            isHttpOnly = true
            path = "/"
            maxAge = 0
        }
    }
}
