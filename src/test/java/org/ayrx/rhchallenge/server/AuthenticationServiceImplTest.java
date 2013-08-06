package org.ayrx.rhchallenge.server;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.ayrx.rhchallenge.shared.ConfirmationTokens;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.jdbc2.optional.SimpleDataSource;

import java.lang.reflect.Method;
import java.util.Set;

import static junit.framework.Assert.*;


/**
 * @author: Terry Chia (Ayrx)
 */

public class AuthenticationServiceImplTest {

    private AuthenticationServiceImpl authenticationService;
    private Method m;

    @Before
    public void setUp() throws Exception {
        authenticationService = new AuthenticationServiceImpl();
        m = authenticationService.getClass().getDeclaredMethod("randomQuestions");
        m.setAccessible(true);

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
    public void testRandomQuestions() throws Exception {
        Set<Integer> result = (Set<Integer>)m.invoke(authenticationService);
        assert result.size() == 150;
    }

    @Test
    public void testLookupEmailFromToken() {
        assertEquals("zx@zx.com", authenticationService.lookupEmailFromToken("asdf"));
        assertNull(authenticationService.lookupEmailFromToken("qwqw"));
    }

    @Test
    public void testRegisterStudent() {
        assertTrue(authenticationService.registerStudent("terrycwk1994@gmail.com", "password", "Terry", "Chia",
                "86112688", "Singapore", "65", "Temasek Polytechnic", "James", "Bond", "jbond@tp.edu.sg", "English"));
        assertFalse(authenticationService.registerStudent("terrycwk1994@gmail.com", "password", "Terry", "Chia",
                "12345678", "Singapore", "65", "Temasek Polytechnic", "James", "Bond", "jbond@tp.edu.sg", "English"));
        assertFalse(authenticationService.registerStudent("asdf@gmail.com", "password", "Terry", "Chia",
                "86112688", "Singapore", "65", "Temasek Polytechnic", "James", "Bond", "jbond@tp.edu.sg", "English"));
    }

    @Test
    public void testAuthenticateStudent() {
        assertFalse(authenticationService.authenticateStudent("zx@zx.com", "asdf", false));
        assertFalse(authenticationService.isAuthenticated());

        assertFalse(authenticationService.authenticateStudent("popop@popp.com", "asdf", false));
        assertFalse(authenticationService.isAuthenticated());

        assertTrue(authenticationService.authenticateStudent("zx@zx.com", "password", false));
        assertTrue(authenticationService.isAuthenticated());
        SecurityUtils.getSubject().logout();
    }

    @Test
    public void testSetConfirmationStatus() {
        assertFalse(authenticationService.setConfirmationStatus("zxzxzx"));
        assertTrue(authenticationService.setConfirmationStatus("qwerty"));
    }

    @Test
    public void testTriggerResetPassword() {
        assertFalse(authenticationService.triggerResetPassword("zx@zx.com", "98956649"));
        assertTrue(authenticationService.triggerResetPassword("zx@zx.com", "95681265"));
    }
}
