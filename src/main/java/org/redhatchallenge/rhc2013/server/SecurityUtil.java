package org.redhatchallenge.rhc2013.server;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class SecurityUtil {

    /**
     * Looking at the various GWT widgets, I don't think that XSS is will be a concern.
     * The official GWT documentation seems to agree with me:
     * http://www.gwtproject.org/articles/security_for_gwt_applications.html
     *
     * However, it is still a good idea to strip the "<", ">" and "&" characters anyway
     * since they aren't expected inputs.
     *
     * @param input  String to be escaped.
     * @return  Escaped string.
     */
    public static String escapeInput(String input) {
        if (input == null) {
            return null;
        }

        return input.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
                ">", "&gt;");
    }


    /**
     * Hashes password using the Bcrypt algorithm made available through jBCrypt.
     *
     * @param password  Plaintext password to be hashed.
     * @return  Hashed password.
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }
}
