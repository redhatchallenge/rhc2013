package org.ayrx.rhchallenge.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.shiro.SecurityUtils;
import org.ayrx.rhchallenge.client.ProfileService;
import org.ayrx.rhchallenge.shared.Student;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {

    @Override
    public Student getProfileData() throws IllegalArgumentException {
        try {
            String email = SecurityUtils.getSubject().getPrincipal().toString();

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria criteria = session.createCriteria(Student.class);
            criteria.add(Restrictions.eq("email", email));
            Student student = (Student)criteria.uniqueResult();
            session.close();

            return student;
        } catch (HibernateException e) {
            log("Failed to retrieve profile information from the database", e);
            throw new RuntimeException("Failed to retrieve profile information from the database");
        }
    }

    @Override
    public Boolean updateProfileData(String email, String oldPassword, String newPassword, String firstName,
                                     String lastName, String contact, String country, String countryCode,
                                     String school, String lecturerFirstName, String lecturerLastName,
                                     String lecturerEmail, String language) throws IllegalArgumentException {

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

        try {
            String principal = SecurityUtils.getSubject().getPrincipal().toString();
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria criteria = session.createCriteria(Student.class);
            criteria.add(Restrictions.eq("email", principal));

            Student student = (Student)criteria.uniqueResult();
            student.setEmail(email);

            if(!newPassword.isEmpty()) {
                if(BCrypt.checkpw(oldPassword, student.getPassword())) {
                    student.setPassword(SecurityUtil.hashPassword(newPassword));
                }

                else {
                    return false;
                }
            }

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
            return false;
        }
    }
}