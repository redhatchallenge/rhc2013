package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.redhatchallenge.rhc2013.resources.Resources;
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

    @UiField Label questionNumberLabel;
    @UiField HTML questionLabel;
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

        Resources.INSTANCE.main().ensureInjected();

        initWidget(UiBinder.createAndBindUi(this));

        questionNumberLabel.setText("Question " + questionNumber);
        questionLabel.setHTML(new SafeHtmlBuilder().appendEscapedLines(question.getQuestion().replaceAll("replacethis", "\n")).toSafeHtml());

        if(!question.getAnswers().get(0).isEmpty()) {
            firstChoiceLabel.setText(question.getAnswers().get(0));
            firstChoiceLabel.setVisible(true);
            firstChoice.setVisible(true);
        }

        else {
            firstChoiceLabel.setVisible(false);
            firstChoice.setVisible(false);
        }

        if(!question.getAnswers().get(1).isEmpty()) {
            secondChoiceLabel.setText(question.getAnswers().get(1));
            secondChoiceLabel.setVisible(true);
            secondChoice.setVisible(true);
        }

        else {
            secondChoiceLabel.setVisible(false);
            secondChoice.setVisible(false);
        }


        if(!question.getAnswers().get(2).isEmpty()) {
            thirdChoiceLabel.setText(question.getAnswers().get(2));
            thirdChoiceLabel.setVisible(true);
            thirdChoice.setVisible(true);
        }

        else {
            thirdChoiceLabel.setVisible(false);
            thirdChoice.setVisible(false);
        }


        if(!question.getAnswers().get(3).isEmpty()) {
            fourthChoiceLabel.setText(question.getAnswers().get(3));
            fourthChoiceLabel.setVisible(true);
            fourthChoice.setVisible(true);
        }

        else {
            fourthChoiceLabel.setVisible(false);
            fourthChoice.setVisible(false);
        }

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
        questionNumberLabel.setText("Question " + questionNumber);
        questionLabel.setHTML(new SafeHtmlBuilder().appendEscapedLines(question.getQuestion().replaceAll("replacethis", "\n")).toSafeHtml());

        if(!question.getAnswers().get(0).isEmpty()) {
            firstChoiceLabel.setText(question.getAnswers().get(0));
            firstChoiceLabel.setVisible(true);
            firstChoice.setVisible(true);
        }

        else {
            firstChoiceLabel.setVisible(false);
            firstChoice.setVisible(false);
        }

        if(!question.getAnswers().get(1).isEmpty()) {
            secondChoiceLabel.setText(question.getAnswers().get(1));
            secondChoiceLabel.setVisible(true);
            secondChoice.setVisible(true);
        }

        else {
            secondChoiceLabel.setVisible(false);
            secondChoice.setVisible(false);
        }


        if(!question.getAnswers().get(2).isEmpty()) {
            thirdChoiceLabel.setText(question.getAnswers().get(2));
            thirdChoiceLabel.setVisible(true);
            thirdChoice.setVisible(true);
        }

        else {
            thirdChoiceLabel.setVisible(false);
            thirdChoice.setVisible(false);
        }


        if(!question.getAnswers().get(3).isEmpty()) {
            fourthChoiceLabel.setText(question.getAnswers().get(3));
            fourthChoiceLabel.setVisible(true);
            fourthChoice.setVisible(true);
        }

        else {
            fourthChoiceLabel.setVisible(false);
            fourthChoice.setVisible(false);
        }

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