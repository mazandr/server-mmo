/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.utils.outputbattle.BattleConsoleOutput;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Pattern N8 in the ../Requirements/templates.docx
 *
 * @author omelnyk
 */
public class Pattern08SplashMegaphoneTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern08SplashMegaphoneTest.class.getName());
    private BMField field = null;
    private static final String MY_UNIT_IDENTITY = "A";
//    private static final String ENEMY_UNIT_IDENTITY = "E";
    private static final String ENEMY_MEGATHONE = "enemyMegaphone";
    private static final String MEGATHONE = "megaphone";
    private int captureTime;
    private int captureTimeInFrames;
    private int reloadTime;
    private int reloadTimeInFrames;
    private int worktime;
    private int worktimeInFrames;
    private int affectedAreaRadius;
    private int megaphoneDamageValue;
    private int curTimeTick;
    private Map<Integer, Boolean> megaphoneActivityMap = new HashMap<>();
    private Map<Integer, String> megaphoneAllActivitiesMap = new HashMap<>();
    private Map<Integer, String> myUnitDistanceFromEpicenterMap = new HashMap<>();
    private Map<Integer, String> myUnitActivityMap = new HashMap<>();
    private Map<Integer, Integer> myUnitHpMap = new HashMap<>();
    private Map<Integer, Float> distanceMap = new HashMap<>();
    private BMCell myUnitPosition = null;
    private BMCell megaphonePosition = null;
    private Map<Integer, Integer> megaphoneEtalonActivityMap;
    private int[][] megaphoneEtalonActivities = {{242, 150}, {243, 140}, {252, 140}, {253, 130}, {262, 130}, {263, 130},
        {262, 130}, {263, 120}, {272, 120}, {273, 110}, {282, 110}, {283, 110}, {292, 110}, {293, 110},
        {302, 110}, {303, 100}, {312, 100}, {313, 90}, {322, 90}, {323, 80}, {332, 80}, {333, 70},
        {342, 70}, {343, 70}, {342, 70}, {343, 70}, {352, 70}, {353, 70}, {367, 70}, {368, 60},
        {377, 60}, {378, 50}, {387, 50}, {388, 50}, {407, 50}, {408, 50}, {417, 50}, {418, 50},
        {427, 50}, {428, 50}, {437, 50}, {438, 50}, {447, 50}, {448, 50}, {457, 50}, {458, 50},
        {467, 50}, {468, 50}, {477, 50}, {478, 50}, {487, 50}, {488, 50}, {497, 50}, {498, 50},
        {507, 50}, {508, 50}, {517, 50}, {518, 50}, {527, 50}, {528, 50}, {537, 50}, {538, 50}};

    @Test
    public void test() {
        makeMegaphoneEtalonActivityMap();
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
        loader.load("config/wm_ptrn8.json");
        getWorldModelParams(loader);
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn8.json");
        return loader;
    }

    public void getWorldModelParams(LocalWorldModelConfigLoader loader) {
        captureTime = Integer.parseInt(loader.getBuildingDistributedActionAcTypeParams(MEGATHONE, 1, ProtocolStrings.C_T_T));
        captureTimeInFrames = captureTime / 100;
        reloadTime = Integer.parseInt(loader.getBuildingDistributedActionAcTypeParams(MEGATHONE, 1, ProtocolStrings.R_T));
        reloadTimeInFrames = reloadTime / 100;
        worktime = Integer.parseInt(loader.getBuildingDistributedActionAcTypeParams(MEGATHONE, 1, ProtocolStrings.TIME));
        worktimeInFrames = worktime / 100;
        affectedAreaRadius = Integer.parseInt(loader.getBuildingDistributedActionParam(MEGATHONE, 1, ProtocolStrings.SDR).toString());
        megaphoneDamageValue = Integer.parseInt(loader.getBuildingDistributedActionParam(MEGATHONE, 1, ProtocolStrings.VALUE).toString());
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        curTimeTick = engine.getTimeTicks();
        String affectedAreaSign = "";
        Boolean isMyUnitAlive = true;
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
                logUnitActivity(curEntity);
                myUnitPosition = curEntity.getPosition();
                isMyUnitAlive = curEntity.getComponent(BMHealsComponent.class).getCurrentHP() > 0;
                affectedAreaSign = (calcDistance(getMegaphoneCenter(), curEntity.getPosition()) <= affectedAreaRadius) ? "+" : ".";
                myUnitDistanceFromEpicenterMap.put(curTimeTick, affectedAreaSign);
//                isAllEnemyUnitsIdle = isAllEnemyUnitsIdle && curEntity.getActionState().contains(BMEntityActionState.idle);
            } else if (curEntity.getIdentifier().equalsIgnoreCase(ENEMY_MEGATHONE)) {
                logMegaphoneActivity(curEntity);
                if (megaphonePosition == null) {
                    megaphonePosition = curEntity.getPosition();
                }
            }
        }
        distanceMap.put(curTimeTick, calcDistance(getMegaphoneCenter(), myUnitPosition));
        if (!isMyUnitAlive) {
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
        if (curEntity.getIdentifier().equalsIgnoreCase(MY_UNIT_IDENTITY)) {
            myUnitActivityMap.put(curTimeTick, actionSign);
            int curHp = curEntity.getComponent(BMHealsComponent.class).getCurrentHP();
            myUnitHpMap.put(curTimeTick, curHp);
        }
    }

    private void logMegaphoneActivity(BMFractionEntity curEntity) {
        if (curEntity.getActionState().contains(BMEntityActionState.idle)) {
            megaphoneActivityMap.put(curTimeTick, false);
        } else if (curEntity.getActionState().contains(BMEntityActionState.attack)) {
            megaphoneActivityMap.put(curTimeTick, true);
        }
    }

    private void makeMegaphoneEtalonActivityMap() {
        megaphoneEtalonActivityMap  = new HashMap<>();
        for (int i = 0; i < megaphoneEtalonActivities.length; i++) {
            megaphoneEtalonActivityMap.put(megaphoneEtalonActivities[i][0], megaphoneEtalonActivities[i][1]);
        }
    }

    private boolean checkMegaphoneActivity() {
        for (int i = 0; i < curTimeTick; i++) {
            if (megaphoneEtalonActivityMap.get(i) != null){
                int unitHp = myUnitHpMap.get(i);
                int etalHp = megaphoneEtalonActivityMap.get(i);
                   if (unitHp != etalHp) {
                        return false;
                    }
            }
        }
        return true;
    }

    private BMCell getMegaphoneCenter() {
        return getEntityByName(field, ENEMY_MEGATHONE).getComponent(BMLargeLocatorComponent.class).getCenter();
    }

    private void printActivityTable() {
        String captionTitle = "                ";
        String megafonTitle = "megaphone       ";
        String myUnitTitle = "my unit         ";
        String myUnitInAffectedArea = "in affected area";
        String distanceTitle = "distance        ";
        String myUnitHpTitle = "my unit HP      ";
        String etalonHpTitle = "etalon  HP      ";
        for (int i = 0; i < curTimeTick; i++) {
            if (megaphoneActivityMap.get(i) != null) {
                captionTitle += Utility.addCharTo(" ", String.valueOf(i), 5);
                if (megaphoneActivityMap.get(i)) {
                    megafonTitle += Utility.addCharTo(" ", "O", 5);
                } else {
                    megafonTitle += Utility.addCharTo(" ", ".", 5);
                }
                if (myUnitActivityMap.get(i) != null) {
                    myUnitTitle += Utility.addCharTo(" ", myUnitActivityMap.get(i), 5);
                }
                if (myUnitDistanceFromEpicenterMap.get(i) != null) {
                    myUnitInAffectedArea += Utility.addCharTo(" ", myUnitDistanceFromEpicenterMap.get(i), 5);
                }
                if (distanceMap.get(i) != null) {
                    distanceTitle += Utility.addCharTo(" ", String.valueOf(distanceMap.get(i)), 5);
                }
                if (myUnitHpMap.get(i) != null) {
                    myUnitHpTitle += Utility.addCharTo(" ", String.valueOf(myUnitHpMap.get(i)), 5);
                }
                if (megaphoneEtalonActivityMap.get(i) != null) {
                    etalonHpTitle += Utility.addCharTo(" ", String.valueOf(megaphoneEtalonActivityMap.get(i)), 5);
                } else {
                    etalonHpTitle += Utility.addCharTo(" ", ".", 5);
                }               

            }
        }
        
        StringBuilder sOut = new StringBuilder();
        sOut.append("\n")
            .append(captionTitle).append("\n")
            .append(megafonTitle).append("\n")
            .append(myUnitTitle).append("\n")
            .append(myUnitInAffectedArea).append("\n")
            .append(distanceTitle).append("\n")
            .append(myUnitHpTitle).append("\n")
            .append(etalonHpTitle).append("\n");
        logger.info(sOut.toString());        
    }

    @Override
    public void executeEndLoop() {
//      Method for printing activity table        
        if (getUseResultOutput()) {
            printActivityTable();
        }
        Assert.assertTrue("Megaphone doesn't work correctly", checkMegaphoneActivity());
    }
}
