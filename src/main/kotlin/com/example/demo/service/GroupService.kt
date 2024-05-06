//package com.example.demo.service
//
//import com.example.demo.models.Group
//import com.example.demo.repository.GroupsRepository
//import org.springframework.stereotype.Service
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage
//
//@Service
//class GroupService(
//        private val repository: GroupsRepository,
//        private val bot: BotOutput) {
//    fun getGroups(chatId: Long) {
//        val groups = repository.findAll().mapIndexed { index, group -> "$index) ${group.name}\n" }.joinToString("")
//        val message = SendMessage.builder()
//                .text("Groups: \n$groups")
//                .chatId(chatId)
//                .build()
//        bot.execute(message)
//    }
//
//    fun addGroup(text: String) {
//        val (first, second) = text.split(";")
//        val group = Group()
//        group.name = second.trim()
//        group.priority = first.toInt()
//        repository.save(group)
//        println()
//    }
//}
