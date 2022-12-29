package com.example.myfirstapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class bookRoomTest {
    @Rule
    public ActivityScenarioRule<bookRoom> activityScenarioRule= new ActivityScenarioRule<>(bookRoom.class);
    String room = "21";
    String checkInDate="5.9.2022";
    String checkOutDate="5.9.2022";
    String checkOutDate2="5.12.2022";

    // First Log in to the app
    @Test
    public void checkIfNumberIsGiven(){
        onView(withId(R.id.et_roomNumber)).perform(ViewActions.typeText(room));
        assertNotNull(R.id.et_roomNumber);
    }
    @Test
    public void checkIfRoomNumberIsNotGiven(){
        //onView(withId(R.id.et_roomNumber)).perform(ViewActions.typeText(room));
        assertNotNull(R.id.et_roomNumber);
        onView(withId(R.id.btn_book)).perform(click());

    }

    // Tests for when check in date is and is not given
    @Test
    public void checkIfCheckInIsGiven(){
        onView(withId(R.id.et_checkin)).perform(click());
        assertNotNull(R.id.et_checkin);
    }

    @Test
    public void checkIfCheckInIsNotGiven(){
        onView(withId(R.id.et_checkin)).perform(click());
        assertNull(R.id.et_checkin);
    }


    // Tests for when check out date is and is not given
    @Test
    public void checkIfCheckOutIsGiven(){
        onView(withId(R.id.et_checkout)).perform(click());
        assertNotNull(R.id.et_checkout);
    }

    @Test
    public void checkIfCheckOutIsNotGiven(){
        onView(withId(R.id.et_checkout)).perform(click());
        assertNull(R.id.et_checkout);
    }

    // Tests for when check in and checkout date match and don't match
    @Test
    public void checkIfCheckInAndCheckOutMatch(){
        onView(withId(R.id.et_checkin)).perform(ViewActions.typeText(checkInDate));
        onView(withId(R.id.et_checkout)).perform(ViewActions.typeText(checkOutDate));
        assertNotEquals(checkInDate, checkOutDate);
    }
    @Test
    public void checkIfCheckInAndCheckOutDoNotMatch(){
        onView(withId(R.id.et_checkin)).perform(ViewActions.typeText(checkInDate));
        onView(withId(R.id.et_checkout)).perform(ViewActions.typeText(checkOutDate2));
        assertEquals(checkInDate, checkOutDate2);
    }

    // Tests for when user books a room they are and are not a resident of
    @Test
    public void checkIfStudentIsResident(){
        onView(withId(R.id.et_roomNumber)).perform(ViewActions.typeText(room));
        assertEquals("22",room);
    }
    @Test
    public void checkIfStudentIsNotResident(){
        assertNotEquals("22",room);
    }

    // Test whether booking functionality buttons work
    @Test
    public void checkBookButton(){
        onView(withId(R.id.et_roomNumber)).perform(ViewActions.typeText(room));
        onView(withId(R.id.et_checkin)).perform(ViewActions.typeText(checkInDate));
        onView(withId(R.id.et_checkout)).perform(ViewActions.typeText(checkOutDate2));

        onView(withId(R.id.btn_book)).perform(click());
        assertNotNull(R.id.et_roomNumber);
        assertNotNull(R.id.et_checkin);
        assertNotNull(R.id.et_checkout);

    }
}