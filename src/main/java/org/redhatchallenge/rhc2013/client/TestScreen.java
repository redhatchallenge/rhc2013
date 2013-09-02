package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.redhatchallenge.rhc2013.shared.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TestScreen extends Composite {
    interface TestScreenUiBinder extends UiBinder<HTMLPanel, TestScreen> {
    }

    private static TestScreenUiBinder UiBinder = GWT.create(TestScreenUiBinder.class);

    @UiField HorizontalPanel questionWidgetPanel;
    @UiField Button submitButton;

    MultipleChoiceWidget questionWidget;
    List<Question> questions;
    int counter = 0;

    private TestServiceAsync testService = null;

    public TestScreen() {

        initWidget(UiBinder.createAndBindUi(this));

        testService = TestService.Util.getInstance();
        testService.loadQuestions(new AsyncCallback<List<Question>>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(List<Question> result) {
                questionWidget = new MultipleChoiceWidget(result.get(counter));
                questions = result;
                counter += 1;
                questionWidgetPanel.add(questionWidget);
            }
        });
    }

    @UiHandler("submitButton")
    public void handleSubmitButtonClick(ClickEvent event) {
        if(questionWidget.getSelectedAnswers().size() != 0) {
            testService = TestService.Util.getInstance();
            testService.submitAnswer(questionWidget.getCurrentQuestionId(), questionWidget.getSelectedAnswers(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(Boolean result) {
                    if(counter <= questions.size() - 1) {
                        questionWidget.clear();
                        questionWidget.setQuestion(questions.get(counter));
                        counter += 1;
                    }

                    else {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("You have reached the end"));
                    }
                }
            });
        }
    }
}