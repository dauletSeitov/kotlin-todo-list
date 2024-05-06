package com.example.demo.service

import com.example.demo.models.Task
import com.example.demo.repository.TaskRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import kotlin.random.Random

@Service
class TaskService(private val repository: TaskRepository, private val bot: BotOutput) {
    fun getList(chatId: Long) {

        val tasks = repository.getAllNotDeletedOrdered()
        for ((index, task) in tasks.withIndex()) {
            sendTask(task.name, task.id, chatId, index, index == tasks.lastIndex)
        }

    }

    private fun sendTask(name: String, id: Long, chatId: Long, index: Int, isLast: Boolean) {

        val markup = InlineKeyboardMarkup.builder().keyboardRow(listOf(
                InlineKeyboardButton.builder().text("✅").callbackData("m;$id").build(),
                InlineKeyboardButton.builder().text("↑").callbackData("up;$id").build(),
                InlineKeyboardButton.builder().text("↓").callbackData("down;$id").build(),
                InlineKeyboardButton.builder().text("✘").callbackData("d;$id").build()
        )).build()

        val message = SendMessage.builder()
                .text("$index) $name")
                .chatId(chatId)
                .replyMarkup(markup)
                .disableNotification(!isLast)
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

        val tasks = repository.getAllNotDeletedOrdered()
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

