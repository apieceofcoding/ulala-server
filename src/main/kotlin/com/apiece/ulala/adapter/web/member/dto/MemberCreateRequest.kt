package com.apiece.ulala.adapter.web.member.dto

import jakarta.validation.constraints.Size

class MemberCreateRequest(

    @field:Size(min = 6, max = 30, message = "사용자이름는 6자 이상 30자 이하여야 합니다")
    val username: String?,
)
