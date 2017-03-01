package com.miso.menu;

import android.app.ProgressDialog;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.miso.menu.options.MySeekBar;
import com.miso.menu.options.OptionsActivityLoaderCallbackImpl;
import com.miso.menu.options.PlayerLevelCalculator;
import com.miso.thegame.GameData.ButtonTypeEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.GameData.OptionStrings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by michal.hornak on 14.01.2016.
 */
public class PlayerOptions extends OptionsActivityLoaderCallbackImpl {

    private MySeekBar healthSeekBar;
    private MySeekBar ammoSeekBar;
    private MySeekBar speedSeekBar;

    @BindView(R.id.first_button_type_spinner)
    Spinner firstButtonTypeSpinner;
    private String firstButtonType;
    @BindView(R.id.second_button_type_spinner)
    Spinner secondButtonTypeSpinner;
    private String secondButtonType;
    @BindView(R.id.player_kills_text_view)
    TextView playerKillsTextView;
    @BindView(R.id.player_level_points_text_view)
    TextView playerLevelPointsTextView;
    @BindView(R.id.player_skill_points_spent)
    TextView playerSkillPointsSpentTextView;
    @BindView(R.id.player_skill_points_remaining)
    TextView playerSkillPointsRemainingTextView;

    private String playerType;
    private SharedPreferences settings;
    private AdView mAdView;
    private final int PLAYER_STATS_LIST_ID = 1111;
    private ProgressDialog dialog;

    public PlayerLevelCalculator mPlayerLevelCalculator = new PlayerLevelCalculator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_options_layout);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getLoaderManager().initLoader(PLAYER_STATS_LIST_ID, null, this);

        this.dialog=new ProgressDialog(PlayerOptions.this);
        dialog.setMessage("message");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    //<editor-fold desc="Spinner stuff">
    private void initializeFirstButtonSpinner() {
        List<String> firstButtonTypeList = new ArrayList<>();
        for (ButtonTypeEnum buttonTypeString : ButtonTypeEnum.values()) {
            firstButtonTypeList.add(buttonTypeString.getButtonTypeString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, firstButtonTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.firstButtonTypeSpinner.setAdapter(adapter);

        if (!this.firstButtonType.equals(null) | this.firstButtonType.equals("")) {
            int spinnerPosition = adapter.getPosition(this.firstButtonType);
            this.firstButtonTypeSpinner.setSelection(spinnerPosition);
        }
    }

    private void initializeSecondButtonSpinner() {
        List<String> secondButtonTypeList = new ArrayList<>();
        for (ButtonTypeEnum buttonTypeString : ButtonTypeEnum.values()) {
            secondButtonTypeList.add(buttonTypeString.getButtonTypeString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, secondButtonTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.secondButtonTypeSpinner.setAdapter(adapter);

        if (!this.secondButtonType.equals(null) | this.secondButtonType.equals("")) {
            int spinnerPosition = adapter.getPosition(this.secondButtonType);
            this.secondButtonTypeSpinner.setSelection(spinnerPosition);
        }
    }
    //</editor-fold>

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 1:
                return true;
            case 2:
                return true;
            default:
                return false;
        }
    }

    private void initializeSeekbars() {
        this.settings = getPreferences(0);

        mPlayerLevelCalculator.initialize(this.settings);

        this.healthSeekBar = new MySeekBar(this, (SeekBar) findViewById(R.id.health_seekBar), 5, MySeekBar.SEEK_BAR_TYPE.health);
        this.healthSeekBar.init(settings.getInt(OptionStrings.playerBonusHealth, 0));

        this.ammoSeekBar = new MySeekBar(this, (SeekBar) findViewById(R.id.ammo_seekBar), 5, MySeekBar.SEEK_BAR_TYPE.ammo);
        this.ammoSeekBar.init(settings.getInt(OptionStrings.playerBonusAmmo, 0));

        this.speedSeekBar = new MySeekBar(this, (SeekBar) findViewById(R.id.speed_seekBar), 5, MySeekBar.SEEK_BAR_TYPE.speed);
        this.speedSeekBar.init(settings.getInt(OptionStrings.playerMaxSpeed, 0));

        this.firstButtonType = settings.getString(OptionStrings.firstButtonType, "");
        this.secondButtonType = settings.getString(OptionStrings.secondButtonType, "");
        this.playerType = settings.getString(OptionStrings.playerType, "");
    }

    private void saveSettings() {
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(OptionStrings.playerBonusHealth, this.healthSeekBar.getCurrentValue());
        editor.putInt(OptionStrings.playerBonusAmmo, this.ammoSeekBar.getCurrentValue());
        editor.putInt(OptionStrings.playerMaxSpeed, this.speedSeekBar.getCurrentValue());

        editor.putString(OptionStrings.firstButtonType, String.valueOf(this.firstButtonTypeSpinner.getSelectedItem()));
        editor.putString(OptionStrings.secondButtonType, String.valueOf(this.secondButtonTypeSpinner.getSelectedItem()));
        // Commit the edits!
        editor.commit();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        this.playerKillsTextView.setVisibility(View.VISIBLE);
        this.playerKillsTextView.setText("Your kill-count: " + data.getString(0));
        this.playerLevelPointsTextView.setVisibility(View.VISIBLE);
        this.playerLevelPointsTextView.setText("Your level-points: " + data.getString(2));

        this.mPlayerLevelCalculator.setPlayerStatPoints(Integer.parseInt(data.getString(2)));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initializeSeekbars();
                initializeFirstButtonSpinner();
                initializeSecondButtonSpinner();
            }
        });

        this.dialog.hide();
    }

    public void refreshDueToHealth(int value){
        mPlayerLevelCalculator.healthLevel = value;
        setNewStatsData();
    }

    public void refreshDueToAmmo(int value){
        mPlayerLevelCalculator.ammoLevel = value;
        setNewStatsData();
    }

    public void refreshDueToSpeed(int value){
        mPlayerLevelCalculator.speedLevel = value;
        setNewStatsData();
    }

    public void setNewStatsData(){
        this.playerSkillPointsRemainingTextView.setText(String.valueOf(mPlayerLevelCalculator.getAvailableStatPoints() - mPlayerLevelCalculator.getDistributedStatPoints()));
        this.playerSkillPointsSpentTextView.setText(String.valueOf(mPlayerLevelCalculator.getDistributedStatPoints()));
    }
}

