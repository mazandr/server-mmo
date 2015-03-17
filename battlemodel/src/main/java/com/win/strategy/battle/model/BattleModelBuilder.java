package com.win.strategy.battle.model;

import com.win.strategy.battle.configuration.factory.BMBuildingInstanceFactory;
import com.win.strategy.battle.configuration.factory.BMHeroInstanceFactory;
import com.win.strategy.battle.configuration.factory.BMSkillInstanceFactory;
import com.win.strategy.battle.configuration.factory.BMTransportInstanceFactory;
import com.win.strategy.battle.configuration.factory.BMUnitInstanceFactory;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.BMMovementType;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.skills.BMBaseSkill;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.common.model.IModelEngine;
import com.win.strategy.common.model.support.IModelBuilder;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IGlobalConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BattleModelBuilder implements IModelBuilder {

    public static final int ENEMY_FRACTION_IDX = 2;
    private IWorldModelConfiguration worldModelConfiguration;
    private IModelConfiguration gameConfiguration;
    private IGlobalConfiguration globalConfiguration;
    private BMBuildingInstanceFactory buildingInstanceFactory;
    private BMHeroInstanceFactory heroInstanceFactory;
    private BMSkillInstanceFactory skillInstanceFactory;
    private BMTransportInstanceFactory transportInstanceFactory;
    private BMUnitInstanceFactory unitInstanceFactory;

    public BattleModelBuilder() {
        buildingInstanceFactory = new BMBuildingInstanceFactory();
        heroInstanceFactory = new BMHeroInstanceFactory();
        skillInstanceFactory = new BMSkillInstanceFactory();
        transportInstanceFactory = new BMTransportInstanceFactory();
        unitInstanceFactory = new BMUnitInstanceFactory();
    }

    public BMBuildingInstanceFactory getBuildingInstanceFactory() {
        return buildingInstanceFactory;
    }

    public void setBuildingInstanceFactory(BMBuildingInstanceFactory buildingInstanceFactory) {
        this.buildingInstanceFactory = buildingInstanceFactory;
    }

    public BMHeroInstanceFactory getHeroInstanceFactory() {
        return heroInstanceFactory;
    }

    public void setHeroInstanceFactory(BMHeroInstanceFactory heroInstanceFactory) {
        this.heroInstanceFactory = heroInstanceFactory;
    }

    public BMSkillInstanceFactory getSkillInstanceFactory() {
        return skillInstanceFactory;
    }

    public void setSkillInstanceFactory(BMSkillInstanceFactory skillInstanceFactory) {
        this.skillInstanceFactory = skillInstanceFactory;
    }

    public BMTransportInstanceFactory getTransportInstanceFactory() {
        return transportInstanceFactory;
    }

    public void setTransportInstanceFactory(BMTransportInstanceFactory transportInstanceFactory) {
        this.transportInstanceFactory = transportInstanceFactory;
    }

    public BMUnitInstanceFactory getUnitInstanceFactory() {
        return unitInstanceFactory;
    }

    public void setUnitInstanceFactory(BMUnitInstanceFactory unitInstanceFactory) {
        this.unitInstanceFactory = unitInstanceFactory;
    }

    @Override
    public IModelEngine createEngine() {
        BattleModelEngine engine = new BattleModelEngine();
        engine.setBuilder(this);
        return engine;
    }

    public Map<String, Object> getUnitTypeByName(String typeName) {

        Map<String, Object> types = getWorldModelConfiguration().getUnitTypes();
        Map<String, Object> utype = (Map<String, Object>) types.get(typeName);
        return utype;
    }

    public Map<String, Object> getCarTypeByName(String typeName) {
        Map<String, Object> types = getWorldModelConfiguration().getCarTypes();
        Map<String, Object> ctype = (Map<String, Object>) types.get(typeName);
        return ctype;
    }

    private BMField createFieldInstance() {
        int tileDivision = 1;

        int maxSizeX;
        int maxSizeY;
        String roadCellsRect;
        String fogCellsRect;
        int speedMultiplier;
        int speedKoef;

        try {
            Map<String, Object> field = (Map<String, Object>) gameConfiguration.getConfig().get(ProtocolStrings.FIELD);
            maxSizeX = Double.valueOf(String.valueOf(field.get("maxX"))).intValue();
            maxSizeY = Double.valueOf(String.valueOf(field.get("maxY"))).intValue();
            roadCellsRect = String.valueOf(field.get("roadCellsRect"));
            fogCellsRect = String.valueOf(field.get("fogCellsRect"));
            speedMultiplier = Double.valueOf(String.valueOf(field.get("speedMultiplier"))).intValue();
            speedKoef = Double.valueOf(String.valueOf(field.get("speedKoef"))).intValue();
        } catch (Exception e) {
            maxSizeX = 120;
            maxSizeY = 120;
            roadCellsRect = "1x1,1x2";
            fogCellsRect = "1x1,1x2";
            speedMultiplier = 1;
            speedKoef = 12;
        }


        maxSizeX = maxSizeX * tileDivision;
        maxSizeY = maxSizeY * tileDivision;
        BMField field = new BMField(maxSizeX, maxSizeY);
        field.setSpeedKoef(speedKoef);
        field.setSpeedMultiplier(speedMultiplier);

        field.setTotalGameFrames(globalConfiguration.getTotalGameFrames());
        field.setGameProcessStep(globalConfiguration.getGameProcessStep());
        field.setItterationSize(globalConfiguration.getItterationSize());

        field.getWinConditionHandler().addWinConditions(
                globalConfiguration.makeWinConditionsFromConfig());

        field.setRoadCells(roadCellsRect);
        List<Rect> roadRects = makeRectanglesFromString(roadCellsRect);
        defineMovementTypeByRects(field, roadRects, BMMovementType.ROAD);

        field.setFogCells(fogCellsRect);
        List<Rect> fogRects = makeRectanglesFromString(fogCellsRect);
        defineMovementTypeByRects(field, fogRects, BMMovementType.FOG);

        return field;
    }

    private void initFactories(BMField field) {
        buildingInstanceFactory.init(field, getWorldModelConfiguration());
        heroInstanceFactory.init(field, getWorldModelConfiguration());
        skillInstanceFactory.init(field, getWorldModelConfiguration());
        transportInstanceFactory.init(field, getWorldModelConfiguration());
        unitInstanceFactory.init(field, getWorldModelConfiguration());
    }

    /**
     * Make list rectangles from string line
     *
     * @param rectangles 12x12,24x24;18x20,30x35
     * @return
     */
    private List<Rect> makeRectanglesFromString(String rectangles) {
        if (rectangles == null || rectangles.isEmpty() || "null".equals(rectangles)) {
            return Collections.EMPTY_LIST;
        }
        String[] rects;
        try {
            rects = rectangles.split(";");
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
        List<Rect> rectanglesList = new ArrayList<>();

        for (String r : rects) {
            rectanglesList.add(new Rect(r.trim()));
        }

        return rectanglesList;
    }

    private void defineMovementTypeByRects(BMField field, List<Rect> rects, BMMovementType type) {
        for (Rect r : rects) {
            for (int ix = r.getStartX(); ix <= r.getStopX(); ix++) {
                for (int iy = r.getStartY(); iy <= r.getStopY(); iy++) {
                    BMCell cell = field.getCell(ix, iy);
                    if (cell != null) {
                        cell.setMovementType(type);
                    }
                }
            }
        }
    }

    private void createBuildings(BMField field) {
        List<Map<String, Object>> buildings = (List<Map<String, Object>>) gameConfiguration.getConfig().get(ProtocolStrings.BUILDINGS);
        if (buildings != null) {
            for (Map<String, Object> building : buildings) {
                BMBaseBuilding b = buildingInstanceFactory.createBuilding(building);
                if (b != null) {
                    field.addBuilding(b);
                }
            }
        }
    }

    private void createUnits(BMField field) {
        List<Map<String, Object>> units = (List<Map<String, Object>>) gameConfiguration.getConfig().get(ProtocolStrings.UNITS);
        if (units != null) {
            for (Map<String, Object> unit : units) {
                BMBaseUnit simpleUnit = unitInstanceFactory.createUnitForField(unit);
                if (simpleUnit != null) {
                    field.registerUnit(simpleUnit);
                    if (!simpleUnit.isInGame() && simpleUnit.getFraction() == BMField.ALLY_FRACTION_IDX) {
                        simpleUnit.setInGame(true);
                    }
                }
            }
        }
    }

    private void createHeros(BMField field) throws NumberFormatException {
        String heroLvl = ProtocolStrings.LVL + 1;
        int posX = 10;
        int posY = 10;
        boolean heroOnField = false;

        Map<String, Object> heroInstanceProps = (Map<String, Object>) gameConfiguration.getConfig().get(ProtocolStrings.HERO);
        if (heroInstanceProps != null) {
            heroOnField = Boolean.valueOf(String.valueOf(heroInstanceProps.get("onfield")));

            if (heroInstanceProps.get(ProtocolStrings.LVL) != null && heroOnField) {
                heroLvl = String.valueOf(heroInstanceProps.get(ProtocolStrings.LVL));
                String pos = String.valueOf(heroInstanceProps.get(ProtocolStrings.POSITION));
                posX = Integer.valueOf(pos.substring(0, pos.indexOf("x")));
                posY = Integer.valueOf(pos.substring(pos.indexOf("x") + 1));
            }
        }

        if (heroOnField) {
            Map<String, Object> heroes = getWorldModelConfiguration().getHeroTypes();
            if (heroes != null) {
                Map<String, Map<String, Object>> hero = (Map<String, Map<String, Object>>) heroes.get(ProtocolStrings.HERO);

                if (hero != null) {
                    Map<String, Object> props = (Map<String, Object>) hero.get(ProtocolStrings.LVLS).get(heroLvl);
                    if (props != null) {
                        BMBaseUnit baseUnit = heroInstanceFactory.createHero(props);
                        baseUnit.setPosition(field.getCell(posX, posY));
                        field.registerUnit(baseUnit);
                    }
                }
            }
        }
    }

    private void initReward(BMField field) {
        Map<String, Object> reward = (Map<String, Object>) gameConfiguration.getConfig().get(ProtocolStrings.REWARD);
        if (reward != null) {
            for (Map.Entry<String, Object> r : reward.entrySet()) {
                Integer value = Integer.valueOf(String.valueOf(r.getValue()));
                field.getGameResult().incResourceValue(r.getKey(), value);
            }
        }
    }

    public BMField createField() throws Exception {
        try {
            BMField field = createFieldInstance();
            initFactories(field);
            createBuildings(field);
            field.finalizeField();
            createUnits(field);
            createHeros(field);
            initReward(field);

            return field;
        } catch (Exception ex) {
            throw new Exception("Create field exceprtion", ex);
        }
    }

    public IWorldModelConfiguration getWorldModelConfiguration() {
        return worldModelConfiguration;
    }

    public void setWorldModelConfiguration(IWorldModelConfiguration worldModelConfiguration) {
        this.worldModelConfiguration = worldModelConfiguration;
    }

    public IModelConfiguration getGameConfiguration() {
        return gameConfiguration;
    }

    public void setGameConfiguration(IModelConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public IGlobalConfiguration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public void setGlobalConfiguration(IGlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }

    public BMBaseSkill createSkill(String skillName, int lvl) {
        Map<String, Object> skills = getWorldModelConfiguration().getSkillTypes();
        String _lvl = ProtocolStrings.LVL + lvl;
        Map<String, Object> skill = (Map<String, Object>) skills.get(skillName);
        if (skill != null) {
            Map<String, Object> lvls = (Map<String, Object>) skill.get(ProtocolStrings.LVLS);
            Map<String, Object> skillProps = (Map<String, Object>) lvls.get(_lvl);
            return skillInstanceFactory.createAbilitySkill(skillName, skillProps);
        } else {
            return null;
        }
    }

    public class Rect {

        private int startX, startY, stopX, stopY;

        public Rect() {
        }

        public Rect(int startX, int startY, int stopX, int stopY) {
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }

        /**
         *
         * @param rect 12x12,20x20
         */
        public Rect(String rect) {
            int coma = rect.indexOf(",");
            String start = rect.substring(0, coma);
            String stop = rect.substring(coma + 1);

            int chX1 = start.indexOf("x");
            startX = Integer.valueOf(start.substring(0, chX1));
            startY = Integer.valueOf(start.substring(chX1 + 1));

            int chX2 = stop.indexOf("x");
            stopX = Integer.valueOf(stop.substring(0, chX2));
            stopY = Integer.valueOf(stop.substring(chX2 + 1));
        }

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public int getStopX() {
            return stopX;
        }

        public void setStopX(int stopX) {
            this.stopX = stopX;
        }

        public int getStopY() {
            return stopY;
        }

        public void setStopY(int stopY) {
            this.stopY = stopY;
        }
    }
}
