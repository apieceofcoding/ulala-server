package com.apiece.ulala.adapter.web.task.dto

import com.apiece.ulala.app.task.TaskStatus
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

class TaskUpdateRequest(

    @field:Size(max = 100, message = "제목은 100자 이하여야 합니다")
    val title: String?,

    val description: String?,

    val status: TaskStatus?,

    val startAt: LocalDateTime?,

    val endAt: LocalDateTime?,

    val dueAt: LocalDateTime?,
)
