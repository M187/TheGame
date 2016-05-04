package com.miso.thegame.Networking.transmitionData.ingameMessages;

import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerPositionData extends TransmissionMessage {

    private Point position;
    private String nickname;

    public PlayerPositionData(Player player, String nickname) {
        this.transmissionType = "10";
        this.nickname = nickname;
        this.position = new Point(player.getX(), player.getY());
    }

    public PlayerPositionData(String nickname, Point position) {
        this.transmissionType = "10";
        this.nickname = nickname;
        this.position = position;
    }

    public static PlayerPositionData unmarshal(String rawTransmissionString) {
        return new PlayerPositionData(
                rawTransmissionString.split("\\|")[1],
                new Point(
                        Integer.parseInt(rawTransmissionString.split("\\|")[2]),
                        Integer.parseInt(rawTransmissionString.split("\\|")[3])));
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.nickname + "|" + this.getPosition().x + "|" + this.getPosition().y;
    }

    public Point getPosition() {
        return position;
    }

    public String getNickname() {
        return nickname;
    }
}
