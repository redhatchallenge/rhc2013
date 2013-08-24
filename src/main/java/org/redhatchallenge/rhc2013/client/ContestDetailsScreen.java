package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.Date;


/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ContestDetailsScreen extends Composite {
    interface ContestDetailsScreenUiBinder extends UiBinder<Widget, ContestDetailsScreen> {
    }

    private static ContestDetailsScreenUiBinder UiBinder = GWT.create(ContestDetailsScreenUiBinder.class);

    private ProfileServiceAsync profileService = null;
    private MessageMessages messages = GWT.create(MessageMessages.class);

    @UiField HTML welcomeLabel;
    @UiField TextBox emailField;
    @UiField TextBox timeSlotField;
    @UiField TextBox languageField;

    public ContestDetailsScreen() {

        Jquery.countdown();

        initWidget(UiBinder.createAndBindUi(this));

        emailField.setReadOnly(true);
        timeSlotField.setReadOnly(true);
        languageField.setReadOnly(true);

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
                        welcomeLabel.setHTML("<FONT SIZE=6>"+ messages.hello() + ", "+ result.getFirstName() + "</FONT>");
                        emailField.setText(result.getEmail());

                        if(result.getLanguage().equals("Chinese (Traditional)"))  {
                            languageField.setText(messages.languageCT());
                        }
                        else if(result.getLanguage().equals("Chinese (Simplified)")){
                            languageField.setText(messages.languageCS());
                        }

                        else{
                            languageField.setText(messages.languageEN());
                        }

                        /**
                         * If timeslot has not yet been assigned, inform the user so.
                         */
                        if(result.getTimeslot() != 0) {
                            Date date = new Date(result.getTimeslot());
                            timeSlotField.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(date));
                        }

                        else {
                            timeSlotField.setText(messages.noTimeSlot());
                        }

                        if(LocaleInfo.getCurrentLocale().getLocaleName().equals("en")) {
                            Jquery.bindEn(safeLongToInt(result.getTimeslot()/1000 - new Date().getTime()/1000));
                        }

                        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
                            Jquery.bindCh(safeLongToInt(result.getTimeslot()/1000 - new Date().getTime()/1000));
                        }
                        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("zh")) {
                            Jquery.bindZh(safeLongToInt(result.getTimeslot()/1000 - new Date().getTime()/1000));
                        }

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
                            localStorage.setItem("timeSlot", Long.toString(result.getTimeslot()));
                        }
                    }
                }
            });
        }

        else {
            welcomeLabel.setHTML("<FONT SIZE=6>"+ messages.hello() + ", "+ localStorage.getItem("firstName")  +"</FONT>");
            emailField.setText(localStorage.getItem("email"));
            if(localStorage.getItem("language").equals("Chinese (Traditional)"))  {
                languageField.setText(messages.languageCT());
            }
            else if(localStorage.getItem("language").equals("Chinese (Simplified)")){
                languageField.setText(messages.languageCS());
            }
            else {
                languageField.setText(messages.languageEN());
            }

            /**
             * If timeslot has not yet been assigned, inform the user so.
             */
            if(Long.parseLong(localStorage.getItem("timeSlot")) != 0) {
                Date date = new Date(Long.parseLong(localStorage.getItem("timeSlot")));
                timeSlotField.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(date));
            }

            else {
                timeSlotField.setText(messages.noTimeSlot());
            }
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        final Storage localStorage = Storage.getLocalStorageIfSupported();
        StorageMap localStorageMap = new StorageMap(localStorage);

        /**
         * For some reason, the Jquery countdown timer doesn't bind to the node when called
         * in the constructor when localStorage contains data.
         *
         * This is a workaround which gets called when localStorage has the necessary data to
         * populate the countdown timer.
         */
        if(localStorageMap.size() == 12) {
            if(LocaleInfo.getCurrentLocale().getLocaleName().equals("en")) {
                Jquery.bindEn(safeLongToInt(Long.parseLong(localStorage.getItem("timeSlot"))/1000 - new Date().getTime()/1000));
            }

            else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
                Jquery.bindCh(safeLongToInt(Long.parseLong(localStorage.getItem("timeSlot"))/1000 - new Date().getTime()/1000));
            }

            else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("zh")) {
                Jquery.bindZh(safeLongToInt(Long.parseLong(localStorage.getItem("timeSlot"))/1000 - new Date().getTime()/1000));
            }
        }
    }

    private int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }


}