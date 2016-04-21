package Utils;

import com.miso.thegame.gameMechanics.GameObject;

/**
 * Created by michal.hornak on 20.01.2016.
 */
public class Initialize {

    public static void initGameObjectCoords(int x, int y, GameObject gO){
        gO.setX(x);
        gO.setY(y);
    }
}
