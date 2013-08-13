package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class MessageScreen extends Composite {
    interface MessageScreenUiBinder extends UiBinder<Widget, MessageScreen> {
    }

    private static MessageScreenUiBinder UiBinder = GWT.create(MessageScreenUiBinder.class);

    @UiField HTML messageLabel;

    public MessageScreen(String message) {
        initWidget(UiBinder.createAndBindUi(this));
        messageLabel.setHTML(message);

        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();
    }
}