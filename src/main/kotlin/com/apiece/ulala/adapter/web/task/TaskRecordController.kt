package com.apiece.ulala.adapter.web.task

import com.apiece.ulala.adapter.web.task.dto.TaskResponse
import com.apiece.ulala.app.task.TaskDailyStats
import com.apiece.ulala.app.task.TaskService
import com.apiece.ulala.app.task.TaskWeeklyStats
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class TaskRecordController(
    private val taskService: TaskService
) {

    @GetMapping("/api/tasks/daily-stats")
    fun getTaskDailyStats(
        @AuthenticationPrincipal user: User,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): List<TaskDailyStats> {
        return taskService.getTaskDailyStats(user.username.toLong(), startDate, endDate)
    }

    @GetMapping("/api/tasks/recent")
    fun getRecentlyModifiedTasks(
        @AuthenticationPrincipal user: User,
        @RequestParam(defaultValue = "5") limit: Int
    ): List<TaskResponse> {
        return taskService.getRecentlyModifiedTasks(user.username.toLong(), limit)
            .map { TaskResponse.from(it) }
            .toList()
    }

    @GetMapping("/api/tasks/weekly-stats")
    fun getTaskWeeklyStats(
        @AuthenticationPrincipal user: User
    ): TaskWeeklyStats {
        return taskService.getTaskWeeklyStats(user.username.toLong())
    }
}
