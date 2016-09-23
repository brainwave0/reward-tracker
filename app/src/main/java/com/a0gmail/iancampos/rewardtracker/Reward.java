package com.a0gmail.iancampos.rewardtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class Reward {
    public float dailyLimit = 0.0f;
    public String name = "undefined";
    Context context;

    public Reward(Context newContext, String newName, float newDailyLimit) {
        name = newName;
        dailyLimit = newDailyLimit;
        context = newContext;
    }

    public String toString() {
        return name;
    }

    public void save() {
        SharedPreferences sharePrefs = context.getSharedPreferences(name + "RewardSaveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharePrefEdit = sharePrefs.edit();

        sharePrefEdit.putFloat("dailyLimit", dailyLimit);

        sharePrefEdit.apply();
    }

    public void load() {
        SharedPreferences sharePrefs = context.getSharedPreferences(name + "RewardSaveData", Context.MODE_PRIVATE);
        dailyLimit = sharePrefs.getFloat("dailyLimit", 0.0f);
    }
}
