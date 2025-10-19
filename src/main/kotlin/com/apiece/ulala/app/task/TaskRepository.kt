package com.apiece.ulala.app.task

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    fun findByMemberIdAndDeleted(memberId: Long, deleted: Boolean, pageable: Pageable): Page<Task>
}
