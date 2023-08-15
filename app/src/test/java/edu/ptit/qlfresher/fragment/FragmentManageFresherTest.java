package edu.ptit.qlfresher.fragment;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FragmentManageFresherTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testGetFresherByName() {
        String key = "hieu";
        String name = "Ta Minh Hieu";
        assertTrue(name.toLowerCase().contains(key.toLowerCase()));
    }
}