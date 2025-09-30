package com.apiece.ulala.app.member.dto

import jakarta.validation.constraints.Size

class MemberUpdateRequest(

    @field:Size(max = 30, message = "닉네임은 30자 이하여야 합니다")
    val nickname: String?,

    val imageUrl: String?,

    val level: String?
)
