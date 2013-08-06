package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.ayrx.rhchallenge.resources.Resources;
import org.ayrx.rhchallenge.shared.Student;

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
                }
            }
        });

    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Jquery.countdown();
        Jquery.bind();
    }
}