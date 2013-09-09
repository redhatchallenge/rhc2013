package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;
import org.redhatchallenge.rhc2013.shared.TimeIsUpException;

import java.util.List;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
@RemoteServiceRelativePath("TestService")
public interface TestService extends RemoteService {

    public boolean submitAnswer(int id, Set<CorrectAnswer> answers) throws IllegalArgumentException, TimeIsUpException;

    public List<Question> loadQuestions() throws IllegalArgumentException;

    public int getScore() throws IllegalArgumentException;

    public static class Util {
        private static final TestServiceAsync Instance = (TestServiceAsync) GWT.create(TestService.class);

        public static TestServiceAsync getInstance() {
            return Instance;
        }
    }
}
