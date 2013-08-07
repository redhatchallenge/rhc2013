package org.ayrx.rhchallenge.shared;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class FieldVerifier {

    public static boolean isValidPassword(String password) {

        // Password must be at least 8 characters and must contain 1 upper
        // and 1 lower character.

        return password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}");
    }

    public static boolean isValidEmail(String email) {
        return email.toUpperCase().matches("^[0-9A-Z._-]+@[0-9A-Z]+.[0-9A-Z]{2,4}$");
    }

    public static boolean isValidContact(String contact) {
        return contact.matches("^[0-9]+$");
    }
}
