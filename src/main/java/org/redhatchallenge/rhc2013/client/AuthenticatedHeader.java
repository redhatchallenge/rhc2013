package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.Student;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class AuthenticatedHeader extends Composite {
    interface AuthenticatedHeaderUiBinder extends UiBinder<Widget, AuthenticatedHeader> {
    }

    private static AuthenticatedHeaderUiBinder UiBinder = GWT.create(AuthenticatedHeaderUiBinder.class);

    private AuthenticationServiceAsync authenticationService = AuthenticationService.Util.getInstance();
    private ProfileServiceAsync profileService = null;
    private MessageMessages messages = GWT.create(MessageMessages.class);

    @UiField Hyperlink indexLink;
    @UiField Hyperlink contestDetailsLink;
    @UiField Hyperlink tcLink;
    @UiField Hyperlink profileLink;
    @UiField Hyperlink logoutLink;
    @UiField Label welcomeLabel;

    public AuthenticatedHeader() {
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();
        initWidget(UiBinder.createAndBindUi(this));

        /**
         * If HTML5 storage does not contain the profile data, retrieves the data
         * from the server through a RPC call. Else retrieves first name from the
         * local storage.
         */
        final Storage localStorage = Storage.getLocalStorageIfSupported();
        StorageMap localStorageMap = new StorageMap(localStorage);
        if(localStorageMap.size() != 12) {
            profileService = ProfileService.Util.getInstance();
            profileService.getProfileData(new AsyncCallback<Student>() {
                @Override
                public void onFailure(Throwable caught) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen(messages.probablyNotLoginIn()));
                }

                @Override
                public void onSuccess(Student result) {
                    if(result == null) {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>"+ messages.loginError() +"?</h1>"));
                    }

                    else {
                        welcomeLabel.setText(messages.hello() + "," + result.getFirstName());
                        /**
                         * If browser supports HTML5 storage, stores the authenticated user's
                         * profile data.
                         */
                        if(localStorage != null) {
                            localStorage.setItem("email", result.getEmail());
                            localStorage.setItem("firstName", result.getFirstName());
                            localStorage.setItem("lastName", result.getLastName());
                            localStorage.setItem("contact", result.getContact());
                            localStorage.setItem("country", result.getCountry());
                            localStorage.setItem("countryCode", result.getCountryCode());
                            localStorage.setItem("school", result.getSchool());
                            localStorage.setItem("lecturerFirstName", result.getLecturerFirstName());
                            localStorage.setItem("lecturerLastName", result.getLecturerLastName());
                            localStorage.setItem("lecturerEmail", result.getLecturerEmail());
                            localStorage.setItem("language", result.getLanguage());
                        }
                    }
                }
            });
        }

        else {
            welcomeLabel.setText(messages.hello() + ","+ localStorage.getItem("firstName"));
        }
    }

    @UiHandler("indexLink")
    public void handleIndexLinkClick(ClickEvent event) {
        History.newItem("", true);
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
                History.newItem("", true);
                Storage localStorage = Storage.getLocalStorageIfSupported();
                localStorage.clear();
            }
        });
    }
}