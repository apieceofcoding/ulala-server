package com.apiece.ulala.adapter.web.reward.dto

import com.apiece.ulala.app.reward.SourceType
import jakarta.validation.constraints.NotNull

class RewardSearchRequest(
    @field:NotNull(message = "sourceType은 필수입니다")
    val sourceType: SourceType,

    @field:NotNull(message = "sourceIds는 필수입니다")
    val sourceIds: List<Long>
)
