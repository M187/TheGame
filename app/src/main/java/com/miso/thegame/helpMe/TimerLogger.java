package com.miso.thegame.helpMe;

import android.util.Log;

import com.miso.thegame.gameMechanics.ConstantHolder;

/**
 * Created by michal.hornak on 23.06.2016.
 * <p/>
 * Class designed to debug running threads.
 * Periodically logs if thread is running.
 */
public class TimerLogger {

    public static final int PERIOD = 15000;
    public static long periodStart = System.currentTimeMillis();

    public static void doPeriodicLog(String message) {

        if (System.currentTimeMillis() - periodStart > PERIOD) {
            Log.d(ConstantHolder.TAG, " ---> I am running | " + (Thread.currentThread().getStackTrace())[0].toString() + " | " + message);
            periodStart = System.currentTimeMillis();
        }
    }
}
