package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface AuthenticationServiceAsync {
    void registerStudent(String email, String password, String firstName, String lastName, String contact,
                         String country, String countryCode, String school, String lecturerFirstName, String lecturerLastName,
                         String lecturerEmail, String language, AsyncCallback<Boolean> async);

    void authenticateStudent(String email, String password, Boolean rememberMe, AsyncCallback<Boolean> async);

    void logout(AsyncCallback<Void> async);

    void isAuthenticated(AsyncCallback<Boolean> async);

    void isRemembered(AsyncCallback<Boolean> async);

    void setConfirmationStatus(String token, AsyncCallback<Boolean> async);

    void triggerResetPassword(String email, String contact, String countryCode, AsyncCallback<Boolean> async);

    void lookupEmailFromToken(String token, AsyncCallback<String> async);

    void resetPassword(String password, String email, AsyncCallback<Boolean> async);

    void resendVerificationEmail(String email, AsyncCallback<Void> async);
}
