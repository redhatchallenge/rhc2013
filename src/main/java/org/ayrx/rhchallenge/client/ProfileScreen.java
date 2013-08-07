package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.ayrx.rhchallenge.resources.Resources;
import org.ayrx.rhchallenge.shared.Student;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ProfileScreen extends Composite {

    interface ProfileScreenUiBinder extends UiBinder<Widget, ProfileScreen> {

    }

    private static ProfileScreenUiBinder UiBinder = GWT.create(ProfileScreenUiBinder.class);

    @UiField TextBox emailField;
    @UiField PasswordTextBox currentPasswordField;
    @UiField PasswordTextBox passwordField;
    @UiField PasswordTextBox confirmPasswordField;
    @UiField TextBox firstNameField;
    @UiField TextBox lastNameField;
    @UiField TextBox contactField;
    @UiField ListBox countryField;
    @UiField ListBox countryCodeField;
    @UiField TextBox schoolField;
    @UiField TextBox lecturerFirstNameField;
    @UiField TextBox lecturerLastNameField;
    @UiField TextBox lecturerEmailField;
    @UiField ListBox languageField;
    @UiField Image updateButton;
    @UiField Label errorLabel;

    private ProfileServiceAsync profileService = null;

    public ProfileScreen() {
        initWidget(UiBinder.createAndBindUi(this));
        profileService = ProfileService.Util.getInstance();

        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();

        updateButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        /**
         * If HTML5 storage does not contain the profile data, retrieves the data
         * from the server through a RPC call. Else retrieves first name from the
         * local storage.
         */
        StorageMap localStorageMap = new StorageMap(LocalStorage.INSTANCE.getLocalStorage());
        if(localStorageMap.size() != 11) {
            profileService.getProfileData(new AsyncCallback<Student>() {
                @Override
                public void onFailure(Throwable throwable) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("You are probably not logged in"));
                }

                @Override
                public void onSuccess(Student student) {
                    emailField.setText(student.getEmail());
                    firstNameField.setText(student.getFirstName());
                    lastNameField.setText(student.getLastName());
                    contactField.setText(student.getContact());
                    countryField.setSelectedIndex(getIndexFromValue(student.getCountry(), countryField));
                    countryCodeField.setSelectedIndex(getIndexFromValue(student.getCountryCode(), countryCodeField));
                    schoolField.setText(student.getSchool());
                    lecturerFirstNameField.setText(student.getLecturerFirstName());
                    lecturerLastNameField.setText(student.getLecturerLastName());
                    lecturerEmailField.setText(student.getLecturerEmail());
                    languageField.setSelectedIndex(getIndexFromValue(student.getLanguage(), languageField));

                    Storage localStorage = LocalStorage.INSTANCE.getLocalStorage();
                    /**
                     * If browser supports HTML5 storage, stores the authenticated user's
                     * profile data.
                     */
                    if(localStorage != null) {
                        localStorage.setItem("email", student.getEmail());
                        localStorage.setItem("firstName", student.getFirstName());
                        localStorage.setItem("lastName", student.getLastName());
                        localStorage.setItem("contact", student.getContact());
                        localStorage.setItem("country", student.getCountry());
                        localStorage.setItem("countryCode", student.getCountryCode());
                        localStorage.setItem("school", student.getSchool());
                        localStorage.setItem("lecturerFirstName", student.getLecturerFirstName());
                        localStorage.setItem("lecturerLastName", student.getLecturerLastName());
                        localStorage.setItem("lecturerEmail", student.getLecturerEmail());
                        localStorage.setItem("language", student.getLanguage());
                    }
                }
            });
        }

        else {
            emailField.setText(localStorageMap.get("email"));
            firstNameField.setText(localStorageMap.get("firstName"));
            lastNameField.setText(localStorageMap.get("lastName"));
            contactField.setText(localStorageMap.get("contact"));
            countryField.setSelectedIndex(getIndexFromValue(localStorageMap.get("country"), countryField));
            countryCodeField.setSelectedIndex(getIndexFromValue(localStorageMap.get("countryCode"), countryCodeField));
            schoolField.setText(localStorageMap.get("school"));
            lecturerFirstNameField.setText(localStorageMap.get("lecturerFirstName"));
            lecturerLastNameField.setText(localStorageMap.get("lecturerLastName"));
            lecturerEmailField.setText(localStorageMap.get("lecturerEmail"));
            languageField.setSelectedIndex(getIndexFromValue(localStorageMap.get("language"), languageField));
        }
    }

    @UiHandler("countryField")
    public void handleChange(ChangeEvent event) {
        switch (countryField.getSelectedIndex()) {
            // Singapore
            case 0:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(0);
                break;
            // Malaysia
            case 1:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(1);
                break;
            // Thailand
            case 2:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(2);
                break;
            // China
            case 3:
                languageField.setSelectedIndex(1);
                countryCodeField.setSelectedIndex(3);
                break;
            // Hong Kong
            case 4:
                languageField.setSelectedIndex(2);
                countryCodeField.setSelectedIndex(4);
                break;
            // Taiwan
            case 5:
                languageField.setSelectedIndex(2);
                countryCodeField.setSelectedIndex(5);
                break;
        }
    }

    @UiHandler("updateButton")
    public void handleUpdateButtonClick(ClickEvent event) {
        updateProfile();
    }

    /**
     * This function iterates over the values of a listbox and compares it
     * against returns the index of a match.
     *
     * @param value  Value to be compared against
     * @param listBox  ListBox to be iterated
     * @return  Index of the item in the listbox that matches the provided value.
     */
    private int getIndexFromValue(String value, ListBox listBox) {
        for(int i=0; i < listBox.getItemCount(); i++) {
            if(listBox.getItemText(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private void updateProfile() {

        String email = emailField.getText();
        String oldPassword = currentPasswordField.getText();
        String newPassword = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String contact = contactField.getText();
        String country = countryField.getItemText(countryField.getSelectedIndex());
        String countryCode = countryCodeField.getItemText(countryCodeField.getSelectedIndex());
        String school = schoolField.getText();
        String lecturerFirstName = lecturerFirstNameField.getText();
        String lecturerLastName = lecturerLastNameField.getText();
        String lecturerEmail = lecturerEmailField.getText();
        String language = languageField.getItemText(languageField.getSelectedIndex());


        profileService = ProfileService.Util.getInstance();

        profileService.updateProfileData(email, oldPassword, newPassword, firstName, lastName, contact,
                country, countryCode, school, lecturerFirstName, lecturerLastName,
                lecturerEmail, language, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                errorLabel.setText("An unexpected error has occurred, please try again later!");
            }

            @Override
            public void onSuccess(Boolean bool) {
                if(bool) {
                    errorLabel.setText("Profile update successful!");
                }

                else {
                    errorLabel.setText("Profile update failed! Please double check your inputs!");
                }
            }
        });
    }
}