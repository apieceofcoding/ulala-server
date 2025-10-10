package com.apiece.ulala.adapter.web

import com.apiece.ulala.app.member.MemberService
import com.apiece.ulala.app.member.dto.MemberListResponse
import com.apiece.ulala.app.member.dto.MemberResponse
import com.apiece.ulala.app.member.dto.MemberUpdateRequest
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
        return memberService.getByMemberId(user.username)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: Long): MemberResponse {
        return memberService.get(id)
    }

    @GetMapping("/memberId/{memberId}")
    fun getMemberByMemberId(@PathVariable memberId: String): MemberResponse {
        return memberService.getByMemberId(memberId)
    }

    @GetMapping
    fun getAllMembers(
        @PageableDefault(size = 10) pageable: Pageable
    ): Page<MemberListResponse> {
        return memberService.getPagedMembers(pageable)
    }

    @PutMapping("/{id}")
    fun updateMember(
        @PathVariable id: Long,
        @Valid @RequestBody request: MemberUpdateRequest
    ): MemberResponse {
        return memberService.updateMember(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember(@PathVariable id: Long) {
        memberService.deleteMember(id)
    }
}
