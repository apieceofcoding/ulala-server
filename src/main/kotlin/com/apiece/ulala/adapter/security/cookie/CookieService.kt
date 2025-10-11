package com.apiece.ulala.adapter.security.cookie

import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class CookieService(
    private val cookieProperty: CookieProperty,
) {

    fun createRefreshTokenCookie(value: String): ResponseCookie {
        return ResponseCookie.from("refreshToken", value)
            .path("/")
            .maxAge(Duration.ofDays(7))
            .secure(cookieProperty.secure)
            .httpOnly(true)
            .sameSite(cookieProperty.sameSite)
            .build()
    }

    fun deleteRefreshTokenCookie(): ResponseCookie {
        return ResponseCookie.from("refreshToken", "")
            .path("/")
            .maxAge(0)
            .secure(cookieProperty.secure)
            .httpOnly(true)
            .sameSite(cookieProperty.sameSite)
            .build()
    }

    fun deleteJsessionidCookie(): ResponseCookie {
        return ResponseCookie.from("JSESSIONID", "")
            .path("/")
            .maxAge(0)
            .secure(cookieProperty.secure)
            .httpOnly(true)
            .sameSite(cookieProperty.sameSite)
            .build()
    }
}
