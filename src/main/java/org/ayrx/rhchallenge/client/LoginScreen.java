package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.ayrx.rhchallenge.resources.Resources;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class LoginScreen extends Composite {
    interface LoginScreenUiBinder extends UiBinder<Widget, LoginScreen> {
    }

    private static LoginScreenUiBinder UiBinder = GWT.create(LoginScreenUiBinder.class);

    @UiField TextBox emailField;
    @UiField PasswordTextBox passwordField;
    @UiField Image loginButton;
    @UiField CheckBox rememberMeField;
    @UiField Hyperlink resetPasswordLink;
    @UiField Label errorLabel;

    private AuthenticationServiceAsync authenticationService = null;

    public LoginScreen() {
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();
        initWidget(UiBinder.createAndBindUi(this));

        loginButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

    }

    @UiHandler("loginButton")
    public void handleLoginButtonClick(ClickEvent event) {
        authenticateStudent();
    }

    @UiHandler({"emailField", "passwordField", "rememberMeField"})
    public void handleKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            authenticateStudent();
        }
    }

    @UiHandler("resetPasswordLink")
    public void handleResetPasswordLinkClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new TriggerPasswordResetScreen());
    }

    private void authenticateStudent() {

        final String email = emailField.getText();
        String password = passwordField.getText();
        Boolean rememberMe = rememberMeField.getValue();

        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.authenticateStudent(email, password, rememberMe, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                errorLabel.setText("An unexpected error has occurred, please try again later!");
            }

            @Override
            public void onSuccess(Boolean bool) {
                if(bool) {
                    ContentContainer.INSTANCE.setContent(new ContestDetailsScreen());
                    RootPanel.get("header").clear();
                    RootPanel.get("header").add(new AuthenticatedHeader());
                }

                else {
                    errorLabel.setText("Your login attempt was a unsuccessful, please double check your inputs.");
                }

            }
        });
    }
}