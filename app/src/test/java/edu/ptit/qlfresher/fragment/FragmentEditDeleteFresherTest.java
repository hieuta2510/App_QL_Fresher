package edu.ptit.qlfresher.fragment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FragmentEditDeleteFresherTest {
    private FragmentEditDeleteFresher fragmentEditDeleteFresher;
    @Before
    public void setUp() throws Exception {
        fragmentEditDeleteFresher = new FragmentEditDeleteFresher();
    }

    @Test
    public void testCorrectScoreFresher() {
        String score1 = "6", score2 = "7", score3 = "7";
        String result = fragmentEditDeleteFresher.scoreFresher(score1, score2, score3);
        String expectRes = "6.67";
        assertTrue(expectRes.equals(result));
    }

    @Test
    public void testCorrectScoreFresher2() {
        String score1 = "6", score2 = "7", score3 = "";
        String result = fragmentEditDeleteFresher.scoreFresher(score1, score2, score3);
        String expectRes = "6.50";
        assertTrue(expectRes.equals(result));
    }



    @Test
    public void testWrongScoreFresher() {
        String score1 = "6", score2 = "7", score3 = "7";
        String result = fragmentEditDeleteFresher.scoreFresher(score1, score2, score3);
        String expectRes = "6.66";
        assertFalse(expectRes.equals(result));
    }


}