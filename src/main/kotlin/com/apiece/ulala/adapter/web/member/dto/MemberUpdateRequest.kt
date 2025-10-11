package com.apiece.ulala.adapter.web.member.dto

import jakarta.validation.constraints.Size

class MemberUpdateRequest(

    @field:Size(min = 6, max = 30, message = "회원아이디는 6자 이상 30자 이하여야 합니다")
    val memberId: String?,

    @field:Size(max = 30, message = "표시이름 30자 이하여야 합니다")
    val displayName: String?,
)
