package com.apiece.ulala.app.task

import com.apiece.ulala.app.db.IdGenerator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val idGenerator: IdGenerator,
) {

    fun createTask(
        memberId: Long,
        title: String,
        description: String?,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?,
        dueAt: LocalDateTime?
    ): Task {
        val task = Task.create(
            id = idGenerator.nextId(),
            memberId = memberId,
            title = title,
            description = description,
            startAt = startAt,
            endAt = endAt,
            dueAt = dueAt,
        )
        return taskRepository.save(task)
    }

    fun getById(id: Long): Task {
        return taskRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 태스크입니다") }
    }

    fun getPagedTasks(pageable: Pageable): Page<Task> {
        return taskRepository.findAll(pageable)
    }

    fun getPagedTasksByMemberId(memberId: Long, pageable: Pageable): Page<Task> {
        return taskRepository.findByMemberIdAndDeleted(memberId, false, pageable)
    }

    fun updateTask(
        id: Long,
        title: String?,
        description: String?,
        status: TaskStatus?,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?,
        dueAt: LocalDateTime?
    ): Task {
        val task = getById(id)
        task.update(title, description, status, startAt, endAt, dueAt)
        return taskRepository.save(task)
    }

    fun deleteTask(id: Long) {
        val task = getById(id)
        task.delete()
        taskRepository.save(task)
    }
}
