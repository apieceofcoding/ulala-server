package com.apiece.ulala.adapter.web.character

import com.apiece.ulala.adapter.web.character.dto.CharacterCreateRequest
import com.apiece.ulala.adapter.web.character.dto.CharacterResponse
import com.apiece.ulala.adapter.web.character.dto.CharacterUpdateRequest
import com.apiece.ulala.app.character.CharacterService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
class CharacterController(
    private val characterService: CharacterService,
) {

    @PostMapping("/api/characters")
    fun createCharacter(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: CharacterCreateRequest
    ): CharacterResponse {
        val character = characterService.createCharacter(
            memberId = user.username.toLong(),
            roleModel = request.roleModel,
        )
        return CharacterResponse.from(character)
    }

    @GetMapping("/api/characters/me")
    fun getMyCharacters(@AuthenticationPrincipal user: User): List<CharacterResponse> {
        val characters = characterService.getByMemberId(user.username.toLong())
        return characters.map { CharacterResponse.from(it) }
    }

    @GetMapping("/api/characters/{id}")
    fun getCharacter(@PathVariable id: Long): CharacterResponse {
        val character = characterService.getById(id)
        return CharacterResponse.from(character)
    }

    @PatchMapping("/api/characters/{id}")
    fun updateCharacter(
        @PathVariable id: Long,
        @RequestBody request: CharacterUpdateRequest
    ): CharacterResponse {
        val character = characterService.updateCharacter(id, request.roleModel)
        return CharacterResponse.from(character)
    }

    @DeleteMapping("/api/characters/{id}")
    fun deleteCharacter(@PathVariable id: Long) {
        characterService.deleteCharacter(id)
    }
}
