package com.apiece.ulala.adapter.security.oauth

import com.apiece.ulala.adapter.security.cookie.CookieService
import com.apiece.ulala.adapter.security.cors.CorsProperty
import com.apiece.ulala.adapter.security.token.JwtProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class OAuthenticationSuccessHandler(
    private val jwtProvider: JwtProvider,
    private val cookieService: CookieService,
    private val corsProperty: CorsProperty,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuthUser = authentication.principal as OAuthUser
        val member = oAuthUser.member

        val refreshToken = jwtProvider.generateRefreshToken(member.id.toString())

        val refreshTokenCookie = cookieService.createRefreshTokenCookie(refreshToken)
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())

        request.session?.invalidate()
        val deletedJsessionidCookie = cookieService.deleteJsessionidCookie()
        response.addHeader(HttpHeaders.SET_COOKIE, deletedJsessionidCookie.toString())

        val redirectUrl = "${corsProperty.allowedOrigins.first()}/profile"
        response.sendRedirect(redirectUrl)

        log.info { "로그인 성공 → id=${member.id}, username=@${member.username}" }
    }
}
