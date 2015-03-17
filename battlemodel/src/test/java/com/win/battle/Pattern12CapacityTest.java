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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Pattern N12 in the ../Requirements/templates.docx
 *
 * @author omelnyk
 */
public class Pattern12CapacityTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern12CapacityTest.class.getName());    
    private BMField field = null;
    private static final String MY_UNIT_PREFIX = "myUnit";
    private static final String MY_UNIT_A_IDENTITY = "myUnitA";
    private static final String MY_UNIT_B_IDENTITY = "myUnitB";
    private static final String MY_UNIT_C_IDENTITY = "myUnitC";
    private static final String MY_CAR_IDENTITY = "myCar";
    private static final String ENEMY_CAR_PREFIX = "enemyCar";
    private static final String ENEMY_CAR_A_IDENTITY = "enemyCarA";
    private static final String ENEMY_CAR_B_IDENTITY = "enemyCarB";
    private static final String ENEMY_CAR_C_IDENTITY = "enemyCarC";
    private BMBaseBuilding myCar = null;
    private int curFrame = 0;
    private Map<Integer, String> myUnitAActivityMap = new HashMap<>();
    private Map<Integer, String> myUnitBActivityMap = new HashMap<>();
    private Map<Integer, String> myUnitCActivityMap = new HashMap<>();


    @Test
    public void test() {
        engine.addCallback(this);
        if (getUseFullOutput()){
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
        loader.load("config/bt_ptrn12.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        myCar = field.getBuildings().get(MY_CAR_IDENTITY);
   
        Boolean isMyUnitCaptured = true;
        if (curFrame == 0) {
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().startsWith(MY_UNIT_PREFIX)) {
                    curEntity.setCanBeCaptured(true);
                } 
            }
        }

        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().startsWith(MY_UNIT_PREFIX)) {
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(150);
                logUnitActivity(curEntity);
//                myUnitFinalPosition = curEntity.getPosition();
                isMyUnitCaptured = isMyUnitCaptured && curEntity.getActionState().contains(BMEntityActionState.capture);
            } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_CAR_IDENTITY)||curEntity.getIdentifier().startsWith(ENEMY_CAR_PREFIX)) {
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(1200);
            }
        }

//        printAllEntities(field);
        curFrame++;
        if (isMyUnitCaptured) {
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
        if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_A_IDENTITY)) {
            myUnitAActivityMap.put(curFrame, actionSign);
        } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_B_IDENTITY)) {
            myUnitBActivityMap.put(curFrame, actionSign);
        } if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_C_IDENTITY)) {
            myUnitCActivityMap.put(curFrame, actionSign);
        } 
    }



    private void printActivityTable() {
        String captionTitle   = "         ";
        String myUnitATitle    = "my unit A";
        String myUnitBTitle    = "my unit B";
        String myUnitCTitle    = "my unit C";
        for (int i = 0; i < curFrame; i++) {
            captionTitle += Utility.addCharTo(" ", String.valueOf(i), 4);
            if (myUnitAActivityMap.get(i) != null) {
                myUnitATitle += Utility.addCharTo(" ", myUnitAActivityMap.get(i), 4);
            }
            if (myUnitBActivityMap.get(i) != null) {
                myUnitBTitle += Utility.addCharTo(" ", myUnitBActivityMap.get(i), 4);
            }
            if (myUnitCActivityMap.get(i) != null) {
                myUnitCTitle += Utility.addCharTo(" ", myUnitCActivityMap.get(i), 4);
            }

        }
        
        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
            .append(captionTitle).append("\n")
            .append(myUnitATitle).append("\n")
            .append(myUnitBTitle).append("\n")
            .append(myUnitCTitle).append("\n");
        logger.info(sOut.toString());        
        
    }

    @Override
    public void executeEndLoop() {
//      Method for printing activity table        
        if (getUseResultOutput()){        
            printActivityTable();
            printAllEntities(field);
        }
        Assert.assertTrue("My Unit has been captured", checkMyUnitsActivity());
//          Assert.assertTrue("My Unit has been captured by the right car", checkIsUnitCapturedByNeutralCar());
    }

    private boolean checkMyUnitsActivity(){
        Boolean isMyUnitACaptured = false;
        Boolean isMyUnitBCaptured = false;
        Boolean isMyUnitCCaptured = false;
        
        for (int i = 0; i < curFrame; i++) {
            if (myUnitAActivityMap.get(i) != null) {
                 isMyUnitACaptured = isMyUnitACaptured || myUnitAActivityMap.get(i).trim().equalsIgnoreCase("c");
            }
            if (myUnitBActivityMap.get(i) != null) {
                 isMyUnitBCaptured = isMyUnitBCaptured || myUnitBActivityMap.get(i).trim().equalsIgnoreCase("c");
            }
            if (myUnitCActivityMap.get(i) != null) {
                 isMyUnitCCaptured = isMyUnitCCaptured || myUnitCActivityMap.get(i).trim().equalsIgnoreCase("c");
            }
            
        }
        return isMyUnitACaptured&&isMyUnitBCaptured&&isMyUnitBCaptured;
    }    
    
    private void printAllEntities() {
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            printEntity(curEntity);
        }
    }

}
