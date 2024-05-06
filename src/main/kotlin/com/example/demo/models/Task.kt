package com.example.demo.models

import jakarta.persistence.*


@Entity
@Table(name = "tasks")
class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L
    var name = ""
    var isDeleted = false
    var isFinished = false
    var displayOrder = 0.0
}

