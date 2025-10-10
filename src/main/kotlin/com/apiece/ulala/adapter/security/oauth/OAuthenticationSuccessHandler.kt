package com.apiece.ulala.adapter.security.oauth

import com.apiece.ulala.adapter.security.cors.CorsProperty
import com.apiece.ulala.adapter.security.token.JwtProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class OAuthenticationSuccessHandler(
    private val jwtProvider: JwtProvider,
    private val cookieProperty: CookieProperty,
    private val corsProperty: CorsProperty,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuthUser = authentication.principal as OAuthUser
        val member = oAuthUser.member

        val refreshToken = jwtProvider.generateRefreshToken(member.memberId)

        log.info { "로그인 성공 → memberId=@${member.memberId}" }

        val cookie = Cookie("refreshToken", refreshToken).apply {
            isHttpOnly = true
            secure = cookieProperty.refreshTokenSecure
            path = "/"
            maxAge = 7 * 24 * 60 * 60
        }
        response.addCookie(cookie)

        val redirectUrl = "${corsProperty.allowedOrigins.first()}/profile"
        response.sendRedirect(redirectUrl)
    }
}

@ConfigurationProperties(prefix = "cookie")
class CookieProperty(
    val refreshTokenSecure: Boolean,
)
