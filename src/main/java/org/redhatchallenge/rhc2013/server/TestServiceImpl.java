package org.redhatchallenge.rhc2013.server;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.shiro.SecurityUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.redhatchallenge.rhc2013.client.TestService;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;
import org.redhatchallenge.rhc2013.shared.Student;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        questionMap = parseCSV("/questions.csv");
    }

    @Override
    public List<Question> loadQuestions() throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Question> listOfQuestions = new ArrayList<>(150);

//        try {
//            String id = SecurityUtils.getSubject().getPrincipal().toString();
//            session.beginTransaction();
//            Student student = (Student)session.get(Student.class, Integer.parseInt(id));
//
//            int[] questionsArray = student.getQuestions();
//            for (int i : questionsArray) {
//                listOfQuestions.add(questionMap.get(i));
//            }
//
//            return listOfQuestions;
//
//        } catch (HibernateException e) {
//            throw new RuntimeException("Failed to retrieve profile information from the database");
//        } finally {
//            session.close();
//        }

        listOfQuestions.addAll(questionMap.values());
        return listOfQuestions;
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


    private Map<Integer, Question> parseCSV(String filename) {

        /**
         * TODO: Write a unit test for this function.
         */
        HashMap<Integer, Question> map = new HashMap<>();

        try {
            InputStream in = TestServiceImpl.class.getResourceAsStream(filename);
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

                List<CorrectAnswer> correctAnswers = new ArrayList<CorrectAnswer>(4);
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
            throw new RuntimeException("Unable to parse " + filename);
        }
    }
}