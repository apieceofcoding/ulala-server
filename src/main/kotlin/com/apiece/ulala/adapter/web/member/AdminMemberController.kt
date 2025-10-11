package com.apiece.ulala.adapter.web.member

import com.apiece.ulala.adapter.web.member.dto.MemberCreateRequest
import com.apiece.ulala.adapter.web.member.dto.MemberResponse
import com.apiece.ulala.app.member.MemberIdGenerator
import com.apiece.ulala.app.member.MemberProvider
import com.apiece.ulala.app.member.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/api/members")
class AdminMemberController(
    private val memberService: MemberService,
    private val memberIdGenerator: MemberIdGenerator,
) {

    @ResponseStatus(HttpStatus.CREATED)
    fun createMember(@Valid @RequestBody request: MemberCreateRequest): MemberResponse {
        val providerUserId = memberIdGenerator.generate()
        val member = memberService.getOrCreateMember(providerUserId, MemberProvider.ULALA, request.memberId)
        return MemberResponse.from(member)
    }
}
