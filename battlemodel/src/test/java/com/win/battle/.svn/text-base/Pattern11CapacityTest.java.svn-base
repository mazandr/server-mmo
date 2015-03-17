/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import static com.win.battle.Pattern08SplashMegaphoneTest.logger;
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
 * Pattern N11 in the ../Requirements/templates.docx
 *
 * @author omelnyk
 */
public class Pattern11CapacityTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern11CapacityTest.class.getName());    
    private BMField field = null;
    private static final String MY_UNIT_IDENTITY = "myUnit";
    private static final String MY_CAR1_IDENTITY = "myCar1";
    private static final String MY_CAR2_IDENTITY = "myCar2";
    private static final String ENEMY_CAR_IDENTITY = "enemyCar";
    private BMBaseBuilding myCar1 = null;
    private BMBaseBuilding myCar2 = null;
    private BMCell myUnitFinalPosition;
    private int curFrame = 0;
    private Map<Integer, String> myUnitActivityMap = new HashMap<>();

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
        loader.load("config/wm_ptrn11.json");
//        loader.load("config/wm.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn11.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        myCar1 = field.getBuildings().get(MY_CAR1_IDENTITY);
        myCar2 = field.getBuildings().get(MY_CAR2_IDENTITY);
        Boolean isMyUnitCaptured = true;
        if (curFrame == 0) {
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                    curEntity.setCanBeCaptured(true);
                }
            }
        }

        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                curEntity.getComponent(BMHealsComponent.class).setCurrentHP(150);
                logUnitActivity(curEntity);
                myUnitFinalPosition = curEntity.getPosition();
                isMyUnitCaptured = curEntity.getActionState().contains(BMEntityActionState.capture);
            } else if (curEntity.getIdentifier().equalsIgnoreCase(MY_CAR1_IDENTITY) || curEntity.getIdentifier().equalsIgnoreCase(MY_CAR2_IDENTITY)
                    || curEntity.getIdentifier().equalsIgnoreCase(ENEMY_CAR_IDENTITY)) {
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

    private Boolean checkIsUnitCapturedByNeutralCar() {
        Boolean res = false;
        float destToCar1 = calcDistance(myUnitFinalPosition, myCar1.getPosition());
        float destToCar2 = calcDistance(myUnitFinalPosition, myCar2.getPosition());
        if (destToCar1 > destToCar2) {
            res = true;
        }
        return res;
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
        }

    }

    private void printActivityTable() {
        String captionTitle = "          ";
        String myUnitTitle = "my unit   ";
        for (int i = 0; i < curFrame; i++) {
            captionTitle += Utility.addCharTo(" ", String.valueOf(i), 4);
            if (myUnitActivityMap.get(i) != null) {
                myUnitTitle += Utility.addCharTo(" ", myUnitActivityMap.get(i), 4);
            }

        }
//        System.out.println(captionTitle);
//        System.out.println(myUnitTitle);
        
        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
            .append(captionTitle).append("\n")
            .append(myUnitTitle).append("\n");
        logger.info(sOut.toString());         
    }

    @Override
    public void executeEndLoop() {
//      Method for printing activity table 
        if (getUseResultOutput()){        
            printActivityTable();
            printAllEntities(field);
        }    
        Assert.assertTrue("My Unit hasn't been captured", checkMyUnitsActivity());
        Assert.assertTrue("My Unit hasn't been captured by the right car", checkIsUnitCapturedByNeutralCar());
    }

    private boolean checkMyUnitsActivity() {
        Boolean isMyUnitCaptured = false;

        for (int i = 0; i < curFrame; i++) {
            if (myUnitActivityMap.get(i) != null) {
                isMyUnitCaptured = isMyUnitCaptured || myUnitActivityMap.get(i).trim().equalsIgnoreCase("c");
            }
        }
        return isMyUnitCaptured;
    }


}
