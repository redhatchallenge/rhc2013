package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import org.redhatchallenge.rhc2013.shared.Student;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
@RemoteServiceRelativePath("ProfileService")
public interface ProfileService extends RemoteService {

    public Student getProfileData() throws IllegalArgumentException;

    public Boolean updateProfileData(String email, String firstName, String lastName, String contact,
                                     String country, String countryCode, String school, String lecturerFirstName, String lecturerLastName,
                                     String lecturerEmail, String language) throws IllegalArgumentException;

    public Boolean changePassword(String oldPassword, String newPassword) throws IllegalArgumentException;

    public static class Util {
        private static final ProfileServiceAsync Instance= (ProfileServiceAsync) GWT.create(ProfileService.class);

        public static ProfileServiceAsync getInstance() {
            return Instance;
        }
    }
}
