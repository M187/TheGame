package com.miso.thegame.gameMechanics.objects.movingObjects;

import com.miso.thegame.gameMechanics.gameViews.GameView2;
import com.miso.thegame.gameMechanics.map.MapManager;
import com.miso.thegame.gameMechanics.objects.movingObjects.player.Player;

/**
 * Created by Miso on 1.11.2015.
 */
public class Anchor {

    private final int BORDER_THRESHOLD = 50;

    protected int xCoordSideRange;
    protected int yCoordSideRange;
    private int x;
    private int y;
    private Player player;

    /**
     * Initiate anchor object. Set its default params as player position + half size of display resolution for both axis.
     * This should set it as one of the corners.
     *
     * @param player          player used
     * @param xCoordSideRange range for x coord. When player enters this range, background is moved instead player.
     * @param yCoordSideRange range for y coord. When player enters this range, background is moved instead player.
     */
    public Anchor(Player player, int xCoordSideRange, int yCoordSideRange) {

        this.player = player;
        this.setX(player.getX() - (GameView2.WIDTH / 2));
        this.setY(player.getY() - (GameView2.HEIGHT / 2));
        this.xCoordSideRange = xCoordSideRange;
        this.yCoordSideRange = yCoordSideRange;
    }

    /**
     * Check if player intersects with bordering rectangles. If so, move anchor and update player.
     * This should be called after players update.
     */
    public void update() {

        //TODO: stop moving anchor when borders are visible. Will give bigger sight range when player is near borders.

        int delta;
        //Is player intersecting with bordering rectangles?
        //left
        if (player.getX() < getX() + getxCoordSideRange()) {
            delta = getX() + getxCoordSideRange() - player.getX();
            setX(getX() - delta);

            if (this.getX() < -BORDER_THRESHOLD) this.setX(-BORDER_THRESHOLD);
        }
        //right
        if (player.getX() > getX() + GameView2.WIDTH - getxCoordSideRange()) {
            delta = player.getX() - (getX() + GameView2.WIDTH - getxCoordSideRange());
            setX(getX() + delta);
            if (this.getX() + GameView2.WIDTH >  MapManager.getWorldWidth() + BORDER_THRESHOLD) this.setX(MapManager.getWorldWidth() - GameView2.WIDTH + BORDER_THRESHOLD);
        }

        //top
        if (player.getY() < getY() + getyCoordSideRange() + 50) {
            delta = getY() + getyCoordSideRange() - player.getY() + 50;
            setY(getY() - delta);
            if (this.getY() < -BORDER_THRESHOLD) this.setY(-BORDER_THRESHOLD);
        }
        //bottom
        if (player.getY() > getY() + GameView2.HEIGHT - getyCoordSideRange() - 100) {
            delta = player.getY() - (getY() + GameView2.HEIGHT - getyCoordSideRange() - 100);
            setY(getY() + delta);
            if (this.getY() +  GameView2.HEIGHT > MapManager.getWorldHeight() + BORDER_THRESHOLD) this.setY(MapManager.getWorldHeight() - GameView2.HEIGHT + BORDER_THRESHOLD);
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
