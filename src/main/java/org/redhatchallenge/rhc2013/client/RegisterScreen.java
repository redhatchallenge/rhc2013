package org.redhatchallenge.rhc2013.client;

import com.claudiushauptmann.gwt.recaptcha.client.RecaptchaWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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
    @UiField Label emailLabel;
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
    @UiField VerticalPanel recaptchaPanel;
    @UiField Image registerButton;
    @UiField Label errorLabel;

    @UiField TableElement table;

    private RecaptchaWidget recaptchaWidget;

    private AuthenticationServiceAsync authenticationService = null;

    public RegisterScreen() {

        Resources.INSTANCE.main().ensureInjected();
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.buttons().ensureInjected();

        initWidget(UiBinder.createAndBindUi(this));

        recaptchaWidget = new RecaptchaWidget("6LfzBeUSAAAAAOvz40gsWXHN3TGp2oyB862qQeGl");
        recaptchaPanel.add(recaptchaWidget);

        table.setCellPadding(3);
        table.setCellSpacing(3);
        table.setBorder(0);

        registerButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        contactField.setWatermark("XXXXXXXX");
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
                contactField.setWatermark("0XXXXXXXXX");
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
        if(!FieldVerifier.isValidEmail(emailField.getText())) {
            emailLabel.setText(messages.invalidEmailFormat());
        }

        else {
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

    private void registerStudent() {

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

        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.registerStudent(email, password, firstName, lastName, contact,
                country, countryCode, school, lecturerFirstName, lecturerLastName,
                lecturerEmail, language, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                errorLabel.setText(messages.unexpectedError());
            }

            @Override
            public void onSuccess(Boolean bool) {
                if(bool) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen(messages.verifyMailMessage(firstName, email)));
                }

                else {
                    errorLabel.setText(messages.emailTaken());
                }
            }
        });
    }
}