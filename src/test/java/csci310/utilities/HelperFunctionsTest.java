package csci310.utilities;

import org.junit.Test;

import java.io.IOException;
import java.security.SecureRandom;

import static org.junit.Assert.*;

public class HelperFunctionsTest {

    @Test
    public void testGet_success() {
        new HelperFunctions();
        String res = null;
        try {
            res = HelperFunctions.get("https://www.google.com");
        } catch (IOException e) {}
        assertNotNull(res);
    }

    @Test
    public void testGet_fail() {
        String res = null;
        try {
            res = HelperFunctions.get("https://www.google2.com");
        } catch (IOException e) {}
        assertNull(res);
    }

    @Test
    public void testShared() {
        assertTrue(HelperFunctions.shared() != null);
    }

    @Test
    public void testGetSalt() {
        String salt = HelperFunctions.getSalt();
        assertNotEquals(0,salt.length());
    }

    @Test
    public void testGetSecurePassword() {
        String psw = "abcdefgh";
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
        String hashedPsw = HelperFunctions.getSecurePassword(psw,salt,"SHA-512");
        assertTrue(hashedPsw.length() > 50);
        assertTrue(hashedPsw != psw);
    }

    @Test
    public void testGetSecurePassword_throwsException() {
        assertNull(HelperFunctions.getSecurePassword("psw","salt","no"));
    }

}