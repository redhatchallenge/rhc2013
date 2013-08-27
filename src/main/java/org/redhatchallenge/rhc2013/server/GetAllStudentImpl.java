package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.redhatchallenge.rhc2013.client.GetAllStudentService;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jun
 * Date: 30/8/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetAllStudentImpl extends RemoteServiceServlet implements GetAllStudentService{
    @Override
    public List<Student> getListOfStudents() throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //noinspection unchecked
            List<Student> studentList = session.createCriteria(Student.class).list();
            session.close();
            return studentList;
        } catch (HibernateException e) {
            session.close();
            return null;
        }
    }

}
