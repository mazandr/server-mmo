/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.buildings.BMBuildingState;
import com.win.strategy.battle.utils.outputbattle.BattleConsoleOutput;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author omelnyk
 */
public class Pattern13DamageTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern13DamageTest.class.getName());
    private static final String MY_UNIT_IDENTITY = "myUnit";
    private static final String ENEMY_UNIT_IDENTITY = "enemyCar_unit_0";
    private static final String ENEMY_CAR_IDENTITY = "enemyCar";
    private Boolean isFirstTick = true;
    private BMField field = null;
    private BMBaseBuilding enemyCar = null;
    private int myUnitFinalHP = 0;
    private final int MAX_FAMES = 550;
    private int myUnitsCountGlobal = 0;
    private int enemyUnitsCountGlobal = 0;
    private Boolean isEnemyCarDestroyed = false;
    private int curFrame = 0;
    private Map<Integer, String> myUnitActivityMap = new HashMap<>();
    private Map<Integer, String> enemyUnitActivityMap = new HashMap<>();
    private Map<Integer, String> enemyCarActivityMap = new HashMap<>();
    private Map<Integer, String> enemyCarEtalonActivityMap;
    private String[][] enemyCarEtalonActivities = {{"478", "i"}, {"479", "x"}};
    private Map<Integer, String> enemyUnitEtalonActivityMap;
    private String[][] enemyUnitEtalonActivities = {{"478", "c"}, {"479", "l"}, {"543", "l"}, {"544", "x"}};

    @Test
    public void test() {
        makeObjectsEtalonActivityMap();
        engine.addCallback(this);
        if (getUseFullOutput()) {
            engine.addCallback(new BattleConsoleOutput(engine.getField()));
        }
        engine.run();
    }

    private void makeObjectsEtalonActivityMap() {
        enemyCarEtalonActivityMap = new HashMap<>();
        for (int i = 0; i < enemyCarEtalonActivities.length; i++) {
            enemyCarEtalonActivityMap.put(Integer.parseInt(enemyCarEtalonActivities[i][0]), enemyCarEtalonActivities[i][1]);
        }
        enemyUnitEtalonActivityMap = new HashMap<>();
        for (int i = 0; i < enemyUnitEtalonActivities.length; i++) {
            enemyUnitEtalonActivityMap.put(Integer.parseInt(enemyUnitEtalonActivities[i][0]), enemyUnitEtalonActivities[i][1]);
        }

    }

    @Override
    public IWorldModelConfiguration getWorldModelConfiguration() {
//        RemoteWorldModelConfigLoader loader = new RemoteWorldModelConfigLoader();
//        loader.load(null);
        LocalWorldModelConfigLoader loader = new LocalWorldModelConfigLoader();
        loader.load("config/wm_ptrn13.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn13.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        if (isFirstTick) {
            isFirstTick = false;
            enemyCar = field.getBuildings().get(ENEMY_CAR_IDENTITY);
            enemyCar.setCurrentHP(1200);
        } else {
            enemyCar = field.getBuildings().get(ENEMY_CAR_IDENTITY);
            isEnemyCarDestroyed = enemyCar.getBuildingState() == BMBuildingState.destroyed;
            int myUnitsCount = 0;
            int enemyUnitsCount = 0;
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                    myUnitFinalHP = curEntity.getComponent(BMHealsComponent.class).getCurrentHP();
                    myUnitsCount++;
                } else if ((curEntity.getIdentifier().equalsIgnoreCase(ENEMY_CAR_IDENTITY))
                        || (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_UNIT_IDENTITY))) {
                    enemyUnitsCount++;
                }
                logUnitActivity(curEntity);
            }
            myUnitsCountGlobal = myUnitsCount;
            enemyUnitsCountGlobal = enemyUnitsCount;
            curFrame++;
            if ((enemyUnitsCount == 0) || (myUnitsCount == 0)) {
                executeEndLoop();
                engine.interrupt();
            }
        }

    }

    private void logUnitActivity(BMFractionEntity curEntity) {
        String actionSign = "";
        if (curEntity.getActionState().contains(BMEntityActionState.idle)) {
            actionSign += "i";
        } else if (curEntity.getActionState().contains(BMEntityActionState.attack)) {
            actionSign += "a";
        } else if (curEntity.getActionState().contains(BMEntityActionState.move)) {
            actionSign += "m";
        } else if (curEntity.getActionState().contains(BMEntityActionState.capture)) {
            actionSign += "c";
        } else if (curEntity.getActionState().contains(BMEntityActionState.land)) {
            actionSign += "l";
        }
        if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
            myUnitActivityMap.put(curFrame, actionSign);
        } else if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_CAR_IDENTITY)) {
            enemyCarActivityMap.put(curFrame, actionSign);
        }
        if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_UNIT_IDENTITY)) {
            enemyUnitActivityMap.put(curFrame, actionSign);
        }

    }

    @Override
    public void executeEndLoop() {
        if (getUseResultOutput()) {
            printActivityTable();
            printAllEntities(field);
//            System.out.println("EnemyCarActivityTime: " + getEnemyCarActivityTime());
//            System.out.println("EnemyUnitActivityTime: " + getEnemyUnitActivityTime());
        }
        Assert.assertEquals("My unit final HP ", 100, myUnitFinalHP);
        Assert.assertTrue("Enemy car is destroyed", isEnemyCarDestroyed);
        Assert.assertTrue("Enemy car didn't fight 48s ", checkEnemyCarStatus());
        Assert.assertTrue("Enemy unit didn't fight  6s ", checkEnemyUnitStatus());
    }

    private void printActivityTable() {
        String captionTitle = "          ";
        String myUnitTitle = "my unit   ";
        String enemyCarTitle = "enemy car ";
        String enemyUnitTitle = "enemy unit";
        for (int i = 0; i < curFrame; i++) {
            captionTitle += Utility.addCharTo(" ", String.valueOf(i), 4);
            if (myUnitActivityMap.get(i) != null) {
                myUnitTitle += Utility.addCharTo(" ", myUnitActivityMap.get(i), 4);
            } else {
                myUnitTitle += Utility.addCharTo(" ", "x", 4);
            }
            if (enemyCarActivityMap.get(i) != null) {
                enemyCarTitle += Utility.addCharTo(" ", enemyCarActivityMap.get(i), 4);
            } else {
                enemyCarTitle += Utility.addCharTo(" ", "x", 4);
            }
            if (enemyUnitActivityMap.get(i) != null) {
                enemyUnitTitle += Utility.addCharTo(" ", enemyUnitActivityMap.get(i), 4);
            } else {
                enemyUnitTitle += Utility.addCharTo(" ", "x", 4);
            }

        }

        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
                .append(captionTitle).append("\n")
                .append(myUnitTitle).append("\n")
                .append(enemyCarTitle).append("\n")
                .append(enemyUnitTitle).append("\n");
        logger.info(sOut.toString());

    }

    private Boolean checkEnemyCarStatus() {
        for (int i = 0; i < curFrame; i++) {
            if (enemyCarEtalonActivityMap.get(i) != null) {
                String carState = enemyCarActivityMap.get(i);
                String etalState = enemyCarEtalonActivityMap.get(i);
                if (carState != null) {
                    if (!carState.equalsIgnoreCase(etalState)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Boolean checkEnemyUnitStatus() {
        for (int i = 0; i < curFrame; i++) {
            if (enemyUnitEtalonActivityMap.get(i) != null) {
                String enemyUnitState = enemyUnitActivityMap.get(i);
                String etalState = enemyUnitEtalonActivityMap.get(i);
                if (enemyUnitState != null) {
                    if (!enemyUnitState.equalsIgnoreCase(etalState)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int getEnemyCarActivityTime() {
        int frameAtWhichCarWasDestroyed = 0;
        for (int i = 0; i < curFrame; i++) {
            if (enemyCarActivityMap.get(i) == null) {
                frameAtWhichCarWasDestroyed = i;
                break;
            }
        }
        int carActivityTime = Math.round(frameAtWhichCarWasDestroyed / 10);
        return carActivityTime;
    }

    private int getEnemyUnitActivityTime() {
        int frameAtWhichUnitAppeared = 0;
        int frameAtWhichUnitDisappeared = 0;
        Boolean isUnitAppeared = false;
        for (int i = 0; i < curFrame; i++) {
            if (enemyCarActivityMap.get(i) != null) {
                frameAtWhichUnitAppeared = i;
                isUnitAppeared = true;
            }
            if ((enemyCarActivityMap.get(i) == null) && isUnitAppeared) {
                frameAtWhichUnitDisappeared = i;
            }
        }
        int carActivityTime = Math.round((frameAtWhichUnitDisappeared - frameAtWhichUnitAppeared) / 10);
        return carActivityTime;
    }
}
