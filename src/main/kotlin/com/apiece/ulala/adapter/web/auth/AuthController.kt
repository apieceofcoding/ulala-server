package com.apiece.ulala.adapter.web.auth

import com.apiece.ulala.adapter.security.token.JwtProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtProvider: JwtProvider
) {

    @PostMapping("/token")
    fun refreshToken(request: HttpServletRequest): TokenResponse {
        val refreshToken = request.cookies?.find { it.name == "refreshToken" }?.value
        requireNotNull(refreshToken) { "Refresh token이 없습니다" }
        require(jwtProvider.validateRefreshToken(refreshToken)) { "유효하지 않은 refresh token입니다" }

        val memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken)
        val accessToken = jwtProvider.generateAccessToken(memberId)

        log.info { "Access token 발급 → memberId=@$memberId" }

        return TokenResponse(accessToken)
    }
}

class TokenResponse(
    val accessToken: String
)
