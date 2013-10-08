package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import org.redhatchallenge.rhc2013.shared.*;

import java.util.List;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
@RemoteServiceRelativePath("TestService")
public interface TestService extends RemoteService {

    public boolean submitAnswer(int id, Set<CorrectAnswer> answers) throws IllegalArgumentException, TimeIsUpException, UnauthenticatedException;

    public List<Question> loadQuestions() throws IllegalArgumentException, TimeslotExpiredException, UnauthenticatedException;

    public int getScore() throws IllegalArgumentException, UnauthenticatedException;

    public boolean checkIfTestIsOver() throws IllegalArgumentException, UnauthenticatedException;

    public int getTimeLeft() throws IllegalArgumentException, UnauthenticatedException;

    public static class Util {
        private static final TestServiceAsync Instance = (TestServiceAsync) GWT.create(TestService.class);

        public static TestServiceAsync getInstance() {
            return Instance;
        }
    }
}
