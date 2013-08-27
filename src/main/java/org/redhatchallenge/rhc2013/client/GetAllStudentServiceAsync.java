package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jun
 * Date: 30/8/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GetAllStudentServiceAsync {
    void getListOfStudents(AsyncCallback<List<Student>> async);
}
