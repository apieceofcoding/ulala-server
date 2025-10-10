package com.apiece.ulala.adapter.security.oauth

import com.apiece.ulala.app.member.MemberProvider
import com.apiece.ulala.app.member.MemberService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class OAuthUserService(
    private val memberService: MemberService
) : OidcUserService() {

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = super.loadUser(userRequest)

        val providerUserId = oidcUser.subject
        val provider = MemberProvider.of(userRequest.clientRegistration.registrationId)

        val member = memberService.getOrCreateMember(providerUserId, provider)

        return OAuthUser(oidcUser, member)
    }
}
