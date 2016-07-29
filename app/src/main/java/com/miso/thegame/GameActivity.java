package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.miso.thegame.GameData.ButtonTypeEnum;
import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.UserInterface.ButtonsTypeData;
import com.miso.thegame.gameMechanics.gameViews.GamePanelMultiplayer;
import com.miso.thegame.gameMechanics.gameViews.GamePanelSingleplayer;
import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.multiplayer.ConnectionManager;
import com.miso.thegame.gameMechanics.multiplayer.WaiterForAllConnections;

import java.util.ArrayList;


public class GameActivity extends Activity {

    public static volatile boolean areAllPlayersConnected = false;
    public static DisplayMetrics metrics = new DisplayMetrics();
    public boolean gameOver = false;
    public ArrayList<Client> registeredPlayers = new ArrayList<>();
    public ArrayList<Client> playersThatEnteredGame = new ArrayList<>();
    public GamePlayerTypeEnum playerType;
    private ButtonsTypeData buttonsTypeData = new ButtonsTypeData();
    private ConnectionManager connectionManager;
    private WaiterForAllConnections waiterForAllConnections;

    private GamePanelMultiplayer multiplayerSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(ConstantHolder.TAG, " --> Entered main game Activity.");
        GameMapEnum mapToCreate = getMapToCreate();
        loadPlayerData();

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        createGameView(mapToCreate);

        if (gameOver) {
            this.finish();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        MenuActivity.isGameOn = false;
        try {
            this.connectionManager.terminate();
        } catch (NullPointerException e) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Loads player data and saves them into constantHolder class.
     * Later on to be used by game to affect gameObjects.
     */
    private void loadPlayerData() {
        SharedPreferences settings = getSharedPreferences("PlayerOptions", 0);

        int maxHealth = settings.getInt(OptionStrings.playerBonusHealth, 0);
        int maxAmmo = settings.getInt(OptionStrings.playerBonusAmmo, 0);
        int maxSpeed = settings.getInt(OptionStrings.playerMaxSpeed, 0);

        this.buttonsTypeData.firstButtonType = ButtonTypeEnum.getButtonTypeFromButtonTypeString(settings.getString(OptionStrings.firstButtonType, "Shockwave"));
        this.buttonsTypeData.secondButtonType = ButtonTypeEnum.getButtonTypeFromButtonTypeString(settings.getString(OptionStrings.secondButtonType, "Timestop"));
        this.playerType = GamePlayerTypeEnum.getPlayerTypeFromTypeString(settings.getString(OptionStrings.playerType, "Saucer"));

        ConstantHolder.loadSettingsData(maxHealth, maxAmmo, maxSpeed);
    }

    /**
     * Loads connected players data and saves them into arrayList.
     * Needed for multiplayer game.
     */
    private void loadConnectedPlayersNetworkData() {
        boolean hasMoreData = true;
        int i = 0;
        SharedPreferences settings = getSharedPreferences("MultiplayerLobby", 0);
        while (hasMoreData) {
            String playerNetworkData = settings.getString("Player" + i + "networkData", "0");
            i += 1;
            if (playerNetworkData.contains("free slot")) {
                hasMoreData = false;
            } else {
                this.registeredPlayers.add(
                        new Client(
                                playerNetworkData.split("\\|")[1].split(":")[0],
                                connectionManager.PORT,
                                playerNetworkData.split("\\|")[0],
                                true
                        ));
            }
        }
    }

    /**
     * Creates game view.
     *
     * @param mapToCreate for game instance.
     */
    private void createGameView(GameMapEnum mapToCreate) {
        if (this.getIntent().getExtras().getString(OptionStrings.multiplayerInstance, "nope") != "nope") {

            int playerIndex = Integer.parseInt(this.getIntent().getExtras().getString(OptionStrings.multiplayerInstance, "nope"));

            //<editor-fold @name="Create multiplayer view.">
            this.setContentView(R.layout.waiting_for_other_players);
            GameView2.isMultiplayerGame = true;
            loadConnectedPlayersNetworkData();
            this.playersThatEnteredGame = new ArrayList<>(this.registeredPlayers);
            this.connectionManager = new ConnectionManager(this.registeredPlayers);
            this.multiplayerSurfaceView = new GamePanelMultiplayer(
                    this,
                    mapToCreate,
                    this.getIntent().getExtras().getString(OptionStrings.myNickname, "--"),
                    this.playerType,
                    new Point(300 + 300 * playerIndex, 500), //TODO: <- do this better / validate somehow (Map dependant?)
                    this.buttonsTypeData,
                    connectionManager);

            this.waiterForAllConnections = new WaiterForAllConnections(this.multiplayerSurfaceView, this);
            executeServerAndWaiter();
        }
        //</editor-fold>
        else {
            setContentView(new GamePanelSingleplayer(this, mapToCreate, this.playerType, this.buttonsTypeData));
        }
    }

    /**
     * Simple wrapper to execute server and async task that waits for all player connections.
     * Waiting for other players is done in async task so that activity is not  blocked.
     */
    private void executeServerAndWaiter() {
        this.connectionManager.localServer.start();
        this.waiterForAllConnections.start();
    }

    /**
     * Basic switch to decide what map should be created.
     *
     * @return map for GameView to be created.
     */
    private GameMapEnum getMapToCreate() {
        try {
            switch (getIntent().getExtras().getString("Level")) {
                case "Ground":
                    return GameMapEnum.Level1;
                case "Space":
                    return GameMapEnum.SpaceLevel1;
                default:
                    return GameMapEnum.BlankMap;
            }
        } catch (NullPointerException e) {
            return GameMapEnum.BlankMap;
        }
    }
}
