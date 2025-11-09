package com.apiece.ulala.adapter.web.reward

import com.apiece.ulala.adapter.web.reward.dto.RewardCreateRequest
import com.apiece.ulala.adapter.web.reward.dto.RewardResponse
import com.apiece.ulala.adapter.web.reward.dto.RewardSearchRequest
import com.apiece.ulala.adapter.web.reward.dto.RewardUpdateRequest
import com.apiece.ulala.app.reward.RewardService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
class RewardController(
    private val rewardService: RewardService
) {

    @PostMapping("/api/rewards")
    @ResponseStatus(HttpStatus.CREATED)
    fun createReward(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: RewardCreateRequest
    ): RewardResponse {
        val reward = rewardService.createReward(
            memberId = user.username.toLong(),
            sourceId = request.sourceId,
            sourceType = request.sourceType,
            point = request.point,
            exp = request.exp,
        )
        return RewardResponse.from(reward)
    }

    @GetMapping("/api/rewards/{id}")
    fun getReward(@PathVariable id: String): RewardResponse {
        val reward = rewardService.getById(id.toLong())
        return RewardResponse.from(reward)
    }

    @GetMapping("/api/rewards")
    fun getMyRewards(
        @AuthenticationPrincipal user: User,
        @PageableDefault(size = 10) pageable: Pageable
    ): Page<RewardResponse> {
        return rewardService.getPagedRewardsByMemberId(user.username.toLong(), pageable)
            .map { RewardResponse.from(it) }
    }

    @PutMapping("/api/rewards/{id}")
    fun updateReward(
        @PathVariable id: String,
        @Valid @RequestBody request: RewardUpdateRequest
    ): RewardResponse {
        val reward = rewardService.updateReward(
            id = id.toLong(),
            sourceId = request.sourceId,
            sourceType = request.sourceType,
            point = request.point,
            exp = request.exp,
        )
        return RewardResponse.from(reward)
    }

    @DeleteMapping("/api/rewards/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteReward(@PathVariable id: String) {
        rewardService.deleteReward(id.toLong())
    }

    @PostMapping("/api/rewards/search")
    fun searchRewards(
        @Valid @RequestBody request: RewardSearchRequest
    ): List<RewardResponse> {
        return rewardService.getRewardsBySourceTypeAndSourceIds(request.sourceType, request.sourceIds)
            .map { RewardResponse.from(it) }
    }
}
