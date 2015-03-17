/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.utils.outputbattle.BattleConsoleOutput;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.EnumSet;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author omelnyk
 */
public class Pattern03MovementTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern03MovementTest.class.getName());
    
    private BMField field = null;
    private Boolean isFirstTick = true;
    private static final String MY_CAR_IDENTITY = "mycar";
    private static final String MY_UNIT_IDENTITY = "allyUnit1";
    private static final String ENEMY_CAR_IDENTITY = "enemycar";
    private BMBaseBuilding myCar = null;
    private BMBaseBuilding enemyCar = null;
    private Boolean isMyUnitAlive = true;
    private Boolean isMyUnitUnderDefence = false;
    private Boolean isMyUnitAtacking = false;
    private EnumSet<BMEntityActionState> unitState = null;
    private BMCell myUnitAtackPosition;

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
        loader.load("config/bt_ptrn3.json"); // 6 zombies in my car (test N2)
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        myCar = field.getBuildings().get(MY_CAR_IDENTITY);
        enemyCar = field.getBuildings().get(ENEMY_CAR_IDENTITY);
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                isMyUnitAlive = curEntity.getComponent(BMHealsComponent.class).getCurrentHP() > 0;
                unitState = curEntity.getActionState();
                isMyUnitUnderDefence = unitState.contains(BMEntityActionState.capture);
                isMyUnitAtacking = isMyUnitAtacking || unitState.contains(BMEntityActionState.attack);
                if (isMyUnitAtacking)
                    myUnitAtackPosition = curEntity.getPosition();
            } else if ((curEntity.getIdentifier().equalsIgnoreCase(MY_CAR_IDENTITY))
                    || (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_CAR_IDENTITY))) {
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(1200); // reheal
            }
            if ((isMyUnitUnderDefence) || (isMyUnitAtacking)) {
                executeEndLoop();
                engine.interrupt();
            }
        }
        if (getUseFullOutput()) {
            printAllEntities(field);
        }
    }

    @Override
    public void executeEndLoop() {
        if (getUseFullOutput()) {
//            System.out.println("\nfinal frame: ");
//            System.out.println("\nmy unit atack position: " + myUnitAtackPosition);
            printAllEntities(field);
        }
        Assert.assertTrue("My test unit is attacking", isMyUnitAtacking);
        Assert.assertFalse("My test unit is not under defence ", isMyUnitUnderDefence);
    }
}
