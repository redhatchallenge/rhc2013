package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.Question;
import org.redhatchallenge.rhc2013.shared.TimeIsUpException;
import org.redhatchallenge.rhc2013.shared.TimeslotExpiredException;
import org.redhatchallenge.rhc2013.shared.UnauthenticatedException;

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
    @UiField(provided = true) PushButton submitButton;
    @UiField HTML warningLabel;

    MultipleChoiceWidget questionWidget;
    List<Question> questions;
    int counter = 0;

    private TestServiceAsync testService = null;

    public TestScreen() {

        RootPanel.get("header").clear();
        this.submitButton = new PushButton(new Image(Resources.INSTANCE.submitButton()), new Image(Resources.INSTANCE.submitButtonGrey()));
        initWidget(UiBinder.createAndBindUi(this));

        submitButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        testService = TestService.Util.getInstance();
        testService.checkIfTestIsOver(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if(caught instanceof UnauthenticatedException) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Please login</h1>"));
                }
            }

            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>You have already completed the test</h1>"));
                }

                else {
                    loadQuestionsRPC(1);
                }
            }
        });
    }

    @UiHandler("submitButton")
    public void handleSubmitButtonClick(ClickEvent event) {
        if(questionWidget.getSelectedAnswers().size() != 0) {
            submitButton.setEnabled(false);
            warningLabel.setHTML("");
            testService = TestService.Util.getInstance();
            testService.submitAnswer(questionWidget.getCurrentQuestionId(), questionWidget.getSelectedAnswers(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    if(caught instanceof TimeIsUpException) {
                        ContentContainer.INSTANCE.setContent(new ScoreScreen());
                    }

                    else if(caught instanceof UnauthenticatedException) {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Please login</h1>"));
                    }

                    else {
                        warningLabel.setHTML("<font color='red'>An unexpected error has occurred. Please wait a while before submitting your answer again.</font>");
                        submitButton.setEnabled(true);
                    }
                }

                @Override
                public void onSuccess(Boolean result) {
                    if(counter <= questions.size() - 1) {
                        submitButton.setEnabled(true);
                        questionWidget.clear();
                        /**
                         * TODO: Change the value "6" on the following line to 151 for the actual thing.
                         */
                        questionWidget.setQuestion(6-questions.size()+counter, questions.get(counter));
                        counter += 1;
                    }

                    else {
                        ContentContainer.INSTANCE.setContent(new ScoreScreen());
                    }
                }
            });
        }

        else {
            warningLabel.setHTML("<font color='red'>Please select an option before submitting...</font>");
            submitButton.setEnabled(true);
        }
    }

    private void loadQuestionsRPC(final int count) {
        if(count<=5) {
            testService.loadQuestions(new AsyncCallback<List<Question>>() {
                @Override
                public void onFailure(Throwable caught) {
                    if(caught instanceof TimeslotExpiredException) {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Your timeslot is over. Sorry.</h1>"));
                    }

                    else if(caught instanceof UnauthenticatedException) {
                        ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Please login</h1>"));
                    }

                    else {
                        loadQuestionsRPC(count+1);
                    }
                }

                @Override
                public void onSuccess(List<Question> result) {
                    /**
                     * TODO: Change the value "6" on the following line to 151 for the actual thing.
                     */
                    questionWidget = new MultipleChoiceWidget(6-result.size(), result.get(counter));
                    questions = result;
                    counter += 1;
                    questionWidgetPanel.add(questionWidget);

                    testService.getTimeLeft(new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            if(caught instanceof UnauthenticatedException) {
                                ContentContainer.INSTANCE.setContent(new MessageScreen("<h1>Please login</h1>"));
                            }
                        }

                        @Override
                        public void onSuccess(Integer result) {
                            Jquery.questionTimer(Integer.toString(result / 60), Integer.toString(result % 60));
                        }
                    });
                }
            });
        }
    }
}