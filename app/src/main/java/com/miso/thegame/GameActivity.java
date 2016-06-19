package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.GamePlayerTypeEnum;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameMechanics.multiplayer.ConnectionManager;
import com.miso.thegame.gameViews.GamePanelMultiplayer;
import com.miso.thegame.gameViews.GamePanelSingleplayer;
import com.miso.thegame.gameViews.GameView2;

import java.util.ArrayList;


public class GameActivity extends Activity {

    public static DisplayMetrics metrics = new DisplayMetrics();
    public boolean gameOver = false;
    public ArrayList<Client> registeredPlayers = new ArrayList<>();
    public ArrayList<Client> playersThatEnteredGame = new ArrayList<>();
    public GamePlayerTypeEnum playerType;
    private ConnectionManager connectionManager;

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
    protected void onDestroy() {
        MenuActivity.isGameOn = false;
        super.onDestroy();
    }

    /**
     * Loads player data and saves them into constantHolder class.
     * Later on to be used by game to affect gameObjects.
     */
    private void loadPlayerData() {
        SharedPreferences settings = getSharedPreferences("PlayerOptions", 0);

        int maxHealth = settings.getInt(OptionStrings.playerMaxHealth, 0);
        int maxAmmo = settings.getInt(OptionStrings.playerMaxAmmo, 0);
        int maxSpeed = settings.getInt(OptionStrings.playerMaxSpeed, 0);

        this.playerType = GamePlayerTypeEnum.getPlayerTypeFromTypeString(settings.getString(OptionStrings.playerType, ""));

        ConstantHolder.loadSettingData(maxHealth, maxAmmo, maxSpeed);
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
                                //Integer.parseInt(playerNetworkData.split("\\|")[1].split(":")[1]),
                                ConnectionManager.PORT,
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
        if (this.getIntent().getExtras().getBoolean(OptionStrings.multiplayerInstance, false)) {

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
                    connectionManager);

            //todo: this can't be run on main thread, since it makes activity unresponsive.
            (new Runnable() {
                private GamePanelMultiplayer surfaceView;
                private Activity activity;

                public Runnable init(GamePanelMultiplayer surfaceView, Activity activity) {
                    this.activity = activity;
                    this.surfaceView = surfaceView;
                    return this;
                }

                public void run() {
                    this.surfaceView.createConnections();

                    runOnUiThread((new Runnable() {
                        private Activity activity;
                        private GamePanelMultiplayer surfaceView;

                        @Override
                        public void run() {
                            this.activity.setContentView(this.surfaceView);
                        }

                        public Runnable init(Activity activity, GamePanelMultiplayer surfaceView) {
                            this.activity = activity;
                            this.surfaceView = surfaceView;
                            return this;
                        }
                    }).init(this.activity, this.surfaceView));
                }
            }).init(this.multiplayerSurfaceView, this).run();
        }
        //</editor-fold>
        else {
            setContentView(new GamePanelSingleplayer(this, mapToCreate, this.playerType));
        }
    }

    /**
     *
     */
    private void initConnections(){

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
                    return GameMapEnum.Level2;
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
