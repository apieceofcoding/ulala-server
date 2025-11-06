package com.apiece.ulala.adapter.web.reward.dto

import com.apiece.ulala.app.reward.SourceType
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal

class RewardCreateRequest(

    val sourceId: Long,

    val sourceType: SourceType,

    @field:DecimalMin(value = "0", message = "포인트는 0 이상이어야 합니다")
    val point: BigDecimal,

    @field:DecimalMin(value = "0", message = "경험치는 0 이상이어야 합니다")
    val exp: BigDecimal,
)
