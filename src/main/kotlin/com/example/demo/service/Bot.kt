package com.example.demo.service

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update


class BotInput(token: String,
               private val userName: String,
               private val taskService: TaskService) : TelegramLongPollingBot(token) {
    override fun getBotUsername() = userName

    override fun onUpdateReceived(update: Update?) {
        println(update)
        val text = update?.message?.text
        val data = update?.callbackQuery?.data
        if (text != null) {
            when {
                text.startsWith("/geta") -> taskService.getAll(update.message.chatId)      //get tasks from default list
                text.startsWith("/get") -> taskService.getList(update.message.chatId)      //get tasks from default list
                !text.startsWith("/") -> taskService.addTask(text.trim())             //add task
            }
        }

        if (data != null) {
            val (first, second) = data.split(";")
            when (first) {
                "up" -> taskService.up(second)
                "down" -> taskService.down(second)
                "d" -> taskService.delete(second)
                "m" -> taskService.markAsFinished(second)
            }


        }
    }
}

class BotOutput(token: String, private val userName: String) : TelegramLongPollingBot(token) {
    override fun getBotUsername() = userName
    override fun onUpdateReceived(p0: Update?) {
    }
}