package com.apiece.ulala.adapter.web.character.dto

import com.apiece.ulala.app.character.RoleModel
import jakarta.validation.constraints.NotNull

class CharacterCreateRequest(

    @field:NotNull(message = "롤모델은 필수입니다")
    val roleModel: RoleModel,
)
