package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.redhatchallenge.rhc2013.resources.Resources;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class MessageScreen extends Composite {
    interface MessageScreenUiBinder extends UiBinder<Widget, MessageScreen> {
    }

    private static MessageScreenUiBinder UiBinder = GWT.create(MessageScreenUiBinder.class);

    @UiField HTML messageLabel;
    @UiField HTML urlLink;
    @UiField Label sentLabel;

    private static MessageMessages messages = GWT.create(MessageMessages.class);

    private AuthenticationServiceAsync authenticationService = null;

    public MessageScreen(String message) {
        initWidget(UiBinder.createAndBindUi(this));
        messageLabel.setHTML(message);

        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();
    }

    public MessageScreen(String message, String url, final String para) {
        initWidget(UiBinder.createAndBindUi(this));
        messageLabel.setHTML(message);
        urlLink.setHTML(url);

        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();

        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                authenticationService = AuthenticationService.Util.getInstance();
                authenticationService.resendVerificationEmail(para, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Void result) {
                        sentLabel.setText(messages.verificationMailSent());
                    }
                });
            }
        };

        urlLink.addClickHandler(handler);
    }

}