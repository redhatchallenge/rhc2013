package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.shiro.SecurityUtils;
import org.redhatchallenge.rhc2013.client.ProfileService;
import org.redhatchallenge.rhc2013.shared.Student;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {

    @Override
    public Student getProfileData() throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            String contestant_id = SecurityUtils.getSubject().getPrincipal().toString();


            session.beginTransaction();

            return (Student)session.get(Student.class, Integer.parseInt(contestant_id));
        } catch (HibernateException e) {
            log("Failed to retrieve profile information from the database", e);
            throw new RuntimeException("Failed to retrieve profile information from the database");
        } finally {
            session.close();
        }
    }

    @Override
    public Boolean updateProfileData(String email, String firstName,
                                     String lastName, String contact, String country, String countryCode,
                                     String school, String lecturerFirstName, String lecturerLastName,
                                     String lecturerEmail, String language) throws IllegalArgumentException {

        email = SecurityUtil.escapeInput(email.toLowerCase());
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

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            String contestant_id = SecurityUtils.getSubject().getPrincipal().toString();

            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(contestant_id));

            student.setEmail(email);
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

            session.update(student);
            session.getTransaction().commit();

            return true;
        } catch (HibernateException e) {
            log("Profile update failed", e);
            session.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Boolean changePassword(String oldPassword, String newPassword) throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try{
            String contestant_id = SecurityUtils.getSubject().getPrincipal().toString();

            session.beginTransaction();
            Student student = (Student)session.get(Student.class, Integer.parseInt(contestant_id));

            if (!newPassword.isEmpty()){
                if(BCrypt.checkpw(oldPassword, student.getPassword())){
                    student.setPassword(SecurityUtil.hashPassword(newPassword));
                }

                else{
                    session.getTransaction().rollback();
                    return false;
                }
            }

            session.update(student);
            session.getTransaction().commit();

            return true;
        }
        catch(HibernateException e){
            log("Change password failed", e);
            session.getTransaction().rollback();
            return false;
        }


    }


}