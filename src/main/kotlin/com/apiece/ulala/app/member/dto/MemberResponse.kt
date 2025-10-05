package com.apiece.ulala.app.member.dto

import com.apiece.ulala.app.member.Member
import java.time.LocalDateTime

class MemberResponse(
    val id: Long,
    val memberId: String,
    val displayName: String?,
    val imageUrl: String?,
    val level: String,
    val createdAt: LocalDateTime?,
    val createdBy: String?,
    val modifiedAt: LocalDateTime?,
    val modifiedBy: String?,
) {

    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id,
                memberId = member.memberId,
                displayName = member.displayName,
                imageUrl = member.imageUrl,
                level = member.memberLevel,
                createdAt = member.createdAt,
                createdBy = member.createdBy,
                modifiedAt = member.modifiedAt,
                modifiedBy = member.modifiedBy,
            )
        }
    }
}
