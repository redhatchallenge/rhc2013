package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.ayrx.rhchallenge.resources.Resources;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class AuthenticatedHeader extends Composite {
    interface AuthenticatedHeaderUiBinder extends UiBinder<Widget, AuthenticatedHeader> {
    }

    private static AuthenticatedHeaderUiBinder UiBinder = GWT.create(AuthenticatedHeaderUiBinder.class);

    private AuthenticationServiceAsync authenticationService = AuthenticationService.Util.getInstance();

    @UiField Hyperlink indexLink;
    @UiField Hyperlink contestDetailsLink;
    @UiField Hyperlink tcLink;
    @UiField Hyperlink profileLink;
    @UiField Hyperlink logoutLink;

    public AuthenticatedHeader() {
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();
        initWidget(UiBinder.createAndBindUi(this));
    }

    @UiHandler("indexLink")
    public void handleIndexLinkClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new IndexScreen());
    }

    @UiHandler("contestDetailsLink")
    public void handleContestDetailsLinkClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new ContestDetailsScreen());
    }

    @UiHandler("tcLink")
    public void handleTCLinkClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new TCScreen());
    }

    @UiHandler("profileLink")
    public void handleProfileLinkClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new ProfileScreen());
    }

    @UiHandler("logoutLink")
    public void handleClick(ClickEvent event) {
        authenticationService.logout(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(Void aVoid) {
                RootPanel.get("header").clear();
                RootPanel.get("header").add(new Header());
                ContentContainer.INSTANCE.setContent(new IndexScreen());
                LocalStorage.INSTANCE.clearLocalStorage();
            }
        });
    }
}