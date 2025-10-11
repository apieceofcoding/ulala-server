package com.apiece.ulala.adapter.security.cookie

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cookie")
class CookieProperty(
    val refreshTokenSecure: Boolean,
)
