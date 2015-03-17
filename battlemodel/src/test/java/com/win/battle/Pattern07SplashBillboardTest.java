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
 * Pattern N7 in the ../Requirenments/templates.docx
 *
 * @author omelnyk
 */
public class Pattern07SplashBillboardTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern07SplashBillboardTest.class.getName());
    
    private BMField field = null;
    private static final String MY_UNIT_IDENTITY = "A";
    private static final String MY_BILLBOARD = "B";
    private static final String ENEMY_UNIT_PREFIX = "E";
    private static final String ENEMY_UNIT1_IDENTITY = "E1";
    private static final String ENEMY_UNIT2_IDENTITY = "E2";
    private static final String ENEMY_UNIT3_IDENTITY = "E3";
    private final int MAX_FAMES = 220;
    private int curFrame = 0;
    private Map<Integer, Boolean> billboardActivityMap;
    private Map<Integer, String> enemyUnit1ActivityMap;
    private Map<Integer, Float> enemyUnit1DistanceToBillboardMap;
    private Map<Integer, String> enemyUnit2ActivityMap;
    private Map<Integer, Float> enemyUnit2DistanceToBillboardMap;
    private Map<Integer, String> enemyUnit3ActivityMap;
    private Map<Integer, Float> enemyUnit3DistanceToBillboardMap;
    private BMCell billboardPosition = null;
    
    private Map<Integer, String> unit1EtalonActivityMap;
    private String[][] unit1EtalonActivities = {{"63", "m"}, {"64", "i"}, {"100", "i"}, {"101", "m"}, 
        {"176", "m"}, {"177", "i"}, {"201", "i"}, {"202", "m"}};

    private Map<Integer, String> unit2EtalonActivityMap;
    private String[][] unit2EtalonActivities = {{"60", "m"}, {"61", "i"}, {"100", "i"}, {"101", "m"}, 
        {"170", "m"}, {"171", "i"}, {"201", "i"}, {"202", "m"}};

    private Map<Integer, String> unit3EtalonActivityMap;
    private String[][] unit3EtalonActivities = {{"60", "m"}, {"61", "m"}, {"101", "m"}, {"102", "m"}, 
        {"170", "m"}, {"171", "m"}, {"185", "m"}, {"186", "i"}, {"201", "i"}, {"202", "m"}};
    

    @Test
    public void test() {
        makeUnitsEtalonActivityMap();
        engine.addCallback(this);
        if (getUseFullOutput()) {
            engine.addCallback(new BattleConsoleOutput(engine.getField()));
        }
        engine.run();
    }
    
    private void makeUnitsEtalonActivityMap() {
        unit1EtalonActivityMap  = new HashMap<>();
        for (int i = 0; i < unit1EtalonActivities.length; i++) {
            unit1EtalonActivityMap.put(Integer.parseInt(unit1EtalonActivities[i][0]), unit1EtalonActivities[i][1]);
        }
        unit2EtalonActivityMap  = new HashMap<>();
        for (int i = 0; i < unit2EtalonActivities.length; i++) {
            unit2EtalonActivityMap.put(Integer.parseInt(unit2EtalonActivities[i][0]), unit2EtalonActivities[i][1]);
        }        
        unit3EtalonActivityMap  = new HashMap<>();
        for (int i = 0; i < unit3EtalonActivities.length; i++) {
            unit3EtalonActivityMap.put(Integer.parseInt(unit3EtalonActivities[i][0]), unit3EtalonActivities[i][1]);
        }            
    }    

    @Override
    public IWorldModelConfiguration getWorldModelConfiguration() {
//        RemoteWorldModelConfigLoader loader = new RemoteWorldModelConfigLoader();
//        loader.load(null);
        LocalWorldModelConfigLoader loader = new LocalWorldModelConfigLoader();
        loader.load("config/wm_ptrn7.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn7.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        curFrame = engine.getTimeTicks();
        if (curFrame == 0) {
            billboardActivityMap = new HashMap<>();
            enemyUnit1ActivityMap = new HashMap<>();
            enemyUnit1DistanceToBillboardMap = new HashMap<>();
            enemyUnit2ActivityMap = new HashMap<>();
            enemyUnit2DistanceToBillboardMap = new HashMap<>();
            enemyUnit3ActivityMap = new HashMap<>();
            enemyUnit3DistanceToBillboardMap = new HashMap<>();

            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().equalsIgnoreCase(MY_BILLBOARD)) {
                    if (billboardPosition == null) {
                        billboardPosition = curEntity.getPosition();
                    }
                }
            }

        }
        Boolean isAllEnemyUnitsIdle = true;
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().startsWith(ENEMY_UNIT_PREFIX) || curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                logUnitActivity(curEntity);
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(150); //recreate unit's HP
                isAllEnemyUnitsIdle = isAllEnemyUnitsIdle && curEntity.getActionState().contains(BMEntityActionState.idle);
            } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_BILLBOARD)) {
                logBillboardActivity(curEntity);
            }
        }
        if ((isAllEnemyUnitsIdle)||(curFrame == MAX_FAMES)) {
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
        }
        if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_UNIT1_IDENTITY)) {
            enemyUnit1ActivityMap.put(curFrame, actionSign);
            enemyUnit1DistanceToBillboardMap.put(curFrame,calcDistance(billboardPosition, curEntity.getPosition()));
        } else if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_UNIT2_IDENTITY)) {
            enemyUnit2ActivityMap.put(curFrame, actionSign);
            enemyUnit2DistanceToBillboardMap.put(curFrame,calcDistance(billboardPosition, curEntity.getPosition()));
        } else if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_UNIT3_IDENTITY)) {
            enemyUnit3ActivityMap.put(curFrame, actionSign);
            enemyUnit3DistanceToBillboardMap.put(curFrame,calcDistance(billboardPosition, curEntity.getPosition()));
            
        }
    }

    private void logBillboardActivity(BMFractionEntity curEntity) {
        if (curEntity.getActionState().contains(BMEntityActionState.idle)) {
            billboardActivityMap.put(curFrame, false);
        } else if (curEntity.getActionState().contains(BMEntityActionState.attack)) {
            billboardActivityMap.put(curFrame, true);
        }

    }


    private Boolean checkUnit1Activity() {
        for (int i = 0; i < curFrame; i++) {
            if (unit1EtalonActivityMap.get(i) != null){
                String unitState = enemyUnit1ActivityMap.get(i);
                String etalState = unit1EtalonActivityMap.get(i);
                   if (!unitState.equalsIgnoreCase(etalState)) {
                        return false;
                    }
            }
        }
        return true;
    }
    
    private Boolean checkUnit2Activity() {
        for (int i = 0; i < curFrame; i++) {
            if (unit2EtalonActivityMap.get(i) != null){
                String unitState = enemyUnit2ActivityMap.get(i);
                String etalState = unit2EtalonActivityMap.get(i);
                   if (!unitState.equalsIgnoreCase(etalState)) {
                        return false;
                    }
            }
        }
        return true;
    }    
    
    private Boolean checkUnit3Activity() {
        for (int i = 0; i < curFrame; i++) {
            if (unit3EtalonActivityMap.get(i) != null){
                String unitState = enemyUnit3ActivityMap.get(i);
                String etalState = unit3EtalonActivityMap.get(i);
                   if (!unitState.equalsIgnoreCase(etalState)) {
                        return false;
                    }
            }
        }
        return true;
    }       


    private void printActivityTable() {
        String captionTitle        = "                   ";
        String billboardTitle      = "billboard          ";
        String enemyUnit1Title     = "enemy 1            ";
        String myUnit1EtalonState  = "unit 1 etalon state";
        String enemyUnit1DistTitle = "dist 1             ";
        String enemyUnit2Title     = "enemy 2            ";
        String myUnit2EtalonState  = "unit 2 etalon state";
        String enemyUnit2DistTitle = "dist 2             ";
        String enemyUnit3Title     = "enemy 3            ";
        String myUnit3EtalonState  = "unit 3 etalon state";
        String enemyUnit3DistTitle = "dist 3             ";
        int step = 5;
        for (int i = 0; i < curFrame; i++) {
            if (billboardActivityMap.get(i) != null) {
                captionTitle += Utility.addCharTo(" ", String.valueOf(i), step);
                if (billboardActivityMap.get(i)) {
                    billboardTitle += Utility.addCharTo(" ", "O", step);
                } else {
                    billboardTitle += Utility.addCharTo(" ", ".", step);
                }
                if (enemyUnit1ActivityMap.get(i) != null) {
                    enemyUnit1Title += Utility.addCharTo(" ", enemyUnit1ActivityMap.get(i), step);
                }
                if (unit1EtalonActivityMap.get(i) != null) {
                    myUnit1EtalonState += Utility.addCharTo(" ", String.valueOf(unit1EtalonActivityMap.get(i)), step);
                } else {
                    myUnit1EtalonState += Utility.addCharTo(" ", ".", step);
                }  
                if (enemyUnit1DistanceToBillboardMap.get(i) != null) {
                    enemyUnit1DistTitle += Utility.addCharTo(" ", String.valueOf(enemyUnit1DistanceToBillboardMap.get(i)), step);
                }
                if (enemyUnit2ActivityMap.get(i) != null) {
                    enemyUnit2Title += Utility.addCharTo(" ", enemyUnit2ActivityMap.get(i), step);
                }
                if (unit2EtalonActivityMap.get(i) != null) {
                    myUnit2EtalonState += Utility.addCharTo(" ", String.valueOf(unit2EtalonActivityMap.get(i)), step);
                } else {
                    myUnit2EtalonState += Utility.addCharTo(" ", ".", step);
                }                  
                if (enemyUnit2DistanceToBillboardMap.get(i) != null) {
                    enemyUnit2DistTitle += Utility.addCharTo(" ", String.valueOf(enemyUnit2DistanceToBillboardMap.get(i)), step);
                }
                if (enemyUnit3ActivityMap.get(i) != null) {
                    enemyUnit3Title += Utility.addCharTo(" ", enemyUnit3ActivityMap.get(i), step);
                }
                if (unit3EtalonActivityMap.get(i) != null) {
                    myUnit3EtalonState += Utility.addCharTo(" ", String.valueOf(unit3EtalonActivityMap.get(i)), step);
                } else {
                    myUnit3EtalonState += Utility.addCharTo(" ", ".", step);
                }                  
                if (enemyUnit3DistanceToBillboardMap.get(i) != null) {
                    enemyUnit3DistTitle += Utility.addCharTo(" ", String.valueOf(enemyUnit3DistanceToBillboardMap.get(i)), step);
                }
                
            }
        }
       
        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
            .append(captionTitle).append("\n")
            .append(billboardTitle).append("\n")
            .append(enemyUnit1Title).append("\n")
            .append(myUnit1EtalonState).append("\n")                
            .append(enemyUnit1DistTitle).append("\n")
            .append(enemyUnit2Title).append("\n")
            .append(myUnit2EtalonState).append("\n")                
            .append(enemyUnit2DistTitle).append("\n")
            .append(enemyUnit3Title).append("\n")
            .append(myUnit3EtalonState).append("\n")                
            .append(enemyUnit3DistTitle).append("\n");
        logger.info(sOut.toString());          
        
    }

    @Override
    public void executeEndLoop() {
//      Method for printing activity table        
        if (getUseResultOutput()) {
            printActivityTable();
        }
        Assert.assertTrue("Unit 1 didn't get under influence of the billboard", checkUnit1Activity());
        Assert.assertTrue("Unit 2 didn't get under influence of the billboard", checkUnit2Activity());
        Assert.assertTrue("Unit 3 didn't get under influence of the billboard", checkUnit3Activity());
    }
}
