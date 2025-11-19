package com.apiece.ulala.app.character

import org.springframework.data.jpa.repository.JpaRepository

interface CharacterRepository : JpaRepository<Character, Long> {

    fun findByIdAndDeletedFalse(id: Long): Character?

    fun findByMemberIdAndDeletedFalse(memberId: Long): List<Character>

    fun findByMemberIdAndRoleModelAndDeletedFalse(memberId: Long, roleModel: RoleModel): Character?
}
