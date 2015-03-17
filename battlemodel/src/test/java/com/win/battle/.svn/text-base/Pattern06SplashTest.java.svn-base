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
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.battle.utils.outputbattle.BattleConsoleOutput;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author omelnyk
 */
public class Pattern06SplashTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern06SplashTest.class.getName());
    
    private BMField field = null;
    private Boolean isFirstTick = true;
    private static final String MY_UNIT_IDENTITY = "G";
    private static final String ENEMY_UNIT_PREFIX = "E";
    private EnumSet<BMEntityActionState> unitState = null;
    private int curFrame = 0;
    private Map<String, Integer> mapStartHP = new HashMap<>();
    private Map<String, Integer> mapStopHP = new HashMap<>();
    private List<BMBaseUnit> enemies = null;
    private final int DAMAGE_VALUE = 40;
    private Boolean isExpectedDamageCatch = false;

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
        loader.load("config/wm_ptrn6.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn6.json");
        return loader;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        enemies = field.getEnemyUnits();

        for (BMBaseUnit unit : enemies) {
            if (curFrame == 0) {
                mapStartHP.put(unit.getIdentifier(), unit.getCurrentHP());
            }
            mapStopHP.put(unit.getIdentifier(), unit.getCurrentHP());
        }

        isExpectedDamageCatch = isExpectedDamageCatch || checkEnemiesStatus();
        if ((curFrame == 100) || isExpectedDamageCatch) {
            executeEndLoop();
            engine.interrupt();
        }
        if (getUseFullOutput()) {
            printHP(curFrame);
        }
        curFrame++;
    }

    private Boolean checkEnemiesStatus() {
        Boolean res = true;
        String ident;
        int startHP;
        int stopHP;
        for (BMBaseUnit unit : enemies) {
            ident = unit.getIdentifier();
            startHP = mapStartHP.get(ident);
            stopHP = mapStopHP.get(ident);
            res = res && ((startHP - stopHP) == DAMAGE_VALUE);
        }
        return res;
    }

    private void printHP(int curFrame) {
        StringBuilder sOut = new StringBuilder();
        sOut.append("Frame ").append(curFrame).append("; start HP values: ");
        for (Map.Entry<String, Integer> entry : mapStartHP.entrySet()) {
            sOut.append(entry.getKey()).append(" : ").append(entry.getValue()).append(";");
        }
        sOut.append("\n").append("all entities HP values: ").append("\n");
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            sOut.append(curEntity.getIdentifier()).append(curEntity.getPosition()).append(" : ").append(curEntity.getComponent(BMHealsComponent.class).getCurrentHP()).append("; ");
            
        }
        sOut.append("\n");
        logger.info(sOut.toString());
    }

    @Override
    public void executeEndLoop() {
        Assert.assertTrue("Enemies status", isExpectedDamageCatch);
    }
}
