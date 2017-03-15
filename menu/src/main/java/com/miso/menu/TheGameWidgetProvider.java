package com.miso.menu;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by michal.hornak on 2/8/2017.
 */

public class TheGameWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        ComponentName thisWidget = new ComponentName(context, TheGameWidgetProvider.class);
        for (int widgetId: appWidgetManager.getAppWidgetIds(thisWidget)){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.game_widget_layout);

            remoteViews.setTextViewText(R.id.widget_player_kills, context.getString(R.string.widget_waiting_for_data));

            Intent configIntent = new Intent(context, MenuActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.launch_game_action, configPendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}