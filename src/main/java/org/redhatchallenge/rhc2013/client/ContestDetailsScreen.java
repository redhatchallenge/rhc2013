package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.shared.Student;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ContestDetailsScreen extends Composite {
    interface ContestDetailsScreenUiBinder extends UiBinder<Widget, ContestDetailsScreen> {
    }

    private static ContestDetailsScreenUiBinder UiBinder = GWT.create(ContestDetailsScreenUiBinder.class);

    private ProfileServiceAsync profileService = null;

    @UiField HTML welcomeLabel;

    public ContestDetailsScreen() {

        ScriptInjector.fromUrl("js/jquery-1.7.1.min.js").inject();

        initWidget(UiBinder.createAndBindUi(this));

        /**
         * If HTML5 storage does not contain the profile data, retrieves the data
         * from the server through a RPC call. Else retrieves first name from the
         * local storage.
         */
        final Storage localStorage = Storage.getLocalStorageIfSupported();
        StorageMap localStorageMap = new StorageMap(localStorage);
        if(localStorageMap.size() != 11) {
            profileService = ProfileService.Util.getInstance();
            profileService.getProfileData(new AsyncCallback<Student>() {
                @Override
                public void onFailure(Throwable caught) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(Student result) {
                    if(result == null) {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Oops, are you sure you are logged in?</h1>"));
                    }

                    else {
                        welcomeLabel.setHTML("<h1>Hello,"+result.getFirstName()+"</h1>");
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
            welcomeLabel.setHTML("<h1>Hello,"+ localStorage.getItem("firstName") +"</h1>");
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Jquery.countdown();
        Jquery.bind(10*24*60*60*1000);
    }
}