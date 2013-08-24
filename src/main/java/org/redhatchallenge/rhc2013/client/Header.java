package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;

/**
 * @author: Terry Chia (Ayrx)
 */
public class Header extends Composite {
    interface HeaderUiBinder extends UiBinder<Widget, Header> {
    }

    private static HeaderUiBinder UiBinder = GWT.create(HeaderUiBinder.class);

    @UiField Hyperlink indexLink;
    @UiField Hyperlink registrationLink;
    @UiField Hyperlink tcLink;
    @UiField Hyperlink loginLink;

    public Header() {
        Resources.INSTANCE.main().ensureInjected();
        Resources.INSTANCE.grid().ensureInjected();
        initWidget(UiBinder.createAndBindUi(this));
    }

    @UiHandler("indexLink")
    public void handleIndexLinkClick(ClickEvent event) {
        History.newItem("", true);
    }
}