package com.apiece.ulala.app.member

import com.apiece.ulala.app.member.dto.MemberCreateRequest
import com.apiece.ulala.app.member.dto.MemberListResponse
import com.apiece.ulala.app.member.dto.MemberResponse
import com.apiece.ulala.app.member.dto.MemberUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    fun createMember(request: MemberCreateRequest): MemberResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 존재하는 이메일입니다")
        }

        if (memberRepository.existsByNickname(request.nickname)) {
            throw IllegalArgumentException("이미 존재하는 닉네임입니다")
        }

        val member = Member.create(request)

        val savedMember = memberRepository.save(member)
        return MemberResponse.from(savedMember)
    }

    fun getMember(id: Long): MemberResponse {
        val member = findMemberById(id)
        return MemberResponse.from(member)
    }

    fun getMemberByEmail(email: String): MemberResponse {
        val member = memberRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
        return MemberResponse.from(member)
    }

    fun getAllMembers(pageable: Pageable): Page<MemberListResponse> {
        return memberRepository.findAll(pageable)
            .map { MemberListResponse.from(it) }
    }

    fun updateMember(id: Long, request: MemberUpdateRequest): MemberResponse {
        val member = findMemberById(id)
        request.nickname?.let {
            if (memberRepository.existsByNickname(it)) {
                throw IllegalArgumentException("이미 존재하는 닉네임입니다")
            }
        }
        member.update(request)

        val updatedMember = memberRepository.save(member)
        return MemberResponse.from(updatedMember)
    }

    fun deleteMember(id: Long) {
        if (!memberRepository.existsById(id)) {
            throw IllegalArgumentException("존재하지 않는 회원입니다")
        }
        memberRepository.deleteById(id)
    }

    private fun findMemberById(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
    }
}
