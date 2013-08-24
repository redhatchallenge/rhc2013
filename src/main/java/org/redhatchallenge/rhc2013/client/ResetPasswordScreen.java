package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.LocaleInfo;
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
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.FieldVerifier;

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
    @UiField Anchor socialButton1;
    @UiField Anchor socialButton2;

    @UiField Label confirmPasswordLabel;
    @UiField Label resetPasswordLabel;

    private AuthenticationServiceAsync authenticationService = null;

    public ResetPasswordScreen(String token) {
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

    @UiHandler("resetPasswordButton")
    public void handleResetPasswordButtonClick(ClickEvent event) {
        int successCounter = 0;

        if(FieldVerifier.passwordIsNull(passwordField.getText())){
            resetPasswordLabel.setText(messages.emptyPassword());
        }
            else if(!FieldVerifier.isValidPassword(passwordField.getText())){
                resetPasswordLabel.setText(messages.passwordInvalidFormat());
        }
                else{
                    resetPasswordLabel.setText("");
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

        if(successCounter == 2){
            resetPassword();
        }

    }

    @UiHandler({"passwordField", "confirmPasswordField"})
    public void handleKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            resetPassword();
        }
    }


    private void resetPassword() {

        resetPasswordButton.setResource(Resources.INSTANCE.submitButtonGrey());

        final String email = emailLabel.getText();
        final String password = passwordField.getText();

        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.resetPassword(password, email, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(messages.unexpectedError());
                resetPasswordButton.setResource(Resources.INSTANCE.submitButton());
            }

            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen(messages.passwordResetSuccess()));
                }

                else {
                    errorLabel.setText(messages.errorResetPassword());
                    resetPasswordButton.setResource(Resources.INSTANCE.submitButton());
                }
            }
        });
    }
}