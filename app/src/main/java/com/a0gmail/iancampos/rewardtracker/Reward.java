package com.a0gmail.iancampos.rewardtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class Reward {
    public double dailyLimit = 0.0;
    public String name = "undefined";
    Context context;

    public Reward(Context newContext, String newName, double newDailyLimit) {
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

        sharePrefEdit.putLong("dailyLimit", Double.doubleToLongBits(dailyLimit));

        sharePrefEdit.apply();
    }

    public void load() {
        SharedPreferences sharePrefs = context.getSharedPreferences(name + "RewardSaveData", Context.MODE_PRIVATE);
        dailyLimit = Double.longBitsToDouble(sharePrefs.getLong("dailyLimit", 0));
    }

    public double getDailyLimit() {
        return dailyLimit;
    }
}
