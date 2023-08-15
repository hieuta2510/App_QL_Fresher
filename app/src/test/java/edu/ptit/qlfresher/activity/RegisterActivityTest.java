package edu.ptit.qlfresher.activity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RegisterActivityTest {
    private RegisterActivity registerActivity;

    @Before
    public void setUp() {
        registerActivity = new RegisterActivity();
    }

    @Test
    public void testShortPass() {
        assertFalse(registerActivity.validatePassword("123"));
    }

    @Test
    public void testLongPass() {
        assertFalse(registerActivity.validatePassword("1234567890123456"));
    }

    @Test
    public void testEmptyPass() {
        assertFalse(registerActivity.validatePassword(""));
    }

    @Test
    public void testValidPass() {
        assertTrue(registerActivity.validatePassword("validPass"));
    }

    @Test
    public void testInvalidUsername(){
        assertFalse(registerActivity.validateUsername("btaminhaaa"));
    }

    @Test
    public void testValidUsername(){
        assertTrue(registerActivity.validateUsername("btaminhhi@gmail.com"));
    }

}