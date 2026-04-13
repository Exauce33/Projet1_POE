import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Registration registration = new Registration();
        login auth = new login();

        System.out.println("--- REGISTRATION ---");
        System.out.print("Enter your first name: ");
        String firstname = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastname = scanner.nextLine();

        // Username Loop
        String username = "";
        while (true) {
            System.out.println("\nEnter your Username (Please ensure that the username must include an underscore(_) and not be longer than five characters): ");
            username = scanner.nextLine();
            if (registration.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            }
            System.out.println("Username is not correctly formatted; please follow the instruction. \nTry again!\n");
        }

        // Password Loop
        String password = "";
        while (true) {
            System.out.println("\nset up your password \nRules: 8 characters min, 1capital letter, 1number, 1special character.\n");
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
            if (registration.checkPassword(password)) {
                System.out.println("Password successfully captured.");
                break;
            }
            System.out.println("Password is not correctly formatted. (the password must contains 8 chars, 1 Capital, 1 Number, 1 Special).");
        }

        // Phone Loop
        // Source: YouTube Tutorial; URL: [https://www.youtube.com/watch?v=2M1CpEJZ6rk]
        String phone = "";
        while (true) {
            System.out.println("\nEnter phone (e.g., +27123456789): ");
            phone = scanner.nextLine();
            if (registration.checkCellPhoneNumber(phone)) {
                System.out.println("Phone number valid!");
                break;
            }
            System.out.println("Phone number incorrectly formatted or does not contain international code");
        }

        // Confirmation of subscription
        auth.registerUser(username, password, firstname, lastname);
        System.out.println("\nRegistration Complete!\n");

        // --- LOGIN TEST ---
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
        System.out.println("Login completed\n" );
    }
}

