package com.apiece.ulala.app.member

import com.apiece.ulala.app.BaseEntity
import com.apiece.ulala.app.member.dto.MemberCreateRequest
import com.apiece.ulala.app.member.dto.MemberUpdateRequest
import jakarta.persistence.*

@Entity
@Table(name = "members")
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_gen")
    @SequenceGenerator(
        name = "member_seq_gen",
        sequenceName = "member_seq",
        allocationSize = 1
    )
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 100)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false, length = 30)
    var nickname: String,

    var imageUrl: String,

    @Column(nullable = false)
    var memberLevel: String = "1",
) : BaseEntity() {

    companion object {
        fun create(request: MemberCreateRequest, encodedPassword: String): Member {
            return Member(
                email = request.email,
                password = encodedPassword,
                nickname = request.nickname,
                imageUrl = request.imageUrl
            )
        }
    }

    fun update(request: MemberUpdateRequest) {
        request.nickname?.let { this.nickname = it }
        request.imageUrl?.let { this.imageUrl = it }
        request.level?.let { this.memberLevel = it }
    }
}
