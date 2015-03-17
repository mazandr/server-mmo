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
import com.win.strategy.battle.utils.outputbattle.BattleConsoleOutput;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Pattern N10 in the ../Requirements/templates.docx
 *
 * @author omelnyk
 */
public class Pattern10CapacityTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern10CapacityTest.class.getName());
    private BMField field = null;
    private static final String MY_UNIT1_IDENTITY = "A1";
    private static final String MY_UNIT2_IDENTITY = "A2";
    private static final String MY_CAR_IDENTITY = "C";
    private static final String ENEMY_CAR_IDENTITY = "enemyCar";
    private int curFrame = 0;
//    private Map<Integer, Boolean> megaphoneActivityMap = new HashMap<>();
    private Map<Integer, String> myUnit1ActivityMap = new HashMap<>();
    private Map<Integer, String> myUnit2ActivityMap = new HashMap<>();
    private Map<Integer, Integer> myCarCapacityMap = new HashMap<>();

//    @Ignore
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
        loader.load("config/wm_ptrn10.json");
//        loader.load("config/wm.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn10.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        curFrame = engine.getTimeTicks();
        Boolean isMyUnitAlive = true;
        if (curFrame == 0) {
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT1_IDENTITY) || curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT2_IDENTITY)) {
                    curEntity.setCanBeCaptured(true);
                }
            }
        }
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT1_IDENTITY) || curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT2_IDENTITY)) {
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(150);
                logUnitActivity(curEntity);
            } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_CAR_IDENTITY) || curEntity.getIdentifier().equalsIgnoreCase(ENEMY_CAR_IDENTITY)) {
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(1200);
            }
        }
        if (getUseFullOutput()) {
            printAllEntities(field);
        }
        if (curFrame == 100) {
            executeEndLoop();
            engine.interrupt();
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
        if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT1_IDENTITY)) {
            myUnit1ActivityMap.put(curFrame, actionSign);
        } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT2_IDENTITY)) {
            myUnit2ActivityMap.put(curFrame, actionSign);
//            int curHp = curEntity.getComponent(BMHealsComponent.class).getCurrentHP(); 
//            myUnitHpMap.put(curFrame, curHp);
        }
    }

    private void printActivityTable() {
        String captionTitle = "          ";
        String myUnit1Title = "my unit 1 ";
        String myUnit2Title = "my unit 2 ";
        for (int i = 0; i < curFrame; i++) {
            captionTitle += Utility.addCharTo(" ", String.valueOf(i), 4);
            if (myUnit1ActivityMap.get(i) != null) {
                myUnit1Title += Utility.addCharTo(" ", myUnit1ActivityMap.get(i), 4);
            }
            if (myUnit2ActivityMap.get(i) != null) {
                myUnit2Title += Utility.addCharTo(" ", myUnit2ActivityMap.get(i), 4);
            }

        }
        
        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
            .append(captionTitle).append("\n")
            .append(myUnit1Title).append("\n")
            .append(myUnit2Title).append("\n");
        logger.info(sOut.toString());         
    }

    @Override
    public void executeEndLoop() {
//      Method for printing activity table    
        if (getUseResultOutput()) {
            printActivityTable();
            printAllEntities(field);
        }
        Assert.assertTrue("None of my Units 1 or 2 has been captured by the car", checkMyUnitsActivity());
    }

    private boolean checkMyUnitsActivity() {
        Boolean isUnit1Captured = false;
        Boolean isUnit2Captured = false;

        for (int i = 0; i < curFrame; i++) {
            if (myUnit1ActivityMap.get(i) != null) {
                isUnit1Captured = isUnit1Captured || myUnit1ActivityMap.get(i).trim().equalsIgnoreCase("c");
            }
            if (myUnit2ActivityMap.get(i) != null) {
                isUnit2Captured = isUnit2Captured || myUnit2ActivityMap.get(i).trim().equalsIgnoreCase("c");
            }
        }
        return (isUnit1Captured && !isUnit2Captured) || (!isUnit1Captured && isUnit2Captured);
    }
}
