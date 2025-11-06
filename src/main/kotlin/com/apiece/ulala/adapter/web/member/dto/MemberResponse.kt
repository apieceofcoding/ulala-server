package com.apiece.ulala.adapter.web.member.dto

import com.apiece.ulala.app.member.Member
import java.math.BigDecimal

class MemberResponse(
    val id: String,
    val username: String,
    val displayName: String?,
    val imageUrl: String?,
    val level: Int,
    val point: BigDecimal,
    val exp: BigDecimal,
    val requiredExp: BigDecimal,
) {

    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id.toString(),
                username = member.username,
                displayName = member.displayName,
                imageUrl = member.imageUrl,
                level = member.memberLevel,
                point = member.point,
                exp = member.exp,
                requiredExp = member.getRequiredExpForNextLevel()
            )
        }
    }
}
