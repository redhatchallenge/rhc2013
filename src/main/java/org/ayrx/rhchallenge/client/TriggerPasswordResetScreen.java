package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TriggerPasswordResetScreen extends Composite {

    interface ResetPasswordScreenUiBinder extends UiBinder<Widget, TriggerPasswordResetScreen> {
    }

    private static ResetPasswordScreenUiBinder UiBinder = GWT.create(ResetPasswordScreenUiBinder.class);

    @UiField TextBox emailField;
    @UiField TextBox contactField;
    @UiField Image resetPasswordButton;
    @UiField Label errorLabel;

    private AuthenticationServiceAsync authenticationService = null;

    public TriggerPasswordResetScreen() {
        initWidget(UiBinder.createAndBindUi(this));
    }

    @UiHandler("resetPasswordButton")
    public void handleResetPasswordButtonClick(ClickEvent event) {
        resetPassword();
    }

    @UiHandler({"emailField", "contactField"})
    public void handleKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            resetPassword();
        }
    }

    private void resetPassword() {

        String email = emailField.getText();
        String contact = contactField.getText();

        authenticationService = AuthenticationService.Util.getInstance();

        authenticationService.triggerResetPassword(email, contact, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText("An unexpected error has occurred, please try again later!");
            }

            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("Please check your email for instructions to" +
                            "reset your password."));
                }

                else {
                    errorLabel.setText("Error with password reset, please check that your email and contact is correct.");
                }
            }
        });
    }
}