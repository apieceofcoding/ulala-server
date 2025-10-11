package com.apiece.ulala.adapter.web.member.dto

import com.apiece.ulala.app.member.Member

class MemberListResponse(
    val id: Long,
    val memberId: String,
    val displayName: String?,
    val level: String,
) {

    companion object {
        fun from(member: Member): MemberListResponse {
            return MemberListResponse(
                id = member.id,
                memberId = member.memberId,
                displayName = member.displayName,
                level = member.memberLevel,
            )
        }
    }
}
