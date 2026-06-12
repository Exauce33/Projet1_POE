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

    // ===== ARRAYS (Part 3) =====
    private final List<Message> sentMessages        = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages      = new ArrayList<>();
    private final List<String>  messageHashes       = new ArrayList<>();
    private final List<String>  messageIDs          = new ArrayList<>();

    private int numMessagesSent = 0;

    // Generates a 10-digit ID
    public String generateMessageID() {
        long min = 1_000_000_000L;
        long max = 9_999_999_999L;
        long id  = min + (long)(new Random().nextDouble() * (max - min));
        return String.valueOf(id);
    }

    // Check that the ID is no longer than 10 characters
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

    // Add a message to the correct array based on its status
    public void addMessage(Message msg) {
        String status = msg.getStatus();
        if ("Sent".equals(status)) {
            sentMessages.add(msg);
            numMessagesSent++;
        } else if ("Stored".equals(status)) {
            storedMessages.add(msg);
        } else if ("Disregarded".equals(status)) {
            disregardedMessages.add(msg);
        }
        // Track hash and ID for every message
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());
    }

    // a) Sender and recipient of ALL sent messages
    public String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Recipient: ").append(m.getRecipient())
                    .append(" | Message: ").append(m.getMessageText()).append("\n");
        }
        return sb.length() == 0 ? "No sent messages." : sb.toString();
    }

    // b) Longest message (sent + stored)
    public String displayLongestMessage() {
        Message longest = null;
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        for (Message m : all) {
            if (longest == null || m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return (longest == null) ? "No messages found." : longest.getMessageText();
    }

    // c) Search by Message ID -> returns recipient + message
    public String searchByMessageID(String id) {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);

        for (Message m : all) {
            if (m.getMessageID().equals(id)) {
                return "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    // d) Search all messages for a given recipient
    public String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);

        for (Message m : all) {
            if (m.getRecipient().equals(recipient)) {
                sb.append("\"").append(m.getMessageText()).append("\"\n");
            }
        }
        return sb.length() == 0 ? "No messages found for " + recipient : sb.toString();
    }

    // e) Delete a message using its hash
    public String deleteByHash(String hash) {
        List<List<Message>> arrays = new ArrayList<>();
        arrays.add(sentMessages);
        arrays.add(storedMessages);
        arrays.add(disregardedMessages);

        for (List<Message> arr : arrays) {
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getMessageHash().equals(hash)) {
                    String text = arr.get(i).getMessageText();
                    arr.remove(i);
                    messageHashes.remove(hash);
                    return "Message: \"" + text + "\" successfully deleted.";
                }
            }
        }
        return "Message hash not found.";
    }

    // f) Full report of all sent messages
    public String displayReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=========== SENT MESSAGES REPORT ===========\n");
        for (Message m : sentMessages) {
            sb.append("Message Hash : ").append(m.getMessageHash()).append("\n");
            sb.append("Recipient    : ").append(m.getRecipient()).append("\n");
            sb.append("Message      : ").append(m.getMessageText()).append("\n");
            sb.append("-".repeat(40)).append("\n");
        }
        return sentMessages.isEmpty() ? "No sent messages to report." : sb.toString();
    }

    // ===== Total (ONE definition only) =====
    public int returnTotalMessages() {
        return numMessagesSent;
    }

    // ===== Print (ONE definition only) =====
    public String printMessages() {
        return displayReport();
    }

    // ===== Getters for unit tests =====
    public List<Message> getSentMessages()        { return sentMessages; }
    public List<Message> getDisregardedMessages() { return disregardedMessages; }
    public List<Message> getStoredMessages()      { return storedMessages; }
    public List<String>  getMessageHashes()       { return messageHashes; }
    public List<String>  getMessageIDs()          { return messageIDs; }

    // ===== Load from JSON into storedMessages array =====
    public void loadStoredMessagesFromJSON() {
        String filename = "stored_messages.json";
        try (FileReader reader = new FileReader(filename)) {
            Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
            List<Message> existing = new Gson().fromJson(reader, listType);
            if (existing != null) {
                storedMessages.addAll(existing);
                for (Message m : existing) {
                    messageHashes.add(m.getMessageHash());
                    messageIDs.add(m.getMessageID());
                }
            }
        } catch (IOException e) {
            System.out.println("No stored messages file found yet.");
        }
    }

    // ===== Store to JSON (ONE definition only) =====
    public void storeMessage(Message msg) {
        String filename = "stored_messages.json";
        List<Message> stored = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {
            Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
            List<Message> existing = new Gson().fromJson(reader, listType);
            if (existing != null) stored.addAll(existing);
        } catch (IOException ignored) {}

        stored.add(msg);

        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(stored, writer);
            System.out.println("Message saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

}
