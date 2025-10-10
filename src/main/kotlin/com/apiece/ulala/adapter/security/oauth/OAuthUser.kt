package com.apiece.ulala.adapter.security.oauth

import com.apiece.ulala.app.member.Member
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class OAuthUser(
    private val oidcUser: OidcUser,
    val member: Member
) : OidcUser by oidcUser {

    override fun getName(): String {
        return member.memberId
    }
}
