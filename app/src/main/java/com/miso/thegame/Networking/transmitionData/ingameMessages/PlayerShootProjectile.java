package com.miso.thegame.Networking.transmitionData.ingameMessages;

import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerShootProjectile extends TransmissionMessage {

    private Point fromPosition;
    private Point delta;

    public PlayerShootProjectile(Point fromPosition, Point delta) {
        this.transmissionType = "20";
        this.fromPosition = fromPosition;
        this.delta = delta;
    }

    public PlayerShootProjectile(String rawTransmissionString) {
        this.transmissionType = "20";
        this.fromPosition = new Point(
                Integer.parseInt(rawTransmissionString.split("\\|")[1]),
                Integer.parseInt(rawTransmissionString.split("\\|")[2]));
        this.delta = new Point(
                Integer.parseInt(rawTransmissionString.split("\\|")[3]),
                Integer.parseInt(rawTransmissionString.split("\\|")[4]));
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.fromPosition.x + "|" + this.fromPosition.y + "|" + this.delta.x + "|" + this.delta.y;
    }

    public Point getFromPosition() {
        return fromPosition;
    }

    public Point getDelta() {
        return delta;
    }
}
