package com.miso.thegame.Networking;

import java.util.ArrayList;

/**
 * Created by michal.hornak on 20.04.2016.
 *
 * Class to synchronize gameState with other instance.
 *
 * Main purpose is synchronize method.
 */
public class Synchronizer {

    public Synchronizer(){

    }

    /**
     * Method to synchronize this game instance with other game instances.
     *
     * Big switch to process incoming messages and decide what to do with them.
     */
    public void synchronizeGameState(ArrayList<Client> clientConnections){}
}
