package com.apiece.ulala.adapter.web.auth

import com.apiece.ulala.adapter.security.cookie.CookieService
import com.apiece.ulala.adapter.security.token.JwtProvider
import com.apiece.ulala.adapter.web.auth.dto.AuthTokenResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtProvider: JwtProvider,
    private val cookieService: CookieService,
) {

    @PostMapping("/token")
    fun refreshToken(request: HttpServletRequest): AuthTokenResponse {
        val refreshToken = request.cookies?.find { it.name == "refreshToken" }?.value
        requireNotNull(refreshToken) { "Refresh token이 없습니다" }
        require(jwtProvider.validateRefreshToken(refreshToken)) { "유효하지 않은 refresh token입니다" }

        val memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken)
        val accessToken = jwtProvider.generateAccessToken(memberId)

        log.info { "Access token 발급 → memberId=@$memberId" }

        return AuthTokenResponse(accessToken)
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        val refreshToken = request.cookies?.find { it.name == "refreshToken" }?.value

        if (refreshToken != null && jwtProvider.validateRefreshToken(refreshToken)) {
            val memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken)
            log.info { "로그아웃 → memberId=@$memberId" }
        }

        val deletedCookie = cookieService.deleteRefreshTokenCookie()
        response.addCookie(deletedCookie)
    }
}
