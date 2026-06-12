import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessagesTest {

    Messages messages = new Messages();

    // Test Data pour les nouveaux tests
    private Message msg1, msg2, msg3, msg4, msg5;

    @BeforeEach
    public void setUp() {
        messages = new Messages();

        msg1 = new Message("1000000001", 1, "+27834557896",
                "Did you get the cake?", "10:1:DIDCAKE");
        msg1.setStatus("Sent");

        msg2 = new Message("1000000002", 2, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "10:2:WHERETIME");
        msg2.setStatus("Stored");

        msg3 = new Message("1000000003", 3, "+27834484567",
                "Yohoooo, I am at your gate.", "10:3:YOHOOOOGATE");
        msg3.setStatus("Disregarded");

        msg4 = new Message("1000000004", 4, "0838884567",
                "It is dinner time !", "10:4:ITTIME");
        msg4.setStatus("Sent");

        msg5 = new Message("1000000005", 5, "+27838884567",
                "Ok, I am leaving without you.", "10:5:OKYOU");
        msg5.setStatus("Stored");

        messages.addMessage(msg1);
        messages.addMessage(msg2);
        messages.addMessage(msg3);
        messages.addMessage(msg4);
        messages.addMessage(msg5);
    }

    // ========== TES TESTS EXISTANTS (1 à 5) ==========

    @Test
    void testMessageLengthSuccess() {
        String msg = "Hi Mike, can you join us for dinner tonight?";
        assertEquals("Message ready to send.", messages.validateMessage(msg));
    }

    @Test
    void testMessageLengthFailure() {
        String longMsg = "A".repeat(260);
        assertEquals(
                "Message exceeds 250 characters by 10; please reduce the size.",
                messages.validateMessage(longMsg));
    }

    @Test
    void testRecipientCellSuccess() {
        assertEquals("Cell phone number successfully captured.",
                messages.checkRecipientCell("+27718693002"));
    }

    @Test
    void testRecipientCellFailure() {
        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain " +
                        "an international code. Please correct the number and try again.",
                messages.checkRecipientCell("08575975889"));
    }

    @Test
    void testMessageHashTestCase1() {
        String hash = messages.createMessageHash(
                "0012345678", 0,
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    void testMessageHashesInLoop() {
        String[][] testData = {
                {"0012345678", "0",
                        "Hi Mike, can you join us for dinner tonight?",
                        "00:0:HITONIGHT"},
        };
        for (String[] data : testData) {
            String result = messages.createMessageHash(
                    data[0], Integer.parseInt(data[1]), data[2]);
            assertEquals(data[3], result,
                    "Hash incorrect for message: " + data[2]);
        }
    }

    @Test
    void testMessageIDCreated() {
        String id = messages.generateMessageID();
        System.out.println("Message ID generated: " + id);
        assertTrue(messages.checkMessageID(id),
                "The ID must be 10 characters or less.");
        assertEquals(10, id.length(),
                "The generated ID must be exactly 10 digits long.");
    }

    @Test
    void testSentMessageSend() {
        assertEquals("Message successfully sent.", messages.sentMessage("1"));
    }

    @Test
    void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", messages.sentMessage("2"));
    }

    @Test
    void testSentMessageStore() {
        assertEquals("Message successfully stored.", messages.sentMessage("3"));
    }

    //Part3

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(2, messages.getSentMessages().size());
        assertEquals("Did you get the cake?",
                messages.getSentMessages().get(0).getMessageText());
        assertEquals("It is dinner time !",
                messages.getSentMessages().get(1).getMessageText());
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                messages.displayLongestMessage());
    }

    @Test
    public void testSearchByMessageID() {
        String result = messages.searchByMessageID("1000000004");
        assertTrue(result.contains("0838884567"));
        assertTrue(result.contains("It is dinner time !"));
    }

    @Test
    public void testSearchByRecipient() {
        String result = messages.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByHash() {
        String result = messages.deleteByHash("10:2:WHERETIME");
        assertTrue(result.contains("successfully deleted"));
        assertFalse(messages.getStoredMessages().stream()
                .anyMatch(m -> m.getMessageHash().equals("10:2:WHERETIME")));
    }

    @Test
    public void testDisplayReport() {
        String report = messages.displayReport();
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
    }
}
