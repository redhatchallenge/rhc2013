package org.redhatchallenge.rhc2013.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class Question implements Serializable {

    private int id;
    private String question;
    private List<String> answers;

    private Set<CorrectAnswer> correctAnswers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Set<CorrectAnswer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Set<CorrectAnswer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
