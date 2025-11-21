package com.apiece.ulala.adapter.web.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import tools.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets

private val log = KotlinLogging.logger {}

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class LoggingFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cachedRequest = request as? ContentCachingRequestWrapper ?: ContentCachingRequestWrapper(request, 0)
        val cachedResponse = response as? ContentCachingResponseWrapper ?: ContentCachingResponseWrapper(response)

        val startTime = System.currentTimeMillis()

        try {
            filterChain.doFilter(cachedRequest, cachedResponse)
        } finally {
            val duration = System.currentTimeMillis() - startTime

            logRequestResponse(cachedRequest, cachedResponse, duration)

            cachedResponse.copyBodyToResponse()
        }
    }

    private fun logRequestResponse(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        duration: Long
    ) {
        val requestLog = buildRequestLog(request)
        val responseLog = buildResponseLog(response, duration)

        log.info { "$requestLog => $responseLog" }
    }

    private fun buildRequestLog(request: ContentCachingRequestWrapper): String {
        val method = request.method
        val uri = request.requestURI
        val queryString = request.queryString?.let { "?$it" } ?: ""
        val headers = getHeaders(request)
        val body = getRequestBody(request)

        return "$method $uri$queryString $body $headers"
    }

    private fun buildResponseLog(response: ContentCachingResponseWrapper, duration: Long): String {
        val status = response.status
        val body = getResponseBody(response)

        return "$status ${duration}ms $body"
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        IMPORTANT_REQUEST_HEADERS.forEach { headerName ->
            request.getHeader(headerName)?.let { value ->
                headers[headerName] = value
            }
        }
        return headers
    }

    private fun getRequestBody(request: ContentCachingRequestWrapper): String {
        val content = request.contentAsByteArray
        if (content.isNotEmpty()) {
            return try {
                val body = String(content, StandardCharsets.UTF_8)
                formatJson(body)
            } catch (_: Exception) {
                "[Unable to parse]"
            }
        }
        return ""
    }

    private fun getResponseBody(response: ContentCachingResponseWrapper): String {
        val content = response.contentAsByteArray
        if (content.isNotEmpty()) {
            return try {
                val body = String(content, StandardCharsets.UTF_8)
                formatJson(body)
            } catch (_: Exception) {
                "[Unable to parse]"
            }
        }
        return ""
    }

    private fun formatJson(content: String): String {
        return try {
            val json = objectMapper.readTree(content)
            objectMapper.writeValueAsString(json)
        } catch (_: Exception) {
            content
        }
    }

    companion object {
        private val IMPORTANT_REQUEST_HEADERS = listOf(
            "Origin",
            "Authorization",
            "User-Agent"
        )
    }
}
