package org.ayrx.rhchallenge.server;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.ayrx.rhchallenge.shared.Student;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.jdbc2.optional.SimpleDataSource;

import java.lang.reflect.Method;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.expect;


/**
 * @author: Terry Chia (Ayrx)
 */

public class ProfileServiceImplTest {

    private ProfileServiceImpl profileService;

    @Before
    public void setUp() throws Exception {
        profileService = new ProfileServiceImpl();

        JdbcRealm realm = new JdbcRealm();
        SimpleDataSource datasource = new SimpleDataSource();
        datasource.setServerName("127.0.0.1");
        datasource.setDatabaseName("postgres");
        datasource.setPortNumber(5432);
        datasource.setUser("rhc2013");
        datasource.setPassword("rhcSQL@2013");

        realm.setDataSource(datasource);
        realm.setCredentialsMatcher(new BcryptCredentialsMatcher());
        realm.setAuthenticationQuery("SELECT password FROM test.contestant WHERE email = ?");

        SecurityManager securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);

    }

    @Test
    public void testGetProfileData() {
        SecurityUtils.getSubject().login(new UsernamePasswordToken("zx@zx.com", "password"));
        Student student = profileService.getProfileData();
        assertEquals("zx@zx.com", student.getEmail());
        assertEquals("zx", student.getFirstName());
        assertEquals("z", student.getLastName());
        assertEquals("95681265", student.getContact());
        assertEquals("Singapore", student.getCountry());
        assertEquals("65", student.getCountryCode());
        assertEquals("TP", student.getSchool());
        assertEquals("James", student.getLecturerFirstName());
        assertEquals("Bond", student.getLecturerLastName());
        assertEquals("jbond@tp.edu.sg", student.getLecturerEmail());
        assertEquals("English", student.getLanguage());
    }
}
