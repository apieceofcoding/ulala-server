package com.apiece.ulala.app.member.dto

import com.apiece.ulala.app.member.Member
import java.time.LocalDateTime

class MemberResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val imageUrl: String,
    val level: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
) {

    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                nickname = member.nickname,
                imageUrl = member.imageUrl,
                level = member.memberLevel,
                createdAt = member.createdAt!!,
                modifiedAt = member.modifiedAt!!
            )
        }
    }
}
