package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.redhatchallenge.rhc2013.client.AuthenticationService;
import org.redhatchallenge.rhc2013.shared.ConfirmationTokens;
import org.redhatchallenge.rhc2013.shared.ResetPasswordTokens;
import org.redhatchallenge.rhc2013.shared.Student;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService {

    private Cache<String, Integer> cache;

    public AuthenticationServiceImpl() {
        GlobalConfigurationBuilder global = new GlobalConfigurationBuilder();
        global.globalJmxStatistics()
                .allowDuplicateDomains(true).jmxDomain("org.redhatchallenge.rhc2013");
        ConfigurationBuilder builder = new ConfigurationBuilder();
        Configuration config = builder.build(true);
        EmbeddedCacheManager cacheManager = new DefaultCacheManager(config);
        cache = cacheManager.getCache();

        cache.put("A1", 0);
        cache.put("A2", 0);
    }

    @Override
    public Boolean registerStudent(String email, String password, String firstName, String lastName,
                                   String contact, String country, String countryCode, String school,
                                   String lecturerFirstName, String lecturerLastName, String lecturerEmail,
                                   String language) throws IllegalArgumentException {


        /**
         * Escape the all inputs received except for the password.
         * This is because the password will be hashed anyway so
         * it won't be affected by XSS is any way.
         */

        email = SecurityUtil.escapeInput(email);
        firstName = SecurityUtil.escapeInput(firstName);
        lastName = SecurityUtil.escapeInput(lastName);
        contact = SecurityUtil.escapeInput(contact);
        country = SecurityUtil.escapeInput(country);
        countryCode = SecurityUtil.escapeInput(countryCode);
        school = SecurityUtil.escapeInput(school);
        lecturerFirstName = SecurityUtil.escapeInput(lecturerFirstName);
        lecturerLastName = SecurityUtil.escapeInput(lecturerLastName);
        lecturerEmail = SecurityUtil.escapeInput(lecturerEmail);
        language = SecurityUtil.escapeInput(language);

        password = SecurityUtil.hashPassword(password);

        Student student = new Student();
        student.setEmail(email);
        student.setPassword(password);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setContact(contact);
        student.setCountry(country);
        student.setCountryCode(countryCode);
        student.setSchool(school);
        student.setLecturerFirstName(lecturerFirstName);
        student.setLecturerLastName(lecturerLastName);
        student.setLecturerEmail(lecturerEmail);
        student.setLanguage(language);

        /**
         * Runnable class to allow sending of email to be
         * delegated to another thread. This is to preserve
         * the response time of the application.
         */
        class SendConfirmationEmail implements Runnable {
            String email;

            SendConfirmationEmail(String email) {
                this.email = email;
            }

            @Override
            public void run() {

                ConfirmationTokens token = new ConfirmationTokens();
                token.setToken(EmailUtil.generateToken(32));
                token.setEmail(email);
                EmailUtil.sendEmail("Confirmation of account",
                        "<html>Click here to confirm your account: " + "http://register-redhatchallenge.rhcloud.com/register/?confirmToken=" + token.getToken() + "</html>",
                        "Your client does not support HTML messages, your token is " + token.getToken(),
                        email);

                Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
                currentSession.beginTransaction();
                currentSession.save(token);
                currentSession.getTransaction().commit();
            }
        }

        /**
         * Opens a session using Hibernate and saves the Student object.
         * Calls the SendConfirmationEmail runnable if the commit is
         * successful.
         */

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(student);
            session.getTransaction().commit();

            Thread t = new Thread(new SendConfirmationEmail(email));
            t.start();

            return true;
        } catch (ConstraintViolationException e) {
            session.getTransaction().rollback();
            return false;

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Boolean authenticateStudent(String email, String password, Boolean rememberMe) throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Subject currentUser = SecurityUtils.getSubject();

        try {
            /**
             * This retrieves the contestant ID to use as the "username"
             * for the authentication process. This is to avoid problems
             * with the current session when the email is updated in a
             * normal profile update.
             */
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Student.class);
            criteria.add(Restrictions.eq("email", email));
            Student student = (Student)criteria.uniqueResult();

            /**
             * If the supplied username does not exist, the query will return
             * a null Student object. Return false in this case.
             */
            if(student == null) {
                return false;
            }

            else {

                UsernamePasswordToken token = new UsernamePasswordToken(String.valueOf(student.getContestantId()), password);
                token.setRememberMe(rememberMe);

                currentUser.login(token);
                return true;
            }

        } catch (AuthenticationException e) {
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }

    /**
     * Checks if the user has been authenticated for the current session.
     *
     * @return  True if user is authenticated. False if otherwise.
     */
    @Override
    public boolean isAuthenticated() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }

    /**
     * Checks if user has been authenticated. This is a less strict check
     * compared to isAuthenticated() as it will return true if user is
     * authenticated through the use of a remember me cookie.
     *
     * @return  True if user is authenticated. False if otherwise.
     */
    @Override
    public boolean isRemembered() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isRemembered() || currentUser.isAuthenticated();
    }

    @Override
    public boolean setConfirmationStatus(String token) throws IllegalArgumentException {

        ConfirmationTokens tokens = lookUpConfirmationToken(token);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            if(tokens!=null) {
                session.beginTransaction();
                Criteria criteria = session.createCriteria(Student.class);
                criteria.add(Restrictions.eq("email", tokens.getEmail()));
                Student student = (Student)criteria.uniqueResult();

                Set<Integer> questions = randomQuestions();
                Integer[] arr = questions.toArray(new Integer[questions.size()]);
                int[] questionsArray = ArrayUtils.toPrimitive(arr);

                student.setQuestions(questionsArray);

                String timeslot = assignTimeslot(student.getCountry());
                student.setTimeslot(convertTimeSlot(timeslot));

                student.setVerified(true);

                session.update(student);

                session.delete(tokens);
                session.getTransaction().commit();

                cache.replace(timeslot, cache.get(timeslot)+1);
                return true;
            }

            else {
                return false;
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean triggerResetPassword(String email, String contact) throws IllegalArgumentException {

        /**
         * Runnable class to allow sending of email to be
         * delegated to another thread. This is to preserve
         * the response time of the application.
         */
        class SendPasswordResetEmail implements Runnable {
            String email;

            SendPasswordResetEmail(String email) {
                this.email = email;
            }

            @Override
            public void run() {

                ResetPasswordTokens token = new ResetPasswordTokens();
                token.setToken(EmailUtil.generateToken(32));
                token.setEmail(email);
                EmailUtil.sendEmail("Password Reset",
                        "<html>Reset your password here: " + "http://register-redhatchallenge.rhcloud.com/register/?resetToken=" + token.getToken() + "</html>",
                        "Your client does not support HTML messages, your token is " + token.getToken(),
                        email);

                Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
                currentSession.beginTransaction();
                currentSession.save(token);
                currentSession.getTransaction().commit();
            }
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Student.class);
            criteria.add(Restrictions.eq("email", email));
            Student student = (Student)criteria.uniqueResult();

            if(student.getContact().equals(contact)) {
                Thread t = new Thread(new SendPasswordResetEmail(email));
                t.start();

                return true;
            }

            else {
                return false;
            }
        } catch (HibernateException e) {
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public String lookupEmailFromToken(String token) throws IllegalArgumentException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();
            ResetPasswordTokens tokens = (ResetPasswordTokens)session.get(ResetPasswordTokens.class, token);

            session.delete(tokens);
            session.getTransaction().commit();

            return tokens.getEmail();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public boolean resetPassword(String password, String email) throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();

            Criteria criteria = session.createCriteria(Student.class);
            criteria.add(Restrictions.eq("email", email));
            Student student = (Student)criteria.uniqueResult();

            student.setPassword(SecurityUtil.hashPassword(password));
            session.update(student);
            session.getTransaction().commit();

            return true;
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            return false;
        }
    }

    /**
     * Look up ConfirmationTokens object based on the toke value.
     *
     * @param token  Token value of the object
     * @return  ConfirmationTokens object containing the correct token value.
     */
    private ConfirmationTokens lookUpConfirmationToken(String token) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            ConfirmationTokens tokens = (ConfirmationTokens)session.get(ConfirmationTokens.class, token);
            session.getTransaction().commit();
            return tokens;
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            return null;
        }
    }

    /**
     * Randomly generate a set of 150 questions based on the
     * given set of 500 questions.
     *
     * @return  Set of 150 random questions.
     */
    private Set<Integer> randomQuestions() {

        Random rand = new Random();
        int max;
        int min;

        Set<Integer> listOfQuestions = new HashSet<Integer>();
        int levelOne = 69;
        int levelTwo = 52;
        int levelThree = 29;

        while(listOfQuestions.size()<levelOne) {

            max = 230;
            min = 1;
            listOfQuestions.add(rand.nextInt(max - min + 1) + min);
        }

        while(listOfQuestions.size()<levelOne + levelTwo) {
            max = 406;
            min = 231;
            listOfQuestions.add(rand.nextInt(max - min + 1) + min);
        }

        while(listOfQuestions.size()<levelOne + levelTwo + levelThree) {
            max = 500;
            min = 407;
            listOfQuestions.add(rand.nextInt(max - min + 1) + min);
        }

        return listOfQuestions;
    }

    /**
     * Assigns time slot based on the country
     *
     * @param country  Input country
     * @return  Assigned time slot. Null if none available
     */
    private String assignTimeslot(String country) {

        if(country.substring(0,5).equalsIgnoreCase("china")) {
            return null;
        }

        else {
            if(cache.get("A1") <=300) {
                return "A1";
            }

            else if(cache.get("A2") <= 300) {
                return "A2";
            }

            else {
                return null;
            }
        }
    }

    /**
     * Converts assigned time slot into an actual time value.
     *
     * @param timeslot  Timeslot to be converted
     * @return  Actual time value in millisecond format
     */
    private long convertTimeSlot(String timeslot) {

        DateTimeZone.setDefault(DateTimeZone.UTC);

        if(timeslot == null) {
            return 0;
        }

        if(timeslot.equalsIgnoreCase("A1")) {
            DateTime a1Time = new DateTime(2013, 12, 07, 14, 0);
            return a1Time.toInstant().getMillis();
        }

        else {
            DateTime a2Time = new DateTime(2013, 12, 07, 16, 0);
            return a2Time.toInstant().getMillis();
        }
    }
}