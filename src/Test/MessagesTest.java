import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessagesTest {

    Messages messages = new Messages();

    //TEST 1 — Message length (≤ 250 chars)


    @Test
    void testMessageLengthSuccess() {
        String msg = "Hi Mike, can you join us for dinner tonight?";
        assertEquals("Message ready to send.", messages.validateMessage(msg));
    }

    @Test
    void testMessageLengthFailure() {
        // Message of 260 characters → exceeds by 10
        String longMsg = "A".repeat(260);
        assertEquals(
                "Message exceeds 250 characters by 10; please reduce the size.",
                messages.validateMessage(longMsg)
        );
    }


    //  TEST 2 — Recipient phone number

    @Test
    void testRecipientCellSuccess() {
        assertEquals(
                "Cell phone number successfully captured.",
                messages.checkRecipientCell("+27718693002")
        );
    }

    @Test
    void testRecipientCellFailure() {
        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain " +
                        "an international code. Please correct the number and try again.",
                messages.checkRecipientCell("08575975889")
        );
    }


    //  TEST 3 — Hash du message

    @Test
    void testMessageHashTestCase1() {
        // ID starts with "00", messageNumber = 0
        // Message: "Hi Mike, can you join us for dinner tonight?"
        // Waiting: 00:0:HITONIGHT

        String hash = messages.createMessageHash(
                "0012345678",
                0,
                "Hi Mike, can you join us for dinner tonight?"
        );
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    void testMessageHashesInLoop() {

        // [messageID, messageNumber, message, hash waiting]
        String[][] testData = {
                {
                        "0012345678", "0",
                        "Hi Mike, can you join us for dinner tonight?",
                        "00:0:HITONIGHT"
                },
                {
                        "08575975889", "1",
                        "Hi Keegan, did you receive the payment?",
                        "12:1:HIPAYMENT"
                }
        };

        for (String[] data : testData) {
            String result = messages.createMessageHash(
                    data[0],
                    Integer.parseInt(data[1]),
                    data[2]
            );
            assertEquals(data[3], result,
                    "Hash incorrect for message: " + data[2]);
        }
    }


    //  TEST 4 — Message ID generated

    @Test
    void testMessageIDCreated() {
        String id = messages.generateMessageID();
        System.out.println("Message ID generated: " + id);

        //Verify that the ID is valid (≤ 10 characters)
        assertTrue(
                messages.checkMessageID(id),
                "The ID must be 10 characters or less."
        );

        // Check that it is exactly 10 digits
        assertEquals(10, id.length(),
                "The generated ID must be exactly 10 digits long.");
    }


    //  TEST 5 — Actions sur le message

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
}