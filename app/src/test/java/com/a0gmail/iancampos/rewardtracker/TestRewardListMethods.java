package com.a0gmail.iancampos.rewardtracker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestRewardListMethods {
    @Mock Reward testReward;
    RewardList rewardListSpy;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void init() {
        rewardListSpy = spy(new RewardList(new MainActivity()));
    }

    @Test
    public void price() throws Exception {
        doReturn(testReward).when(rewardListSpy).getReward("test");

        doReturn(1).when(rewardListSpy).avgPointsPerDay();
        doReturn(2).when(rewardListSpy).numRewards();
        when(testReward.getDailyLimit()).thenReturn(1d);
        Assert.assertEquals("fail", 0.5, rewardListSpy.price("test"), 0.0001);

        doReturn(0).when(rewardListSpy).avgPointsPerDay();
        doReturn(1).when(rewardListSpy).numRewards();
        when(testReward.getDailyLimit()).thenReturn(1d);
        Assert.assertEquals("fail", 0, rewardListSpy.price("test"), 0.0001);

        doReturn(0.5).when(rewardListSpy).avgPointsPerDay();
        doReturn(2).when(rewardListSpy).numRewards();
        when(testReward.getDailyLimit()).thenReturn(1.5);
        Assert.assertEquals("fail", 0.1667, rewardListSpy.price("test"), 0.0001);
    }

    @Test
    public void spend() throws Exception {
        doReturn(1).when(rewardListSpy).price("test");
        rewardListSpy.spend(1.5, "test");
        assertEquals("fail", -1.5, rewardListSpy.points, 0);

        doReturn(0.5).when(rewardListSpy).price("test");
        rewardListSpy.spend(1, "test");
        assertEquals("fail", -2, rewardListSpy.points, 0);

        doReturn(1.5).when(rewardListSpy).price("test");
        rewardListSpy.spend(1, "test");
        assertEquals("fail", -3.5, rewardListSpy.points, 0);
    }
}