package com.miso.menu.options;

import android.widget.SeekBar;

import com.miso.menu.PlayerOptions;

/**
 * Created by michal.hornak on 2/28/2017.
 */

public class MySeekBar {

    public enum SEEK_BAR_TYPE {
        health, ammo, speed
    }

    private SeekBar thisSeekBar;
    private SEEK_BAR_TYPE mType;
    private PlayerOptions mPlayerOptions;
    private int lastProgress = 0;

    public MySeekBar(PlayerOptions context, SeekBar seekBar, int maxValue, SEEK_BAR_TYPE type) {
        this.mPlayerOptions = context;
        this.mType = type;
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
        return lastProgress;
    }

    public void setCurrentValue(int progress) {
        if (lastProgress < progress) {
            if (mPlayerOptions.mPlayerLevelCalculator.getAvailableStatPoints() - mPlayerOptions.mPlayerLevelCalculator.getDistributedStatPoints() > 0) {
                lastProgress = progress;
                this.thisSeekBar.setProgress(progress);
                switch (this.mType) {
                    case health:
                        mPlayerOptions.refreshDueToHealth(progress);
                        break;
                    case ammo:
                        mPlayerOptions.refreshDueToAmmo(progress);
                        break;
                    case speed:
                        mPlayerOptions.refreshDueToSpeed(progress);
                        break;
                    default:
                }
            } else {
                this.thisSeekBar.setProgress(lastProgress);
            }
        } else {
            lastProgress = progress;
            this.thisSeekBar.setProgress(progress);
            switch (this.mType) {
                case health:
                    mPlayerOptions.refreshDueToHealth(progress);
                    break;
                case ammo:
                    mPlayerOptions.refreshDueToAmmo(progress);
                    break;
                case speed:
                    mPlayerOptions.refreshDueToSpeed(progress);
                    break;
                default:
            }
        }
    }
}

