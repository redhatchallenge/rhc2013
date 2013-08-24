package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.redhatchallenge.rhc2013.shared.Student;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface ProfileServiceAsync {
    void getProfileData(AsyncCallback<Student> async);

    void updateProfileData(String email, String firstName, String lastName, String contact,
                           String country, String countryCode, String school, String lecturerFirstName, String lecturerLastName,
                           String lecturerEmail, String language, AsyncCallback<Boolean> async);

    void changePassword(String oldPassword, String newPassword, AsyncCallback<Boolean> async);
}
