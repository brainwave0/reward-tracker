package com.a0gmail.iancampos.rewardtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RewardList rewardList = new RewardList(this);
    private TextView priceText;
    private TextView costText;
    private TextView pointsText;
    private EditText spendingAmountInput;
    private EditText rewardNameInput;
    private EditText dailyLimitInput;
    private Spinner rewardSpinner;
    private ArrayAdapter<Reward> rewardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointsText = (TextView) findViewById(R.id.pointsText);
        costText = (TextView) findViewById(R.id.costText);
        priceText = (TextView) findViewById(R.id.priceText);
        spendingAmountInput = (EditText) findViewById(R.id.spendField);
        rewardNameInput = (EditText) findViewById(R.id.rewardNameField);
        dailyLimitInput = (EditText) findViewById(R.id.dailyLimitField);
        rewardSpinner = (Spinner) findViewById(R.id.rewardSpinner);

        rewardList.load();

        spendingAmountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                update();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setRewardListAdapter();
        update();
    }

    public void setRewardListAdapter() {
        rewardListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rewardList.rewardsList);
        rewardListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rewardSpinner.setAdapter(rewardListAdapter);
        rewardSpinner.setSelection(0);
        rewardSpinner.setOnItemSelectedListener(this);
    }

    public void addPointButtonClicked(View view) {
        rewardList.addPoint();
        update();
    }


    public void spendButtonClicked(View view) {
        rewardList.spend(Float.parseFloat(spendingAmountInput.getText().toString()), rewardSpinner.getSelectedItem().toString());
        update();
    }

    public void newRewardButtonClicked(View view) {
        rewardList.addReward(rewardNameInput.getText().toString(), Float.parseFloat(dailyLimitInput.getText().toString()));
        rewardNameInput.setText("");
        dailyLimitInput.setText("0");
        rewardListAdapter.notifyDataSetChanged();
    }

    public void deleteRewardButtonClicked(View view) {
        rewardList.removeReward(rewardNameInput.getText().toString());
        rewardNameInput.setText("");
        dailyLimitInput.setText("0");
        rewardListAdapter.notifyDataSetChanged();
    }

    public void onPause() {
        super.onPause();
        rewardList.save();
    }

    public void resetButtonClicked(View view) {
        rewardList = new RewardList(this);
        setRewardListAdapter();
        update();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        update();
   }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void update() {
        pointsText.setText("Points: " + rewardList.points);

        Reward reward = (Reward) rewardSpinner.getSelectedItem();
        if (reward == null) {
            rewardNameInput.setText("");
            dailyLimitInput.setText("0");
            priceText.setText("0");
        }
        else {
            rewardNameInput.setText(reward.name);
            dailyLimitInput.setText(String.valueOf(reward.dailyLimit));
            priceText.setText(String.valueOf(rewardList.price(reward.name)));
        }

        rewardListAdapter.notifyDataSetChanged();

        try {
            costText.setText("for " + Float.parseFloat(spendingAmountInput.getText().toString()) * rewardList.price(rewardSpinner.getSelectedItem().toString()) + " points.");
        }
        catch (NullPointerException e) {
            costText.setText("for 0 points.");
        }
        catch (NumberFormatException e) {

        }
    }
}
