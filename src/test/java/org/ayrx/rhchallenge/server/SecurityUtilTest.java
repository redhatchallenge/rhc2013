package org.ayrx.rhchallenge.server;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static junit.framework.Assert.*;


/**
 * @author: Terry Chia (Ayrx)
 */
public class SecurityUtilTest {

    @Test
    public void escapeInputTest() {
        String test_param_1 = "<Hello World! & Goodbye World! >";
        String expected_output_1 = "&lt;Hello World! &amp; Goodbye World! &gt;";

        String test_param_2 = null;
        String expected_output_2 = null;

        assertEquals(expected_output_1, SecurityUtil.escapeInput(test_param_1));
        assertEquals(expected_output_2, SecurityUtil.escapeInput(test_param_2));
    }

    @Test
    public void hashPasswordTest() {
        String test_param = "correcthorsebatterystaple";
        String hashedPassword = SecurityUtil.hashPassword(test_param);
        assertTrue(BCrypt.checkpw(test_param,hashedPassword));
    }
}
