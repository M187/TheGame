package com.miso.thegame.gameMechanics.movingObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.miso.thegame.gameMechanics.ConstantHolder;
import com.miso.thegame.R;
import com.miso.thegame.gameMechanics.collisionHandlers.CollisionObjectType;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.map.pathfinding.Pathfinder;

import java.util.ArrayList;

/**
 * Created by Miso on 29.4.2015.
 */
public class Player extends MovableObject {

    protected int score;
    public boolean playing = true;
    protected boolean movementDisabled = false;
    protected MapManager mapManager;
    protected Point currentGridCoordinates;
    public boolean changingTile = false;

    protected boolean isMoving = false;
    public boolean turningClockwise = false;
    public boolean isTurningCounterclockwise = false;
    protected PlayerEngineMotors playerEngineMotors;

    public int objectivesCollected = 0;

    // Toolbar things
    public int maxHealth = ConstantHolder.maximumPlayerHealth;
    public int currentHealth = this.maxHealth;
    public int primaryAmunitionMaxValue = ConstantHolder.primaryAmunitionMaxValue;
    public int primaryAmunition = this.primaryAmunitionMaxValue;

    protected int lastHeading = 0;

    public Player(){}

    //TODO: static variables for coordinates. ? Init to a static variable in GamePanel?
    public Player(Resources res, Point startingPosition, MapManager mapManager) {
        setX(startingPosition.x);
        setY(startingPosition.y);
        this.collisionObjectType = CollisionObjectType.Player;
        setSpeed(ConstantHolder.maximumPlayerSpeed);
        score = 0;
        dx = (getX());
        dy = (getY());

        this.mapManager = mapManager;
        setImage(BitmapFactory.decodeResource(res, R.drawable.player));
        this.playerEngineMotors = new PlayerEngineMotors(res);
        this.setCurrentGridCoordinates(super.getGridCoordinates());
    }

    public void update() {

        this.checkGameProgress();

        this.lastHeading = this.heading;

        if (this.currentHealth <= 0) {
            System.out.println("Game over comrade!");
            this.playing = false;
        }
        if (movementNotDisabled()) {
            setPositionBeforeMoving();
            this.moveObject();
            //Was tile changed during movement?
            Point temp = this.getGridCoordinates();
            if (!this.currentGridCoordinates.equals(temp) && !(mapManager.mapGrid.mapGrid[temp.x][temp.y].obstaclePresent)) {
                setCurrentGridCoordinates(temp);
                changingTile = true;
            } else if (!this.currentGridCoordinates.equals(temp)) {
                adjustMovementDueToObstacle(temp);
                changingTile = false;
            } else {
                changingTile = false;
            }
            turnCheck();
        }
        setMovementDisabled(false);
    }

    public void turnCheck() {
        int turnThreshold = 20;
        int deltaDegrees = Math.abs(Math.abs(this.lastHeading) - Math.abs(this.heading));
        if ( deltaDegrees >  turnThreshold && (Math.abs(Math.abs(this.lastHeading) - Math.abs(this.heading) + 360) > turnThreshold) && (Math.abs(Math.abs(this.lastHeading) - Math.abs(this.heading) - 360) > turnThreshold)){
            int newDegrees;
            if ( this.lastHeading > this.heading ) {
                if (deltaDegrees > 180 ) {
                    newDegrees = this.lastHeading + turnThreshold;
                    if (newDegrees > 0) {
                        newDegrees -= 360;
                    }
                    this.heading = (newDegrees);
                    this.turningClockwise = true;
                } else {
                    newDegrees = this.lastHeading - turnThreshold;
                    if (newDegrees < -360) {
                        newDegrees += 360;
                    }
                    this.heading = (newDegrees);
                    this.isTurningCounterclockwise = true;
                }
            } else {
                if (deltaDegrees < 180){
                    newDegrees = this.lastHeading + turnThreshold;
                    if (newDegrees > 0 ){ newDegrees -= 360;}
                    this.heading = (newDegrees);
                    this.turningClockwise = true;
                } else {
                    newDegrees = this.lastHeading - turnThreshold;
                    if (newDegrees < -360 ){ newDegrees += 360;}
                    this.heading = (newDegrees);
                    this.isTurningCounterclockwise = true;
                }
            }
            this.restorePositionBeforeMoving();
        }
    }

    @Override
    public void moveObject() {
        if (!(this.frameDeltaX == 0 && this.frameDeltaY == 0)) {
            this.isMoving = true;
            this.calculateHeading();
            setX(getDx());
            setY(getDy());
        } else {
            this.isMoving = false;
        }
    }

    public void checkGameProgress(){

    }

    /**
     * If player tries to wnter tile with obstacle present, set his coords to a border position.
     * Check which axis has border violation.
     * Adjust coords due to it.
     *
     * @param obstacleTilee title which is player trying to enter and has a obstacle present.
     */
    public void adjustMovementDueToObstacle(Point obstacleTilee) {
        //TODO think of something
        if (obstacleTilee.x == currentGridCoordinates.x) {
            if (obstacleTilee.y < this.currentGridCoordinates.y) {
                this.y = this.currentGridCoordinates.y * MapManager.getMapTileHeight();
            } else {
                this.y = obstacleTilee.y * MapManager.getMapTileHeight() - 1;
            }
        } else if (obstacleTilee.y == currentGridCoordinates.y) {
            if (obstacleTilee.x < this.currentGridCoordinates.x) {
                this.x = this.currentGridCoordinates.x * MapManager.getMapTileWidth();
            } else {
                this.x = obstacleTilee.x * MapManager.getMapTileWidth() - 1;
            }
        } else {
            if (Pathfinder.mapGrid.mapGrid[this.currentGridCoordinates.x][obstacleTilee.y].obstaclePresent && Pathfinder.mapGrid.mapGrid[obstacleTilee.x][this.currentGridCoordinates.y].obstaclePresent) {
                if (obstacleTilee.x < this.currentGridCoordinates.x) {
                    this.x = this.currentGridCoordinates.x * MapManager.getMapTileWidth();
                } else {
                    this.x = obstacleTilee.x * MapManager.getMapTileWidth() - 1;
                }
                if (obstacleTilee.y < this.currentGridCoordinates.y) {
                    this.y = this.currentGridCoordinates.y * MapManager.getMapTileHeight();
                } else {
                    this.y = obstacleTilee.y * MapManager.getMapTileHeight() - 1;
                }
            } else if (Pathfinder.mapGrid.mapGrid[this.currentGridCoordinates.x][obstacleTilee.y].obstaclePresent) {
                if (obstacleTilee.y < this.currentGridCoordinates.y) {
                    this.y = this.currentGridCoordinates.y * MapManager.getMapTileHeight();
                } else {
                    this.y = obstacleTilee.y * MapManager.getMapTileHeight() - 1;
                }
            } else if (Pathfinder.mapGrid.mapGrid[obstacleTilee.x][this.currentGridCoordinates.y].obstaclePresent) {
                if (obstacleTilee.x < this.currentGridCoordinates.x) {
                    this.x = this.currentGridCoordinates.x * MapManager.getMapTileWidth();
                } else {
                    this.x = obstacleTilee.x * MapManager.getMapTileWidth() - 1;
                }
            }
        }
    }

    public void setDxViaJoystick(int xCoord) {
        this.dx = xCoord;
        if (this.dx < 0) {
            this.dx = 0;
        }
        if (this.dx > MapManager.getWorldWidth() - 1) {
            this.dx = MapManager.getWorldWidth() - 1;
        }
    }

    public void setDyViaJoystick(int yCoord) {
        this.dy = yCoord;
        if (this.dy < 0) {
            this.dy = 0;
        }
        if (this.dy > MapManager.getWorldHeight() - 1) {
            this.dy = MapManager.getWorldHeight() - 1;
        }
    }

    public void setFrameDeltaX(int xDeltaValue) {
        this.frameDeltaX = xDeltaValue;
    }

    public void setFrameDeltaY(int yDeltaValue) {
        this.frameDeltaY = yDeltaValue;
    }

    public void setDxViaBlink(int xCoord) {
        this.dx = xCoord;
        if (this.dx < 0) {
            this.dx = 0;
        }
        if (this.dx >= MapManager.getWorldWidth()) {
            this.dx = MapManager.getWorldWidth() - 1;
        }
    }

    public void setDyViaBlink(int yCoord) {
        this.dy = yCoord;
        if (this.dy < 0) {
            this.dy = 0;
        }
        if (this.dy >= MapManager.getWorldHeight()) {
            this.dy = MapManager.getWorldHeight() - 1;
        }
    }

    public boolean movementNotDisabled() {
        return (!movementDisabled);
    }

    public void setMovementDisabled(boolean movementDisabled) {
        this.movementDisabled = movementDisabled;
    }

    public void setCurrentGridCoordinates(Point currentGridCoordinates) {
        this.currentGridCoordinates = currentGridCoordinates;
    }

    public void removeHealth(int damage) {
        this.currentHealth -= damage;
        if (this.currentHealth < -1) {
            this.currentHealth = -1;
        }
    }

    public void addHealth(int heal) {
        this.currentHealth += heal;
    }

    /**
     * Heal to max health.
     */
    public void addHealth() {
        this.currentHealth = this.maxHealth;
    }

    public void replenishPrimaryAmmo() {
        this.primaryAmunition = this.primaryAmunitionMaxValue;
    }

    @Override
    public ArrayList<Point> getObjectCollisionVertices() {
        ArrayList<Point> objectRotatedVertices = new ArrayList<>();

        for (Point p : this.getNonRotatedVertices()) {
            objectRotatedVertices.add(rotateVertexAroundCurrentPosition(p));
        }
        return objectRotatedVertices;
    }

    protected ArrayList<Point> getNonRotatedVertices() {
        ArrayList<Point> vertices = new ArrayList();
        vertices.add(new Point(this.x,this.y - 50));
        vertices.add(new Point(this.x-25,this.y + 37));
        vertices.add(new Point(this.x,this.y+50));
        vertices.add(new Point(this.x+25,this.y+37));

        return vertices;
    }

    public void drawObject(Canvas canvas, int x, int y){

        //Draw engine fire
        if (this.isMoving){
            this.playerEngineMotors.drawObject(this, canvas, x, y);
        }
        //Draw object
        canvas.drawBitmap(this.getImage(), x, y, null);
    }

    /**
     * Created by Miso on 25.1.2016.
     */
    public static class PlayerEngineMotors {

        public Bitmap image;

        public PlayerEngineMotors(Resources res){
            this.image = BitmapFactory.decodeResource(res, R.drawable.playerenginefire);
        }

        public void drawObject(Player player, Canvas canvas, int x, int y){
            if (player.turningClockwise) {
                canvas.drawBitmap(this.image, x + 8, y + 85, null);
                player.turningClockwise = false;
            }
            else if (player.isTurningCounterclockwise) {
                canvas.drawBitmap(this.image, x + 32, y + 85, null);
                player.isTurningCounterclockwise = false;
            } else {
                canvas.drawBitmap(this.image, x + 8, y + 85, null);
                canvas.drawBitmap(this.image, x + 32, y + 85, null);
            }
        }
    }
}
