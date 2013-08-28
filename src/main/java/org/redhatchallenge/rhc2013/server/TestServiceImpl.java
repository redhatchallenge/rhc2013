package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.redhatchallenge.rhc2013.client.TestService;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TestServiceImpl extends RemoteServiceServlet implements TestService {

    private Map<Integer, Question> questionMap;

    public TestServiceImpl() {
        questionMap = new HashMap<>();

        /**
         * TODO: Add an XML parser to parse and populate the HashMap
         */
    }

    @Override
    public boolean submitAnswer(int id, List<CorrectAnswer> answers) throws IllegalArgumentException {
        /**
         * TODO: Implement Logic
         */

        if(compare(id, answers)) {
            updateScore(true);
            return true;
        }

        else {
            updateScore(false);
            return false;
        }
    }

    private boolean compare(int id, List<CorrectAnswer> provided) {
        List<CorrectAnswer> correctAnswers = questionMap.get(id).getCorrectAnswers();
        return !correctAnswers.retainAll(provided);
    }

    private void updateScore(boolean correct) {
        /**
         * TODO: Implement Logic
         */
    }
}