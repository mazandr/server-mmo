package com.win.battle;

import com.win.battle.utils.GlobalConfiguration;
import com.win.strategy.battle.model.BattleModelBuilder;
import com.win.strategy.battle.model.BattleModelEngine;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;

/**
 *
 * @author okopach
 */
public abstract class BaseBattleTest {

    private final static Logger logger = Logger.getLogger(BaseBattleTest.class.getName());
    private BattleModelBuilder builder = new BattleModelBuilder();
    private Boolean useFullOutput = false;
    private Boolean useResultOutput = false;
    private Properties testProperties = new Properties();
    private static final String propertyFile = "test.properties";
    protected BattleModelEngine engine;

    @Before
    public void init() {
        try {
            loadTestConfiguration();
            useFullOutput = Boolean.parseBoolean(testProperties.getProperty("use.full.output"));
            useResultOutput = Boolean.parseBoolean(testProperties.getProperty("use.result.output"));
            getBuilder().setGameConfiguration(getBattleConfiguration());
            getBuilder().setWorldModelConfiguration(getWorldModelConfiguration());
            getBuilder().setGlobalConfiguration(new GlobalConfiguration());
            this.engine = (BattleModelEngine) getBuilder().createEngine();
            this.engine.intializeField();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public abstract IWorldModelConfiguration getWorldModelConfiguration();

    public abstract IModelConfiguration getBattleConfiguration();

    public void loadTestConfiguration() throws FileNotFoundException {
        InputStream input = null;
        try {
            input = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
            if (input == null) {
                logger.log(Level.SEVERE, "Sorry, unable to find props file " + propertyFile);
                throw new FileNotFoundException();
//                return;
            }
            testProperties.load(input);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage());
                }
            }
        }
    }

    /**
     * @return the useFullOutput
     */
    public Boolean getUseFullOutput() {
        return useFullOutput;
    }

    /**
     * @return the useResultOutput
     */
    public Boolean getUseResultOutput() {
        return useResultOutput;
    }

    public void printEntity(BMFractionEntity curEntity) {
        StringBuilder message = new StringBuilder("");
        if (curEntity.getType() != null) {
            message.append(curEntity.getType()).append("; ");
        }
        if (curEntity.innerType() != null) {
            message.append(curEntity.innerType()).append("; ");
        }
        if (curEntity.getIdentifier() != null) {
            message.append(curEntity.getIdentifier()).append("; ");
        }
        if (curEntity.getActionState() != null) {
            message.append(curEntity.getActionState()).append("; ");
        }
        if (curEntity.getCenter() != null) {
            message.append("(").append(curEntity.getCenter().getPosX()).append(",").append(curEntity.getCenter().getPosY()).append("); ");
        }
        BMHealsComponent comp = curEntity.getComponent(BMHealsComponent.class);
        if (comp != null) {
            message.append("HP - ").append(curEntity.getComponent(BMHealsComponent.class).getCurrentHP());
        }
        logger.info(message.toString());
    }

    public void printAllEntities(BMField field) {
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (!curEntity.getType().equalsIgnoreCase("hero_house")) {
                printEntity(curEntity);
            }
        }
    }

    public float calcDistance(BMCell cellA, BMCell cellB) {
        int deltaX = Math.abs(cellA.getPosX() - cellB.getPosX());
        int deltaY = Math.abs(cellA.getPosY() - cellB.getPosY());
        float res = (float) Math.round(Math.sqrt(deltaX * deltaX + deltaY * deltaY) * 10) / 10;
        return res;
    }

    public BMFractionEntity getEntityByName(BMField field, String entityName) {
        for (BMFractionEntity curEntity : field.getAllEntities()) {
            if (curEntity.getIdentifier().equalsIgnoreCase(entityName)) {
                return curEntity;
            }
        }
        return null;
    }

    /**
     * @return the builder
     */
    public BattleModelBuilder getBuilder() {
        return builder;
    }


}
