package com.apiece.ulala.app.task

import com.apiece.ulala.app.db.IdGenerator
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.time.LocalDate
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
        return taskRepository.findByMemberIdAndDeletedFalse(memberId, pageable)
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

    fun getTaskDailyStats(memberId: Long, startDate: LocalDate, endDate: LocalDate): List<TaskDailyStats> {
        require(startDate <= endDate) { "startAt must be before or equal to endAt" }

        val startAt = startDate.atStartOfDay()
        val endAt = endDate.plusDays(1).atStartOfDay()
        val tasks: List<TaskModifiedAt> = taskRepository.findByMemberIdAndModifiedAtBetween(memberId, startAt, endAt)

        val stats = tasks
            .groupBy { it.modifiedAt.toLocalDate() }
            .map { (date, taskList) -> TaskDailyStats(date, count = taskList.size) }
            .sortedBy { it.date }

        return stats
    }

    fun getRecentlyModifiedTasks(memberId: Long, pageSize: Int = 5): Slice<Task> {
        val pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "modifiedAt"))
        return taskRepository.findTopByMemberIdAndDeletedFalse(memberId, pageable)
    }

    fun getTaskWeeklyStats(memberId: Long): TaskWeeklyStats {
        val today = LocalDate.now()
        val startDate = today.minusDays(6)
        val startAt = startDate.atStartOfDay()
        val endAt = today.plusDays(1).atStartOfDay()

        val tasks = taskRepository.findByMemberIdAndCreatedAtBetweenAndDeletedFalse(memberId, startAt, endAt)

        val totalCount = tasks.size
        val completedCount = tasks.count { it.status == TaskStatus.DONE }

        return TaskWeeklyStats(totalCount, completedCount)
    }
}
