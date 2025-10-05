package com.apiece.ulala.adapter.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class OAuthenticationSuccessHandler(
    private val jwtProvider: JwtProvider,
    private val objectMapper: ObjectMapper
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuthUser = authentication.principal as OAuthUser
        val member = oAuthUser.member

        val accessToken = jwtProvider.generateToken(member.id.toString())

        log.info { "로그인 성공 → memberId=@${member.memberId}\naccessToken=$accessToken" }

        returnResponse(response, accessToken)
    }

    private fun returnResponse(response: HttpServletResponse, accessToken: String) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_OK

        val loginResponse = LoginResponse(accessToken = accessToken)
        objectMapper.writeValue(response.writer, loginResponse)
    }
}
