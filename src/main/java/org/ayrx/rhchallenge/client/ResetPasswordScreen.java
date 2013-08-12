package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ResetPasswordScreen extends Composite {

    interface ResetPasswordScreenUiBinder extends UiBinder<Widget, ResetPasswordScreen> {
    }

    private static ResetPasswordScreenUiBinder UiBinder = GWT.create(ResetPasswordScreenUiBinder.class);

    @UiField Label emailLabel;
    @UiField PasswordTextBox passwordField;
    @UiField PasswordTextBox confirmPasswordField;
    @UiField Button resetPasswordButton;
    @UiField Label errorLabel;
    @UiField Hyperlink loginLink;

    private String token = Window.Location.getParameter("resetToken");
    private AuthenticationServiceAsync authenticationService = null;

    public ResetPasswordScreen() {
        initWidget(UiBinder.createAndBindUi(this));
        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.lookupEmailFromToken(token, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                ContentContainer.INSTANCE.setContent(new MessageScreen("Error with password reset token"));
            }

            @Override
            public void onSuccess(String result) {
                emailLabel.setText(result);
            }
        });
    }

    @UiHandler("resetPasswordButton")
    public void handleResetPasswordButtonClick(ClickEvent event) {
        resetPassword();
    }

    @UiHandler({"passwordField", "confirmPasswordField"})
    public void handleKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            resetPassword();
        }
    }

    @UiHandler("loginLink")
    public void handleLoginLinkClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new LoginScreen());
    }

    private void resetPassword() {

        final String email = emailLabel.getText();
        final String password = passwordField.getText();

        resetPasswordButton.setEnabled(false);
        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.resetPassword(password, email, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText("An unexpected error has occurred, please try again later!");
                resetPasswordButton.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Password reset is successful!</h1>"));
                }

                else {
                    errorLabel.setText("<h1>An unexpected error has occurred, please try resetting your password again.</h1>");
                    resetPasswordButton.setEnabled(true);
                }
            }
        });
    }
}