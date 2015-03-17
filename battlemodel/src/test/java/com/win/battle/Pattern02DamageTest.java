/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.components.BMCarrierComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity.BMEntityType;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.buildings.BMBuildingState;
import com.win.strategy.battle.utils.outputbattle.BattleConsoleOutput;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author omelnyk
 */
public class Pattern02DamageTest extends BaseBattleTest implements IModelCallback {

    static final Logger logger = Logger.getLogger(Pattern02DamageTest.class.getName());
    
    private static final String ENEMY_BUILDING_IDENTITY = "enemy";
    private List<BMCell> firstPriorityLandingArea = new ArrayList<>();
    private List<BMCell> secondPriorityArea = new ArrayList<>();
    private boolean useAreas = false;
    private int enemyCarrierUnitSize = 0;
    private int myCarrierUnitSize = 0;
    private BMCell enemyCarPosition;
    private BMCell myCarPosition;
    private static final String ENEMY_CAR_IDENTITY = "enemycar";
    private static final String MY_CAR_IDENTITY = "mycar";
    private Boolean isFirstTick = true;
    private BMField field = null;
    private BMBaseBuilding myCar = null;
    private BMBaseBuilding enemyCar = null;
    private Boolean isMyCarDestroyed = false;
    private Boolean isEnemyCarDestroyed = false;
    private int myUnitsCountGlobal = 0;
    private int enemyUnitsCountGlobal = 0;
    private int[][] hpValues = {{150,0},{150,0},{150,0},{50,0}};
    private int curTimeTick;

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
        loader.load("config/wm_ptrn1.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
//        RemoteTestBattleConfigLoader loader = new RemoteTestBattleConfigLoader();
//        loader.load(null);
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_ptrn2.json"); // 6 zombies in my car  (test N1)     
        return loader;
    }

    
    
    private Boolean checkIfEnemyUnitsHaveDefinedHP() {
        for (BMFractionEntity curEntity : field.getAllEntities()) {
          if ((curEntity.innerType().contains(BMEntityType.unit))&&(curEntity.getFraction() == 2)&&
               (curEntity.getIdentifier().startsWith(enemyCar.getIdentifier()))) {
                int curHp = curEntity.getComponent(BMHealsComponent.class).getCurrentHP();  
                for (int i = 0; i < hpValues.length; i++) {
                    if((hpValues[i][0] == curHp)&&(hpValues[i][1] == 0)) {
                       hpValues[i][1] = 1; 
                       break;
                    }
                }
          }
        }
        Boolean res = true;
        for (int i = 0; i < hpValues.length; i++) {
            if(hpValues[i][1] == 0) {
                res = false; 
                break;
            }
        }
        
        return res;
    }

    @Override
    public void executeInsideLoop() {
        field = engine.getField();
        curTimeTick =  engine.getTimeTicks();
        if (curTimeTick == 0) {
            myCar = field.getBuildings().get(MY_CAR_IDENTITY);
            myCar.setCurrentHP(400);
            myCarrierUnitSize = myCar.getComponent(BMCarrierComponent.class).getCurrentSize();
            myCarPosition = myCar.getPosition();

            enemyCar = field.getBuildings().get(ENEMY_CAR_IDENTITY);
            enemyCar.setCurrentHP(1200);
            enemyCarrierUnitSize = enemyCar.getComponent(BMCarrierComponent.class).getCurrentSize();
            enemyCarPosition = enemyCar.getPosition();
        } else {
            myCar = field.getBuildings().get(MY_CAR_IDENTITY);
            isMyCarDestroyed = myCar.getBuildingState() == BMBuildingState.destroyed;

            enemyCar = field.getBuildings().get(ENEMY_CAR_IDENTITY);
            isEnemyCarDestroyed = enemyCar.getBuildingState() == BMBuildingState.destroyed;

            int myUnitsCount = 0;
            int enemyUnitsCount = 0;            

            for (BMFractionEntity curEntity : field.getAllEntities()) {
                if (curEntity.getFraction() == 1) {
                    if (curEntity.getIdentifier().startsWith(myCar.getIdentifier())) {
                        myUnitsCount++;
                    }
                } else if (curEntity.getFraction() == 2) {
                    if (curEntity.getIdentifier().startsWith(enemyCar.getIdentifier())) {
                        enemyUnitsCount++;
                    }
                }
            }
            
            myUnitsCountGlobal = myUnitsCount;
            enemyUnitsCountGlobal = enemyUnitsCount;
            if ((isEnemyCarDestroyed && enemyUnitsCount == 0) || (isMyCarDestroyed && myUnitsCount == 0)) {
                engine.interrupt();
                executeEndLoop();
            }

        }
        
        if (getUseFullOutput()){
            printAllEntities(field);
        }         

    }

    @Override
    public void executeEndLoop() {
        if (getUseFullOutput()){
            printAllEntities(field);
        }    
        Assert.assertTrue("My car isn't destroyed", isMyCarDestroyed);
        Assert.assertEquals("All my units didn't die and their count isn't ", 0, myUnitsCountGlobal);
        Assert.assertTrue("Enemy car isn't destroyed", isEnemyCarDestroyed);
        Assert.assertTrue("checkIfEnemyUnitsHaveDefinedHP", checkIfEnemyUnitsHaveDefinedHP());
    }
    
    
}
