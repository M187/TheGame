package com.miso.thegame.Networking.transmitionData.ingameMessages;

import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerPositionData extends TransmissionMessage {

    private Point position;

    public PlayerPositionData(Player player) {
        this.transmissionType = "10";
        this.position = new Point(player.getX(), player.getY());
    }

    public PlayerPositionData(int x, int y) {
        this.transmissionType = "10";
        this.position = new Point(x, y);
    }

    public PlayerPositionData(String rawTransmissionString) {
        this.transmissionType = "10";
        this.position = new Point(
                Integer.parseInt(rawTransmissionString.split("\\|")[1]),
                Integer.parseInt(rawTransmissionString.split("\\|")[2]));
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.getPosition().x + "|" + this.getPosition().y;
    }

    public Point getPosition() {
        return position;
    }
}
