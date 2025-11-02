package com.apiece.ulala.app.task

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface TaskRepository : JpaRepository<Task, Long> {

    fun findByMemberIdAndDeletedFalse(memberId: Long, pageable: Pageable): Page<Task>

    @Query(
        value = """
        SELECT t.modifiedAt as modifiedAt
        FROM Task t
        WHERE t.memberId = :memberId
        AND t.deleted = false
        AND t.modifiedAt >= :startAt
        AND t.modifiedAt < :endAt
        """
    )
    fun findByMemberIdAndModifiedAtBetween(
        @Param("memberId") memberId: Long,
        @Param("startAt") startAt: LocalDateTime,
        @Param("endAt") endAt: LocalDateTime
    ): List<TaskModifiedAt>

    fun findTopByMemberIdAndDeletedFalse(memberId: Long, pageable: Pageable): Slice<Task>

    fun findByMemberIdAndCreatedAtBetweenAndDeletedFalse(
        memberId: Long,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): List<Task>
}
