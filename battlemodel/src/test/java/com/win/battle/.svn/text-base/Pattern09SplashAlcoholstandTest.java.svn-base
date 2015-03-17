/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.components.BMEffectsHolderComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.components.BMMoveComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;
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
 * Pattern N9 in the ../Requirements/templates.docx
 *
 * @author omelnyk
 */
public class Pattern09SplashAlcoholstandTest extends BaseBattleTest implements IModelCallback {

    private final static Logger logger = Logger.getLogger(Pattern09SplashAlcoholstandTest.class.getName());
    
    private BMField field = null;
    private static final String MY_UNIT_PREFIX = "myUnit";
    private static final String MY_UNIT1_IDENTITY = "myUnit1";
    private static final String MY_UNIT2_IDENTITY = "myUnit2";
    private static final String ENEMY_CAR_IDENTITY = "enemyCar";
    private static final String ENEMY_ALCOHOLSTAND = "enemyAlcoholstand";
    private final int AFFECTED_AREA_RADIUS = 10;
    private final int MAX_FAMES = 360;
    private int curFrame = 0;
    private Map<Integer, Boolean> alcoholstandActivityMap = new HashMap<>();
    private Map<Integer, String> myUnit1ActivityMap = new HashMap<>();
    private Map<Integer, String> myUnit2ActivityMap = new HashMap<>();
    private Map<Integer, Integer> myUnit1SpeedMap = new HashMap<>();
    private Map<Integer, Integer> myUnit2SpeedMap = new HashMap<>();
    private Map<Integer, String> myUnit1AppliedEffectsMap = new HashMap<>();
    private Map<Integer, String> myUnit2AppliedEffectsMap = new HashMap<>();
    private Map<Integer, String> myUnit1DistanceFromEpicenterMap = new HashMap<>();
    private Map<Integer, String> myUnit2DistanceFromEpicenterMap = new HashMap<>();
    private BMCell alcoholstandPosition;
    private Map<Integer, Integer> unit1EtalonActivityMap = new HashMap<>();    
    private int[][] unit1EtalonActivities = {{49, 0}, {50, 40}, {75, 40}, {76, 0}, {124, 0}, {125, 40},
        {150, 40}, {151, 0}, {199, 0}, {200, 40}, {225, 40}, {226, 0}, {274, 0}, {275, 40}, {300, 40},
        {301, 0}, {349, 0}, {350, 40}};
    private Map<Integer, Integer> unit2EtalonActivityMap = new HashMap<>();    
    private int[][] unit2EtalonActivities = {{20, 40}, {21, 0}, {69, 0}, {70, 40}, {75, 40}, {76, 0},
        {124, 0}, {125, 40},{150, 40}, {151, 0}, {199, 0}, {200, 40}, {225, 40}, {226, 0}, {274, 0}, 
        {275, 40}, {300, 40}, {301, 0}, {349, 0}, {350, 40}};    
    

    @Test
    public void test() {
        makeUnitsEtalonActivityMap();
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
        loader.load("config/wm_ptrn9.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn9.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        curFrame = engine.getTimeTicks();
        if (alcoholstandPosition == null) {
            alcoholstandPosition = getEntityByName(field, ENEMY_ALCOHOLSTAND).getPosition();
            
        }
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().startsWith(MY_UNIT_PREFIX)) {
                logUnitActivity(curEntity);
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(150);
            } else if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_CAR_IDENTITY)) {
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(12000);
            } else if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_ALCOHOLSTAND)) {
                logAlcoholstandActivity(curEntity);
            }
        }
        if (curFrame == MAX_FAMES) {
            executeEndLoop();
            engine.interrupt();
        }
    }
    
    private void makeUnitsEtalonActivityMap() {
        for (int i = 0; i < unit1EtalonActivities.length; i++) {
            unit1EtalonActivityMap.put(unit1EtalonActivities[i][0], unit1EtalonActivities[i][1]);
        }
        for (int i = 0; i < unit2EtalonActivities.length; i++) {
            unit2EtalonActivityMap.put(unit2EtalonActivities[i][0], unit2EtalonActivities[i][1]);
        }        
    }    
    
    private BMCell getAlcoholstandCenter(){
        return getEntityByName(field, ENEMY_ALCOHOLSTAND).getComponent(BMLargeLocatorComponent.class).getCenter();
    }

    private void logUnitActivity(BMFractionEntity curEntity) {
        String actionSign = "";
        String affectedAreaSign = "";
        if (curEntity.getActionState().contains(BMEntityActionState.idle)) {
            actionSign += "i";
        } else if (curEntity.getActionState().contains(BMEntityActionState.attack)) {
            actionSign += "a";
        } else if (curEntity.getActionState().contains(BMEntityActionState.move)) {
            actionSign += "m";
        }
        if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT1_IDENTITY)) {
            myUnit1ActivityMap.put(curFrame, actionSign);
            myUnit1SpeedMap.put(curFrame, Integer.valueOf(Double.valueOf(curEntity.getComponent(BMMoveComponent.class).calculateMoveSpeed()).intValue()));
            myUnit1AppliedEffectsMap.put(curFrame, ".");
            Map<String, BMBaseEffect.BMEffectHolder> effects = curEntity.getComponent(BMEffectsHolderComponent.class).getEffects();
            for (String key : effects.keySet()) {
                if (key.startsWith("mod_movespeed") && effects.get(key).size() > 0) {
                    myUnit1AppliedEffectsMap.put(curFrame, "e");
                    break;
                }
            }
            affectedAreaSign = (calcDistance(getAlcoholstandCenter(),curEntity.getPosition()) <= AFFECTED_AREA_RADIUS) ? "+":".";
            myUnit1DistanceFromEpicenterMap.put(curFrame, affectedAreaSign);

        } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT2_IDENTITY)) {
            myUnit2ActivityMap.put(curFrame, actionSign);
            myUnit2SpeedMap.put(curFrame, Integer.valueOf(Double.valueOf(curEntity.getComponent(BMMoveComponent.class).calculateMoveSpeed()).intValue()));
            myUnit2AppliedEffectsMap.put(curFrame, ".");
            Map<String, BMBaseEffect.BMEffectHolder> effects = curEntity.getComponent(BMEffectsHolderComponent.class).getEffects();
            for (String key : effects.keySet()) {
                if (key.startsWith("mod_movespeed") && effects.get(key).size() > 0) {
                    myUnit2AppliedEffectsMap.put(curFrame, "e");
                    break;
                }
            }
            affectedAreaSign = (calcDistance(getAlcoholstandCenter(),curEntity.getPosition()) <= AFFECTED_AREA_RADIUS) ? "+":".";
            myUnit2DistanceFromEpicenterMap.put(curFrame, affectedAreaSign);
            
        }

    }

    private void logAlcoholstandActivity(BMFractionEntity curEntity) {
        if (curEntity.getActionState().contains(BMEntityActionState.idle)) {
            alcoholstandActivityMap.put(curFrame, false);
        } else if (curEntity.getActionState().contains(BMEntityActionState.attack)) {
            alcoholstandActivityMap.put(curFrame, true);
        }

    }


    private Boolean checkMyUnit1Activity() {
        for (int i = 0; i < MAX_FAMES; i++) {
            if (unit1EtalonActivityMap.get(i) != null){
                int unitSpeed = myUnit1SpeedMap.get(i);
                int unitEtalSpeed = unit1EtalonActivityMap.get(i);
                   if (unitSpeed != unitEtalSpeed) {
                        return false;
                    }
            }
        }
        return true;        
    }
    
    private Boolean checkMyUnit2Activity() {
        for (int i = 0; i < MAX_FAMES; i++) {
            if (unit2EtalonActivityMap.get(i) != null){
                int unitSpeed = myUnit2SpeedMap.get(i);
                int unitEtalSpeed = unit2EtalonActivityMap.get(i);
                   if (unitSpeed != unitEtalSpeed) {
                        return false;
                   }
            }
        }
        return true;        
    }    

    private void printActivityTable() {
        String captionTitle          = "                   ";
        String alcoholstandTitle     = "alcoholstand       ";
        
        String myUnit1Title          = "unit 1             ";
        String myUnit1InAffectedArea = "in affected area   ";
        String myUnit1EffectsTitle   = "unit 1 Effects     ";
        String myUnit1Speed          = "unit 1 speed       ";
        String myUnit1EtalonSpeed    = "unit 1 etalon speed";
        
        String myUnit2Title          = "unit 2             ";
        String myUnit2InAffectedArea = "in affected area   ";
        String myUnit2EffectsTitle   = "unit 2 Effects     ";
        String myUnit2Speed          = "unit 2 speed       ";
        String myUnit2EtalonSpeed    = "unit 2 etalon speed";

        for (int i = 0; i < curFrame; i++) {
            if (alcoholstandActivityMap.get(i) != null) {
                captionTitle += Utility.addCharTo(" ", String.valueOf(i), 4);
                if (alcoholstandActivityMap.get(i)) {
                    alcoholstandTitle += Utility.addCharTo(" ", "O", 4);
                } else {
                    alcoholstandTitle += Utility.addCharTo(" ", ".", 4);
                }
                if (myUnit1ActivityMap.get(i) != null) {
                    myUnit1Title += Utility.addCharTo(" ", myUnit1ActivityMap.get(i), 4);
                }
                if (myUnit1DistanceFromEpicenterMap.get(i) != null) {
                    myUnit1InAffectedArea += Utility.addCharTo(" ", myUnit1DistanceFromEpicenterMap.get(i), 4);
                }
                if (myUnit1SpeedMap.get(i) != null) {
                    myUnit1Speed += Utility.addCharTo(" ", myUnit1SpeedMap.get(i).toString(), 4);
                }
                
                if (unit1EtalonActivityMap.get(i) != null) {
                    myUnit1EtalonSpeed += Utility.addCharTo(" ", String.valueOf(unit1EtalonActivityMap.get(i)), 4);
                } else {
                    myUnit1EtalonSpeed += Utility.addCharTo(" ", ".", 4);
                }  
                
                if (myUnit1AppliedEffectsMap.get(i) != null) {
                    myUnit1EffectsTitle += Utility.addCharTo(" ", String.valueOf(myUnit1AppliedEffectsMap.get(i)), 4);
                }
                if (myUnit2ActivityMap.get(i) != null) {
                    myUnit2Title += Utility.addCharTo(" ", myUnit2ActivityMap.get(i), 4);
                }
                if (myUnit2DistanceFromEpicenterMap.get(i) != null) {
                    myUnit2InAffectedArea += Utility.addCharTo(" ", myUnit2DistanceFromEpicenterMap.get(i), 4);
                }
                if (myUnit2SpeedMap.get(i) != null) {
                    myUnit2Speed += Utility.addCharTo(" ", myUnit2SpeedMap.get(i).toString(), 4);
                }
                if (unit2EtalonActivityMap.get(i) != null) {
                    myUnit2EtalonSpeed += Utility.addCharTo(" ", String.valueOf(unit2EtalonActivityMap.get(i)), 4);
                } else {
                    myUnit2EtalonSpeed += Utility.addCharTo(" ", ".", 4);
                }                 
                if (myUnit2AppliedEffectsMap.get(i) != null) {
                    myUnit2EffectsTitle += Utility.addCharTo(" ", String.valueOf(myUnit2AppliedEffectsMap.get(i)), 4);
                }

            }
        }
        
        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
            .append(captionTitle).append("\n")
            .append(alcoholstandTitle).append("\n").append("\n")
            .append(myUnit1Title).append("\n")
            .append(myUnit1InAffectedArea).append("\n")
            .append(myUnit1EffectsTitle).append("\n")
            .append(myUnit1Speed).append("\n")
            .append(myUnit1EtalonSpeed).append("\n").append("\n")
            .append(myUnit2Title).append("\n")
            .append(myUnit2InAffectedArea).append("\n")
            .append(myUnit2EffectsTitle).append("\n")
            .append(myUnit2Speed).append("\n")
            .append(myUnit2EtalonSpeed).append("\n").append("\n");
        logger.info(sOut.toString());
    }

    @Override
    public void executeEndLoop() {
        if (getUseResultOutput()) {
            printActivityTable();
        }
        Assert.assertTrue("My Unit 1 didn't got under influence of the Alcoholstand", checkMyUnit1Activity());
        Assert.assertTrue("My Unit 2 didn't got under influence of the Alcoholstand", checkMyUnit2Activity());
    }
}
