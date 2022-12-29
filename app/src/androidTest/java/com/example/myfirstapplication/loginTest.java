package com.example.myfirstapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class loginTest {
    @Rule
    public ActivityScenarioRule<login> activityScenarioRule = new ActivityScenarioRule<>(login.class);
    String saver = "root@gmail.com";

    @Test
    public void checkIfInputIsGiven(){
        onView(withId(R.id.et_email)).perform(ViewActions.typeText(saver));
        assertNotNull(R.id.et_email);
    }
    @Test
    public void checkIfInputEqualAdminUsername(){
        onView(withId(R.id.et_email)).perform(ViewActions.typeText(saver));
        assertEquals("root@gmail.com",saver);
    }
    @Test
     public void checkIfInputNotEqualAdminUserName(){
        assertNotEquals("root@gmail.com",saver);
    }
    @Test
    public void checkIfInputIsNotGiven(){
        onView(withId(R.id.et_email)).perform(ViewActions.typeText(saver));
        assertNull(R.id.et_email);
    }
    @Test
    public void checkButton() {
        onView(withId(R.id.et_email)).perform(ViewActions.typeText("root@gmail.com"));
        onView(withId(R.id.et_pass)).perform(ViewActions.typeText("rooter"));
        onView(withId(R.id.btn_login)).perform(click());
        assertNotNull(R.id.et_email);
    }
}