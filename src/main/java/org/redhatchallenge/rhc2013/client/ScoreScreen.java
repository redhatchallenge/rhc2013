package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.UnauthenticatedException;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ScoreScreen extends Composite {
    interface ScoreScreenUiBinder extends UiBinder<Widget, ScoreScreen> {
    }

    private static MessageMessages messages = GWT.create(MessageMessages.class);
    private static ScoreScreenUiBinder UiBinder = GWT.create(ScoreScreenUiBinder.class);

    @UiField HTML message;
    @UiField HTML score;
    @UiField Image medal;
    @UiField Hyperlink homeLink;

    private TestServiceAsync testService = null;

    public ScoreScreen() {
        initWidget(UiBinder.createAndBindUi(this));
        testService = TestService.Util.getInstance();
        getScoreRPC(1);
    }

    @UiHandler("homeLink")
    public void handleClick(ClickEvent event) {
        RootPanel.get("header").clear();
        RootPanel.get("header").add(new AuthenticatedHeader());
        ContentContainer.INSTANCE.setContent(new IndexScreen());
    }

    private void getScoreRPC(final int count) {
        if(count<=5) {
            testService.getScore(new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    if(caught instanceof UnauthenticatedException) {
                        ContentContainer.INSTANCE.setContent(new MessageScreen(messages.errorLogin()));
                    }

                    else {
                        getScoreRPC(count+1);
                    }
                }

                @Override
                public void onSuccess(Integer result) {
                    message.setHTML(messages.completeMessage());
                    if(result>=240) {
                        medal.setResource(Resources.INSTANCE.goldTrophy());
                    }

                    else if(result<240 && result >= 150) {
                        medal.setResource(Resources.INSTANCE.silverTrophy());
                    }

                    else {
                        medal.setResource(Resources.INSTANCE.bronzeTrophy());
                    }

                    score.setHTML("<center><h1>" + result + " POINTS</h1></center>");
                }
            });
        }
    }
}