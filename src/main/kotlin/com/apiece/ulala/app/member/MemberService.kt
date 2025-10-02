package com.apiece.ulala.app.member

import com.apiece.ulala.app.member.dto.MemberListResponse
import com.apiece.ulala.app.member.dto.MemberResponse
import com.apiece.ulala.app.member.dto.MemberUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    fun getMember(id: Long): MemberResponse {
        val member = findMemberById(id)
        return MemberResponse.from(member)
    }

    fun getMemberByMemberId(memberId: String): MemberResponse {
        val member = memberRepository.findByMemberId(memberId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
        return MemberResponse.from(member)
    }

    fun getPagedMembers(pageable: Pageable): Page<MemberListResponse> {
        return memberRepository.findAll(pageable)
            .map { MemberListResponse.from(it) }
    }

    fun updateMember(id: Long, request: MemberUpdateRequest): MemberResponse {
        val member = findMemberById(id)
        request.memberId?.let {
            if (memberRepository.existsByMemberId(it)) {
                throw IllegalArgumentException("이미 존재하는 회원아이디입니다")
            }
        }
        member.update(request)

        val updatedMember = memberRepository.save(member)
        return MemberResponse.from(updatedMember)
    }

    fun deleteMember(id: Long) {
        val member = findMemberById(id)
        member.delete()
        memberRepository.save(member)
    }

    fun getOrCreateMember(providerUserId: String, provider: MemberProvider, memberId: String? = null): Member {
        return memberRepository.findByProviderUserIdAndProvider(providerUserId, provider)
            ?: run {
                val newMemberId = memberId ?: generateRandomId()
                val newMember = Member.create(
                    memberId = newMemberId,
                    providerUserId = providerUserId,
                    provider = provider
                )
                memberRepository.save(newMember)
            }
    }

    fun generateRandomId(length: Int = 20): String {
        val random = SecureRandom()
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
        val sb = StringBuilder()
        repeat(length) {
            val index = random.nextInt(chars.length)
            sb.append(chars[index])
        }
        return sb.toString()
    }

    private fun findMemberById(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
    }
}
