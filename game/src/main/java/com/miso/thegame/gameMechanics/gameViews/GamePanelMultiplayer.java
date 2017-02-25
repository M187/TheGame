package com.miso.thegame.gameMechanics.gameViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;

import com.miso.thegame.GameActivity;
import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.Networking.Sender;
import com.miso.thegame.Networking.server.logicExecutors.GamePlayLogicExecutor;
import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.Networking.transmitionData.beforeGameMessages.ReadyToPlayMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerDestroyedMessage;
import com.miso.thegame.Networking.transmitionData.ingameMessages.PlayerPositionData;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.GameState;
import com.miso.thegame.gameMechanics.MainGameThread;
import com.miso.thegame.gameMechanics.UserInterface.ButtonsTypeData;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.multiplayer.ConnectionManager;
import com.miso.thegame.gameMechanics.multiplayer.NetworkGameStateUpdater;
import com.miso.thegame.gameMechanics.multiplayer.otherPlayer.OtherPlayerManager;
import com.miso.thegame.gameMechanics.objects.collisionHandlers.CollisionHandlerMultiplayer;

import java.util.ArrayList;

/**
 * Created by Miso on 8.10.2015.
 * <p/>
 * Copy of GamePanel. Reworked to support multiplayer functionality.
 */
public class GamePanelMultiplayer extends GameView2 implements SurfaceHolder.Callback {

    public ConnectionManager connectionManager;
    protected CollisionHandlerMultiplayer collisionHandler;
    private volatile ArrayList<TransmissionMessage> arrivingMessagesList = new ArrayList<>();
    private OtherPlayerManager otherPlayersManager = null;
    private NetworkGameStateUpdater networkGameStateUpdater = new NetworkGameStateUpdater(this);

    public GamePanelMultiplayer(GameActivity context, GameMapEnum mapToCreate, String myNickname, GamePlayerTypeEnum playerType, Point playerStartingPosition, ButtonsTypeData buttonsTypeData, ConnectionManager connectionManager) {
        super(context, playerType, buttonsTypeData);
        this.playerStartingPosition = playerStartingPosition;
        Log.d(ConstantHolder.TAG, " --> Trying to create game panel for multiplayer.");
        this.mapToCreate = MapManager.initializeMap(GameMapEnum.MultiplayerMap1, getResources());

        this.myNickname = myNickname;
        this.connectionManager = connectionManager;
        this.otherPlayersManager = new OtherPlayerManager(this.connectionManager.registeredPlayers);
        this.connectionManager.localServer.setMessageLogicExecutor(new GamePlayLogicExecutor(this.arrivingMessagesList, this.connectionManager.registeredPlayers));
        this.sender = new Sender(this.connectionManager.registeredPlayers);

        this.context = context;
        this.thread = new MainGameThread(getHolder(), this);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.connectionManager.localServer.terminate();
        super.surfaceDestroyed(holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surface) {
        super.surfaceCreated(surface);

        collisionHandler = new CollisionHandlerMultiplayer(getPlayer(), getOtherPlayersManager(), getSpellManager(), this.mapManager, getResources());
        this.sender.sendMessage(new ReadyToPlayMessage(this.myNickname));
        this.connectionManager.waitForPlayersToReady();
        this.thread.setRunning(true);
        this.thread.start();
    }

    /**
     * Function to update game state.
     * Called for each frame/lifecycle of a game.
     * Take care, order depends!
     */
    public void update() {

        this.networkGameStateUpdater.processRecievedMessages();

        switch (this.gameState.getGameState()) {
            case playing:
                this.connectionManager.gameSynchronizer.waitForClientsToSignalizeReadyForNextFrame();
                inputHandler.processFrameInput();
                getPlayer().update();
                anchor.update();
                getPlayer().updateMiddleDrawCoords(anchor);
                getSpellManager().update();
                getOtherPlayersManager().update();
                getStaticAnimationManager().update();
                collisionHandler.performCollisionCheck();
                sendFrameData();
                break;
            case defeated:
                this.getSpellManager().primaryShootingActive = false;
                getOtherPlayersManager().update();
            case victory:
                this.getSpellManager().primaryShootingActive = false;
                getSpellManager().update();
                collisionHandler.performCollisionCheck();
                //todo: uninit network components
                break;
        }
    }

    private void sendFrameData(){
        if (this.gameState.getGameState() == GameState.GameStates.defeated) {
            sender.sendMessage(new PlayerDestroyedMessage(this.myNickname));
        } else {
            sender.sendMessage(new PlayerPositionData(this.player, GameView2.myNickname));
            sender.sendMessage(new ReadyToPlayMessage(this.myNickname));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final int savedState = canvas.save();

            switch (this.gameState.getGameState()){
                case playing:
                    bg.draw(canvas, anchor);
                    this.mapManager.draw(canvas);
                    borders.draw(canvas);
                    getSpellManager().draw(canvas);
                    drawManager.drawOnDisplay(getPlayer(), canvas);
                    getOtherPlayersManager().draw(canvas);
                    getStaticAnimationManager().draw(canvas);
                    toolbar.draw(canvas);
                    break;
                case victory:
                case defeated:
                    bg.draw(canvas, anchor);
                    this.mapManager.draw(canvas);
                    borders.draw(canvas);
                    getSpellManager().draw(canvas);
                    getOtherPlayersManager().draw(canvas);
                    endgameEvents.draw(canvas, this.gameState.getGameState());
                    break;
            }
            canvas.restoreToCount(savedState);
        }
    }

    public void postDrawTasks() {
        //collisionHandler.performCollisionCheck();
    }

    public OtherPlayerManager getOtherPlayersManager() {
        return otherPlayersManager;
    }

    public ArrayList<TransmissionMessage> getArrivingMessagesList() {
        return arrivingMessagesList;
    }
}
