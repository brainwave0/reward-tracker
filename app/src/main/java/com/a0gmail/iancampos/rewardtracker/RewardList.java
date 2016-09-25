package com.a0gmail.iancampos.rewardtracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


class RewardList {
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

    public void save() {
        SharedPreferences sharePrefs = context.getSharedPreferences("rewardListSaveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharePrefEdit = sharePrefs.edit();

        sharePrefEdit.putFloat("cumulativePointsEarned", cumulativePointsEarned);
        sharePrefEdit.putFloat("points", points);

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

        cumulativePointsEarned = sharePrefs.getInt("cumulativePointsEarned", 0);
        points = sharePrefs.getFloat("points", 0.0f);
        for (String rewardName : sharePrefs.getStringSet("rewards", new HashSet<String>())) {
            Reward newReward = new Reward(context, rewardName, 0);
            newReward.load();
            rewardsList.add(newReward);
            rewardsMap.put(newReward.name, newReward);
        }
    }
}