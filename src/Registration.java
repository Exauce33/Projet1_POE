import java.util.Scanner;
public class Registration {


    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }


        public boolean checkPassword(String password) {
            if (password.length() < 8) return false;
            boolean hasUpper = false, hasDigit = false, hasSpecial = false;
            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUpper = true;
                else if (Character.isDigit(c)) hasDigit = true;
                else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
            }
            return hasUpper && hasDigit && hasSpecial;
        }
     public boolean checkCellPhoneNumber(String phone){
        // Regex: Sign +, country code (1-3 chiffres),(1-10 chiffres)
        String phoneRegex = "^\\+\\d{1,3}\\d{1,10}$";
        return phone.matches(phoneRegex);


        }
    }

