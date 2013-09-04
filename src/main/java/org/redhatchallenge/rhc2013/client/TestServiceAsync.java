package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;

import java.util.List;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface TestServiceAsync {
    void submitAnswer(int id, Set<CorrectAnswer> answers, AsyncCallback<Boolean> async);

    void loadQuestions(AsyncCallback<List<Question>> async);

    void getScore(AsyncCallback<Integer> async);
}
