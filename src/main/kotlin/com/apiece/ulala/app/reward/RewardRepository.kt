package com.apiece.ulala.app.reward

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RewardRepository : JpaRepository<Reward, Long> {

    fun findByMemberId(memberId: Long, pageable: Pageable): Page<Reward>

    fun findByMemberIdAndSourceIdAndSourceType(memberId: Long, sourceId: Long, sourceType: SourceType): Reward?

    fun findBySourceTypeAndSourceIdIn(sourceType: SourceType, sourceIds: List<Long>): List<Reward>
}
