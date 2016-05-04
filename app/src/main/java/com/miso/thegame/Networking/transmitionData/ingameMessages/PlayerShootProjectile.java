package com.miso.thegame.Networking.transmitionData.ingameMessages;

import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerShootProjectile extends TransmissionMessage {

    private Point fromPosition;
    private Point delta;
    private String nickname;

    public PlayerShootProjectile(String nickname, Point fromPosition, Point delta) {
        this.transmissionType = "20";
        this.nickname = nickname;
        this.fromPosition = fromPosition;
        this.delta = delta;
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
        return this.transmissionType + "|" + this.getNickname() + "|" + this.fromPosition.x + "|" + this.fromPosition.y + "|" + this.delta.x + "|" + this.delta.y;
    }

    public Point getFromPosition() {
        return fromPosition;
    }

    public Point getDelta() {
        return delta;
    }

    public String getNickname() {
        return nickname;
    }
}
