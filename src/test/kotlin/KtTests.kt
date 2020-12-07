import org.junit.Test
import ru.netology.*

class KtTests {

    @Test(expected = IllegalStateException::class)
    fun ChatService_Misc() {
        val cs = ChatService(500)
        val c1 = cs.messageToUser(700, "123")
        val c1a = cs.messageToUser(700, "123")
        assert(c1 == c1a)
        val c2 = cs.createChat(800)
        assert(cs.getChats().count() == 2)
        c2.newMessage("4")
        c2.incomeMessage("5")
        c2.incomeMessage("6")
        val uc: Int = cs.getUnreadChatsCount()
        assert(uc == 2)
        cs.deleteChat(c1.id)
        assert(cs.getChats().count() == 1)
        val cm = cs.getChatMessages(c2.id, 1, 3)
        cs.getChatMessages(c2.id - 100, 1, 3)
        println(cm.count())
        assert(cm.count() == 2)
    }

    @Test(expected = IllegalStateException::class)
    fun Chat_Misc() {
        val cs = ChatService(500)
        val c = cs.createChat(300)
        c.newMessage("1")
        val m = c.editMessage(0, "111")
        c.deleteMessage(m.id)
        c.incomeMessage("2")
        c.incomeMessage("3")
        val cm: List<Message> = c.getMessages(1, 1)
        c.editMessage(-1, "")
        assert(cm.count() == 1)
    }
}