package com.apiece.ulala.adapter.web.task.dto

import com.apiece.ulala.app.task.Task
import com.apiece.ulala.app.task.TaskStatus
import java.time.LocalDateTime

class TaskResponse(
    val id: String,
    val memberId: String,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?,
    val dueAt: LocalDateTime?,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
) {

    companion object {
        fun from(task: Task): TaskResponse {
            return TaskResponse(
                id = task.id.toString(),
                memberId = task.memberId.toString(),
                title = task.title,
                description = task.description,
                status = task.status,
                startAt = task.startAt,
                endAt = task.endAt,
                dueAt = task.dueAt,
                createdAt = task.createdAt,
                modifiedAt = task.modifiedAt,
            )
        }
    }
}
