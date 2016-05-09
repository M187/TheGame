package com.miso.thegame.Networking.transmitionData.ingameMessages;

import android.graphics.Point;

import com.miso.thegame.Networking.transmitionData.TransmissionMessage;

/**
 * Created by michal.hornak on 20.04.2016.
 */
public class PlayerShootProjectile extends TransmissionMessage {

    private static String projectileIterator = "0";

    private Point fromPosition;
    private Point movementDelta;
    private String nickname;
    private String projectilelId;

    public PlayerShootProjectile(String nickname, Point fromPosition, Point movementDelta) {
        this.transmissionType = "20";
        this.nickname = nickname;
        this.fromPosition = fromPosition;
        this.movementDelta = movementDelta;
        this.projectilelId = Integer.toString(Integer.parseInt(projectileIterator) + 1);

        if (Integer.parseInt(projectileIterator) > 1000){
            projectileIterator = "0";
        }
    }

    public PlayerShootProjectile(String nickname, String projectilelId, Point fromPosition, Point movementDelta) {
        this.transmissionType = "20";
        this.nickname = nickname;
        this.projectilelId = projectilelId;
        this.fromPosition = fromPosition;
        this.movementDelta = movementDelta;
        this.projectilelId = Integer.toString(Integer.parseInt(projectileIterator) + 1);

        if (Integer.parseInt(projectileIterator) > 1000){
            projectileIterator = "0";
        }
    }

    public static PlayerShootProjectile unmarshal(String rawTransmissionString) {
        return new PlayerShootProjectile(
                rawTransmissionString.split("\\|")[1],
                rawTransmissionString.split("\\|")[2],
                new Point(
                        Integer.parseInt(rawTransmissionString.split("\\|")[3]),
                        Integer.parseInt(rawTransmissionString.split("\\|")[4])),
                new Point(
                        Integer.parseInt(rawTransmissionString.split("\\|")[5]),
                        Integer.parseInt(rawTransmissionString.split("\\|")[6])));
    }

    public String getPacket() {
        return this.transmissionType + "|" + this.getNickname() + "|" + this.projectilelId + "|" + this.fromPosition.x + "|" + this.fromPosition.y + "|" + this.movementDelta.x + "|" + this.movementDelta.y;
    }

    public Point getFromPosition() {
        return fromPosition;
    }

    public Point getMovementDelta() {
        return movementDelta;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIdentificator(){
        return this.nickname + this.projectilelId;
    }
}
