package com.apiece.ulala.app.member.dto

import com.apiece.ulala.app.member.Member

class MemberListResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val level: String
) {

    companion object {
        fun from(member: Member): MemberListResponse {
            return MemberListResponse(
                id = member.id!!,
                email = member.email,
                nickname = member.nickname,
                level = member.memberLevel
            )
        }
    }
}
