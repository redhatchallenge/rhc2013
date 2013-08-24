package org.redhatchallenge.rhc2013.shared;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class FieldVerifier {

    public static boolean isValidEmail(String email) {
        return email.toUpperCase().matches("^[0-9A-Z._-]+@[0-9A-Z.]{2,120}$");
    }

    public static boolean emailIsNull(String email){

        return email.isEmpty();
    }

    public static boolean isValidPassword(String password) {

        // Password should contain at least 8 characters with at least 1 uppercase letter, 1 lowercase letter and 1 numeric character
        return password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}");
    }

    public static boolean passwordIsNull(String password){

        return password.isEmpty();
    }

    public static boolean fnIsNull(String firstName){

        return firstName.isEmpty();
    }

    public static boolean lnIsNull(String lastName){

        return lastName.isEmpty();
    }

    public static boolean contactIsNull(String contact){

        return contact.isEmpty();
    }

    public static boolean isValidContact(String contact) {
        return contact.matches("^[0-9]+$");
    }

    public static boolean schoolIsNull(String school){

        return school.isEmpty();
    }

}
