package com.miso.menu;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by michal.hornak on 2/20/2017.
 */

public class TheGameApplication extends MultiDexApplication {

    public Tracker mTracker;

    public void startTracking(){
        if (mTracker == null){
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            mTracker = ga.newTracker(R.xml.track_app);
            ga.enableAutoActivityReports(this);
        }
    }

    public Tracker getTracker(){
        startTracking();
        return mTracker;
    }
}
