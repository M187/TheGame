package com.miso.thegame.gameMechanics.multiplayer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.view.SurfaceHolder;

import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.R;
import com.miso.thegame.gameViews.GameView2;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 15.06.2016.
 */
public class ConnectionManager {

    public static final int PORT = 12372;
    public Server localServer = new Server(this.PORT);
    public volatile ArrayList<Client> registeredPlayers = new ArrayList<>();
    public GameSynchronizer gameSynchronizer;

    public ConnectionManager(ArrayList<Client> registeredPlayers) {

        this.registeredPlayers = registeredPlayers;

    }

    public void initializeAllConnectionsToOtherPlayersServers() {

        //this.drawLoadingScreen(surfaceHolder, res);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.localServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.localServer.execute();
        }
        this.gameSynchronizer = new GameSynchronizer(this.registeredPlayers);
    }

    public void drawLoadingScreen(SurfaceHolder holder, Resources res) {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(null);
            synchronized (holder) {
                canvas.drawBitmap( Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(res, R.drawable.waitingforotherplayers),
                        GameView2.WIDTH,
                        GameView2.HEIGHT,
                        false)
                        , 0, 0, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void waitForPlayersToReady() {
        boolean somePlayerNotReady = true;
        while (somePlayerNotReady) {
            somePlayerNotReady = false;
            for (Client player : this.registeredPlayers) {
                somePlayerNotReady = (player.isReadyForGame) ? somePlayerNotReady : true;
            }
        }
    }

}
