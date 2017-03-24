package com.miso.menu.multiplayer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

/**
 * Created by michal.hornak on 3/24/2017.
 */

public class FindMyIp extends AsyncTask<String, Void, String> {

    private Context mContext;

    public FindMyIp(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String[] params) {
        try {
            java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A");
            String myIp = s.next();
            System.out.println("My current IP address is " + myIp);
            return myIp;
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @Override
    protected void onPostExecute(String result){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(result);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
