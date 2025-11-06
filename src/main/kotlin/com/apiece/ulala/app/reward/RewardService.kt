package com.apiece.ulala.app.reward

import com.apiece.ulala.app.member.MemberService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class RewardService(
    private val rewardRepository: RewardRepository,
    private val memberService: MemberService,
) {

    fun createReward(
        memberId: Long,
        sourceId: Long,
        sourceType: SourceType,
        point: BigDecimal,
        exp: BigDecimal
    ): Reward {
        val reward = Reward.create(
            memberId = memberId,
            sourceId = sourceId,
            sourceType = sourceType,
            point = point,
            exp = exp
        )

        memberService.addReward(memberId, point, exp)

        return rewardRepository.save(reward)
    }

    fun getById(id: Long): Reward {
        return rewardRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 보상입니다") }
    }

    fun getPagedRewardsByMemberId(memberId: Long, pageable: Pageable): Page<Reward> {
        return rewardRepository.findByMemberId(memberId, pageable)
    }

    fun updateReward(
        id: Long,
        sourceId: Long?,
        sourceType: SourceType?,
        point: BigDecimal?,
        exp: BigDecimal?
    ): Reward {
        val reward = getById(id)
        reward.update(sourceId, sourceType, point, exp)
        return rewardRepository.save(reward)
    }

    fun deleteReward(id: Long) {
        val reward = getById(id)
        rewardRepository.delete(reward)
    }

    fun createRewardIfNotExists(
        memberId: Long,
        sourceId: Long,
        sourceType: SourceType,
        point: BigDecimal,
        exp: BigDecimal
    ): Reward? {
        rewardRepository.findByMemberIdAndSourceIdAndSourceType(memberId, sourceId, sourceType)
            ?.let { return null }

        return createReward(
            memberId = memberId,
            sourceId = sourceId,
            sourceType = sourceType,
            point = point,
            exp = exp
        )
    }

    fun getTodayRewards(memberId: Long): List<Reward> {
        val today = LocalDate.now()
        val startAt = today.atStartOfDay()
        val endAt = today.plusDays(1).atStartOfDay()

        return rewardRepository.findByMemberIdAndCreatedAtBetween(memberId, startAt, endAt)
    }

    fun getTodayRewardSummary(memberId: Long): Pair<BigDecimal, BigDecimal> {
        val todayRewards = getTodayRewards(memberId)

        val totalPoint = todayRewards.sumOf { it.point }
        val totalExp = todayRewards.sumOf { it.exp }

        return Pair(totalPoint, totalExp)
    }
}
