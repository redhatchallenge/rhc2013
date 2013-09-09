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
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.shared.Question;
import org.redhatchallenge.rhc2013.shared.TimeIsUpException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TestScreen extends Composite {
    interface TestScreenUiBinder extends UiBinder<Widget, TestScreen> {
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
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<Question> result) {
                questionWidget = new MultipleChoiceWidget(counter+1, result.get(counter));
                questions = result;
                counter += 1;
                questionWidgetPanel.add(questionWidget);
            }
        });
    }

    @UiHandler("submitButton")
    public void handleSubmitButtonClick(ClickEvent event) {
        if(questionWidget.getSelectedAnswers().size() != 0) {
            submitButton.setEnabled(false);
            testService = TestService.Util.getInstance();
            testService.submitAnswer(questionWidget.getCurrentQuestionId(), questionWidget.getSelectedAnswers(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    if(caught instanceof TimeIsUpException) {
                        ContentContainer.INSTANCE.setContent(new ScoreScreen());
                    }
                }

                @Override
                public void onSuccess(Boolean result) {
                    submitButton.setEnabled(true);
                    if(counter <= questions.size() - 1) {
                        questionWidget.clear();
                        questionWidget.setQuestion(counter+1, questions.get(counter));
                        counter += 1;
                    }

                    else {
                        ContentContainer.INSTANCE.setContent(new ScoreScreen());
                    }
                }
            });
        }
    }
}