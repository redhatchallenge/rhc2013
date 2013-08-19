package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.redhatchallenge.rhc2013.client.AuthenticationService;
import org.redhatchallenge.rhc2013.client.AuthenticationServiceAsync;
import org.redhatchallenge.rhc2013.client.ContentContainer;
import org.redhatchallenge.rhc2013.client.MessageScreen;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ResetPasswordScreen extends Composite {

    interface ResetPasswordScreenUiBinder extends UiBinder<Widget, ResetPasswordScreen> {
    }

    private static ResetPasswordScreenUiBinder UiBinder = GWT.create(ResetPasswordScreenUiBinder.class);
    private static MessageMessages messages = GWT.create(MessageMessages.class);

    @UiField Label emailLabel;
    @UiField PasswordTextBox passwordField;
    @UiField PasswordTextBox confirmPasswordField;
    @UiField Image resetPasswordButton;
    @UiField Label errorLabel;

    private String token = Window.Location.getParameter("resetToken");
    private AuthenticationServiceAsync authenticationService = null;

    public ResetPasswordScreen() {
        initWidget(UiBinder.createAndBindUi(this));
        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.lookupEmailFromToken(token, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                ContentContainer.INSTANCE.setContent(new MessageScreen(messages.passwordTokenError()));
            }

            @Override
            public void onSuccess(String result) {
                emailLabel.setText(result);
            }
        });

        resetPasswordButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

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


    private void resetPassword() {

        final String email = emailLabel.getText();
        final String password = passwordField.getText();

        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.resetPassword(password, email, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(messages.unexpectedError());
            }

            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen(messages.passwordResetSuccess()));
                }

                else {
                    errorLabel.setText(messages.errorResetPassword());
                }
            }
        });
    }
}