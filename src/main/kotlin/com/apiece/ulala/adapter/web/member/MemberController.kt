package com.apiece.ulala.adapter.web.member

import com.apiece.ulala.adapter.web.member.dto.MemberListResponse
import com.apiece.ulala.adapter.web.member.dto.MemberResponse
import com.apiece.ulala.app.member.MemberService
import com.apiece.ulala.adapter.web.member.dto.MemberUpdateRequest
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
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
        val member = memberService.getByMemberId(user.username)
        return MemberResponse.from(member)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: Long): MemberResponse {
        val member = memberService.get(id)
        return MemberResponse.from(member)
    }

    @GetMapping("/memberId/{memberId}")
    fun getMemberByMemberId(@PathVariable memberId: String): MemberResponse {
        val member = memberService.getByMemberId(memberId)
        return MemberResponse.from(member)
    }

    @GetMapping
    fun getAllMembers(@PageableDefault(size = 10) pageable: Pageable): Page<MemberListResponse> {
        return memberService.getPagedMembers(pageable)
            .map { MemberListResponse.from(it) }
    }

    @PutMapping("/{id}")
    fun updateMember(
        @PathVariable id: Long,
        @Valid @RequestBody request: MemberUpdateRequest
    ): MemberResponse {
        val member = memberService.updateMember(id, request.memberId, request.displayName)
        return MemberResponse.from(member)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember(@PathVariable id: Long) {
        memberService.deleteMember(id)
    }
}
