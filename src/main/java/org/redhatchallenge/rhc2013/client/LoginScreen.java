package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.UnconfirmedStudentException;
import org.redhatchallenge.rhc2013.shared.FieldVerifier;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class LoginScreen extends Composite {
    interface LoginScreenUiBinder extends UiBinder<Widget, LoginScreen> {
    }

    private static LoginScreenUiBinder UiBinder = GWT.create(LoginScreenUiBinder.class);
    private MessageMessages messages = GWT.create(MessageMessages.class);

    @UiField TextBox emailField;
    @UiField PasswordTextBox passwordField;
    @UiField Image loginButton;
    @UiField CheckBox rememberMeField;
    @UiField Hyperlink resetPasswordLink;
    @UiField Label errorLabel;
    @UiField Anchor socialButton1;
    @UiField Anchor socialButton2;

    @UiField Label loginEmailLabel;
    @UiField Label loginPasswordLabel;

    private AuthenticationServiceAsync authenticationService = null;

    public LoginScreen() {
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();
        initWidget(UiBinder.createAndBindUi(this));

        loginButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

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

    @UiHandler("loginButton")
    public void handleLoginButtonClick(ClickEvent event) {

        int successCounter = 0;

        if(FieldVerifier.emailIsNull(emailField.getText())){
            loginEmailLabel.setText(messages.emailEmpty());
        }
            else if(!FieldVerifier.isValidEmail(emailField.getText())){
                loginEmailLabel.setText(messages.invalidEmailFormat());
            }
                else{
                    loginEmailLabel.setText("");
                    successCounter++;
                }

        if(FieldVerifier.passwordIsNull(passwordField.getText())){
            loginPasswordLabel.setText(messages.emptyPassword());
        }
            else{
                loginPasswordLabel.setText("");
                successCounter++;
            }
        if(successCounter == 2){
            authenticateStudent();
        }
    }

    @UiHandler({"emailField", "passwordField", "rememberMeField"})
    public void handleKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            authenticateStudent();
        }
    }

    @UiHandler("resetPasswordLink")
    public void handleResetPasswordLinkClick(ClickEvent event) {
        History.newItem("forget-password", true);
    }

    private void authenticateStudent() {

        loginButton.setResource(Resources.INSTANCE.loginButtonGrey());

        final String email = emailField.getText();
        final String password = passwordField.getText();
        final Boolean rememberMe = rememberMeField.getValue();

        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.authenticateStudent(email, password, rememberMe, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                if(throwable instanceof UnconfirmedStudentException) {
                   ContentContainer.INSTANCE.setContent(new MessageScreen(messages.verifyBeforeLogin(email), messages.verifyBeforeLoginUrl(), email));
                }

                else {
                    errorLabel.setText(messages.unexpectedError());
                    loginButton.setResource(Resources.INSTANCE.loginButton());
                }
            }

            @Override
            public void onSuccess(Boolean bool) {
                if(bool) {
                    /**
                     * Clears the local storage on a fresh login to prevent the
                     * data of an old user from being populated.
                     */
                    Storage localStorage = Storage.getLocalStorageIfSupported();
                    localStorage.clear();

                    RootPanel.get("header").clear();
                    RootPanel.get("header").add(new AuthenticatedHeader());
                    History.newItem("details", true);
                }

                else {
                    errorLabel.setText(messages.loginUnsuccessful());
                    loginButton.setResource(Resources.INSTANCE.loginButton());
                }

            }
        });
    }
}