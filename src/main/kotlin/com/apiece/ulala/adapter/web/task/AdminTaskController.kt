package com.apiece.ulala.adapter.web.task

import com.apiece.ulala.adapter.web.task.dto.TaskResponse
import com.apiece.ulala.adapter.web.task.dto.TaskUpdateRequest
import com.apiece.ulala.app.task.TaskService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/api/tasks")
class AdminTaskController(
    private val taskService: TaskService
) {

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: String): TaskResponse {
        val task = taskService.getById(id.toLong())
        return TaskResponse.from(task)
    }

    @GetMapping
    fun getAllTasks(@PageableDefault(size = 10) pageable: Pageable): Page<TaskResponse> {
        return taskService.getPagedTasks(pageable)
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
            displayOrder = request.displayOrder,
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
