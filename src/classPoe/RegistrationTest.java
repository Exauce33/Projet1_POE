import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    // I am introducing the class to testing
    Registration registration = new Registration();
    login login = new login();

    @Test
    void testUsernameValidation() {
        //Username correctly formatted (True)
        assertTrue(registration.checkUserName("kyl_1"));

        //Username incorrectly formatted (False)
        assertFalse(registration.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void testPasswordComplexity() {
        //Password meets the complexity requirements (True)
        assertTrue(registration.checkPassword("Ch&&sec@ke99!"));

        //Password does not meet the complexity requirements (False)
        assertFalse(registration.checkPassword("password"));
    }

    @Test
    void testPhoneFormat() {
        //Cell phone number correctly formatted (True)
        assertTrue(registration.checkCellPhoneNumber("+27838968976"));

        //Cell phone number incorrectly formatted (False)
        assertFalse(registration.checkCellPhoneNumber("08966553"));
    }

    @Test
    void testLoginFunctionality() {
        // I am trying to first register a fictitious user
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "Ray", "TAT");

        // Login Successful (True)
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));

        // Login Failed (False)
        assertFalse(login.loginUser("kyl_1", "WrongPass123!"));
    }
}