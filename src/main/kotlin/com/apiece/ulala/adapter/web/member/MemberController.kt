package com.apiece.ulala.adapter.web.member

import com.apiece.ulala.adapter.web.member.dto.MemberIdCheckResponse
import com.apiece.ulala.adapter.web.member.dto.MemberResponse
import com.apiece.ulala.adapter.web.member.dto.MemberUpdateRequest
import com.apiece.ulala.app.member.MemberService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/me")
    fun getMe(@AuthenticationPrincipal user: User): MemberResponse {
        val member = memberService.getById(user.username.toLong())
        return MemberResponse.from(member)
    }

    @PatchMapping("/me")
    fun updateMe(
        @AuthenticationPrincipal user: User,
        @RequestBody request: MemberUpdateRequest
    ): MemberResponse {
        val updatedMember = memberService.updateMember(user.username.toLong(), request.memberId, request.displayName)
        return MemberResponse.from(updatedMember)
    }

    @GetMapping("{memberId}/check")
    fun checkMemberId(@PathVariable memberId: String): MemberIdCheckResponse {
        val exists = memberService.checkMemberIdExists(memberId)
        return MemberIdCheckResponse(exists)
    }
}
