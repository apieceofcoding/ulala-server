package com.apiece.ulala.app.db

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

private val log = KotlinLogging.logger {}

@Component
class CurrentAuditorAware : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        val auditor: String = try {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication != null && authentication.isAuthenticated) {
                authentication.name
            } else {
                "system"
            }
        } catch (e: Exception) {
            log.warn(e) { "Failed to get current auditor" }
            "system"
        }

        return Optional.of(auditor)
    }
}
