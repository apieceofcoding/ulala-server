package com.apiece.ulala.adapter.web.task.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

class TaskCreateRequest(

    @field:NotBlank(message = "제목은 필수입니다")
    @field:Size(max = 100, message = "제목은 100자 이하여야 합니다")
    val title: String,

    val description: String?,

    val startAt: LocalDateTime?,

    val endAt: LocalDateTime?,

    val dueAt: LocalDateTime?,
)
