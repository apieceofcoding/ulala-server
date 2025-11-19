package com.apiece.ulala.app.character

import com.apiece.ulala.app.member.MemberService
import org.springframework.stereotype.Service

@Service
class CharacterService(
    private val characterRepository: CharacterRepository,
    private val memberService: MemberService,
) {

    fun getById(id: Long): Character {
        return characterRepository.findByIdAndDeletedFalse(id)
            ?: throw IllegalArgumentException("존재하지 않는 캐릭터입니다")
    }

    fun getByMemberId(memberId: Long): List<Character> {
        return characterRepository.findByMemberIdAndDeletedFalse(memberId)
    }

    fun createCharacter(memberId: Long, roleModel: RoleModel): Character {
        // 회원 존재 여부 확인
        memberService.getById(memberId)

        // 동일한 roleModel을 가진 캐릭터가 이미 있는지 확인
        characterRepository.findByMemberIdAndRoleModelAndDeletedFalse(memberId, roleModel)?.let {
            throw IllegalArgumentException("이미 동일한 롤모델의 캐릭터가 존재합니다")
        }

        val character = Character.create(
            roleModel = roleModel,
            memberId = memberId,
        )
        return characterRepository.save(character)
    }

    fun updateCharacter(id: Long, roleModel: RoleModel?): Character {
        val character = getById(id)
        character.update(roleModel)
        return characterRepository.save(character)
    }

    fun deleteCharacter(id: Long) {
        val character = getById(id)
        character.delete()
        characterRepository.save(character)
    }
}
