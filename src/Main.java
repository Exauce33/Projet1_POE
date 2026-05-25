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
            System.out.println("3) Quit");
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

                    } else if ("3".equals(action)) {
                        msg.setStatus("Stored");
                        messagesManager.addMessage(msg);
                        messagesManager.storeMessage(msg);
                    }

                    messageCounter++;
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }

        // Summary
        System.out.println("\nTotal messages sent: " + messagesManager.returnTotalMessages());
        System.out.println("\n─── ALL SENT MESSAGES ───");
        System.out.println(messagesManager.printMessages());

    }
}
