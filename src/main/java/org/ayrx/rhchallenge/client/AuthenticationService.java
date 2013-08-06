package org.ayrx.rhchallenge.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import org.ayrx.rhchallenge.shared.Student;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
@RemoteServiceRelativePath("AuthenticationService")
public interface AuthenticationService extends RemoteService {

    public Boolean registerStudent(String email, String password, String firstName, String lastName, String contact,
                                   String country, String countryCode, String school, String lecturerFirstName, String lecturerLastName,
                                   String lecturerEmail, String language) throws IllegalArgumentException;

    public Boolean authenticateStudent(String email, String password, Boolean rememberMe) throws IllegalArgumentException;

    public void logout();

    public boolean isAuthenticated();

    public boolean isRemembered();

    public boolean setConfirmationStatus(String token) throws IllegalArgumentException;

    public boolean triggerResetPassword(String email, String contact) throws IllegalArgumentException;

    public String lookupEmailFromToken(String token) throws IllegalArgumentException;

    public boolean resetPassword(String password, String email) throws IllegalArgumentException;

    public static class Util {
        private static final AuthenticationServiceAsync Instance = (AuthenticationServiceAsync) GWT.create(AuthenticationService.class);

        public static AuthenticationServiceAsync getInstance() {
            return Instance;
        }
    }
}
