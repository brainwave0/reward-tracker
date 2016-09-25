package com.a0gmail.iancampos.rewardtracker;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


class RewardList implements Serializable {
    private HashMap<String, Reward> rewardsMap = new HashMap<>();
    ArrayList<Reward> rewardsList = new ArrayList<>();
    private float cumulativePointsEarned = 0.0f;
    float points = 0.0f;
    private Context context;
    private DateTime startDateTime;

    public RewardList(Context newContext) {
        context = newContext;
        startDateTime = DateTime.now();
    }

    public float price(String rewardName) {
        try {
            return avgPointsPerDay() / rewardsList.size() / rewardsMap.get(rewardName).dailyLimit;
        }
        catch (NullPointerException e) {
            return 0;
        }
    }

    public void spend(float amt, String rewardName) {
        points -= price(rewardName) * amt;
    }

    public void addReward(String name, float dailyLimit) {
        Reward newReward = new Reward(context, name, dailyLimit);
        rewardsMap.put(newReward.name, newReward);
        rewardsList.add(newReward);
    }

    public void removeReward(String name) {
        rewardsList.remove(rewardsMap.get(name));
        rewardsMap.remove(name);
    }

    public void addPoint() {
        points++;
        cumulativePointsEarned++;
    }

    private float avgPointsPerDay() {
        int daysSinceStart = Days.daysBetween(startDateTime, DateTime.now()).getDays();
        return cumulativePointsEarned / daysSinceStart;
    }
}