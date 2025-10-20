package com.apiece.ulala.adapter.web.task

import com.apiece.ulala.adapter.web.task.dto.TaskCreateRequest
import com.apiece.ulala.adapter.web.task.dto.TaskResponse
import com.apiece.ulala.adapter.web.task.dto.TaskUpdateRequest
import com.apiece.ulala.app.task.TaskService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: TaskCreateRequest
    ): TaskResponse {
        val task = taskService.createTask(
            memberId = user.username.toLong(),
            title = request.title,
            description = request.description,
            startAt = request.startAt,
            endAt = request.endAt,
            dueAt = request.dueAt,
        )
        return TaskResponse.from(task)
    }

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: String): TaskResponse {
        val task = taskService.getById(id.toLong())
        return TaskResponse.from(task)
    }

    @GetMapping
    fun getMyTasks(
        @AuthenticationPrincipal user: User,
        @PageableDefault(size = 10) pageable: Pageable
    ): Page<TaskResponse> {
        return taskService.getPagedTasksByMemberId(user.username.toLong(), pageable)
            .map { TaskResponse.from(it) }
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: String,
        @Valid @RequestBody request: TaskUpdateRequest
    ): TaskResponse {
        val task = taskService.updateTask(
            id = id.toLong(),
            title = request.title,
            description = request.description,
            status = request.status,
            startAt = request.startAt,
            endAt = request.endAt,
            dueAt = request.dueAt,
        )
        return TaskResponse.from(task)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(@PathVariable id: String) {
        taskService.deleteTask(id.toLong())
    }
}
