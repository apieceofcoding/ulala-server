package com.apiece.ulala.app.member

import com.apiece.ulala.app.db.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "members")
class Member private constructor(

    @Id
    var id: Long,

    @Column(nullable = false, length = 30)
    var memberId: String,

    @Column(length = 30)
    var displayName: String? = null,

    var imageUrl: String? = null,

    @Column(nullable = false)
    var memberLevel: String = "1",

    @Column
    var providerUserId: String,

    @Enumerated(EnumType.STRING)
    var provider: MemberProvider,

    @Column(nullable = false)
    var deleted: Boolean = false,

    var deletedAt: LocalDateTime? = null

) : BaseEntity() {

    init {
        require(memberId.isNotBlank()) { "memberId cannot be blank" }
        require(memberId.length <= 30) { "memberId must be less than or equal to 30" }
        displayName?.let { require(it.length <= 30) { "displayName must be less than or equal to 30" } }
        require(providerUserId.isNotBlank()) { "providerUserId cannot be blank" }
    }

    companion object {
        fun create(
            id: Long,
            memberId: String,
            providerUserId: String,
            provider: MemberProvider,
        ): Member {
            return Member(
                id = id,
                memberId = memberId,
                providerUserId = providerUserId,
                provider = provider,
            )
        }
    }

    fun update(memberId: String?, displayName: String?) {
        memberId?.let { this.memberId = it }
        displayName?.let { this.displayName = it }
    }

    fun delete() {
        deleted = true
        deletedAt = LocalDateTime.now()
    }
}
