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
    @UiField ListBox regionField;
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
        final Storage localStorage = Storage.getLocalStorageIfSupported();
        StorageMap localStorageMap = new StorageMap(localStorage);
        if(localStorageMap.size() != 11) {
            profileService.getProfileData(new AsyncCallback<Student>() {
                @Override
                public void onFailure(Throwable throwable) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("You are probably not logged in"));
                }

                @Override
                public void onSuccess(Student student) {
                    final String email = student.getEmail();
                    final String firstName = student.getFirstName();
                    final String lastName = student.getLastName();
                    final String contact = student.getContact();
                    final String country = student.getCountry();
                    final String countryCode = student.getCountryCode();
                    final String school = student.getSchool();
                    final String lecturerFirstName = student.getLecturerFirstName();
                    final String lecturerLastName = student.getLecturerLastName();
                    final String lecturerEmail = student.getLecturerEmail();
                    final String language = student.getLanguage();

                    emailField.setText(email);
                    firstNameField.setText(firstName);
                    lastNameField.setText(lastName);
                    contactField.setText(contact);
                    countryCodeField.setSelectedIndex(getIndexFromValue(countryCode, countryCodeField));
                    schoolField.setText(school);
                    lecturerFirstNameField.setText(lecturerFirstName);
                    lecturerLastNameField.setText(lecturerLastName);
                    lecturerEmailField.setText(lecturerEmail);
                    languageField.setSelectedIndex(getIndexFromValue(language, languageField));

                    /**
                     * Populates both the country and region field if country is China.
                     */
                    if(country.substring(0,5).equalsIgnoreCase("china")) {
                        regionField.setVisible(true);
                        countryField.setSelectedIndex(getIndexFromValue("China", countryField));
                        regionField.setSelectedIndex(getIndexFromValue(country.substring(6), regionField));
                    }

                    else {
                        countryField.setSelectedIndex(getIndexFromValue(country, countryField));
                    }

                    /**
                     * If browser supports HTML5 storage, stores the authenticated user's
                     * profile data.
                     */
                    if(localStorage != null) {
                        localStorage.setItem("email", email);
                        localStorage.setItem("firstName", firstName);
                        localStorage.setItem("lastName", lastName);
                        localStorage.setItem("contact", contact);
                        localStorage.setItem("country", country);
                        localStorage.setItem("countryCode", countryCode);
                        localStorage.setItem("school", school);
                        localStorage.setItem("lecturerFirstName", lecturerFirstName);
                        localStorage.setItem("lecturerLastName", lecturerLastName);
                        localStorage.setItem("lecturerEmail", lecturerEmail);
                        localStorage.setItem("language", language);
                    }
                }
            });
        }

        else {
            emailField.setText(localStorageMap.get("email"));
            firstNameField.setText(localStorageMap.get("firstName"));
            lastNameField.setText(localStorageMap.get("lastName"));
            contactField.setText(localStorageMap.get("contact"));
            countryCodeField.setSelectedIndex(getIndexFromValue(localStorageMap.get("countryCode"), countryCodeField));
            schoolField.setText(localStorageMap.get("school"));
            lecturerFirstNameField.setText(localStorageMap.get("lecturerFirstName"));
            lecturerLastNameField.setText(localStorageMap.get("lecturerLastName"));
            lecturerEmailField.setText(localStorageMap.get("lecturerEmail"));
            languageField.setSelectedIndex(getIndexFromValue(localStorageMap.get("language"), languageField));

            /**
             * Populates both the country and region field if country is China.
             */
            if(localStorageMap.get("country").substring(0,5).equalsIgnoreCase("china")) {
                regionField.setVisible(true);
                countryField.setSelectedIndex(getIndexFromValue("China", countryField));
                regionField.setSelectedIndex(getIndexFromValue(localStorageMap.get("country").substring(6), regionField));
            }

            else {
                countryField.setSelectedIndex(getIndexFromValue(localStorageMap.get("country"), countryField));
            }
        }
    }

    @UiHandler("countryField")
    public void handleChange(ChangeEvent event) {
        switch (countryField.getSelectedIndex()) {
            // Singapore
            case 0:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(0);
                regionField.setVisible(false);
                break;
            // Malaysia
            case 1:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(1);
                regionField.setVisible(false);
                break;
            // Thailand
            case 2:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(2);
                regionField.setVisible(false);
                break;
            // China
            case 3:
                languageField.setSelectedIndex(1);
                countryCodeField.setSelectedIndex(3);
                regionField.setVisible(true);
                break;
            // Hong Kong
            case 4:
                languageField.setSelectedIndex(2);
                countryCodeField.setSelectedIndex(4);
                regionField.setVisible(false);
                break;
            // Taiwan
            case 5:
                languageField.setSelectedIndex(2);
                countryCodeField.setSelectedIndex(5);
                regionField.setVisible(false);
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

        final String email = emailField.getText();
        final String oldPassword = currentPasswordField.getText();
        final String newPassword = passwordField.getText();
        final String firstName = firstNameField.getText();
        final String lastName = lastNameField.getText();
        final String contact = contactField.getText();
        final String countryCode = countryCodeField.getItemText(countryCodeField.getSelectedIndex());
        final String school = schoolField.getText();
        final String lecturerFirstName = lecturerFirstNameField.getText();
        final String lecturerLastName = lecturerLastNameField.getText();
        final String lecturerEmail = lecturerEmailField.getText();
        final String language = languageField.getItemText(languageField.getSelectedIndex());
        final String country;

        /**
         * If country is China, append the region.
         */
        if(countryField.getItemText(countryField.getSelectedIndex()).equalsIgnoreCase("china")) {
            country = countryField.getItemText(countryField.getSelectedIndex()) + "/" +
                    regionField.getItemText(regionField.getSelectedIndex());
        }

        else {
            country = countryField.getItemText(countryField.getSelectedIndex());
        }

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
                    final Storage localStorage = Storage.getLocalStorageIfSupported();
                    /**
                     * If browser supports HTML5 storage, stores the authenticated user's
                     * profile data.
                     */
                    if(localStorage != null) {
                        localStorage.setItem("email", email);
                        localStorage.setItem("firstName", firstName);
                        localStorage.setItem("lastName", lastName);
                        localStorage.setItem("contact", contact);
                        localStorage.setItem("country", country);
                        localStorage.setItem("countryCode", countryCode);
                        localStorage.setItem("school", school);
                        localStorage.setItem("lecturerFirstName", lecturerFirstName);
                        localStorage.setItem("lecturerLastName", lecturerLastName);
                        localStorage.setItem("lecturerEmail", lecturerEmail);
                        localStorage.setItem("language", language);
                    }
                }

                else {
                    errorLabel.setText("Profile update failed! Please double check your inputs!");
                }
            }
        });
    }
}