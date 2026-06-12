import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Registration registration = new Registration();
        login auth = new login();

        //  PART 1 — Inscription
        System.out.println("--- REGISTRATION ---");
        System.out.print("Enter your first name: ");
        String firstname = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastname = scanner.nextLine();

        // Username Loop
        String username = "";
        while (true) {
            System.out.println("\nEnter your Username (must include '_' and be max 5 characters): ");
            username = scanner.nextLine();
            if (registration.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            }
            System.out.println("Username is not correctly formatted. Try again!\n");
        }

        // Password Loop
        String password = "";
        while (true) {
            System.out.println("\nSet up your password (8+ chars, 1 capital, 1 number, 1 special character):");
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
            if (registration.checkPassword(password)) {
                System.out.println("Password successfully captured.");
                break;
            }
            System.out.println("Password is not correctly formatted. Please try again.");
        }

        // Phone Loop
        String phone = "";
        while (true) {
            System.out.println("\nEnter phone (e.g., +27123456789): ");
            phone = scanner.nextLine();
            if (registration.checkCellPhoneNumber(phone)) {
                System.out.println("Phone number valid!");
                break;
            }
            System.out.println("Phone number incorrectly formatted or missing international code.");
        }

        auth.registerUser(username, password, firstname, lastname);
        System.out.println("\nRegistration Complete!\n");

        //  PART 1 — Connexion

        System.out.println("--- LOGIN ---");
        boolean loginSuccessful = false;

        while (!loginSuccessful) {
            System.out.print("Enter Username: ");
            String loginUser = scanner.nextLine();
            System.out.print("Enter Password: ");
            String loginPass = scanner.nextLine();

            if (auth.loginUser(loginUser, loginPass)) {
                System.out.println("\n" + auth.getWelcomeMessage());
                loginSuccessful = true;
            } else {
                System.out.println("Username or password incorrect.");
                System.out.println("Please try again\n");
            }
        }
        System.out.println("Login completed\n");

        //  PART 2 — QuickChat

        System.out.println("\nWelcome to QuickChat.");

        System.out.print("\nHow many messages do you want to send? ");
        int maxMessages = Integer.parseInt(scanner.nextLine());

        Messages messagesManager = new Messages();
        int      messageCounter  = 0;

        boolean running = true;
        while (running) {
            System.out.println("\n========== MENU ==========");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");   // PART 3 — new menu option
            System.out.println("4) Quit");               // PART 3 — Quit moved to option 4
            System.out.print("Choose an option: ");
            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1":
                    if (messageCounter >= maxMessages) {
                        System.out.println("You have reached your message limit of " + maxMessages + ".");
                        break;
                    }

                    // Recipient
                    String recipient = "";
                    while (true) {
                        System.out.print("\nEnter recipient number (e.g., +27123456789): ");
                        recipient = scanner.nextLine();
                        String cellResult = messagesManager.checkRecipientCell(recipient);
                        System.out.println(cellResult);
                        if (cellResult.equals("Cell phone number successfully captured.")) break;
                    }

                    // Message text
                    String messageText = "";
                    while (true) {
                        System.out.print("Enter your message (max 250 characters): ");
                        messageText = scanner.nextLine();
                        String msgResult = messagesManager.validateMessage(messageText);
                        System.out.println(msgResult);
                        if (msgResult.equals("Message ready to send.")) break;
                    }

                    // Generate ID & Hash
                    String messageID = messagesManager.generateMessageID();
                    String hash      = messagesManager.createMessageHash(messageID, messageCounter, messageText);

                    System.out.println("\nMessage ID   : " + messageID);
                    System.out.println("Message Hash : " + hash);

                    // Choose action
                    System.out.println("\n1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message");
                    System.out.print("Choose: ");
                    String action       = scanner.nextLine().trim();
                    String actionResult = messagesManager.sentMessage(action);
                    System.out.println(actionResult);

                    Message msg = new Message(messageID, messageCounter, recipient, messageText, hash);

                    if ("1".equals(action)) {
                        msg.setStatus("Sent");
                        messagesManager.addMessage(msg);
                        System.out.println("\n── Sent Message Details ──");
                        System.out.println("Message ID   : " + messageID);
                        System.out.println("Message Hash : " + hash);
                        System.out.println("Recipient    : " + recipient);
                        System.out.println("Message      : " + messageText);

                    } else if ("2".equals(action)) {
                        msg.setStatus("Disregarded");
                        messagesManager.addMessage(msg);   // PART 3 to store disregarded messages in array

                    } else if ("3".equals(action)) {
                        msg.setStatus("Stored");
                        messagesManager.addMessage(msg);
                        messagesManager.storeMessage(msg);
                    }

                    messageCounter++;
                    break;

                case "2":
                    // PART 3  Display sender and recipient of all sent messages
                    System.out.println("\n RECENTLY SENT MESSAGES ");
                    System.out.println(messagesManager.displaySendersAndRecipients());
                    break;

                case "3":
                    // PART 3  Stored Messages sub-menu
                    messagesManager.loadStoredMessagesFromJSON();   // read JSON into stored array
                    boolean inStoredMenu = true;
                    while (inStoredMenu) {
                        System.out.println("\n STORED MESSAGES MENU ");
                        System.out.println("a) Display sender & recipient of all stored messages");
                        System.out.println("b) Display the longest message");
                        System.out.println("c) Search for a Message ID");
                        System.out.println("d) Search all messages for a recipient");
                        System.out.println("e) Delete a message using its hash");
                        System.out.println("f) Display full report");
                        System.out.println("q) Back to main menu");
                        System.out.print("Choose: ");
                        String sub = scanner.nextLine().trim().toLowerCase();

                        switch (sub) {
                            case "a":
                                // a) Sender and recipient of all stored messages
                                System.out.println(messagesManager.displaySendersAndRecipients());
                                break;
                            case "b":
                                // b) Longest message
                                System.out.println("Longest message: " + messagesManager.displayLongestMessage());
                                break;
                            case "c":
                                // c) Search by Message ID
                                System.out.print("Enter Message ID: ");
                                System.out.println(messagesManager.searchByMessageID(scanner.nextLine()));
                                break;
                            case "d":
                                // d) Search by recipient
                                System.out.print("Enter recipient: ");
                                System.out.println(messagesManager.searchByRecipient(scanner.nextLine()));
                                break;
                            case "e":
                                // e) Delete by hash
                                System.out.print("Enter message hash: ");
                                System.out.println(messagesManager.deleteByHash(scanner.nextLine()));
                                break;
                            case "f":
                                // f) Full report
                                System.out.println(messagesManager.displayReport());
                                break;
                            case "q":
                                inStoredMenu = false;
                                break;
                            default:
                                System.out.println("Invalid option.");
                        }
                    }
                    break;

                case "4":
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3, or 4.");
            }
        }

        // Summary
        System.out.println("\nTotal messages sent: " + messagesManager.returnTotalMessages());
        System.out.println("\n─── ALL SENT MESSAGES ───");
        System.out.println(messagesManager.printMessages());

    }
}

