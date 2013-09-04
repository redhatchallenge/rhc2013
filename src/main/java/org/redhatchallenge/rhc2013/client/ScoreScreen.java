package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ScoreScreen extends Composite {
    interface ScoreScreenUiBinder extends UiBinder<Widget, ScoreScreen> {
    }

    private static ScoreScreenUiBinder UiBinder = GWT.create(ScoreScreenUiBinder.class);

    @UiField HTML message;

    private TestServiceAsync testService = null;

    public ScoreScreen() {
        initWidget(UiBinder.createAndBindUi(this));
        testService = TestService.Util.getInstance();
        testService.getScore(new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(Integer result) {
                message.setHTML("<h1>Congrats! You have a score of " + result);
            }
        });
    }
}