package com.apiece.ulala.adapter.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperty(
    val secret: String,
    val expirationTime: Long
)
