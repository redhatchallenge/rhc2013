package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TCScreen extends Composite {
    interface TCScreenUiBinder extends UiBinder<Widget, TCScreen> {
    }

    private static TCScreenUiBinder UiBinder = GWT.create(TCScreenUiBinder.class);

    public TCScreen() {
        initWidget(UiBinder.createAndBindUi(this));

    }
}