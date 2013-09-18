package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ScoreScreen extends Composite {
    interface ScoreScreenUiBinder extends UiBinder<Widget, ScoreScreen> {
    }

    private static ScoreScreenUiBinder UiBinder = GWT.create(ScoreScreenUiBinder.class);

    @UiField HTML message;
    @UiField HTML score;
    @UiField Image medal;

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
                message.setHTML("<center>Thank you for completing the red hat challenge 2013!<br/>we will notify you if you are qualified for the 2nd round</center>");
                medal.setResource(Resources.INSTANCE.goldTrophy());
                score.setHTML("<center><h1>" + result + " POINTS</h1></center>");
            }
        });
    }
}