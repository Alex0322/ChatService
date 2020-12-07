package ru.netology

data class Message(
    val id: Long,
    val isIncoming: Boolean = false,
    val isReaded: Boolean = false,
    val content: String = ""
)

class Chat(val id: Long, val toUserId: Long) {
    private val messages = mutableListOf<Message>()
    private var messageId: Long = 0

    fun newMessage(content: String) {
        Message(id = messageId++, content = content).also { messages.add(it) }
    }

    fun editMessage(messageId: Long, content: String): Message {
        val messageIndex = messages.asSequence().indexOfFirst { it.id == messageId }
        if (messageIndex < 0)
            throw error("Message not found")
        val m = messages[messageIndex].copy(content = content)
        messages[messageIndex] = m
        return m
    }

    fun deleteMessage(messageId: Long) {
        messages.removeIf { it.id == messageId }
    }

    fun incomeMessage(content: String) {
        Message(id = messageId++, isIncoming = true).also { messages.add(it) }
    }

    fun getMessages(lastMessageId: Long, messagesCount: Int): List<Message> {
        return messages.asSequence().filter {
            it.id >= lastMessageId && it.isIncoming
        }.take(
            messagesCount
        ).map {
            it.copy(isReaded = true)
        }.toList()
    }

    fun hasUnreadMessages(): Boolean {
        return messages.asSequence().indexOfFirst { it.isReaded == false } >= 0
    }
}

class ChatService(val fromUserId: Long) {
    private val chats = mutableListOf<Chat>()
    private var chatId: Long = 0

    fun messageToUser(toUserId: Long, content: String): Chat {
        val chatIndex: Int = chats.asSequence().indexOfFirst { it.toUserId == toUserId }
        val c = if (chatIndex >= 0) chats[chatIndex] else createChat(toUserId)
        c.newMessage(content)
        return c
    }

    fun createChat(toUserId: Long): Chat {
        return Chat(id = chatId++, toUserId = toUserId).also { chats.add(it) }
    }

    fun deleteChat(chatId: Long) {
        chats.removeIf { it.id == chatId }
    }

    fun getChats(): List<Chat> = chats.toList()

    fun getUnreadChatsCount(): Int {
        return chats.asSequence().filter { it.hasUnreadMessages() }.count()
    }

    fun getChatMessages(chatId: Long, lastMessageId: Long, messagesCount: Int): List<Message> {
        val chatIndex = chats.asSequence().indexOfFirst { it.id == chatId }
        if (chatIndex < 0)
            throw error("Chat not found")
        return chats[chatIndex].getMessages(lastMessageId, messagesCount)
    }
}
