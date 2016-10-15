package com.a0gmail.iancampos.rewardtracker;

import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.AssertionFailedError;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class TestApplication {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void initialize() {
        clickButton(R.id.resetButton);
    }

    @Test
    public void testApplication() {
        addReward("calories", 281.1);
        addReward("minutes", 360);
        addReward("songs", 1.993);
        addReward("Naruto", 0.1429);
        addReward("a", 0.2971);
        addReward("b", 0.1315);
        addReward("c", 0.06575);
        addPoints(2);
        spend("calories", 281.1);
        waitOneDay();
        addPoints(4);
        spend("minutes", 360);
        waitOneDay();
        addPoints(8);
        spend("songs", 1.993);
        waitOneDay();
        addPoints(4);
        spend("Naruto", 0.1429);
        waitOneDay();
        addPoints(6);
        spend("a", 0.2971);
        assertPtsApproxEquals(21.3027532297); //couldn't figure out how to actually make this method approximate
    }

    private void assertPtsApproxEquals(double amt) {
        onView(withId(R.id.pointsText)).check(matches(withText("Points: " + String.valueOf(amt))));
    }

    private void waitOneDay() {
        mainActivityActivityTestRule.getActivity().rewardList.waitOneDay();
    }

    private void spend(String rewardName, double amt) {
        selectReward(rewardName);
        setField(R.id.spendField, String.valueOf(amt));
        clickButton(R.id.spendButton);
    }

    private void clickButton(int id) {
        onView(withId(id)).perform(click());
    }

    private void setField(int id, String amt) {
        onView(withId(id)).perform(replaceText(String.valueOf(amt)));
    }

    private void selectReward(String name) {
        onView(withId(R.id.rewardSpinner)).perform(click());
        onData(hasToString(startsWith(name))).perform(click());
    }

    private void addPoints(int amt) {
        for (int i = 0; i < amt; i++) {
            clickButton(R.id.addPointButton);
        }
    }

    private void addReward(String name, double limit) {
        setField(R.id.rewardNameField, name);
        setField(R.id.dailyLimitField, String.valueOf(limit));
        clickButton(R.id.newRewardButton);
    }

    @Test
    public void testSpendButton() {
        addReward("test", 1);
        addPoints(1);
        waitOneDay();
        spend("test", 1);
        assertPtsApproxEquals(0);
    }

    @Test
    public void testSpendField() {
        setField(R.id.spendField, "10");
        onView(withId(R.id.costText)).check(matches(withText("for 0 points.")));
        addReward("test", 1);
        addPoints(1);
        waitOneDay();
        setField(R.id.spendField, "1");
        onView(withId(R.id.costText)).check(matches(withText("for 1.0 points.")));
    }

    @Test
    public void testRewardNameField() {
        addReward("test", 1);
        selectReward("test");
        onView(withId(R.id.rewardNameField)).check(matches(withText("test")));
    }

    @Test
    public void testDailyLimitField() {
        addReward("testicle", 1101);
        selectReward("testicle");
        onView(withId(R.id.dailyLimitField)).check(matches(withText("1101.0")));
    }

    @Test
    public void testResetButton() {
        addReward("foo", 10);
        addReward("bar", 11);
        addReward("baz", 0.01);
        addReward("qux", 0.192);
        addPoints(5);
        spend("bar", 100);
        clickButton(R.id.resetButton);
        assertPtsApproxEquals(0);
        onView(withId(R.id.dailyLimitField)).check(matches(withText("0")));
        onView(withId(R.id.rewardNameField)).check(matches(withText("")));
        assertSpinnerNotContains("foo");
    }

    @Test
    public void testNewRewardButton() {
        addReward("aark", 2);
        addReward("ook", 7);
        selectReward("aark");
        selectReward("ook");
    }

    @Test
    public void deleteRewardButton() {
        addReward("bob", 8);
        clickButton(R.id.deleteRewardButton);
        assertSpinnerNotContains("bob");
    }

    private void assertSpinnerNotContains(String rewardName) {
        try {
            selectReward(rewardName);
            onView(withId(R.id.rewardNameField)).check(matches(withText("")));
        }
        catch (PerformException e) {return;}
        catch (AssertionFailedError e) {return;}

        throw new AssertionFailedError();
    }

    @Test
    public void testAddPointButton() {
        addPoints(10);
        assertPtsApproxEquals(10);
    }

    @Test
    public void testPriceText() {
        addReward("test", 100);
        waitOneDay();
        addPoints(1);
        onView(withId(R.id.priceText)).check(matches(withText("0.01")));
    }

    @Test
    public void testCostText() {
        addReward("foo", 8);
        addPoints(1);
        waitOneDay();
        setField(R.id.spendField, "1");
        onView(withId(R.id.costText)).check(matches(withText("for 0.125 points.")));
    }
}