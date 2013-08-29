package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.shiro.SecurityUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.redhatchallenge.rhc2013.client.TestService;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;
import org.redhatchallenge.rhc2013.shared.Student;

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
    public List<Question> loadQuestions() throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Question> listOfQuestions = new ArrayList<>(150);

        try {
            String id = SecurityUtils.getSubject().getPrincipal().toString();
            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(id));

            int[] questionsArray = student.getQuestions();
            for (int i : questionsArray) {
                listOfQuestions.add(questionMap.get(i));
            }

            return listOfQuestions;

        } catch (HibernateException e) {
            throw new RuntimeException("Failed to retrieve profile information from the database");
        } finally {
            session.close();
        }
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
         * TODO: Implement a way to fetch the current score
         */

        int score = 0; //stub value, this needs to be populated from a db or cache

        if(correct) {
            score += 2;
        }

        else {
            score -= 1;
        }
    }
}