package com.miso.menu;

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
import com.miso.menu.options.OptionsActivityLoaderCallbackImpl;
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

    private SeekBarImpl healthSeekBar;
    private SeekBarImpl ammoSeekBar;
    private SeekBarImpl speedSeekBar;

    @BindView(R.id.first_button_type_spinner)
    Spinner firstButtonTypeSpinner;
    private String firstButtonType;
    @BindView(R.id.second_button_type_spinner)
    Spinner secondButtonTypeSpinner;
    private String secondButtonType;
    @BindView(R.id.player_type_spinner)
    Spinner playerTypeSpinner;
    @BindView(R.id.player_kills_text_view)
    TextView playerKillsTextView;
    private String playerType;

    private SharedPreferences settings;
    private AdView mAdView;
    private final int PLAYER_STATS_LIST_ID = 1111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_options_layout);
        ButterKnife.bind(this);
        this.initialize();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initializeFirstButtonSpinner();
        initializeSecondButtonSpinner();
        initializePlayerSpinner();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getLoaderManager().initLoader(PLAYER_STATS_LIST_ID, null, this);
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

    private void initializePlayerSpinner() {
        List<String> playerTypeList = new ArrayList<>();
        for (GamePlayerTypeEnum playerTypeString : GamePlayerTypeEnum.values()) {
            playerTypeList.add(playerTypeString.getTypeString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playerTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.playerTypeSpinner.setAdapter(adapter);

        if (!this.playerType.equals(null) | this.playerType.equals("")) {
            int spinnerPosition = adapter.getPosition(this.playerType);
            this.playerTypeSpinner.setSelection(spinnerPosition);
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

    private void initialize() {
        this.settings = getPreferences(0);

        this.healthSeekBar = new SeekBarImpl((SeekBar) findViewById(R.id.health_seekBar), 5);
        this.healthSeekBar.setCurrentValue(settings.getInt(OptionStrings.playerBonusHealth, 0));

        this.ammoSeekBar = new SeekBarImpl((SeekBar) findViewById(R.id.ammo_seekBar), 5);
        this.ammoSeekBar.setCurrentValue(settings.getInt(OptionStrings.playerBonusAmmo, 0));

        this.speedSeekBar = new SeekBarImpl((SeekBar) findViewById(R.id.speed_seekBar), 5);
        this.speedSeekBar.setCurrentValue(settings.getInt(OptionStrings.playerMaxSpeed, 0));

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
        editor.putString(OptionStrings.playerType, String.valueOf(this.playerTypeSpinner.getSelectedItem()));

        // Commit the edits!
        editor.commit();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();

        playerKillsTextView.setVisibility(View.VISIBLE);
        playerKillsTextView.setText("Your kill-count: " + data.getString(0));
    }

    private class SeekBarImpl {

        SeekBar thisSeekBar;

        SeekBarImpl(SeekBar seekBar, int maxValue) {
            this.thisSeekBar = seekBar;
            this.thisSeekBar.setMax(maxValue);

            this.thisSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setCurrentValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }

        public int getCurrentValue() {
            return this.thisSeekBar.getProgress();
        }

        public void setCurrentValue(int progress) {
            this.thisSeekBar.setProgress(progress);
        }
    }
}

