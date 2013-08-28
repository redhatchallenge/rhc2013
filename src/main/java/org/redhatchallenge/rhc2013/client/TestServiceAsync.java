package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;

import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface TestServiceAsync {
    void submitAnswer(int id, List<CorrectAnswer> answers, AsyncCallback<Boolean> async);
}
