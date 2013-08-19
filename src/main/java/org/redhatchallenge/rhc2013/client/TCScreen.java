package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TCScreen extends Composite {
    interface TCScreenUiBinder extends UiBinder<Widget, TCScreen> {
    }

    private static TCScreenUiBinder UiBinder = GWT.create(TCScreenUiBinder.class);

    @UiField Image registerImage;

    public TCScreen() {
        initWidget(UiBinder.createAndBindUi(this));

        registerImage.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler({"registerImage"})
    public void handleClick(ClickEvent event) {
        History.newItem("registration", true);
    }
}