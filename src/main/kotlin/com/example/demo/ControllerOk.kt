package com.example.demo

import com.example.demo.models.Task
import com.example.demo.repository.TaskRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class ControllerOk(val repo: TaskRepository) {


    @PostMapping
    fun create(@RequestBody todo: Task) {
        repo.save(todo)
    }

    @GetMapping
    fun get() = repo.findAll()

}