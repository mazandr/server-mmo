/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.utils.outputbattle.BattleConsoleOutput;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author omelnyk
 */

/* Parameter spd in the unit zombie section in wm_zombie_car.json is "4". 
   Thus zombie run 10 cells in 20 seconds
*/

public class Pattern05MovementTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern05MovementTest.class.getName());
    private BMField field = null;
    private Boolean isFirstTick = true;
    private static final String MY_UNIT_IDENTITY = "allyUnit1";
    private Boolean isMyUnitMoving = false;
    private static final int DISTANCE = 10;
    private static final int MOVING_TIME = 20;
    private BMCell myUnitStartPosition;
    private BMCell myUnitCurrentPosition;
    private Boolean isUnitRanOutDistance = false;
    private Boolean isMovingStarted = false;
    private Boolean isInterrupted = false;
    private int timeTicksStart = -1;
    private int timeTicksFinish = -1;

    @Test
    public void test() {
        engine.addCallback(this);
        if (getUseFullOutput()) {
            engine.addCallback(new BattleConsoleOutput(engine.getField()));
        }
        engine.run();
    }

    @Override
    public IWorldModelConfiguration getWorldModelConfiguration() {
//        RemoteWorldModelConfigLoader loader = new RemoteWorldModelConfigLoader();
//        loader.load(null);
        LocalWorldModelConfigLoader loader = new LocalWorldModelConfigLoader();
        loader.load("config/wm_zombie_car.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn5.json"); // 6 zombies in my car (test N2)
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        int curTimeTick = engine.getTimeTicks();
        field = engine.getField();
        if (curTimeTick == 0) {
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                    myUnitStartPosition = curEntity.getPosition();
                    break;
                }
            }
        } else {
            if (!isInterrupted) {
                for (BMFractionEntity curEntity : field.getAllEntities()) {
                    if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                        isMyUnitMoving = curEntity.getActionState().contains(BMEntityActionState.move);
                        myUnitCurrentPosition = curEntity.getPosition();
                        if (!isUnitRanOutDistance) {
                            isUnitRanOutDistance = isReachDistance(DISTANCE);
                            timeTicksFinish = engine.getTimeTicks();
                        } else {
                            logger.log(Level.INFO, "timeTicksFinish: {0}", timeTicksFinish);
                        }
                        break;
                    }
                }
            }
            if (((!isMyUnitMoving) || (isUnitRanOutDistance))&&(!isInterrupted)) {
                isInterrupted = true;
                executeEndLoop();
                engine.interrupt();
            }
        }
        if ((isMyUnitMoving) && (!isMovingStarted)) {
            isMovingStarted = true;
            timeTicksStart = engine.getTimeTicks();
        }


    }

    private Boolean isReachDistance(int distance) {
        return (calcDistance(myUnitCurrentPosition, myUnitStartPosition) == distance);
    }


    private int calcTime() {
        int res = (timeTicksFinish - timeTicksStart) / 10;
        return res;
    }

    @Override
    public void executeEndLoop() {
        Assert.assertEquals("My test unit moved as was told", MOVING_TIME, calcTime());
    }
}
