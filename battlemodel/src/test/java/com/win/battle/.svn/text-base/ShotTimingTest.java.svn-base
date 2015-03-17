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
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author omelnyk
 */
public class ShotTimingTest extends BaseBattleTest implements IModelCallback {

    private static final String ZOMBIE_UNIT_IDENTITY = "zombieUnit";
    private static final String SKELETON_UNIT_IDENTITY = "skeletonUnit";
    private BMField field = null;
    private int zombieUnitHP = 0;
    private int skeletonUnitHP = 0;
    private int stopFrame = 0;
    private int curFrame = 0;
    private Map<Integer, String> zombieUnitActivityMap = new HashMap<>();
    private Map<Integer, Integer> zombieUnitHPMap = new HashMap<>();
    private Map<Integer, String> skeletonUnitActivityMap = new HashMap<>();
    private Map<Integer, Integer> skeletonUnitHPMap = new HashMap<>();

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
        loader.load("config/wm_shot_timing.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_shot_timing.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        if (engine.getTimeTicks() == 0) {
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if ((curEntity.getIdentifier().equalsIgnoreCase(ZOMBIE_UNIT_IDENTITY))
                        || (curEntity.getIdentifier().equalsIgnoreCase(SKELETON_UNIT_IDENTITY))) {
                    curEntity.getComponent(BMHealsComponent.class).setCurrentHP(100);
                }
                logUnitActivity(curEntity);
            }
        } else {
            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getIdentifier().equalsIgnoreCase(ZOMBIE_UNIT_IDENTITY)) {
                    zombieUnitHP = curEntity.getComponent(BMHealsComponent.class).getCurrentHP();
                    zombieUnitHPMap.put(curFrame, zombieUnitHP);
                } else if (curEntity.getIdentifier().equalsIgnoreCase(SKELETON_UNIT_IDENTITY)) {
                    skeletonUnitHP = curEntity.getComponent(BMHealsComponent.class).getCurrentHP(); 
                    skeletonUnitHPMap.put(curFrame, skeletonUnitHP);
                    if ((curEntity.getActionState().contains(BMEntityActionState.idle))&&(stopFrame == 0)) {
                        stopFrame = curFrame + 30;
                    }
                    
                }
                logUnitActivity(curEntity);
            }

            curFrame++;
            if (curFrame == stopFrame) {
                engine.interrupt();
                executeEndLoop();
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
        if (curEntity.getIdentifier().equalsIgnoreCase(ZOMBIE_UNIT_IDENTITY)) {
            zombieUnitActivityMap.put(curFrame, actionSign);
        } else if (curEntity.getIdentifier().equalsIgnoreCase(SKELETON_UNIT_IDENTITY)) {
            skeletonUnitActivityMap.put(curFrame, actionSign);
        }

    }

    @Override
    public void executeEndLoop() {
        if (getUseResultOutput()) {
            printActivityTable();
            printAllEntities(field);
        }
        Assert.assertEquals("Skeleton final HP ", 40, skeletonUnitHP);
    }

    private void printActivityTable() {
        String captionTitle        = "            ";
        String zombieUnitTitle     = "zombie      ";
        String zombieUnitHPTitle   = "zombie HP   ";
        String skeletonUnitTitle   = "skeleton    ";
        String skeletonUnitHPTitle = "skeleton HP ";
        for (int i = 0; i < curFrame; i++) {
            captionTitle += Utility.addCharTo(" ", String.valueOf(i), 4);
            if (zombieUnitActivityMap.get(i) != null) {
                zombieUnitTitle += Utility.addCharTo(" ", zombieUnitActivityMap.get(i), 4);
            } else {
                zombieUnitTitle += Utility.addCharTo(" ", "x", 4);
            }
            
            if (zombieUnitHPMap.get(i) != null) {
                zombieUnitHPTitle += Utility.addCharTo(" ", zombieUnitHPMap.get(i).toString(), 4);
            } else {
                zombieUnitHPTitle += Utility.addCharTo(" ", "x", 4);
            }            

            if (skeletonUnitActivityMap.get(i) != null) {
                skeletonUnitTitle += Utility.addCharTo(" ", skeletonUnitActivityMap.get(i), 4);
            } else {
                skeletonUnitTitle += Utility.addCharTo(" ", "x", 4);
            }
            
            if (skeletonUnitHPMap.get(i) != null) {
                skeletonUnitHPTitle += Utility.addCharTo(" ", skeletonUnitHPMap.get(i).toString(), 4);
            } else {
                skeletonUnitHPTitle += Utility.addCharTo(" ", "x", 4);
            }                

        }
        System.out.println(captionTitle);
        System.out.println(zombieUnitTitle);
        System.out.println(zombieUnitHPTitle);
        System.out.println(skeletonUnitTitle);
        System.out.println(skeletonUnitHPTitle);
    }

}
