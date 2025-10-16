package com.apiece.ulala.adapter.web.member

import com.apiece.ulala.adapter.web.member.dto.MemberCreateRequest
import com.apiece.ulala.adapter.web.member.dto.MemberListResponse
import com.apiece.ulala.adapter.web.member.dto.MemberResponse
import com.apiece.ulala.adapter.web.member.dto.MemberUpdateRequest
import com.apiece.ulala.app.member.UsernameGenerator
import com.apiece.ulala.app.member.MemberProvider
import com.apiece.ulala.app.member.MemberService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/api/members")
class AdminMemberController(
    private val memberService: MemberService,
    private val usernameGenerator: UsernameGenerator,
) {

    @ResponseStatus(HttpStatus.CREATED)
    fun createMember(@Valid @RequestBody request: MemberCreateRequest): MemberResponse {
        val providerUserId = usernameGenerator.generate()
        val member = memberService.getOrCreateMember(providerUserId, MemberProvider.ULALA, request.username)
        return MemberResponse.from(member)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: Long): MemberResponse {
        val member = memberService.getById(id)
        return MemberResponse.from(member)
    }

    @GetMapping("/username/{username}")
    fun getMemberByUsername(@PathVariable username: String): MemberResponse {
        val member = memberService.getByUsername(username)
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
        val member = memberService.updateMember(id, request.username, request.displayName)
        return MemberResponse.from(member)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember(@PathVariable id: Long) {
        memberService.deleteMember(id)
    }
}
