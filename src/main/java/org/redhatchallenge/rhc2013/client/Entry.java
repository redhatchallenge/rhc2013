package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import org.redhatchallenge.rhc2013.resources.Resources;

import java.util.List;
import java.util.Map;

/**
 * @author  Terry Chia (terrycwk1994@gmail.com)
 */
public class Entry implements EntryPoint {

    private AuthenticationServiceAsync authenticationService = AuthenticationService.Util.getInstance();
    private MessageMessages messages = GWT.create(MessageMessages.class);

    @Override
    public void onModuleLoad() {

        authenticationService.isRemembered(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                ContentContainer.INSTANCE.setContent(new MessageScreen(messages.somethingWrong()));
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

                if (historyToken.isEmpty()) {
                    ContentContainer.INSTANCE.setContent(new IndexScreen());
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
                else if(historyToken.equalsIgnoreCase("forget-password")){
                    ContentContainer.INSTANCE.setContent(new TriggerPasswordResetScreen());
                }


                else if(historyToken.substring(0, 10).equalsIgnoreCase("resetToken")) {
                    ContentContainer.INSTANCE.setContent(new ResetPasswordScreen(historyToken.substring(11)));
                }

                else if(historyToken.substring(0, 12).equalsIgnoreCase("confirmToken")) {
                    authenticationService.setConfirmationStatus(historyToken.substring(13), new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>"+ messages.confirmationTokenError() +"</h1>"));
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            if(result) {
                                ContentContainer.INSTANCE.setContent(new MessageScreen("<h2>"+ messages.confirmedAccount() +"</h2>"));
                            }

                            else {
                                ContentContainer.INSTANCE.setContent(new MessageScreen("<h2>"+ messages.confirmationTokenError() +"</h2>"));
                            }
                        }
                    });
                }

                else {
                    ContentContainer.INSTANCE.setContent(new IndexScreen());
                }
            }
        });

        History.fireCurrentHistoryState();
        RootPanel.get("footer").add(new Footer());

        if(LocaleInfo.getCurrentLocale().getLocaleName().equals("en")) {
            StyleInjector.inject("body.lp #page { background: #fff url(\"../images/masthead_microsite_1003x370.jpg\") 0 0 no-repeat; }");
            StyleInjector.inject("#sm-logo { background: transparent url(\"../images/redhathome_logo.png\") 0 30px no-repeat; }");
        }

        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
            StyleInjector.inject("body.lp #page { background: #fff url(\"../images/masthead_microsite_1003x370_ch.jpg\") 0 0 no-repeat; }");
            StyleInjector.inject("#sm-logo { background: transparent url(\"../images/redhathome_logo_ch.png\") 0 30px no-repeat; }");
        }
        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("zh")) {
            StyleInjector.inject("body.lp #page { background: #fff url(\"../images/masthead_microsite_1003x370_zh.jpg\") 0 0 no-repeat; }");
            StyleInjector.inject("#sm-logo { background: transparent url(\"../images/redhathome_logo_zh.png\") 0 30px no-repeat; }");
        }
    }
}
