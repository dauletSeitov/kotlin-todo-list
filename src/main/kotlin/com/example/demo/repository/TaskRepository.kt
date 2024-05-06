package com.example.demo.repository

import com.example.demo.models.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.isDeleted is false ORDER BY t.displayOrder")
    fun getAllNotDeletedOrdered(): List<Task>
    @Modifying
    @Query("UPDATE Task t SET t.isFinished = true WHERE t.id = :id")
    fun markAsFinished(@Param("id") id: Long)
    @Modifying
    @Query("UPDATE Task t SET t.isDeleted = true WHERE t.id = :id")
    fun markAsDeleted(@Param("id") id: Long)
}