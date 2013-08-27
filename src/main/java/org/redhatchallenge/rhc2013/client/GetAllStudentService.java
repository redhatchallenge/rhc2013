package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jun
 * Date: 30/8/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("GetAllStudentService")
public interface GetAllStudentService extends RemoteService{

    List<Student> getListOfStudents() throws IllegalArgumentException;

    public static class Util {
        private static final GetAllStudentServiceAsync Instance = (GetAllStudentServiceAsync) GWT.create(GetAllStudentService.class);

        public static GetAllStudentServiceAsync getInstance(){
            return Instance;
        }
    }
}
