package com.miso.thegame.Networking;

/**
 * Created by Miso on 22.4.2016.
 */
public class PlayerClientPOJO {

    private String hostName;
    private String id;

    public int getColor() {
        return color;
    }

    private int color;

    public PlayerClientPOJO(String hostName, String id, int color) {
        this.hostName = hostName;
        this.id = id;
        this.color = color;
    }

    public String getHostName() {
        return hostName;
    }

    public String getId() {
        return id;
    }
}

