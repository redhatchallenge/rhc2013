package org.ayrx.rhchallenge.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.ayrx.rhchallenge.client.AuthenticationService;
import org.ayrx.rhchallenge.shared.ConfirmationTokens;
import org.ayrx.rhchallenge.shared.ResetPasswordTokens;
import org.ayrx.rhchallenge.shared.Student;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService {

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
                        "<html>Click here to confirm your account: " + "http://register-ayrx.rhcloud.com/register/?confirmToken=" + token.getToken() + "</html>",
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

        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        token.setRememberMe(rememberMe);

        Subject currentUser = SecurityUtils.getSubject();

        try {
            currentUser.login(token);
            return true;
        }   catch (AuthenticationException e) {
            return false;
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

        try {
            ConfirmationTokens tokens = lookUpConfirmationToken(token);
            if(tokens!=null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Criteria criteria = session.createCriteria(Student.class);
                criteria.add(Restrictions.eq("email", tokens.getEmail()));
                Student student = (Student)criteria.uniqueResult();

                Set<Integer> questions = randomQuestions();
                Integer[] arr = questions.toArray(new Integer[questions.size()]);
                int[] questionsArray = ArrayUtils.toPrimitive(arr);

                student.setQuestions(questionsArray);
                student.setVerified(true);

                session.update(student);
                session.getTransaction().commit();

                Session newSession = HibernateUtil.getSessionFactory().getCurrentSession();
                newSession.beginTransaction();
                newSession.delete(tokens);
                newSession.getTransaction().commit();

                return true;
            }

            else {
                return false;
            }
        } catch (HibernateException e) {
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
                        "<html>Reset your password here: " + "http://register-ayrx.rhcloud.com/register/?resetToken=" + token.getToken() + "</html>",
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
            session.close();

            if(student.getContact().equals(contact)) {
                Thread t = new Thread(new SendPasswordResetEmail(email));
                t.start();

                return true;
            }

            else {
                return false;
            }
        } catch (HibernateException e) {
            session.close();
            return false;
        }
    }

    @Override
    public String lookupEmailFromToken(String token) throws IllegalArgumentException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        ResetPasswordTokens tokens = (ResetPasswordTokens)session.get(ResetPasswordTokens.class, token);

        if(tokens!=null) {
            session.delete(tokens);
            session.getTransaction().commit();

            return tokens.getEmail();
        }

        else {
            session.close();
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
        session.beginTransaction();
        ConfirmationTokens tokens = (ConfirmationTokens)session.get(ConfirmationTokens.class, token);
        session.getTransaction().commit();
        return tokens;

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
}