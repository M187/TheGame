package com.miso.thegame.gameMechanics;

import com.miso.thegame.GamePanel;
import com.miso.thegame.gameMechanics.movingObjects.player.Player;

/**
 * Created by Miso on 1.11.2015.
 */
public class Anchor {

    private int x;
    private int y;
    protected int xCoordSideRange;
    protected int yCoordSideRange;
    private Player player;

    /**
     * Initiate anchor object. Set its default params as player position + half size of display resolution for both axis.
     * This should set it as one of the corners.
     *
     * @param player player used
     * @param xCoordSideRange range for x coord. When player enters this range, background is moved instead player.
     * @param yCoordSideRange range for y coord. When player enters this range, background is moved instead player.
     */
    public Anchor(Player player, int xCoordSideRange, int yCoordSideRange){

        this.player = player;
        this.setX(player.getX() - (GamePanel.WIDTH / 2));
        this.setY(player.getY() - (GamePanel.HEIGHT / 2));
        this.xCoordSideRange = xCoordSideRange;
        this.yCoordSideRange = yCoordSideRange;
    }

    /**
     * Check if player intersects with bordering rectangles. If so, move anchor and update player.
     * This should be called after players update.
     *
     */
    public void update(){
        int delta;
        //Is player intersecting with bordering rectangles?
        //left
        if (player.getX() < getX() + getxCoordSideRange()){
            delta =  getX() + getxCoordSideRange() - player.getX();
            setX(getX() - delta);
           //player.setX(player.getX() + delta);
        }
        //right
        if (player.getX() > getX() + GamePanel.WIDTH - getxCoordSideRange()){
            delta =  player.getX() - (getX() + GamePanel.WIDTH - getxCoordSideRange());
            setX(getX() + delta);
            //player.setX(player.getX() - delta);
        }
        //top
        if (player.getY() < getY() + getyCoordSideRange()){
            delta = getY() + getyCoordSideRange() - player.getY();
            setY(getY() - delta);
            //player.setY(player.getY() + delta);
        }
        //bottom
        if (player.getY() > getY() + GamePanel.HEIGHT - getyCoordSideRange() - 100){
            delta = player.getY() - (getY() + GamePanel.HEIGHT - getyCoordSideRange() - 100);
            setY(getY() + delta);
            //player.setY(player.getY() - delta);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxCoordSideRange() {
        return xCoordSideRange;
    }

    public int getyCoordSideRange() {
        return yCoordSideRange;
    }
}
