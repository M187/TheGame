package com.miso.thegame.Networking.transmitionData.ingameMessages;

import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerShootFreezingProjectile extends TransmissionMessage {

    private static String projectileIterator = "0";

    private Point fromPosition;
    private Point movementDelta;
    private String identificator;

    public PlayerShootFreezingProjectile(String nickname, Point fromPosition, Point movementDelta) {
        this.transmissionType = "21";
        this.identificator = nickname;
        this.fromPosition = fromPosition;
        this.movementDelta = movementDelta;
    }

    public static PlayerShootProjectile unmarshal(String rawTransmissionString) {
        return new PlayerShootProjectile(
                rawTransmissionString.split("\\|")[1],
                new Point(
                        Integer.parseInt(rawTransmissionString.split("\\|")[2]),
                        Integer.parseInt(rawTransmissionString.split("\\|")[3])),
                new Point(
                        Integer.parseInt(rawTransmissionString.split("\\|")[4]),
                        Integer.parseInt(rawTransmissionString.split("\\|")[5])));
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.getIdentificator() + "|" + this.fromPosition.x + "|" + this.fromPosition.y + "|" + this.movementDelta.x + "|" + this.movementDelta.y;
    }

    public Point getFromPosition() {
        return fromPosition;
    }

    public Point getMovementDelta() {
        return movementDelta;
    }

    public String getIdentificator() {
        return identificator;
    }
}
