package com.a0gmail.iancampos.rewardtracker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.joda.time.DateTimeUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testApplication() {
        addReward("calories", 281.1f);
        addReward("minutes", 360);
        addReward("songs", 1.993f);
        addReward("Naruto", 0.1429f);
        addReward("a", 0.2971f);
        addReward("b", 0.1315f);
        addReward("c", 0.06575f);
        addPoints(2);
        spend("calories", 281.1f);
        waitOneDay();
        addPoints(4);
        spend("minutes", 360f);
        waitOneDay();
        addPoints(8);
        spend("songs", 1.993f);
        waitOneDay();
        addPoints(4);
        spend("Naruto", 0.1429f);
        waitOneDay();
        addPoints(6);
        spend("a", 0.2971f);
        assertPtsApproxEquals(21.3027532297f);
    }

    private void assertPtsApproxEquals(float amt) {
        onView(withId(R.id.pointsText)).check(matches(withText("Points: " + String.valueOf(amt))));
    }

    private void waitOneDay() {
        DateTimeUtils.setCurrentMillisOffset(86400000);
    }

    private void spend(String rewardName, float amt) {
        selectReward(rewardName);
        setField(R.id.spendField, String.valueOf(amt));
        clickButton(R.id.spendButton);
    }

    private void clickButton(int id) {
        onView(withId(id)).perform(click());
    }

    private void setField(int id, String amt) {
        onView(withId(id)).perform(typeText(String.valueOf(amt)));
    }

    private void selectReward(String name) {
        onView(withId(R.id.rewardSpinner)).perform(click());
        onData(hasToString(startsWith(name))).perform(click());
    }

    private void addPoints(int amt) {
        for (int i = 0; i < amt - 1; i++) {
            clickButton(R.id.addPointButton);
        }
    }

    private void addReward(String name, float limit) {
        setField(R.id.rewardNameField, name);
        setField(R.id.dailyLimitField, String.valueOf(limit));
        clickButton(R.id.newRewardButton);
    }
}