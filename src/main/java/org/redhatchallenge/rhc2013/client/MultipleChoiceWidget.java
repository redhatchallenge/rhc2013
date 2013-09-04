package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class MultipleChoiceWidget extends Composite {
    interface MultipleChoiceWidgetUiBinder extends UiBinder<Widget, MultipleChoiceWidget> {
    }

    private static MultipleChoiceWidgetUiBinder UiBinder = GWT.create(MultipleChoiceWidgetUiBinder.class);

    @UiField Label questionLabel;
    @UiField Label firstChoiceLabel;
    @UiField Label secondChoiceLabel;
    @UiField Label thirdChoiceLabel;
    @UiField Label fourthChoiceLabel;

    @UiField CheckBox firstChoice;
    @UiField CheckBox secondChoice;
    @UiField CheckBox thirdChoice;
    @UiField CheckBox fourthChoice;

    private Question question;

    public MultipleChoiceWidget(int questionNumber, Question question) {

        initWidget(UiBinder.createAndBindUi(this));

        questionLabel.setText(questionNumber + ". " + question.getQuestion());
        firstChoiceLabel.setText(question.getAnswers().get(0));
        secondChoiceLabel.setText(question.getAnswers().get(1));
        thirdChoiceLabel.setText(question.getAnswers().get(2));
        fourthChoiceLabel.setText(question.getAnswers().get(3));

        this.question = question;
    }

    public void clear() {
        firstChoice.setValue(false);
        secondChoice.setValue(false);
        thirdChoice.setValue(false);
        fourthChoice.setValue(false);

        questionLabel.setText("");
        firstChoiceLabel.setText("");
        secondChoiceLabel.setText("");
        thirdChoiceLabel.setText("");
        fourthChoiceLabel.setText("");
    }

    public void setQuestion(int questionNumber, Question question) {
        questionLabel.setText(questionNumber + ". " + question.getQuestion());
        firstChoiceLabel.setText(question.getAnswers().get(0));
        secondChoiceLabel.setText(question.getAnswers().get(1));
        thirdChoiceLabel.setText(question.getAnswers().get(2));
        fourthChoiceLabel.setText(question.getAnswers().get(3));

        this.question = question;
    }

    public Set<CorrectAnswer> getSelectedAnswers() {

        Set<CorrectAnswer> list = new HashSet<CorrectAnswer>(4);

        if(firstChoice.getValue()) {
            list.add(CorrectAnswer.ONE);
        }

        if(secondChoice.getValue()) {
            list.add(CorrectAnswer.TWO);
        }

        if(thirdChoice.getValue()) {
            list.add(CorrectAnswer.THREE);
        }

        if(fourthChoice.getValue()) {
            list.add(CorrectAnswer.FOUR);
        }

        return list;
    }

    public int getCurrentQuestionId() {
        return question.getId();
    }
}