package com.miso.thegame.Networking;

/**
 * Created by michal.hornak on 2/7/2017.
 */

public class NetworkConnectionConstants {

    public static final int DEFAULT_COM_PORT = 12371;

    private static volatile String PLAYER_NICKNAME;


    public static String getPlayerNickname() {
        return PLAYER_NICKNAME;
    }

    public static void setPlayerNickname(String playerNickname) {
        PLAYER_NICKNAME = playerNickname;
    }
}
