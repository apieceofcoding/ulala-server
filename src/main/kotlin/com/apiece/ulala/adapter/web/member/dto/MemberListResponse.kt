package com.apiece.ulala.adapter.web.member.dto

import com.apiece.ulala.app.member.Member

class MemberListResponse(
    val id: Long,
    val username: String,
    val displayName: String?,
    val level: String,
) {

    companion object {
        fun from(member: Member): MemberListResponse {
            return MemberListResponse(
                id = member.id,
                username = member.username,
                displayName = member.displayName,
                level = member.memberLevel,
            )
        }
    }
}
