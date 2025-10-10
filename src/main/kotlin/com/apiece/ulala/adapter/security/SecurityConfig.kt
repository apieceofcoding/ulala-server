package com.apiece.ulala.adapter.security

import com.apiece.ulala.adapter.security.oauth.OAuthUserService
import com.apiece.ulala.adapter.security.token.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
class SecurityConfig(
    private val oAuthenticationSuccessHandler: AuthenticationSuccessHandler,
    private val oAuthenticationFailureHandler: AuthenticationFailureHandler,
    private val oAuthUserService: OAuthUserService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val corsConfigurationSource: CorsConfigurationSource,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            cors { configurationSource = corsConfigurationSource }

            authorizeHttpRequests {
                authorize("/oauth2/authorization/**", permitAll)
                authorize("/api/auth/token", permitAll)
                authorize(anyRequest, authenticated)
            }

            exceptionHandling {
                authenticationEntryPoint = HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
            }

            oauth2Login {
                authorizationEndpoint { baseUri = "/oauth2/authorization" }
                userInfoEndpoint { oidcUserService = oAuthUserService }
                authenticationSuccessHandler = oAuthenticationSuccessHandler
                authenticationFailureHandler = oAuthenticationFailureHandler
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
        }
        return http.build()
    }
}
