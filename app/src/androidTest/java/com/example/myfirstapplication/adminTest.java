package com.example.myfirstapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class adminTest {
    @Rule
    public ActivityScenarioRule<admin> activityScenarioRule = new ActivityScenarioRule<>(admin.class);
    String dName = "Nick";
    String dID = "637736";
    String dEmail = "nickTester@gmail.com";
    String dGender = "Male";
    String dRoomNo = "5";
    String dPass = "monkey";

    @Test
    public void testName(){
        onView(withId(R.id.et_StudentName)).perform(ViewActions.typeText(dName));
       // onView(withId(R.id.btn_CreateUser)).perform(click());
        assertNotNull(R.id.et_StudentName);

    }

    @Test
    public void testID(){
        onView(withId(R.id.et_StudentID)).perform(ViewActions.typeText(dID));
        assertNotNull(R.id.et_StudentID);
        //onView(withId(R.id.btn_CreateUser)).perform(click());
    }

    @Test
    public void testEmail(){
        onView(withId(R.id.et_Email)).perform(ViewActions.typeText(dEmail));
        assertNotNull(R.id.et_Email);
        //onView(withId(R.id.btn_CreateUser)).perform(click());
    }

    @Test
    public void testRoomNo(){
        onView(withId(R.id.et_RoomNumber)).perform(ViewActions.typeText(dRoomNo));
        assertNotNull(R.id.et_RoomNumber);
        // onView(withId(R.id.btn_CreateUser)).perform(click());
    }

    @Test
    public void testPassword(){
        onView(withId(R.id.et_CreatePassword)).perform(ViewActions.typeText(dPass));
        assertNotNull(R.id.et_CreatePassword);
       // onView(withId(R.id.btn_CreateUser)).perform(click());
    }

    @Test
    public void testCreateUser(){
        onView(withId(R.id.et_StudentName)).perform(ViewActions.typeText(dName));
        assertNotNull(R.id.et_StudentName);
        onView(withId(R.id.et_StudentID)).perform(ViewActions.typeText(dID));
        assertNotNull(R.id.et_StudentID);
        onView(withId(R.id.et_Email)).perform(ViewActions.typeText(dEmail));
        assertNotNull(R.id.et_Email);
        onView(withId(R.id.et_Gender)).perform(ViewActions.typeText(dGender));
        assertNotNull(R.id.et_Gender);
        onView(withId(R.id.et_RoomNumber)).perform(ViewActions.typeText(dRoomNo));
        assertNotNull(R.id.et_RoomNumber);
        onView(withId(R.id.et_CreatePassword)).perform(ViewActions.typeText(dPass));
        assertNotNull(R.id.et_CreatePassword);
       //onView(withId(R.id.btn_CreateUser)).perform(click());
    }
}

