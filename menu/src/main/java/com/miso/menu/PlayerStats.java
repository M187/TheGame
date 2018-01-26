package com.miso.menu;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
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
import com.miso.abilities.AbilitiesShop;
import com.miso.menu.options.MySeekBar;
import com.miso.persistence.player.PlayerStatsContract;
import com.miso.persistence.player.StatsActivityLoaderCallbackImpl;
import com.miso.menu.options.PlayerLevelCalculator;
import com.miso.thegame.GameData.ButtonTypeEnum;
import com.miso.thegame.GameData.OptionStrings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by michal.hornak on 14.01.2016.
 *
 * In case of renaming file, you need to change NewLevelActivity, line 61
 */
public class PlayerStats extends Activity {

    private MySeekBar healthSeekBar;
    private MySeekBar ammoSeekBar;
    private MySeekBar speedSeekBar;
    private String firstButtonType;
    private String secondButtonType;
    private int killCount = 0;
    private List<String> availableAbilities;

    @BindView(R.id.first_button_type_spinner)
    Spinner firstButtonTypeSpinner;
    @BindView(R.id.second_button_type_spinner)
    Spinner secondButtonTypeSpinner;
    @BindView(R.id.player_kills_text_view)
    TextView playerKillsTextView;
    @BindView(R.id.player_level_points_text_view)
    TextView playerLevelPointsTextView;
    @BindView(R.id.player_skill_points_spent)
    TextView playerSkillPointsSpentTextView;
    @BindView(R.id.player_skill_points_remaining)
    TextView playerSkillPointsRemainingTextView;

    private SharedPreferences settings;
    private AdView mAdView;
    private final int PLAYER_STATS_LIST_ID = 1;
    private final int PLAYER_STATS_ABILITY_ID = 2;
    private ProgressDialog dialog;
    private boolean isStatsDataReady = false;
    private boolean isAbilityDataReady = false;

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
        this.settings = getPreferences(0);

        getLoaderManager().initLoader(PLAYER_STATS_LIST_ID, null, new PlayerStatsLoader());
        getLoaderManager().initLoader(PLAYER_STATS_ABILITY_ID, null, new PlayerAbilityLoader());

        this.dialog = new ProgressDialog(PlayerStats.this);
        dialog.setMessage("Waiting to fetch data");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(PLAYER_STATS_ABILITY_ID, null, new PlayerAbilityLoader());
    }

    //<editor-fold desc="Spinner stuff">
    private void initializeFirstButtonSpinner(List<String> abilities) {
        this.firstButtonType = settings.getString(OptionStrings.firstButtonType, "");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, abilities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.firstButtonTypeSpinner.setAdapter(adapter);

        if (!this.firstButtonType.equals(null) | this.firstButtonType.equals("")) {
            int spinnerPosition = adapter.getPosition(this.firstButtonType);
            this.firstButtonTypeSpinner.setSelection(spinnerPosition);
        }
    }

    private void initializeSecondButtonSpinner(List<String> abilities) {
        this.secondButtonType = settings.getString(OptionStrings.secondButtonType, "");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, abilities);
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

    //<editor-fold desc="Stats stuff">
    private void initializeSeekbars() {
        mPlayerLevelCalculator.initialize(this.settings);

        this.healthSeekBar = new MySeekBar(this, (SeekBar) findViewById(R.id.health_seekBar), 5, MySeekBar.SEEK_BAR_TYPE.health);
        this.healthSeekBar.init(settings.getInt(OptionStrings.playerBonusHealth, 0));

        this.ammoSeekBar = new MySeekBar(this, (SeekBar) findViewById(R.id.ammo_seekBar), 5, MySeekBar.SEEK_BAR_TYPE.ammo);
        this.ammoSeekBar.init(settings.getInt(OptionStrings.playerBonusAmmo, 0));

        this.speedSeekBar = new MySeekBar(this, (SeekBar) findViewById(R.id.speed_seekBar), 5, MySeekBar.SEEK_BAR_TYPE.speed);
        this.speedSeekBar.init(settings.getInt(OptionStrings.playerMaxSpeed, 0));
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

    public void refreshDueToHealth(int value) {
        mPlayerLevelCalculator.healthLevel = value;
        setNewStatsData();
    }

    public void refreshDueToAmmo(int value) {
        mPlayerLevelCalculator.ammoLevel = value;
        setNewStatsData();
    }

    public void refreshDueToSpeed(int value) {
        mPlayerLevelCalculator.speedLevel = value;
        setNewStatsData();
    }

    public void setNewStatsData() {
        this.playerSkillPointsRemainingTextView.setText(String.valueOf(mPlayerLevelCalculator.getAvailableStatPoints() - mPlayerLevelCalculator.getDistributedStatPoints()));
        this.playerSkillPointsSpentTextView.setText(String.valueOf(mPlayerLevelCalculator.getDistributedStatPoints()));
    }
    //</editor-fold>

    public void startAbilityUnlocker(View view) {
        Intent temp = new Intent(this, AbilitiesShop.class);
        temp.putExtra("kills", killCount);
        startActivity(temp);
    }

    public void hideDialog() {
        if (isStatsDataReady && isAbilityDataReady) dialog.hide();
    }

    private class PlayerStatsLoader implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] settingsProjection = {
                PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_KILLS,
                PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_COMPLETED,
                PlayerStatsContract.PlayerStatisticssEntry.COLUMN_PLAYER_LEVELS_POINTS
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            isStatsDataReady = false;
            CursorLoader loader = new CursorLoader(
                    getBaseContext(),
                    PlayerStatsContract.PlayerAbilitiesEntry.CONTENT_URI.buildUpon().appendPath("statistics").build(),
                    settingsProjection,
                    null,
                    null,
                    null);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            data.moveToFirst();
            killCount = Integer.parseInt(data.getString(0));
            playerKillsTextView.setVisibility(View.VISIBLE);
            playerKillsTextView.setText(getResources().getString(R.string.options_kill_count) + data.getString(0));
            playerLevelPointsTextView.setVisibility(View.VISIBLE);
            playerLevelPointsTextView.setText(getResources().getString(R.string.options_level_points) + data.getString(2));

            mPlayerLevelCalculator.setPlayerStatPoints(Integer.parseInt(data.getString(2)));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initializeSeekbars();
                }
            });

            isStatsDataReady = true;
            hideDialog();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    }

    private class PlayerAbilityLoader implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] settingsProjection = {
                PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_NAME,
                PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_DESCRIPTION,
                PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_PRICE,
                PlayerStatsContract.PlayerAbilitiesEntry.COLUMN_ABILITY_UNLOCKED
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            isAbilityDataReady = false;
            CursorLoader loader = new CursorLoader(
                    getBaseContext(),
                    PlayerStatsContract.PlayerAbilitiesEntry.CONTENT_URI.buildUpon()
                            .appendPath("abilities")
                            .appendPath("unlocked")
                            .build(),
                    settingsProjection,
                    null,
                    null,
                    null);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            availableAbilities = new ArrayList<>();

            if (data.getCount() > 0)
                while (data.moveToNext()) {
                    availableAbilities.add(data.getString(0));
                }
            else {
                availableAbilities.add("No ability available!");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initializeFirstButtonSpinner(availableAbilities);
                    initializeSecondButtonSpinner(availableAbilities);
                }
            });
            isAbilityDataReady = true;
            hideDialog();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    }
}

