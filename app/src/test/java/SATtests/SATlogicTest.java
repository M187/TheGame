package SATtests;

import Utils.Initialize;
import com.miso.thegame.gameMechanics.collisionHandlers.SATCollisionCalculator;
import com.miso.thegame.gameMechanics.movingObjects.Player;
import com.miso.thegame.gameMechanics.movingObjects.enemies.groundEnemies.Enemy_basic;

import junit.framework.TestCase;

/**
 * Created by michal.hornak on 20.01.2016.
 */
public class SATlogicTest extends TestCase {

    private Player mockPlayer;
    private Enemy_basic mockEnemyBasic;
    SATCollisionCalculator sat = new SATCollisionCalculator();

    public void setUp(){
        Initialize.initGameObjectCoords(500,500, this.mockPlayer);
        Initialize.initGameObjectCoords(510,510, this.mockEnemyBasic);
    }

    public void runTest(){
        assert (sat.performSeparateAxisCollisionCheck(mockPlayer.getObjectCollisionVertices(), mockEnemyBasic.getObjectCollisionVertices()));
    }
}
