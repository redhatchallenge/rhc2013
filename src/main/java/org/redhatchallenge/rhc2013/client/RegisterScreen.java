package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.FieldVerifier;

import static org.redhatchallenge.rhc2013.client.LocaleUtil.getCountryFromIndex;
import static org.redhatchallenge.rhc2013.client.LocaleUtil.getLanguageFromIndex;
import static org.redhatchallenge.rhc2013.client.LocaleUtil.getRegionFromIndex;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class RegisterScreen extends Composite {
    interface RegisterScreenUiBinder extends UiBinder<Widget, RegisterScreen> {
    }

    private static RegisterScreenUiBinder UiBinder = GWT.create(RegisterScreenUiBinder.class);
    private MessageMessages messages = GWT.create(MessageMessages.class);

    @UiField HTMLPanel htmlPanel;
    @UiField TextBox emailField;
    @UiField PasswordTextBox passwordField;
    @UiField PasswordTextBox confirmPasswordField;
    @UiField TextBox firstNameField;
    @UiField TextBox lastNameField;
    @UiField WatermarkedTextBox contactField;
    @UiField ListBox countryField;
    @UiField ListBox regionField;
    @UiField ListBox countryCodeField;
    @UiField TextBox schoolField;
    @UiField TextBox lecturerFirstNameField;
    @UiField TextBox lecturerLastNameField;
    @UiField TextBox lecturerEmailField;
    @UiField ListBox languageField;
    @UiField Image registerButton;
    @UiField CheckBox termsCheck;
    @UiField TableElement table;
    @UiField Anchor socialButton1;
    @UiField Anchor socialButton2;

    //Validation Error Labels
    @UiField Label errorLabel;
    @UiField Label emailLabel;
    @UiField Label passwordLabel;
    @UiField Label confirmPasswordLabel;
    @UiField Label firstNameLabel;
    @UiField Label lastNameLabel;
    @UiField Label contactLabel;
    @UiField Label schoolLabel;
    @UiField Label termsLabel;


    private AuthenticationServiceAsync authenticationService = null;

    DecoratedPopupPanel popupPanel = new DecoratedPopupPanel(true);

    public RegisterScreen() {

        Resources.INSTANCE.main().ensureInjected();
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.buttons().ensureInjected();

        initWidget(UiBinder.createAndBindUi(this));

        table.setCellPadding(3);
        table.setCellSpacing(3);
        table.setBorder(0);

        registerButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        contactField.setWatermark("XXXXXXXX");

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
    }

    @UiHandler("countryField")
    public void handleCountryChange(ChangeEvent event) {
        switch (countryField.getSelectedIndex()) {
            // Singapore
            case 0:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(0);
                regionField.setVisible(false);
                contactField.setText("");
                contactField.setWatermark("XXXXXXXX");
                break;
            // Malaysia
            case 1:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(1);
                regionField.setVisible(false);
                contactField.setText("");
                contactField.setWatermark("XXXXXXXXX");
                break;
            // Thailand
            case 2:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(2);
                regionField.setVisible(false);
                contactField.setText("");
                contactField.setWatermark("0XXXXXXXXX");
                break;
            // China
            case 3:
                languageField.setSelectedIndex(1);
                countryCodeField.setSelectedIndex(3);
                regionField.setVisible(true);
                contactField.setText("");
                contactField.setWatermark("1XX-XXXX-XXXX");
                break;
            // Hong Kong
            case 4:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(4);
                regionField.setVisible(false);
                contactField.setText("");
                contactField.setWatermark("XXXXXXXX");
                break;
            // Taiwan
            case 5:
                languageField.setSelectedIndex(2);
                countryCodeField.setSelectedIndex(5);
                regionField.setVisible(false);
                contactField.setText("");
                contactField.setWatermark("9XXXXXXXX");
                break;
        }
    }

    @UiHandler("registerButton")
    public void handleRegisterButtonClick(ClickEvent event) {

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

        if(FieldVerifier.passwordIsNull(passwordField.getText())){
            passwordLabel.setText(messages.emptyPassword());
        }
            else if((passwordField.getText() != null) && (!FieldVerifier.isValidPassword(passwordField.getText()))){
                 passwordLabel.setText(messages.passwordInvalidFormat());
            }
                else{
                    passwordLabel.setText("");
                    successCounter++;
                }

        if(FieldVerifier.passwordIsNull(confirmPasswordField.getText())){
            confirmPasswordLabel.setText(messages.emptyConfirmPassword());
        }
            else if(!confirmPasswordField.getText().equals(passwordField.getText())){
                confirmPasswordLabel.setText(messages.passwordNotMatch());
            }
                else{
                    confirmPasswordLabel.setText("");
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

        if(successCounter == 7){
            registerStudent();
        }

    }

    @UiHandler({"emailField", "passwordField", "confirmPasswordField", "firstNameField",
            "lastNameField", "contactField", "countryField", "countryCodeField",
            "schoolField", "lecturerFirstNameField", "lecturerLastNameField",
            "lecturerEmailField", "languageField"})
    public void handleKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            registerStudent();
        }
    }

    @UiHandler("passwordField")
    public void handleMouseOver(MouseOverEvent event) {
        popupPanel.setWidth("150px");
        popupPanel.setWidget(new HTML(messages.passwordPopup()));
        popupPanel.setPopupPosition(passwordField.getAbsoluteLeft() + passwordField.getOffsetWidth(), passwordField.getAbsoluteTop());
        popupPanel.show();
    }

    @UiHandler("passwordField")
    public void handleMouseOut(MouseOutEvent event) {
        popupPanel.hide();
    }

    @UiHandler("passwordField")
    public void handleFocus(FocusEvent event) {
        popupPanel.setWidth("150px");
        popupPanel.setWidget(new HTML(messages.passwordPopup()));
        popupPanel.setPopupPosition(passwordField.getAbsoluteLeft() + passwordField.getOffsetWidth(), passwordField.getAbsoluteTop());
        popupPanel.show();
    }

    @UiHandler("passwordField")
    public void handleBlur(BlurEvent event) {
        popupPanel.hide();
    }

    private void registerStudent() {

        clearAllLabels();
        registerButton.setResource(Resources.INSTANCE.submitButtonGrey());

        final String email = emailField.getText();
        final String password = passwordField.getText();
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
        final Boolean termsConCheck = termsCheck.getValue();

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

        if(termsConCheck){
            authenticationService = AuthenticationService.Util.getInstance();

            authenticationService.registerStudent(email, password, firstName, lastName, contact,
                    country, countryCode, school, lecturerFirstName, lecturerLastName,
                    lecturerEmail, language, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                    errorLabel.setText(messages.unexpectedError());
                    registerButton.setResource(Resources.INSTANCE.submitButton());
                }

                @Override
                public void onSuccess(Boolean bool) {
                    if(bool) {
                        ContentContainer.INSTANCE.setContent(new verifyMessageScreen(messages.verifyMailMessage(firstName, email)));
                    }

                    else {
                        errorLabel.setText(messages.emailTaken());
                        registerButton.setResource(Resources.INSTANCE.submitButton());
                    }
                }
            });
        }

        else {
            termsLabel.setText(messages.termsCheck());
            registerButton.setResource(Resources.INSTANCE.submitButton());

        }
    }

    private void clearAllLabels() {
        errorLabel.setText("");
        emailLabel.setText("");
        passwordLabel.setText("");
        confirmPasswordLabel.setText("");
        firstNameLabel.setText("");
        lastNameLabel.setText("");
        contactLabel.setText("");
        schoolLabel.setText("");
        termsLabel.setText("");
    }
}