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
    fun getTask(@PathVariable id: Long): TaskResponse {
        val task = taskService.getById(id)
        return TaskResponse.from(task)
    }

    @GetMapping
    fun getAllTasks(@PageableDefault(size = 10) pageable: Pageable): Page<TaskResponse> {
        return taskService.getPagedTasks(pageable)
            .map { TaskResponse.from(it) }
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody request: TaskUpdateRequest
    ): TaskResponse {
        val task = taskService.updateTask(
            id = id,
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
    fun deleteTask(@PathVariable id: Long) {
        taskService.deleteTask(id)
    }
}
