package com.apiece.ulala.app.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findByMemberId(memberId: String): Optional<Member>

    fun existsByMemberId(memberId: String): Boolean

    fun findByProviderUserIdAndProvider(providerUserId: String, provider: MemberProvider): Member?
}
