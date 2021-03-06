package org.redhatchallenge.rhc2013.server;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.EmbeddedCacheManager;
import org.redhatchallenge.rhc2013.client.TestService;
import org.redhatchallenge.rhc2013.shared.*;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TestServiceImpl extends RemoteServiceServlet implements TestService {

    private Map<Integer, Question> questionMapEn;
    private Map<Integer, Question> questionMapCh;
    private Map<Integer, Question> questionMapZh;

    private Cache<String, Integer> scoreMap;
    private Cache<String, int[]> assignedQuestionsMap;

    public TestServiceImpl() {
        InputStream inEn = TestServiceImpl.class.getResourceAsStream("/en.csv");
        InputStream inCh = TestServiceImpl.class.getResourceAsStream("/ch.csv");
        InputStream inZh = TestServiceImpl.class.getResourceAsStream("/zh.csv");

        questionMapEn = parseCSV(inEn);
        questionMapCh = parseCSV(inCh);
        questionMapZh = parseCSV(inZh);

        try {
            InitialContext ic = new InitialContext();

            Object expectedContainer = ic.lookup("java:jboss/infinispan/foobar");
            CacheContainer container = null;

            if (expectedContainer instanceof CacheContainer) {
                container = (CacheContainer) expectedContainer;
            } else {
                throw new RuntimeException("blah");
            }

            scoreMap = container.getCache("scoreMap");
            assignedQuestionsMap = container.getCache("assignedQuestionsMap");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> loadQuestions() throws IllegalArgumentException, TimeslotExpiredException, UnauthenticatedException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {

            if(SecurityUtils.getSubject().getPrincipal() == null) {
                throw new UnauthenticatedException();
            }

            String id = SecurityUtils.getSubject().getPrincipal().toString();
            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(id));

            if(System.currentTimeMillis() > student.getTimeslot() && System.currentTimeMillis() < student.getTimeslot() + 3600000) {
                if(!assignedQuestionsMap.containsKey(id)) {
                    student.setStartTime(new Timestamp(System.currentTimeMillis()));
                    session.update(student);
                    session.getTransaction().commit();

                    class TimesUp extends TimerTask {

                        private final int studentId;

                        TimesUp(int id) {
                            this.studentId = id;
                        }

                        @Override
                        public void run() {
                            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                            try {
                                session.beginTransaction();
                                Student student = (Student)session.get(Student.class, studentId);
                                if(student.getStartTime() != null && student.getEndTime() == null) {
                                    student.setScore(scoreMap.get(String.valueOf(studentId)));
                                    student.setEndTime(new Timestamp(System.currentTimeMillis()));
                                }
                                session.update(student);
                                session.getTransaction().commit();

                            } catch (HibernateException e) {
                                e.printStackTrace();
                                session.getTransaction().rollback();
                            } catch (NullPointerException e) {
                                session.getTransaction().rollback();
                            }
                        }
                    }

                    Timer timer = new Timer();
                    timer.schedule(new TimesUp(student.getContestantId()), 3600000);
                    assignedQuestionsMap.put(id, student.getQuestions());
                    return getQuestionsFromListOfQuestionNumbers(student.getQuestions(), student.getLanguage());
                }

                else {
                    session.close();
                    return getQuestionsFromListOfQuestionNumbers(assignedQuestionsMap.get(id), student.getLanguage());
                }
            }

            else {
                throw new TimeslotExpiredException();
            }

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to retrieve profile information from the database");
        }
    }

    @Override
    public boolean submitAnswer(int id, Set<CorrectAnswer> answers) throws IllegalArgumentException, TimeIsUpException, UnauthenticatedException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {

            if(SecurityUtils.getSubject().getPrincipal() == null) {
                throw new UnauthenticatedException();
            }

            String studentId = SecurityUtils.getSubject().getPrincipal().toString();
            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(studentId));

            int[] array = assignedQuestionsMap.get(studentId);
            array = ArrayUtils.removeElement(array, id);
            assignedQuestionsMap.put(studentId, array);

            if(student.getEndTime() == null) {
                if(compare(id, answers)) {
                    updateScore(true);
                    return true;
                }

                else {
                    updateScore(false);
                    return false;
                }
            }

            else {
                throw new TimeIsUpException();
            }
        } catch (HibernateException e) {
            throw new RuntimeException("Failed to retrieve profile information from the database");
        } finally {
            session.close();
        }
    }

    @Override
    public int getScore() throws IllegalArgumentException, UnauthenticatedException {

        if(SecurityUtils.getSubject().getPrincipal() == null) {
            throw new UnauthenticatedException();
        }

        String id = SecurityUtils.getSubject().getPrincipal().toString();
        int score = scoreMap.get(id);
        flushScoreToDatabase(id);
        scoreMap.remove(id);
        return score;
    }

    @Override
    public boolean checkIfTestIsOver() throws IllegalArgumentException, UnauthenticatedException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {

            if(SecurityUtils.getSubject().getPrincipal() == null) {
                throw new UnauthenticatedException();
            }

            String studentId = SecurityUtils.getSubject().getPrincipal().toString();
            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(studentId));
            return student.getEndTime() != null;
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to get user");
        } finally {
            session.close();
        }
    }

    @Override
    public int getTimeLeft() throws IllegalArgumentException, UnauthenticatedException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {

            if(SecurityUtils.getSubject().getPrincipal() == null) {
                throw new UnauthenticatedException();
            }

            String studentId = SecurityUtils.getSubject().getPrincipal().toString();
            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(studentId));
            long timeLeft = (student.getStartTime().getTime() + 3600000) - System.currentTimeMillis();
            return safeLongToInt(timeLeft/1000);
        } catch (HibernateException e) {
            throw new RuntimeException("Unable to get user", e);
        } finally {
            session.close();
        }
    }

    private boolean compare(int id, Set<CorrectAnswer> provided) {
        Set<CorrectAnswer> correctAnswers = questionMapEn.get(id).getCorrectAnswers();
        return provided.equals(correctAnswers);
    }

    private void updateScore(boolean correct) {

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
            CSVReader reader = new CSVReader(new InputStreamReader(in, "UTF-8"));
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

    private List<Question> getQuestionsFromListOfQuestionNumbers(int[] questionNumberArray, String language) {
        List<Question> listOfQuestions = new ArrayList<>(150);

        if (language.equals("English")) {
            for (int i : questionNumberArray) {
                listOfQuestions.add(questionMapEn.get(i));
            }
        }

        else if (language.equals("Chinese (Simplified)")) {
            for (int i : questionNumberArray) {
                listOfQuestions.add(questionMapCh.get(i));
            }
        }

        else if (language.equals("Chinese (Traditional)")) {
            for (int i : questionNumberArray) {
                listOfQuestions.add(questionMapZh.get(i));
            }
        }

        return listOfQuestions;
    }

    private void flushScoreToDatabase(String id) {

        int score = scoreMap.get(id);

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(id));
            student.setScore(score);
            student.setEndTime(new Timestamp(System.currentTimeMillis()));
            session.getTransaction().commit();
            assignedQuestionsMap.remove(id);
        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}