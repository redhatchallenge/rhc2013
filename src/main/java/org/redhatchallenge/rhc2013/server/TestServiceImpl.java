package org.redhatchallenge.rhc2013.server;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.redhatchallenge.rhc2013.client.TestService;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;
import org.redhatchallenge.rhc2013.shared.Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TestServiceImpl extends RemoteServiceServlet implements TestService {

    private Map<Integer, Question> questionMap;
    private Map<String, Integer> scoreMap = new HashMap<String, Integer>();

    public TestServiceImpl() {
        InputStream in = TestServiceImpl.class.getResourceAsStream("/questions.csv");
        questionMap = parseCSV(in);
    }

    @Override
    public List<Question> loadQuestions() throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            String id = SecurityUtils.getSubject().getPrincipal().toString();
            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(id));

            return getQuestionsFromListOfQuestionNumbers(student.getQuestions());

        } catch (HibernateException e) {
            throw new RuntimeException("Failed to retrieve profile information from the database");
        } finally {
            session.close();
        }
    }

    @Override
    public boolean submitAnswer(int id, Set<CorrectAnswer> answers) throws IllegalArgumentException {

        if(compare(id, answers)) {
            updateScore(true);
            return true;
        }

        else {
            updateScore(false);
            return false;
        }
    }

    @Override
    public int getScore() throws IllegalArgumentException {
        String id = SecurityUtils.getSubject().getPrincipal().toString();
        return scoreMap.get(id);
    }

    private boolean compare(int id, Set<CorrectAnswer> provided) {
        Set<CorrectAnswer> correctAnswers = questionMap.get(id).getCorrectAnswers();
        return provided.equals(correctAnswers);
    }

    private void updateScore(boolean correct) {
        /**
         * TODO: Implement a way to fetch the current score
         *
         * Score is currently stored in a non-persistent HashMap.
         * I'll need to investigate a way to make this storage persistent,
         * either through Infinispan or flushing the HashMap directly into
         * the PostgreSQL database when I'm finished with it.
         */

        String id = SecurityUtils.getSubject().getPrincipal().toString();
        if(!scoreMap.containsKey(id)) {
            scoreMap.put(id, 0);
        }

        int score = scoreMap.get(id);

        if(correct) {
            score += 2;
        }

        else {
            score -= 1;
        }

        scoreMap.put(id, score);

    }


    private Map<Integer, Question> parseCSV(InputStream in) {

        HashMap<Integer, Question> map = new HashMap<>();

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(in));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Question question = new Question();
                question.setId(Integer.parseInt(nextLine[0]));
                question.setQuestion(nextLine[3]);

                List<String> answers = new ArrayList<String>(4);
                answers.add(nextLine[4]);
                answers.add(nextLine[5]);
                answers.add(nextLine[6]);
                answers.add(nextLine[7]);
                question.setAnswers(answers);

                Set<CorrectAnswer> correctAnswers = new HashSet<CorrectAnswer>(4);
                String[] parts = nextLine[8].split(",");
                for (String s : parts) {
                    int selection = Integer.parseInt(s);
                    switch (selection) {
                        case 1:
                            correctAnswers.add(CorrectAnswer.ONE);
                            break;
                        case 2:
                            correctAnswers.add(CorrectAnswer.TWO);
                            break;
                        case 3:
                            correctAnswers.add(CorrectAnswer.THREE);
                            break;
                        case 4:
                            correctAnswers.add(CorrectAnswer.FOUR);
                    }
                }

                question.setCorrectAnswers(correctAnswers);

                map.put(question.getId(), question);
            }

            return map;

        } catch (IOException e) {
            throw new RuntimeException("Unable to parse input stream");
        }
    }

    private List<Question> getQuestionsFromListOfQuestionNumbers(int[] questionNumberArray) {
        List<Question> listOfQuestions = new ArrayList<>(150);

        for (int i : questionNumberArray) {
            listOfQuestions.add(questionMap.get(i));
        }

        return listOfQuestions;
    }
}