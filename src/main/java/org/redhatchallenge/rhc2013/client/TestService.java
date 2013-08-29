package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;

import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
@RemoteServiceRelativePath("TestService")
public interface TestService extends RemoteService {

    public boolean submitAnswer(int id, List<CorrectAnswer> answers) throws IllegalArgumentException;

    public List<Question> loadQuestions() throws IllegalArgumentException;

    public static class Util {
        private static final TestServiceAsync Instance = (TestServiceAsync) GWT.create(TestService.class);

        public static TestServiceAsync getInstance() {
            return Instance;
        }
    }
}
