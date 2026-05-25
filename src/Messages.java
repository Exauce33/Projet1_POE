import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Messages {

    private final List<Message> messageList = new ArrayList<>();
    private int numMessagesSent = 0;

    // Generates a 10-digit ID
    public String generateMessageID() {
        long min = 1_000_000_000L;
        long max = 9_999_999_999L;
        long id  = min + (long)(new Random().nextDouble() * (max - min));
        return String.valueOf(id);
    }

    // Check that the ID is no longer than 10 characters.
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    // Check the recipient's phone number
    public String checkRecipientCell(String cellNumber) {
        String regex = "^\\+\\d{1,3}\\d{1,10}$";
        if (cellNumber != null && cellNumber.matches(regex)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    // Check the message length
    public String validateMessage(String message) {
        if (message == null || message.length() > 250) {
            int excess = (message == null) ? 250 : message.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    // Creates the message hash
    public String createMessageHash(String messageID, int messageNumber, String message) {
        String firstTwo = messageID.substring(0, 2);
        String[] words  = message.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Manages the choice of action
    public String sentMessage(String choice) {
        if ("1".equals(choice)) return "Message successfully sent.";
        if ("2".equals(choice)) return "Press 0 to delete the message.";
        if ("3".equals(choice)) return "Message successfully stored.";
        return "Invalid selection. Please choose 1, 2, or 3.";
    }

    // Add a message to the list
    public void addMessage(Message msg) {
        messageList.add(msg);
        if ("Sent".equals(msg.getStatus())) {
            numMessagesSent++;
        }
    }

    // Returns all sent messages
    public String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : messageList) {
            if ("Sent".equals(m.getStatus())) {
                sb.append("Message ID   : ").append(m.getMessageID()).append("\n");
                sb.append("Message Hash : ").append(m.getMessageHash()).append("\n");
                sb.append("Recipient    : ").append(m.getRecipient()).append("\n");
                sb.append("Message      : ").append(m.getMessageText()).append("\n");
                sb.append("-".repeat(40)).append("\n");
            }
        }
        return sb.length() == 0 ? "No messages sent." : sb.toString();
    }

    // Returns the total number of messages sent.
    public int returnTotalMessages() {
        return numMessagesSent;
    }

    // Stores a message in a JSON file
    public void storeMessage(Message msg) {
        String filename = "stored_messages.json";
        List<Message> storedMessages = new ArrayList<>();

        try {
            FileReader reader = new FileReader(filename);
            Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
            List<Message> existing = new Gson().fromJson(reader, listType);
            if (existing != null) {
                storedMessages.addAll(existing);
            }
            reader.close();
        } catch (IOException ignored) {

            // The file does not yet exist
        }

        storedMessages.add(msg);

        try {
            FileWriter writer = new FileWriter(filename);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(storedMessages, writer);
            writer.close();
            System.out.println("Message saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
}