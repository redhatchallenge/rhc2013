package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.FieldVerifier;
import org.redhatchallenge.rhc2013.shared.Student;

import static org.redhatchallenge.rhc2013.client.LocaleUtil.getCountryFromIndex;
import static org.redhatchallenge.rhc2013.client.LocaleUtil.getIndexFromCountry;
import static org.redhatchallenge.rhc2013.client.LocaleUtil.getIndexFromLanguage;
import static org.redhatchallenge.rhc2013.client.LocaleUtil.getIndexFromRegion;
import static org.redhatchallenge.rhc2013.client.LocaleUtil.getLanguageFromIndex;
import static org.redhatchallenge.rhc2013.client.LocaleUtil.getRegionFromIndex;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ProfileScreen extends Composite {

    interface ProfileScreenUiBinder extends UiBinder<Widget, ProfileScreen> {

    }

    private static ProfileScreenUiBinder UiBinder = GWT.create(ProfileScreenUiBinder.class);
    private  MessageMessages messages = GWT.create(MessageMessages.class);

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
    @UiField Image changePwdButton;
    @UiField Anchor socialButton1;
    @UiField Anchor socialButton2;

    //Validation Error Labels
    @UiField Label errorLabel;
    @UiField Label updateStatusLabel;
    @UiField Label emailLabel;
    @UiField Label currentPasswordLabel;
    @UiField Label passwordLabel;
    @UiField Label confirmPasswordLabel;
    @UiField Label firstNameLabel;
    @UiField Label lastNameLabel;
    @UiField Label contactLabel;
    @UiField Label schoolLabel;

    private ProfileServiceAsync profileService = null;

    public ProfileScreen() {
        initWidget(UiBinder.createAndBindUi(this));
        profileService = ProfileService.Util.getInstance();

        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();

        updateButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
            socialButton1.setVisible(false);
            socialButton2.setTarget("_blank");
            socialButton2.setHref("http://e.weibo.com/redhatchina");
        }
        else {
            socialButton1.setTarget("_blank");
            socialButton1.setHref("https://www.facebook.com/redhatinc?fref=ts");
            socialButton2.setTarget("_blank");
            socialButton2.setHref("https://twitter.com/red_hat_apac");
        }


        /**
         * If HTML5 storage does not contain the profile data, retrieves the data
         * from the server through a RPC call. Else retrieves first name from the
         * local storage.
         */
        final Storage localStorage = Storage.getLocalStorageIfSupported();
        StorageMap localStorageMap = new StorageMap(localStorage);
        if(localStorageMap.size() != 12) {
            profileService.getProfileData(new AsyncCallback<Student>() {
                @Override
                public void onFailure(Throwable throwable) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen(messages.probablyNotLoginIn()));
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
                    languageField.setSelectedIndex(getIndexFromLanguage(language));

                    /**
                     * Populates both the country and region field if country is China.
                     */
                    if(country.substring(0,5).equalsIgnoreCase("china")) {
                        regionField.setVisible(true);
                        countryField.setSelectedIndex(getIndexFromCountry("china"));
                        regionField.setSelectedIndex(getIndexFromRegion(country.substring(6)));
                    }

                    else {
                        countryField.setSelectedIndex(getIndexFromCountry(country));
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
            languageField.setSelectedIndex(getIndexFromLanguage(localStorageMap.get("language")));

            /**
             * Populates both the country and region field if country is China.
             */
            if(localStorageMap.get("country").substring(0,5).equalsIgnoreCase("china")) {
                regionField.setVisible(true);
                countryField.setSelectedIndex(getIndexFromCountry("china"));
                regionField.setSelectedIndex(getIndexFromRegion(localStorageMap.get("country").substring(6)));
            }

            else {
                countryField.setSelectedIndex(getIndexFromCountry(localStorageMap.get("country")));
            }
        }
    }


    @UiHandler("emailField")
    public void handleEmailFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("firstNameField")
    public void handleFNFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("lastNameField")
    public void handleLNFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("contactField")
    public void handleContactFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("schoolField")
    public void handleSchoolFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("lecturerFirstNameField")
    public void handlelecturerFirstNameFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("lecturerLastNameField")
    public void handlelecturerLastNameFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("lecturerEmailField")
    public void handlelecturerEmailFieldClick(ClickEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("countryCodeField")
    public void handleCountryCodeChange(ChangeEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("languageField")
    public void handleLanguageChange(ChangeEvent event) {
        updateStatusLabel.setText("");
    }

    @UiHandler("regionField")
    public void handleRegionFieldChange(ChangeEvent event) {
        updateStatusLabel.setText("");
    }


    @UiHandler("countryField")
    public void handleChange(ChangeEvent event) {
        updateStatusLabel.setText("");
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
        updateStatusLabel.setText("");
        int successCounter = 0;

        if(FieldVerifier.emailIsNull(emailField.getText())){
              emailLabel.setText(messages.emailEmpty());
        }
            else if(!FieldVerifier.isValidEmail(emailField.getText())){
                 emailLabel.setText(messages.emailInvalidFormat());
            }
                else{
                    emailLabel.setText("");
                    successCounter++;
                }

        if(FieldVerifier.fnIsNull(firstNameField.getText())){
            firstNameLabel.setText(messages.emptyFirstName());
        }
            else{
                firstNameLabel.setText("");
                successCounter++;
            }

        if(FieldVerifier.lnIsNull(lastNameField.getText())){
            lastNameLabel.setText(messages.emptyLastName());
        }
            else{
                lastNameLabel.setText("");
                successCounter++;
            }

        if(FieldVerifier.contactIsNull(contactField.getText())){
            contactLabel.setText(messages.emptyContact());
        }
            else if(!FieldVerifier.isValidContact(contactField.getText())){
                contactLabel.setText(messages.contactInvalid());
            }
                else{
                    contactLabel.setText("");
                    successCounter++;
                }

        if(FieldVerifier.schoolIsNull(schoolField.getText())){
            schoolLabel.setText(messages.emptySchool());
        }
            else{
                schoolLabel.setText("");
                successCounter++;
            }

        if(successCounter == 5){
            updateProfile();
        }
    }

    @UiHandler("changePwdButton")
    public void handleChangePwdButtonClick(ClickEvent event) {
        errorLabel.setText("");
        if (!FieldVerifier.passwordIsNull(currentPasswordField.getText())){
            currentPasswordLabel.setText("");
            int successCounter = 0;
            if(FieldVerifier.passwordIsNull(passwordField.getText())){
                passwordLabel.setText(messages.enterNewPassword());
            }
            else if(!FieldVerifier.passwordIsNull(passwordField.getText()))
            {
                if(FieldVerifier.passwordIsNull(confirmPasswordField.getText())){
                    confirmPasswordLabel.setText(messages.emptyConfirmPassword());
                }
                else if(!FieldVerifier.passwordIsNull(confirmPasswordField.getText())){
                    if(!confirmPasswordField.getText().equals(passwordField.getText())){
                        confirmPasswordLabel.setText(messages.passwordNotMatch());
                    }
                    else{
                        confirmPasswordLabel.setText("");
                        successCounter++;
                    }
                }
                else{
                    confirmPasswordLabel.setText("");
                    successCounter++;
                }

                if(!FieldVerifier.isValidPassword(passwordField.getText())){
                    passwordLabel.setText(messages.passwordInvalidFormat());
                }
                else{
                    passwordLabel.setText("");
                    successCounter++;
                }

            }
            if(successCounter == 2){
                changePassword();
            }
        }

        else if(FieldVerifier.passwordIsNull(currentPasswordField.getText())){

            if(!FieldVerifier.passwordIsNull(passwordField.getText())){

                if(FieldVerifier.passwordIsNull(currentPasswordField.getText())){
                    currentPasswordLabel.setText(messages.currentPasswordEmpty());
                }

                if (FieldVerifier.passwordIsNull(confirmPasswordField.getText())){
                    confirmPasswordLabel.setText(messages.emptyConfirmPassword());
                }
                else if(!FieldVerifier.passwordIsNull(confirmPasswordField.getText())){
                    if(!confirmPasswordField.getText().equals(passwordField.getText())){
                        confirmPasswordLabel.setText(messages.passwordNotMatch());
                    }
                    else{
                        confirmPasswordLabel.setText("");
                    }
                }
                else{
                    confirmPasswordLabel.setText("");
                }

                if(!FieldVerifier.isValidPassword(passwordField.getText())){
                    passwordLabel.setText(messages.passwordInvalidFormat());
                }
                else{
                    passwordLabel.setText("");
                }

            }

            else if(!FieldVerifier.passwordIsNull(confirmPasswordField.getText())){

                if(FieldVerifier.passwordIsNull(currentPasswordField.getText())){
                    currentPasswordLabel.setText(messages.currentPasswordEmpty());
                }

                if (FieldVerifier.passwordIsNull(passwordField.getText())){
                    passwordLabel.setText(messages.emptyPassword());
                }
                else if(!FieldVerifier.passwordIsNull(passwordField.getText())){
                    if(!passwordField.getText().equals(confirmPasswordField.getText())){
                        confirmPasswordLabel.setText(messages.passwordNotMatch());
                    }
                    else{
                        confirmPasswordLabel.setText("");
                    }
                    if(!FieldVerifier.isValidPassword(passwordField.getText())){
                        passwordLabel.setText(messages.passwordInvalidFormat());
                    }
                    else{
                        passwordLabel.setText("");
                    }
                }
            }
            else{
                currentPasswordLabel.setText("");
                passwordLabel.setText("");
                confirmPasswordLabel.setText("");
            }
        }
        else{
            currentPasswordLabel.setText("");
            passwordLabel.setText("");
            confirmPasswordLabel.setText("");
        }

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

    private void changePassword(){

        changePwdButton.setResource(Resources.INSTANCE.changePwdButton());

        final String oldPassword = currentPasswordField.getText();
        final String newPassword = passwordField.getText();

        profileService = ProfileService.Util.getInstance();

        profileService.changePassword(oldPassword,newPassword, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                errorLabel.setText(messages.unexpectedError());
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean){
                    errorLabel.setText(messages.passwordChangeSuccessful());
                    changePwdButton.setResource(Resources.INSTANCE.changePwdButton());
                }
                else { 
                    currentPasswordLabel.setText(messages.invalidPassword());
                    changePwdButton.setResource(Resources.INSTANCE.changePwdButton());
                }
            }
        });
    }

    private void updateProfile() {

        updateButton.setResource(Resources.INSTANCE.saveButtonGrey());

        final String email = emailField.getText();
        final String firstName = firstNameField.getText();
        final String lastName = lastNameField.getText();
        final String contact = contactField.getText();
        final String countryCode = countryCodeField.getItemText(countryCodeField.getSelectedIndex());
        final String school = schoolField.getText();
        final String lecturerFirstName = lecturerFirstNameField.getText();
        final String lecturerLastName = lecturerLastNameField.getText();
        final String lecturerEmail = lecturerEmailField.getText();
        final String language = getLanguageFromIndex(languageField.getSelectedIndex());
        final String country;

        /**
         * If country is China, append the region.
         */
        if(getCountryFromIndex(countryField.getSelectedIndex()).equalsIgnoreCase("china")) {
            country = getCountryFromIndex(countryField.getSelectedIndex()) + "/" +
                    getRegionFromIndex(regionField.getSelectedIndex());
        }

        else {
            country = getCountryFromIndex(countryField.getSelectedIndex());
        }

        profileService = ProfileService.Util.getInstance();

        profileService.updateProfileData(email, firstName, lastName, contact,
                country, countryCode, school, lecturerFirstName, lecturerLastName,
                lecturerEmail, language, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                updateStatusLabel.setText(messages.unexpectedError());
                updateButton.setResource(Resources.INSTANCE.saveButton());
            }

            @Override
            public void onSuccess(Boolean bool) {
                if(bool) {
                    updateStatusLabel.setText(messages.profileUpdated());
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

                    RootPanel.get("header").clear();
                    RootPanel.get("header").add(new AuthenticatedHeader());
                    updateButton.setResource(Resources.INSTANCE.saveButton());
                }

                else {
                    updateStatusLabel.setText(messages.profileUpdateFail());
                    updateButton.setResource(Resources.INSTANCE.saveButton());
                }
            }
        });
        updateButton.setResource(Resources.INSTANCE.saveButton());

    }
}