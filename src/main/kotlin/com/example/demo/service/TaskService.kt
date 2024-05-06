package com.example.demo.service

import com.example.demo.models.Task
import com.example.demo.repository.TaskRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import kotlin.random.Random

@Service
class TaskService(private val repository: TaskRepository, private val bot: BotOutput) {
    fun getList(chatId: Long) {
        val tasks = repository.getAllNotDeletedNotFinishedOrdered()
        for ((index, task) in tasks.withIndex()) {
            sendTask(task, chatId, index)
        }
    }

    fun getAll(chatId: Long) {
        val tasks = repository.findAll(Sort.by("displayOrder"))
        for ((index, task) in tasks.withIndex()) {
            sendTask(task, chatId, index)
        }
    }

    private fun sendTask(task: Task, chatId: Long, index: Int) {
        val list = mutableListOf<InlineKeyboardButton>()

        if (!task.isFinished) {
            val button = InlineKeyboardButton.builder().text("✅").callbackData("m;${task.id}").build()
            list.add(button)
        }
        list.add(InlineKeyboardButton.builder().text("↑").callbackData("up;${task.id}").build())
        list.add(InlineKeyboardButton.builder().text("↓").callbackData("down;${task.id}").build())
        if (!task.isDeleted) {
            val button = InlineKeyboardButton.builder().text("✘").callbackData("d;$task").build()
            list.add(button)
        }
        val markup = InlineKeyboardMarkup.builder().keyboardRow(list).build()
        val message = SendMessage.builder()
                .text("$index) ${task.name}")
                .chatId(chatId)
                .replyMarkup(markup)
                .build()
        bot.execute(message)
    }

    fun addTask(taskText: String) {
        val task = Task()
        task.name = taskText
        task.displayOrder = Random.nextDouble()
        repository.save(task)
    }

    fun move(second: String, direction: Int) {

        val tasks = repository.getAllNotDeletedNotFinishedOrdered()
        for ((index, b) in tasks.withIndex()) {
            if (b.id == second.toLong()) {
                val tmp = b.displayOrder
                val a = tasks[index - direction]
                b.displayOrder = a.displayOrder
                a.displayOrder = tmp
                repository.save(b)
                repository.save(a)
                return
            }
        }
    }

    fun up(second: String) {
        move(second, +1)
    }

    fun down(second: String) {
        move(second, -1)
    }

    @Transactional
    fun delete(second: String) {
        repository.markAsDeleted(second.toLong())
    }

    @Transactional
    fun markAsFinished(second: String) {
        repository.markAsFinished(second.toLong())
    }


}

