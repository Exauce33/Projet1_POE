public class login {
    private String registeredUser;
    private String registeredPass;
    private String firstName;
    private String lastName;

    public void registerUser(String user, String pass, String name, String surname) {
        this.registeredUser = user;
        this.registeredPass = pass;
        this.firstName = name;
        this.lastName = surname;
    }

    public boolean loginUser(String user, String pass) {
        return user.equals(registeredUser) && pass.equals(registeredPass);
    }

    public String getWelcomeMessage() {
        return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
    }
}