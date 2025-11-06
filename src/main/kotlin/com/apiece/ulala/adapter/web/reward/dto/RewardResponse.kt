package com.apiece.ulala.adapter.web.reward.dto

import com.apiece.ulala.app.reward.Reward
import com.apiece.ulala.app.reward.SourceType
import java.math.BigDecimal
import java.time.LocalDateTime

class RewardResponse(
    val id: String,
    val memberId: String,
    val sourceId: String,
    val sourceType: SourceType,
    val point: BigDecimal,
    val exp: BigDecimal,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
) {

    companion object {
        fun from(reward: Reward): RewardResponse {
            return RewardResponse(
                id = reward.id.toString(),
                memberId = reward.memberId.toString(),
                sourceId = reward.sourceId.toString(),
                sourceType = reward.sourceType,
                point = reward.point,
                exp = reward.exp,
                createdAt = reward.createdAt,
                modifiedAt = reward.modifiedAt,
            )
        }
    }
}
