package com.miso.thegame.gameMechanics;

/**
 * Created by michal.hornak on 28.09.2016.
 */
public class GameState {

    private final int TIMEOUT = 3000;
    private GameStates currentGameState;
    private long timeOfEndgameEvent = 0L;
    public GameState() {
        playing();
    }

    public void victory(){
        this.currentGameState = GameStates.victory;
        this.timeOfEndgameEvent = System.currentTimeMillis();
    }

    public void defeated(){
        this.currentGameState = GameStates.defeated;
        this.timeOfEndgameEvent = System.currentTimeMillis();
    }

    public void playing(){
        this.currentGameState = GameStates.playing;
    }

    public GameStates getGameState(){
        return this.currentGameState;
    }

    /**
     * After victory and defeat was set as a game state, we need time to informe player about this event.
     * @return if enough time has passed from changing of an game state.
     */
    public boolean eventTimedOut(){
        if (timeOfEndgameEvent > 0
                & System.currentTimeMillis() - timeOfEndgameEvent > TIMEOUT ){
            return true;
        }
        return false;

    }

    public enum GameStates{
        playing, victory, defeated
    }
}
