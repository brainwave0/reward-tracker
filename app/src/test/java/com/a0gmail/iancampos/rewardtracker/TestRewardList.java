package com.a0gmail.iancampos.rewardtracker;

import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRewardList {
    private RewardList rewardList;

    @Before
    public void initRewardList() {
        rewardList = new RewardList(new MainActivity());
    }

    @Test
    public void testClass() {
        rewardList.addReward("calories", 281.1);
        rewardList.addReward("minutes", 360);
        rewardList.addReward("songs", 1.993);
        rewardList.addReward("Naruto", 0.1429);
        rewardList.addReward("a", 0.2971);
        rewardList.addReward("b", 0.1315);
        rewardList.addReward("c", 0.06575);
        addPoints(2);
        Assert.assertEquals("points don' add up", 2, rewardList.points, 0);
        rewardList.waitOneDay();
        rewardList.spend(281.1, "calories");
        Assert.assertEquals("points don' add up", 1.714329314, rewardList.points, 0.25);
        addPoints(4);
        rewardList.waitOneDay();
        rewardList.spend(360, "minutes");
        Assert.assertEquals("points don' add up", 5.285757954, rewardList.points, 0.5);
        addPoints(8);
        rewardList.waitOneDay();
        rewardList.spend(1.993, "songs");
        Assert.assertEquals("points don' add up", 12.6191017778, rewardList.points, 1);
        addPoints(4);
        rewardList.waitOneDay();
        rewardList.spend(0.1429, "Naruto");
        Assert.assertEquals("points don' add up", 15.9760517778, rewardList.points, 1);
        addPoints(6);
        rewardList.waitOneDay();
        rewardList.spend(0.2971, "a");
        Assert.assertEquals("points don' add up", 21.29, rewardList.points, 1);
    }

    private void addPoints(int amt) {
        for (int i = 0; i < amt; i++) {
            rewardList.addPoint();
        }
    }
}