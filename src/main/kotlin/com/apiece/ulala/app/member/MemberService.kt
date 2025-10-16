package com.apiece.ulala.app.member

import com.apiece.ulala.app.db.IdGenerator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val idGenerator: IdGenerator,
    private val usernameGenerator: UsernameGenerator,
) {

    fun getById(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
    }

    fun getByUsername(username: String): Member {
        return memberRepository.findByUsername(username)
            .orElseThrow { IllegalArgumentException("존재하지 않는 회원입니다") }
    }

    fun getPagedMembers(pageable: Pageable): Page<Member> {
        return memberRepository.findAll(pageable)
    }

    fun updateMember(id: Long, username: String?, displayName: String?): Member {
        val member = getById(id)
        username?.let {
            if (username != member.username && memberRepository.existsByUsername(it)) {
                throw IllegalArgumentException("이미 존재하는 사용자이름입니다")
            }
        }
        member.update(username, displayName)

        return memberRepository.save(member)
    }

    fun deleteMember(id: Long) {
        val member = getById(id)
        member.delete()
        memberRepository.save(member)
    }

    fun getOrCreateMember(providerUserId: String, provider: MemberProvider, username: String? = null): Member {
        return memberRepository.findByProviderUserIdAndProvider(providerUserId, provider)
            ?: run {
                val newMember = Member.create(
                    id = idGenerator.nextId(),
                    username = username ?: usernameGenerator.generate(),
                    providerUserId = providerUserId,
                    provider = provider,
                )
                memberRepository.save(newMember)
            }
    }

    fun checkUsernameExists(username: String): Boolean {
        return memberRepository.existsByUsername(username)
    }
}
