package com.apiece.ulala.app.character

import com.apiece.ulala.app.db.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "characters")
class Character private constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var roleModel: RoleModel,

    @Column(nullable = false)
    var memberId: Long,

    @Column(nullable = false)
    var deleted: Boolean = false,

    var deletedAt: LocalDateTime? = null

) : BaseEntity() {

    companion object {
        fun create(
            roleModel: RoleModel,
            memberId: Long,
        ): Character {
            return Character(
                roleModel = roleModel,
                memberId = memberId,
            )
        }
    }

    fun update(roleModel: RoleModel?) {
        roleModel?.let { this.roleModel = it }
    }

    fun delete() {
        deleted = true
        deletedAt = LocalDateTime.now()
    }
}
