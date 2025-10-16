package com.apiece.ulala.adapter.web.member.dto

import com.apiece.ulala.app.member.Member
import java.time.LocalDateTime

class MemberResponse(
    val id: Long,
    val username: String,
    val displayName: String?,
    val imageUrl: String?,
    val level: String,
) {

    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id,
                username = member.username,
                displayName = member.displayName,
                imageUrl = member.imageUrl,
                level = member.memberLevel,
            )
        }
    }
}
