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
    var username: String,

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

    fun delete() {
        deleted = true
        deletedAt = LocalDateTime.now()
    }
}
