package edu.ptit.qlfresher.fragment;

import static org.junit.Assert.*;

import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.ptit.qlfresher.model.Center;

public class FragmentAddFresherTest {
    private FragmentAddFresher addFresher;
    private String format = "";
    @Before
    public void setUp() throws Exception {
        addFresher = new FragmentAddFresher();
        format  = "dd/MM/yyyy";
    }

    @Test
    public void testExistFresher() {
        List<String> mList = new ArrayList<>();
        mList.add("taminhhieu251@gmail.com");
        mList.add("btaminhhieu@gmail.com");
        mList.add("annv@gmail.com");
        mList.add("manhnt@gmail.com");

        String email = "taminhhieu251@gmail.com";
        boolean check = false;
        for (String i : mList) {
            if ((i.equals(email)))
            {
                check = true;
                break;
            }
        }
        assertTrue(check);
    }

    @Test
    public void testDonExistFresher() {
        List<String> mList = new ArrayList<>();
        mList.add("taminhhieu251@gmail.com");
        mList.add("btaminhhieu@gmail.com");
        mList.add("annv@gmail.com");
        mList.add("manhnt@gmail.com");

        String email = "fakeemail@gmail.com";
        boolean check = false;
        for (String i : mList) {
            if ((i.equals(email)))
            {
                check = true;
                break;
            }
        }
        assertFalse(check);
    }

    @Test
    public void testValidDoB()
    {
        String dob = "12/08/2022";
        assertTrue(addFresher.validateDoB(dob,format));
    }

    @Test
    public void testInvalidDoB()
    {
        String dob1 = "1/08/2022";
        String dob2 = "12/8/202";
        String dob3 = "31/13/2022";
        boolean check = true;
        if(!addFresher.validateDoB(dob1, format) && !addFresher.validateDoB(dob2, format) && !addFresher.validateDoB(dob3, format)){
            check = false;
        }
        assertFalse(check);
    }

    @Test
    public void testValidName()
    {
        String name = "Tran Van Quang";
        assertTrue(addFresher.checkValidName(name));
    }

    @Test
    public void testInvalidName()
    {
        String name1 = "Tran Van Quang 12";
        String name2 = "Tran Va%n Qua12ng";
        String name3 = "Tran Van Quang#";

        boolean check = false;
        if (addFresher.checkValidName(name1) && addFresher.checkValidName(name2) && addFresher.checkValidName(name3))
        {
            check = false;
        }
        assertFalse(check);
    }

    @Test
    public void testInvalidUsername(){
        assertFalse(addFresher.validateEmail("btaminhaaa"));
    }

    @Test
    public void testValidUsername(){
        assertTrue(addFresher.validateEmail("btaminhhi@gmail.com"));
    }

    @Test
    public void testValidLanguage()
    {
        boolean check = false;
        String[] list = {"JS", "Java", ".Net", "Python"};
        String language = "Java";
        for(int i = 0;i < list.length;i++)
        {
            if(language.trim().toLowerCase().equals(list[i].toLowerCase()))
            {
                check = true;
                break;
            }
        }
        assertTrue(check);
    }

    @Test
    public void testInvalidLanguage()
    {
        boolean check = false;
        String[] list = {"JS", "Java", ".Net", "Python"};
        String language = "C++";
        for(int i = 0;i < list.length;i++)
        {
            if(language.trim().toLowerCase().equals(list[i].toLowerCase()))
            {
                check = true;
                break;
            }
        }
        assertFalse(check);
    }

     @Test
    public void testValidCenter()
    {
        List<Center> list = new ArrayList<>();
        Center i1 = new Center("PTIT", "name", "add", 0);
        Center i2 = new Center("KMA", "name", "add", 0);
        Center i3 = new Center("HNUE", "name", "add", 0);
        list.add(i1);
        list.add(i2);
        list.add(i3);
        boolean check = false;
        String centerName = "PTIT";
        for (Center i : list){
            if(i.getAcronym().equals(centerName)){
                check = true;
            }
        }

        assertTrue(check);
    }


    @Test
    public void testInvalidCenter()
    {
        List<Center> list = new ArrayList<>();
        Center i1 = new Center("PTIT", "name", "add", 0);
        Center i2 = new Center("KMA", "name", "add", 0);
        Center i3 = new Center("HNUE", "name", "add", 0);
        list.add(i1);
        list.add(i2);
        list.add(i3);
        boolean check = false;
        String centerName = "PTIT";
        for (Center i : list){
            if(i.getAcronym().equals(centerName)){
                check = true;
            }
        }

        assertTrue(check);
    }

}