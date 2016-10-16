package com.a0gmail.iancampos.rewardtracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.joda.time.Seconds.secondsBetween;


class RewardList {
    private HashMap<String, Reward> rewardsMap = new HashMap<>();
    ArrayList<Reward> rewardsList = new ArrayList<>();
    private double cumulativePointsEarned = 0.0;
    double points = 0.0;
    private Context context;
    private DateTime startDateTime;

    public RewardList(Context newContext) {
        context = newContext;
        startDateTime = DateTime.now();
    }

    public double price(String rewardName) {
        try {
            return avgPointsPerDay() / numRewards() / getReward(rewardName).getDailyLimit();
        }
        catch (NullPointerException e) {
            return 0;
        }
    }

    public Reward getReward(String rewardName) {
        return rewardsMap.get(rewardName);
    }

    public int numRewards() {
        return rewardsList.size();
    }

    public void spend(double amt, String rewardName) {
        points -= price(rewardName) * amt;
    }

    public void addReward(String name, double dailyLimit) {
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

    public double avgPointsPerDay() {
        double daysSinceStart = secondsBetween(startDateTime, DateTime.now()).getSeconds() / 24 / Math.pow(60, 2);
        if (daysSinceStart == 0) {
            return 0;
        }
        return cumulativePointsEarned / daysSinceStart;
    }

    public void save() {
        SharedPreferences sharePrefs = context.getSharedPreferences("rewardListSaveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharePrefEdit = sharePrefs.edit();

        sharePrefEdit.putLong("cumulativePointsEarned", Double.doubleToLongBits(cumulativePointsEarned));
        sharePrefEdit.putLong("points", Double.doubleToLongBits(points));
        sharePrefEdit.putLong("startDateTime", startDateTime.getMillis());

        String[] setValues = new String[rewardsList.size()];
        for (int i = 0; i < rewardsList.size(); i++) {
            setValues[i] = rewardsList.get(i).name;
        }
        Set<String> stringSet = new HashSet<>(Arrays.asList(setValues));
        sharePrefEdit.putStringSet("rewards", stringSet);

        for (Reward reward : rewardsList) {
            reward.save();
        }

        sharePrefEdit.apply();
    }

    public void load() {
        SharedPreferences sharePrefs = context.getSharedPreferences("rewardListSaveData", Context.MODE_PRIVATE);

        cumulativePointsEarned = Double.longBitsToDouble(sharePrefs.getLong("cumulativePointsEarned", 0));
        points = Double.longBitsToDouble(sharePrefs.getLong("points", 0));
        startDateTime = new DateTime(sharePrefs.getLong("startDateTime", DateTime.now().getMillis()));
        for (String rewardName : sharePrefs.getStringSet("rewards", new HashSet<String>())) {
            Reward newReward = new Reward(context, rewardName, 0);
            newReward.load();
            rewardsList.add(newReward);
            rewardsMap.put(newReward.name, newReward);
        }
    }

    public void waitOneDay() {
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis() + 86400000);
    }
}