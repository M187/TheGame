package com.miso.thegame.gameViews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.Networking.server.GamePlayLogicExecutor;
import com.miso.thegame.Networking.server.Server;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.MainGameThread;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionHandlerMultiplayer;
import com.miso.thegame.gameMechanics.multiplayer.GameSynchronizer;
import com.miso.thegame.gameMechanics.multiplayer.NetworkGameStateUpdater;
import com.miso.thegame.gameMechanics.multiplayer.otherPlayer.OtherPlayerManager;

import java.util.ArrayList;

/**
 * Created by Miso on 8.10.2015.
 *
 * Copy of GamePanel. Reworked to support multiplayer functionality.
 */
public class GamePanelMultiplayer extends GameView2 implements SurfaceHolder.Callback {

    public static final int PORT = 12371;
    protected CollisionHandlerMultiplayer collisionHandler;
    private Server localServer = new Server(this.PORT);
    private volatile ArrayList<Client> registeredPlayers = new ArrayList<>();
    private volatile ArrayList<TransmissionMessage> arrivingMessages = new ArrayList<>();
    private OtherPlayerManager otherPlayersManager = new OtherPlayerManager();
    private NetworkGameStateUpdater networkGameStateUpdater = new NetworkGameStateUpdater(arrivingMessages, this);
    private GameSynchronizer gameSynchronizer;

    public GamePanelMultiplayer(Context context, GameMapEnum mapToCreate, ArrayList<Client> registeredPlayers, String myNickname) {
        super(context);
        Log.d(ConstantHolder.TAG, "Trying to create game panel for multiplayer.");
        this.mapToCreate = GameMapEnum.BlankMap;
        this.myNickname = myNickname;
        this.registeredPlayers = registeredPlayers;
        this.localServer.setMessageLogicExecutor(new GamePlayLogicExecutor(this.arrivingMessages, this.registeredPlayers));
        this.localServer.execute();
        this.gameSynchronizer = new GameSynchronizer(registeredPlayers);
        this.sender = new Sender(this.registeredPlayers);
        this.context = context;
        this.thread = new MainGameThread(getHolder(), this);
        getHolder().addCallback(this);
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.localServer.terminate();
        boolean retry = true;
        int counter = 0;
        while (retry & counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surface) {
        super.surfaceCreated(surface);
        collisionHandler = new CollisionHandlerMultiplayer(getPlayer(), getOtherPlayersManager(), getSpellManager(), this.mapManager, getResources());

        this.sender.sendMessage(new ReadyToPlayMessage(this.myNickname));
        waitForPlayersToReady();
        this.thread.setRunning(true);
        this.thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //System.out.println(Float.toString(event.getX()) + "  --  " + Float.toString(event.getY()));
        if (getPlayer().playing) {
            return inputHandler.processEvent(event);
        } else {
            return inputHandler.processEndgameEvent(event);
        }
    }

    /**
     * Function to update game state.
     * Called for each frame/lifecycle of a game.
     * Take care, order depends!
     */
    public void update() {
        this.networkGameStateUpdater.processRecievedMessages();

        if (getPlayer().playing) {
            gameSynchronizer.waitForClientsToSignalizeReadyForNextFrame();
            inputHandler.processFrameInput();
            {
                getPlayer().update();
                anchor.update();
                getPlayer().updateMiddleDrawCoords(anchor);
            }
            getSpellManager().update();
            getOtherPlayersManager().update();
            getStaticAnimationManager().update();
            //collisionHandler.performCollisionCheck();
            sender.sendMessage(new ReadyToPlayMessage(this.myNickname));
        } else {
            getOtherPlayersManager().update();
            getSpellManager().update();
            //collisionHandler.performCollisionCheck();

            //todo: uninit network components
            // Server, clients, sender, messageProcessors
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final int savedState = canvas.save();
            if (getPlayer().playing) {
                bg.draw(canvas, anchor);
                this.mapManager.draw(canvas);
                borders.draw(canvas);
                getSpellManager().draw(canvas);
                drawManager.drawOnDisplay(getPlayer(), canvas);
                getOtherPlayersManager().draw(canvas);
                getStaticAnimationManager().draw(canvas);
                toolbar.draw(canvas);
            } else {
                bg.draw(canvas, anchor);
                this.mapManager.draw(canvas);
                borders.draw(canvas);
                getSpellManager().draw(canvas);
                getOtherPlayersManager().draw(canvas);
                endgameEvents.draw(canvas);
            }
            canvas.restoreToCount(savedState);
        }
    }

    public void postDrawTasks(){
        collisionHandler.performCollisionCheck();
    }

    public void waitForPlayersToReady(){
        boolean somePlayerNotReady = true;
        while (somePlayerNotReady){
            somePlayerNotReady = false;
            for (Client player : this.registeredPlayers){
                somePlayerNotReady = ( player.isReadyForGame) ? somePlayerNotReady : true;
            }
        }
    }

    public OtherPlayerManager getOtherPlayersManager() {
        return otherPlayersManager;
    }
}
