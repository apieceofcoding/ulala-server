package com.apiece.ulala.app.member

import com.apiece.ulala.app.db.IdGenerator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val idGenerator: IdGenerator,
    private val memberIdGenerator: MemberIdGenerator,
) {

    fun getById(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
    }

    fun getByMemberId(memberId: String): Member {
        return memberRepository.findByMemberId(memberId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
    }

    fun getPagedMembers(pageable: Pageable): Page<Member> {
        return memberRepository.findAll(pageable)
    }

    fun updateMember(id: Long, memberId: String?, displayName: String?): Member {
        val member = getById(id)
        memberId?.let {
            if (memberId != member.memberId && memberRepository.existsByMemberId(it)) {
                throw IllegalArgumentException("이미 존재하는 회원아이디입니다")
            }
        }
        member.update(memberId, displayName)

        return memberRepository.save(member)
    }

    fun deleteMember(id: Long) {
        val member = getById(id)
        member.delete()
        memberRepository.save(member)
    }

    fun getOrCreateMember(providerUserId: String, provider: MemberProvider, memberId: String? = null): Member {
        return memberRepository.findByProviderUserIdAndProvider(providerUserId, provider)
            ?: run {
                val newMember = Member.create(
                    id = idGenerator.nextId(),
                    memberId = memberId ?: memberIdGenerator.generate(),
                    providerUserId = providerUserId,
                    provider = provider,
                )
                memberRepository.save(newMember)
            }
    }

    fun checkMemberIdExists(memberId: String): Boolean {
        return memberRepository.existsByMemberId(memberId)
    }
}
