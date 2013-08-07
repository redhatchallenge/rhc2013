package org.ayrx.rhchallenge.client;

import com.claudiushauptmann.gwt.recaptcha.client.RecaptchaWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.ayrx.rhchallenge.resources.Resources;
import org.ayrx.rhchallenge.shared.Student;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class RegisterScreen extends Composite {
    interface RegisterScreenUiBinder extends UiBinder<Widget, RegisterScreen> {
    }

    private static RegisterScreenUiBinder UiBinder = GWT.create(RegisterScreenUiBinder.class);

    @UiField HTMLPanel htmlPanel;
    @UiField TextBox emailField;
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
    }

    @UiHandler("countryField")
    public void handleCountryChange(ChangeEvent event) {
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
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(4);
                break;
            // Taiwan
            case 5:
                languageField.setSelectedIndex(2);
                countryCodeField.setSelectedIndex(5);
                break;
        }
    }

    @UiHandler("registerButton")
    public void handleRegisterButtonClick(ClickEvent event) {
        registerStudent();
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
        final String country = countryField.getItemText(countryField.getSelectedIndex());
        final String countryCode = countryCodeField.getItemText(countryCodeField.getSelectedIndex());
        final String school = schoolField.getText();
        final String lecturerFirstName = lecturerFirstNameField.getText();
        final String lecturerLastName = lecturerLastNameField.getText();
        final String lecturerEmail = lecturerEmailField.getText();
        final String language = languageField.getItemText(languageField.getSelectedIndex());

        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.registerStudent(email, password, firstName, lastName, contact,
                country, countryCode, school, lecturerFirstName, lecturerLastName,
                lecturerEmail, language, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                errorLabel.setText("An unexpected error has occurred, please try again later!");
            }

            @Override
            public void onSuccess(Boolean bool) {
                if(bool) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Hi " + firstName + ", almost there!</h1><br/>" +
                            "<h1>Go to " + email + " and confirm your registration.</h1> <br/> <h1>Tell your friends that you have " +
                            "registered for Red Hat Challenge 2013. Get them to join as well!</h1>"));
                }

                else {
                    errorLabel.setText("Someone has already used this email/contact. Try another?");
                }
            }
        });
    }
}