package com.apiece.ulala.adapter.web.character.dto

import com.apiece.ulala.app.character.Character
import com.apiece.ulala.app.character.RoleModel

class CharacterResponse(
    val id: Long,
    val roleModel: RoleModel,
    val roleModelDisplayName: String,
    val memberId: String,
) {

    companion object {
        fun from(character: Character): CharacterResponse {
            return CharacterResponse(
                id = character.id!!,
                roleModel = character.roleModel,
                roleModelDisplayName = character.roleModel.displayName,
                memberId = character.memberId.toString(),
            )
        }
    }
}
