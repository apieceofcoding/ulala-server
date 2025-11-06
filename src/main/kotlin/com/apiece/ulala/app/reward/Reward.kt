package com.apiece.ulala.app.reward

import com.apiece.ulala.app.db.BaseEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "rewards")
class Reward private constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var memberId: Long,

    @Column
    var sourceId: Long,

    @Column
    @Enumerated(EnumType.STRING)
    var sourceType: SourceType,

    @Column(nullable = false, precision = 19)
    var point: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, precision = 19)
    var exp: BigDecimal = BigDecimal.ZERO

) : BaseEntity() {

    init {
        require(point >= BigDecimal.ZERO) { "point must be greater than or equal to 0" }
        require(exp >= BigDecimal.ZERO) { "exp must be greater than or equal to 0" }
    }

    companion object {
        fun create(
            memberId: Long,
            sourceId: Long,
            sourceType: SourceType,
            point: BigDecimal,
            exp: BigDecimal
        ): Reward {
            return Reward(
                memberId = memberId,
                sourceId = sourceId,
                sourceType = sourceType,
                point = point,
                exp = exp
            )
        }
    }

    fun update(
        sourceId: Long?,
        sourceType: SourceType?,
        point: BigDecimal?,
        exp: BigDecimal?
    ) {
        sourceId?.let { this.sourceId = it }
        sourceType?.let { this.sourceType = it }
        point?.let { this.point = it }
        exp?.let { this.exp = it }
    }
}
