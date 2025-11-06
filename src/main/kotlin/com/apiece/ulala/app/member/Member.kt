package com.apiece.ulala.app.member

import com.apiece.ulala.app.db.BaseEntity
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "members")
class Member private constructor(

    @Id
    var id: Long,

    @Column(nullable = false, length = 30)
    var username: String,

    @Column(length = 30)
    var displayName: String? = null,

    var imageUrl: String? = null,

    @Column
    var providerUserId: String,

    @Enumerated(EnumType.STRING)
    var provider: MemberProvider,

    @Column(nullable = false)
    var memberLevel: Int = 1,

    @Column(nullable = false, precision = 19)
    var point: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, precision = 19)
    var exp: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var deleted: Boolean = false,

    var deletedAt: LocalDateTime? = null

) : BaseEntity() {

    init {
        require(username.isNotBlank()) { "username cannot be blank" }
        require(username.length <= 30) { "username must be less than or equal to 30" }
        displayName?.let { require(it.length <= 30) { "displayName must be less than or equal to 30" } }
        require(providerUserId.isNotBlank()) { "providerUserId cannot be blank" }
    }

    companion object {
        fun create(
            id: Long,
            username: String,
            providerUserId: String,
            provider: MemberProvider,
        ): Member {
            return Member(
                id = id,
                username = username,
                providerUserId = providerUserId,
                provider = provider,
            )
        }
    }

    fun update(username: String?, displayName: String?) {
        username?.let { this.username = it }
        displayName?.let { this.displayName = it }
    }

    fun addReward(point: BigDecimal, exp: BigDecimal) {
        this.point = this.point.add(point)
        this.exp = this.exp.add(exp)
    }

    fun checkAndLevelUp() {
        while (exp >= getRequiredExpForNextLevel()) {
            val requiredExp = getRequiredExpForNextLevel()
            exp = exp.subtract(requiredExp)
            memberLevel++
        }
    }

    fun getRequiredExpForNextLevel(): BigDecimal {
        // 레벨 * 100 * (1 + 레벨 * 0.1)
        // Level 1: 110
        // Level 2: 240
        // Level 3: 390
        // Level 4: 560
        // Level 5: 750
        val baseExp = BigDecimal(100)
        val levelMultiplier = BigDecimal(memberLevel)
        val growthFactor = BigDecimal.ONE.add(levelMultiplier.multiply(BigDecimal("0.1")))
        return baseExp.multiply(levelMultiplier).multiply(growthFactor)
    }

    fun delete() {
        deleted = true
        deletedAt = LocalDateTime.now()
    }
}
