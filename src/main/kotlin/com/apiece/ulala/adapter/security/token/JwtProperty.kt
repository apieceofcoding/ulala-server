package com.apiece.ulala.adapter.security.token

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperty(
    val accessSecret: String,
    val accessExpirationTime: Long,
    val refreshSecret: String,
    val refreshExpirationTime: Long,
)
