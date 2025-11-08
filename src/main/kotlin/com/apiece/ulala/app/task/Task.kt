package com.apiece.ulala.app.task

import com.apiece.ulala.app.db.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
class Task private constructor(

    @Id
    var id: Long,

    @Column(nullable = false)
    var memberId: Long,

    @Column(nullable = false, length = 100)
    var title: String,

    @Column(length = 4000) // 한글 약 1333자, 영문 4000자
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TaskStatus = TaskStatus.TODO,

    @Column(nullable = false)
    var displayOrder: Int = 1,

    @Column
    var startAt: LocalDateTime? = null,

    @Column
    var endAt: LocalDateTime? = null,

    @Column
    var dueAt: LocalDateTime? = null,

    @Column(nullable = false)
    var deleted: Boolean = false,

    var deletedAt: LocalDateTime? = null
) : BaseEntity() {

    init {
        require(title.isNotBlank()) { "title cannot be blank" }
        require(title.length <= 100) { "title must be less than or equal to 100" }
    }

    companion object {
        fun create(
            id: Long,
            memberId: Long,
            title: String,
            description: String? = null,
            displayOrder: Int = 1,
            startAt: LocalDateTime? = null,
            endAt: LocalDateTime? = null,
            dueAt: LocalDateTime? = null,
        ): Task {
            return Task(
                id = id,
                memberId = memberId,
                title = title,
                description = description,
                displayOrder = displayOrder,
                startAt = startAt ?: LocalDateTime.now(),
                endAt = endAt,
                dueAt = dueAt,
            )
        }
    }

    fun update(
        title: String?,
        description: String?,
        status: TaskStatus?,
        displayOrder: Int?,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?,
        dueAt: LocalDateTime?
    ) {
        title?.let { this.title = it }
        description?.let { this.description = it }
        status?.let {
            this.status = it
            if (it == TaskStatus.DONE && this.endAt == null) {
                this.endAt = LocalDateTime.now()
            } else if (it != TaskStatus.DONE) {
                this.endAt = null
            }
        }
        displayOrder?.let { this.displayOrder = it }
        startAt?.let { this.startAt = it }
        endAt?.let { this.endAt = it }
        dueAt?.let { this.dueAt = it }
    }

    fun delete() {
        deleted = true
        deletedAt = LocalDateTime.now()
    }
}
