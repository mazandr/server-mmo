/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.components.BMAllSeeAllVisionComponent;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author omelnyk
 */
public class Pattern04MovementTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern04MovementTest.class.getName());
    
    private BMField field = null;
    private Boolean isFirstTick = true;
    private static final String MY_CAR_A_IDENTITY = "mycarA";
    private static final String MY_CAR_B_IDENTITY = "mycarB";
    private static final String MY_UNIT1_IDENTITY = "allyUnit1";
    private static final String MY_UNIT2_IDENTITY = "allyUnit2";
    private static final String MY_UNIT3_IDENTITY = "allyUnit3";
    private static final String MY_UNITS_PREFIX = "allyUnit";
    private static final String ENEMY_CAR_IDENTITY = "enemycar";
    private int curTimeTick;
    private BMBaseBuilding myCarA = null;
    private BMBaseBuilding myCarB = null;
    private BMBaseBuilding enemyCar = null;
    private Boolean isMyUnitAlive = true;
    private Boolean isMyUnitInCar = false;
    private EnumSet<BMEntityActionState> unitState = null;
    private BMCell myUnit1StartPosition;
    private BMCell myUnit1FinalPosition;
    private BMCell myUnit2StartPosition;
    private BMCell myUnit2FinalPosition;
    private BMCell myUnit3StartPosition;
    private BMCell myUnit3FinalPosition;
    private String rightDefenceObjectName;
    private int myUnitAttackRange;
    private Map<Integer, String> myUnit1ActivityMap = new HashMap<>();
    private Map<Integer, Integer> myUnit1HpMap = new HashMap<>();
    private Map<Integer, Float> myUnit1distance2BuildingAMap = new HashMap<>();
    private Map<Integer, Float> myUnit1distance2BuildingBMap = new HashMap<>();
    private Map<Integer, String> myUnit2ActivityMap = new HashMap<>();
    private Map<Integer, Integer> myUnit2HpMap = new HashMap<>();
    private Map<Integer, Float> myUnit2distance2BuildingAMap = new HashMap<>();
    private Map<Integer, Float> myUnit2distance2BuildingBMap = new HashMap<>();
    private Map<Integer, String> myUnit3ActivityMap = new HashMap<>();
    private Map<Integer, Integer> myUnit3HpMap = new HashMap<>();
    private Map<Integer, Float> myUnit3distance2BuildingAMap = new HashMap<>();
    private Map<Integer, Float> myUnit3distance2BuildingBMap = new HashMap<>();
    private boolean interruptFlag = false;

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
        loader.load("config/wm_ptrn4.json");
//        loader.load("config/wm_remote.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn4.json");
//        loader.load("config/bt_ptrn4npc4.json");
        return loader;
    }

    private BMFractionEntity getEntityByName(String entityName) {
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().equalsIgnoreCase(entityName)) {
                return curEntity;
            }
        }
        return null;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        curTimeTick = engine.getTimeTicks();
        myCarA = field.getBuildings().get(MY_CAR_A_IDENTITY);
        myCarB = field.getBuildings().get(MY_CAR_B_IDENTITY);
        enemyCar = field.getBuildings().get(ENEMY_CAR_IDENTITY);
        if (isFirstTick) {
            isFirstTick = false;
            myUnit1StartPosition = getEntityByName(MY_UNIT1_IDENTITY).getPosition();
            myUnit2StartPosition = getEntityByName(MY_UNIT2_IDENTITY).getPosition();
            myUnit3StartPosition = getEntityByName(MY_UNIT3_IDENTITY).getPosition();
            defineDefenceObject();
        } else {
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT1_IDENTITY)) {
//                    curEntity.getComponent(BMHealsComponent.class).setCurrentHP(150); // we recreate HP level
                    isMyUnitAlive = curEntity.getComponent(BMHealsComponent.class).getCurrentHP() > 0;
                    myUnitAttackRange = curEntity.getComponent(BMAllSeeAllVisionComponent.class).getAttackRange();
                    unitState = curEntity.getActionState();
                    isMyUnitInCar = isMyUnitInCar || unitState.contains(BMEntityActionState.capture);
                    if (isMyUnitInCar) {
                        myUnit1FinalPosition = curEntity.getPosition();
                    }
                    logUnitActivity(curEntity);
                } else if ((curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT2_IDENTITY))||(curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT3_IDENTITY))) {
                    logUnitActivity(curEntity);
                } else if ((curEntity.getIdentifier().equalsIgnoreCase(MY_CAR_A_IDENTITY))||
                           (curEntity.getIdentifier().equalsIgnoreCase(MY_CAR_B_IDENTITY))||
                           (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_CAR_IDENTITY))
                          ) {
                    curEntity.getComponent(BMHealsComponent.class).setCurrentHP(1200); // we recreate HP level;
                } 
            }
            if (getUseFullOutput()) {
                printAllEntities(field);
            }

            if(interruptFlag) {
                executeEndLoop();
                engine.interrupt();
            }

            if ((!isMyUnitAlive) || (isMyUnitInCar)) {
                interruptFlag = true;
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
        } else {
            actionSign += ".";
        }

        if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT1_IDENTITY)) {
            myUnit1ActivityMap.put(curTimeTick, actionSign);
            myUnit1HpMap.put(curTimeTick, curEntity.getComponent(BMHealsComponent.class).getCurrentHP());
            myUnit1distance2BuildingAMap.put(curTimeTick, calcDistance(curEntity.getPosition(), getEntityByName(MY_CAR_A_IDENTITY).getPosition()));
            myUnit1distance2BuildingBMap.put(curTimeTick, calcDistance(curEntity.getPosition(), getEntityByName(MY_CAR_B_IDENTITY).getPosition()));
        } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT2_IDENTITY)) {
            myUnit2ActivityMap.put(curTimeTick, actionSign);
            myUnit2HpMap.put(curTimeTick, curEntity.getComponent(BMHealsComponent.class).getCurrentHP());
            myUnit2distance2BuildingAMap.put(curTimeTick, calcDistance(curEntity.getPosition(), getEntityByName(MY_CAR_A_IDENTITY).getPosition()));
            myUnit2distance2BuildingBMap.put(curTimeTick, calcDistance(curEntity.getPosition(), getEntityByName(MY_CAR_B_IDENTITY).getPosition()));
        } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT3_IDENTITY)) {
            myUnit3ActivityMap.put(curTimeTick, actionSign);
            myUnit3HpMap.put(curTimeTick, curEntity.getComponent(BMHealsComponent.class).getCurrentHP());
            myUnit3distance2BuildingAMap.put(curTimeTick, calcDistance(curEntity.getPosition(), getEntityByName(MY_CAR_A_IDENTITY).getPosition()));
            myUnit3distance2BuildingBMap.put(curTimeTick, calcDistance(curEntity.getPosition(), getEntityByName(MY_CAR_B_IDENTITY).getPosition()));
            
        }
    }

    @Override
    public void executeEndLoop() {
//      Method for printing activity table        
        if (getUseResultOutput()) {
            printActivityTable();
        }
        Assert.assertTrue("My test unit (unit 1) isn't alive", isMyUnitAlive);
        Assert.assertTrue("My test unit (unit 1) isn't in the nearest car", isMyUnitInCar);
        Assert.assertTrue("My unit (unit 1) choosed the wrong object as defence ", checkIsCarCapturedByRightObject());
    }

    private void defineDefenceObject() {
        float distFromMyUnitToCarA = calcDistance(myUnit1StartPosition, myCarA.getPosition());
        float distFromMyUnitToCarB = calcDistance(myUnit1StartPosition, myCarB.getPosition());
        float distFromCarAToEnemyUnit = calcDistance(myCarA.getPosition(), enemyCar.getPosition());
        float distFromCarBToEnemyUnit = calcDistance(myCarB.getPosition(), enemyCar.getPosition());
        if (distFromMyUnitToCarA + distFromCarAToEnemyUnit < distFromMyUnitToCarB + distFromCarBToEnemyUnit) {
            rightDefenceObjectName = MY_CAR_A_IDENTITY;
        } else {
            rightDefenceObjectName = MY_CAR_B_IDENTITY;
        }
    }

    private Boolean checkIsCarCapturedByRightObject() {
        Boolean res = false;
        float dest2A = calcDistance(myUnit1FinalPosition, myCarA.getPosition());
        float dest2B = calcDistance(myUnit1FinalPosition, myCarB.getPosition());
        if (((dest2A < dest2B) && (rightDefenceObjectName == MY_CAR_A_IDENTITY))
                || ((dest2A > dest2B) && (rightDefenceObjectName == MY_CAR_B_IDENTITY))
                || (dest2A == dest2B)) {
            res = true;
        }
        return res;
    }

    private void printActivityTable() {
        String captionTitle                   = "            ";
        String myUnit1Title                   = "u1          ";
        String myUnit1HpTitle                 = "u1 HP       ";
        String myUnit1distance2BuildingATitle = "dist u1 to A";
        String myUnit1distance2BuildingBTitle = "dist u1 to B";
        String myUnit2Title                   = "u2          ";
        String myUnit2HpTitle                 = "u2 HP       ";
        String myUnit2distance2BuildingATitle = "dist u2 to A";
        String myUnit2distance2BuildingBTitle = "dist u2 to B";
        String myUnit3Title                   = "u3          ";
        String myUnit3HpTitle                 = "u3 HP       ";
        String myUnit3distance2BuildingATitle = "dist u3 to A";
        String myUnit3distance2BuildingBTitle = "dist u3 to B";
        for (int i = 0; i < curTimeTick; i++) {
            if (myUnit1ActivityMap.get(i) != null) {
                captionTitle += Utility.addCharTo(" ", String.valueOf(i), 6);
                if (myUnit1ActivityMap.get(i) != null) {
                    myUnit1Title += Utility.addCharTo(" ", myUnit1ActivityMap.get(i), 6);
                }
                if (myUnit1HpMap.get(i) != null) {
                    myUnit1HpTitle += Utility.addCharTo(" ", String.valueOf(myUnit1HpMap.get(i)), 6);
                }
                if (myUnit1distance2BuildingAMap.get(i) != null) {
                    myUnit1distance2BuildingATitle += Utility.addCharTo(" ", String.valueOf(myUnit1distance2BuildingAMap.get(i)), 6);
                }
                if (myUnit1distance2BuildingBMap.get(i) != null) {
                    myUnit1distance2BuildingBTitle += Utility.addCharTo(" ", String.valueOf(myUnit1distance2BuildingBMap.get(i)), 6);
                }
                if (myUnit2ActivityMap.get(i) != null) {
                    myUnit2Title += Utility.addCharTo(" ", myUnit2ActivityMap.get(i), 6);
                }
                if (myUnit2HpMap.get(i) != null) {
                    myUnit2HpTitle += Utility.addCharTo(" ", String.valueOf(myUnit2HpMap.get(i)), 6);
                }
                if (myUnit2distance2BuildingAMap.get(i) != null) {
                    myUnit2distance2BuildingATitle += Utility.addCharTo(" ", String.valueOf(myUnit2distance2BuildingAMap.get(i)), 6);
                }
                if (myUnit2distance2BuildingBMap.get(i) != null) {
                    myUnit2distance2BuildingBTitle += Utility.addCharTo(" ", String.valueOf(myUnit2distance2BuildingBMap.get(i)), 6);
                }
                if (myUnit3ActivityMap.get(i) != null) {
                    myUnit3Title += Utility.addCharTo(" ", myUnit3ActivityMap.get(i), 6);
                }
                if (myUnit3HpMap.get(i) != null) {
                    myUnit3HpTitle += Utility.addCharTo(" ", String.valueOf(myUnit3HpMap.get(i)), 6);
                }
                if (myUnit3distance2BuildingAMap.get(i) != null) {
                    myUnit3distance2BuildingATitle += Utility.addCharTo(" ", String.valueOf(myUnit3distance2BuildingAMap.get(i)), 6);
                }
                if (myUnit3distance2BuildingBMap.get(i) != null) {
                    myUnit3distance2BuildingBTitle += Utility.addCharTo(" ", String.valueOf(myUnit3distance2BuildingBMap.get(i)), 6);
                }
                
            }
        }

        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
            .append(captionTitle).append("\n")
            .append(myUnit1Title).append("\n")
            .append(myUnit1HpTitle).append("\n")
            .append(myUnit1distance2BuildingATitle).append("\n")
            .append(myUnit1distance2BuildingBTitle).append("\n")
            .append(myUnit2Title).append("\n")
            .append(myUnit2HpTitle).append("\n")
            .append(myUnit2distance2BuildingATitle).append("\n")
            .append(myUnit2distance2BuildingBTitle).append("\n")
            .append(myUnit3Title).append("\n")
            .append(myUnit3HpTitle).append("\n")
            .append(myUnit3distance2BuildingATitle).append("\n")
            .append(myUnit3distance2BuildingBTitle).append("\n");               
        logger.info(sOut.toString());              
        
    }
}
