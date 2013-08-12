package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.List;
import java.util.Map;

/**
 * @author  Terry Chia (terrycwk1994@gmail.com)
 */
public class Entry implements EntryPoint {

    private AuthenticationServiceAsync authenticationService = AuthenticationService.Util.getInstance();

    @Override
    public void onModuleLoad() {

        ScriptInjector.fromUrl(GWT.getHostPageBaseURL() + "js/jquery-1.7.1.min.js").inject();

        authenticationService.isRemembered(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                ContentContainer.INSTANCE.setContent(new MessageScreen("Oops, something went wrong..."));
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    RootPanel.get("header").clear();
                    RootPanel.get("header").add(new AuthenticatedHeader());
                } else {
                    RootPanel.get("header").clear();
                    RootPanel.get("header").add(new Header());
                }
            }
        });

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                String historyToken = event.getValue();

                if(historyToken.isEmpty()) {
                    parseTokens();
                }

                else if(historyToken.equalsIgnoreCase("registration")) {
                    ContentContainer.INSTANCE.setContent(new RegisterScreen());
                }

                else if(historyToken.equalsIgnoreCase("login")) {
                    ContentContainer.INSTANCE.setContent(new LoginScreen());
                }

                else if(historyToken.equalsIgnoreCase("tc")) {
                    ContentContainer.INSTANCE.setContent(new TCScreen());
                }

                else if(historyToken.equalsIgnoreCase("profile")) {
                    ContentContainer.INSTANCE.setContent(new ProfileScreen());
                }

                else if(historyToken.equalsIgnoreCase("details")) {
                    ContentContainer.INSTANCE.setContent(new ContestDetailsScreen());
                }
            }
        });

        History.fireCurrentHistoryState();
        RootPanel.get("footer").add(new Footer());

    }

    private void parseTokens() {
        String CONFIRM_TOKEN = "confirmToken";
        String RESET_TOKEN = "resetToken";

        AuthenticationServiceAsync authenticationService = AuthenticationService.Util.getInstance();

        Map<String, List<String>> params = Window.Location.getParameterMap();
        if(params.containsKey(RESET_TOKEN)) {
            ContentContainer.INSTANCE.setContent(new ResetPasswordScreen());
        }

        else if(params.containsKey(CONFIRM_TOKEN)) {
            authenticationService.setConfirmationStatus(params.get(CONFIRM_TOKEN).get(0), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("Error with your confirmation token"));
                }

                @Override
                public void onSuccess(Boolean result) {
                    if(result) {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("Thank you for confirming your account!"));
                    }

                    else {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("Error with your confirmation token"));
                    }
                }
            });
        }

        else {
            ContentContainer.INSTANCE.setContent(new IndexScreen());
        }
    }
}
