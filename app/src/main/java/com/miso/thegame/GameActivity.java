package com.miso.thegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.miso.thegame.GameData.GameMapEnum;
import com.miso.thegame.GameData.OptionStrings;
import com.miso.thegame.Networking.client.Client;
import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.gameViews.GamePanelMultiplayer;
import com.miso.thegame.gameViews.GamePanelSingleplayer;

import java.util.ArrayList;


public class GameActivity extends Activity {

    public static DisplayMetrics metrics = new DisplayMetrics();
    public boolean gameOver = false;
    public ArrayList<Client> registeredPlayers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GameMapEnum mapToCreate = getMapToCreate();
        loadPlayerData();

        super.onCreate(savedInstanceState);
        //set fullscreen
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
            if (playerNetworkData.contains("free slot")) {
                hasMoreData = false;
            } else {
                this.registeredPlayers.add(
                        new Client(
                                playerNetworkData.split("\\|")[1].split(":")[0],
                                Integer.parseInt(playerNetworkData.split("\\|")[1].split(":")[1]),
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
            loadConnectedPlayersNetworkData();

//            try {
//                System.out.print("Good night!");
//                Thread.sleep(450000000);3
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            setContentView(
                    new GamePanelMultiplayer(
                            this,
                            mapToCreate,
                            this.registeredPlayers,
                            this.getIntent().getExtras().getString(OptionStrings.myNickname, "--")));
        } else {
            setContentView(new GamePanelSingleplayer(this, mapToCreate));
        }
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
