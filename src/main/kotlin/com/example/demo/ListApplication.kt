package com.example.demo


import com.example.demo.service.BotInput
import com.example.demo.service.BotOutput
import com.example.demo.service.TaskService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession


@EnableJpaRepositories
@SpringBootApplication
class ListApplication {

    @Value("\${app.bot.username}")
    private lateinit var username: String

    @Value("\${app.bot.token}")
    private lateinit var botToken: String

    @Bean
    fun botInput(taskService: TaskService): BotInput {
        return BotInput(botToken, username, taskService)
    }

    @Bean
    fun botOutput(): BotOutput {
        return BotOutput(botToken, username)
    }

    @Bean
    fun telegramBotsApi(input: BotInput): TelegramBotsApi {
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        telegramBotsApi.registerBot(input)
        return telegramBotsApi
    }

}

fun main(args: Array<String>) {
    runApplication<ListApplication>(*args)
}
