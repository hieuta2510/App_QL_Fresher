package edu.ptit.qlfresher.fragment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FragmentChangePassTest {
    private FragmentChangePass fragmentChangePass;

    @Before
    public void setUp() throws Exception {
        fragmentChangePass = new FragmentChangePass();
    }

    @Test
    public void TestValidPass() {
        String trueOldPass = "123456", inputOldPass = "123456", newPass = "12345", confirmPass = "12345";
        assertTrue(fragmentChangePass.validatePass(trueOldPass, inputOldPass, newPass, confirmPass));
    }

    @Test
    public void TestInvalidOldPass() {
        String trueOldPass = "12345", inputOldPass = "123456", newPass = "12345", confirmPass = "12345";
        assertFalse(fragmentChangePass.validatePass(trueOldPass, inputOldPass, newPass, confirmPass));
    }

    @Test
    public void TestInvalidNewPass() {
        String trueOldPass = "123456", inputOldPass = "123456", newPass = "12345", confirmPass = "1234";
        assertFalse(fragmentChangePass.validatePass(trueOldPass, inputOldPass, newPass, confirmPass));
    }


}