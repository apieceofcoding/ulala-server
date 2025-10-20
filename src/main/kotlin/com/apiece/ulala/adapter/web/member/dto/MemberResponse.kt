package com.apiece.ulala.adapter.web.member.dto

import com.apiece.ulala.app.member.Member

class MemberResponse(
    val id: String,
    val username: String,
    val displayName: String?,
    val imageUrl: String?,
    val level: String,
) {

    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id.toString(),
                username = member.username,
                displayName = member.displayName,
                imageUrl = member.imageUrl,
                level = member.memberLevel,
            )
        }
    }
}
