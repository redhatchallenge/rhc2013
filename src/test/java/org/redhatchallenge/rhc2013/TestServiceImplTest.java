package org.redhatchallenge.rhc2013;

import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Test;
import org.redhatchallenge.rhc2013.server.TestServiceImpl;
import org.redhatchallenge.rhc2013.shared.CorrectAnswer;
import org.redhatchallenge.rhc2013.shared.Question;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class TestServiceImplTest extends AbstractShiroTest {

    @Test
    public void testParseCsv() {
        Map<Integer, Question> questionMap;

        TestServiceImpl testService = new TestServiceImpl();

        try {
            Method method = testService.getClass().getDeclaredMethod("parseCSV", InputStream.class);
            method.setAccessible(true);

            InputStream in = new ByteArrayInputStream(("1,Cloud,1,This is the first question!,Choice 1,Choice 2,Choice 3,Choice 4,1\n" +
                    "2,Cloud,2,This is the second question!,Choice 1,Choice 2,Choice 3,Choice 4,\"1,2,3,4\"\n").getBytes());
            questionMap = (Map<Integer, Question>)method.invoke(testService, in);

            List<String> list = new ArrayList<String>();
            list.add("Choice 1");
            list.add("Choice 2");
            list.add("Choice 3");
            list.add("Choice 4");

            /**
             * Sets up the "expected" questionMap for the values we
             * passed in with InputStream in.
             */
            Map<Integer, Question> expectedQuestionMap = new HashMap<>();

            Question one = new Question();
            one.setQuestion("This is the first question!");
            one.setId(1);
            one.setAnswers(list);
            HashSet<CorrectAnswer> correctAnswersOne = new HashSet<>();
            correctAnswersOne.add(CorrectAnswer.ONE);
            one.setCorrectAnswers(correctAnswersOne);

            Question two = new Question();
            two.setQuestion("This is the second question!");
            two.setId(2);
            two.setAnswers(list);
            HashSet<CorrectAnswer> correctAnswersTwo = new HashSet<>();
            correctAnswersTwo.add(CorrectAnswer.ONE);
            correctAnswersTwo.add(CorrectAnswer.TWO);
            correctAnswersTwo.add(CorrectAnswer.THREE);
            correctAnswersTwo.add(CorrectAnswer.FOUR);
            two.setCorrectAnswers(correctAnswersTwo);

            expectedQuestionMap.put(one.getId(), one);
            expectedQuestionMap.put(two.getId(), two);

            /**
             * Asserts that the questionMap obtained from parsing
             * the InputStream in is equal to the expectedQuestionMap
             * that has been crafted by hand.
             */
            for(Map.Entry<Integer, Question> entry : questionMap.entrySet()) {
                assertReflectionEquals(entry.getValue(), expectedQuestionMap.get(entry.getKey()), ReflectionComparatorMode.LENIENT_ORDER);
            }

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateScore() throws NoSuchFieldException, IllegalAccessException {

        /**
         * Mock the Shiro Subject required.
         */
        Subject subjectUnderTest = mock(Subject.class);
        when(subjectUnderTest.isAuthenticated()).thenReturn(true);
        when(subjectUnderTest.getPrincipal()).thenReturn("0");
        setSubject(subjectUnderTest);

        TestServiceImpl testService = new TestServiceImpl();

        Field scoreMapField = testService.getClass().getDeclaredField("scoreMap");
        scoreMapField.setAccessible(true);

        Map<String, Integer> scoreMap = (Map<String, Integer>)scoreMapField.get(testService);

        /**
         * Mocks the value of questionMap in testService using reflection.
         * This helps to avoid the test breaking if/when the value of the
         * questions.csv file gets modified.
         */
        Field questionMapField = testService.getClass().getDeclaredField("questionMap");
        questionMapField.setAccessible(true);
        Map<Integer, Question> questionMap = (Map<Integer, Question>)questionMapField.get(testService);
        Map<Integer, Question> newQuestionMap = new HashMap<>();

        List<String> list = new ArrayList<String>();
        list.add("Choice 1");
        list.add("Choice 2");
        list.add("Choice 3");
        list.add("Choice 4");

        Question question = new Question();
        question.setQuestion("This is the first question!");
        question.setId(1);
        question.setAnswers(list);
        HashSet<CorrectAnswer> correctAnswersOne = new HashSet<>();
        correctAnswersOne.add(CorrectAnswer.ONE);
        question.setCorrectAnswers(correctAnswersOne);

        newQuestionMap.put(question.getId(), question);
        questionMapField.set(testService, newQuestionMap);

        /**
         * Asserts scoreMap() is empty when the servlet has just been
         * initialized.
         */
        assertTrue(scoreMap.isEmpty());

        HashSet<CorrectAnswer> correctAnswers = new HashSet<>();

        /**
         * Assert score is +2 after a correct answer.
         */
        correctAnswers.add(CorrectAnswer.ONE);
        testService.submitAnswer(1, correctAnswers);
        assertFalse(scoreMap.isEmpty());
        assertTrue(scoreMap.get("0").equals(2));

        /**
         * Assert score is -1 after a wrong answer.
         */
        correctAnswers.clear();
        correctAnswers.add(CorrectAnswer.TWO);
        correctAnswers.add(CorrectAnswer.THREE);
        testService.submitAnswer(1, correctAnswers);
        assertFalse(scoreMap.isEmpty());
        assertTrue(scoreMap.get("0").equals(1));
    }

    @Test
    public void testGetQuestionsFromListOfQuestionNumbers() throws Exception {

        TestServiceImpl testService = new TestServiceImpl();

        /**
         * Mocks the value of questionMap in testService using reflection.
         * This helps to avoid the test breaking if/when the value of the
         * questions.csv file gets modified.
         */
        Field questionMapField = testService.getClass().getDeclaredField("questionMap");
        questionMapField.setAccessible(true);
        Map<Integer, Question> questionMap = (Map<Integer, Question>)questionMapField.get(testService);
        Map<Integer, Question> newQuestionMap = new HashMap<>();

        List<String> list = new ArrayList<String>();
        list.add("Choice 1");
        list.add("Choice 2");
        list.add("Choice 3");
        list.add("Choice 4");

        Question one = new Question();
        one.setQuestion("This is the first question!");
        one.setId(1);
        one.setAnswers(list);
        HashSet<CorrectAnswer> correctAnswersOne = new HashSet<>();
        correctAnswersOne.add(CorrectAnswer.ONE);
        one.setCorrectAnswers(correctAnswersOne);

        newQuestionMap.put(one.getId(), one);

        Question two = new Question();
        two.setQuestion("This is the first question!");
        two.setId(2);
        two.setAnswers(list);
        HashSet<CorrectAnswer> correctAnswersTwo = new HashSet<>();
        correctAnswersTwo.add(CorrectAnswer.ONE);
        two.setCorrectAnswers(correctAnswersTwo);

        newQuestionMap.put(two.getId(), two);

        Question three = new Question();
        three.setQuestion("This is the first question!");
        three.setId(3);
        three.setAnswers(list);
        HashSet<CorrectAnswer> correctAnswersThree = new HashSet<>();
        correctAnswersThree.add(CorrectAnswer.ONE);
        three.setCorrectAnswers(correctAnswersThree);

        newQuestionMap.put(three.getId(), three);

        questionMapField.set(testService, newQuestionMap);

        Method method = testService.getClass().getDeclaredMethod("getQuestionsFromListOfQuestionNumbers", int[].class);
        method.setAccessible(true);

        int[] array = {1, 3};
        List<Question> listOfQuestions = (List<Question>)method.invoke(testService, array);

        List<Question> expectedListOfQuestions = new ArrayList<>();
        expectedListOfQuestions.add(one);
        expectedListOfQuestions.add(three);

        assertReflectionEquals(listOfQuestions, expectedListOfQuestions, ReflectionComparatorMode.LENIENT_ORDER);
    }

    @After
    public void tearDown() throws Exception {
        clearSubject();
    }
}
