package com.win.strategy.battle.model.entity;

import com.win.strategy.battle.model.entity.basic.actions.BMActionExecutor;
import com.win.strategy.battle.BMWinConditionHandler;
import com.win.strategy.battle.model.components.behavior.BMBehaviorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.effects.BMEffectFactory;
import com.win.strategy.battle.model.entity.basic.buildings.BMBuildingState;
import com.win.strategy.battle.model.entity.basic.resources.BMResource;
import com.win.strategy.battle.model.entity.basic.resources.BMUsableResources;
import com.win.strategy.battle.model.entity.basic.skills.BMBaseSkill;
import com.win.strategy.battle.model.entity.basic.units.BMTransport;
import com.win.strategy.battle.pathfinder.AStar;
import com.win.strategy.battle.pathfinder.AreaMap;
import com.win.strategy.battle.pathfinder.heuristics.AStarHeuristic;
import com.win.strategy.battle.pathfinder.heuristics.ClosestHeuristic;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author vlischyshyn
 */
public class BMField extends BMObject {
    
    public static final int SKILL_COST_INCREASED_PERSENT = 2;
    public static final int ALLY_FRACTION_IDX = 1;
    public static final int NETRAL_FRACTION_IDX = 0;
    public static final int ENEMY_FRACTION_IDX = 2;
    private int gameProcessStep = 100;
    private int totalGameFrames = 1800; //*100ms
    private int itterationSize = 10;
    private int maxSizeX;
    private int maxSizeY;
    private double speedMultiplier = 1;
    private double speedKoef = 12d;
    private ArrayList<ArrayList<BMCell>> cells;
    private Map<String, IBMBehaviorable> activities = new ConcurrentHashMap<>();
    private Map<String, BMBaseBuilding> buildings = new HashMap<>();
    private Map<String, BMBaseSkill> availabeSkills = new HashMap<>();
    private List<BMFractionEntity> allEntities = new ArrayList<>();
    private List<BMBaseUnit> enemyUnits = new ArrayList<>();
    private List<BMBaseUnit> allyUnits = new ArrayList<>();
    private List<BMBaseBuilding> allyWinCondBuildings = new ArrayList<>();
    private List<BMBaseBuilding> enemyWinCondBuildings = new ArrayList<>();
    private List<IBMBehaviorable> durableActions = new ArrayList<>();
    private AreaMap areaMap;
    private AStarHeuristic heuristic = new ClosestHeuristic();
    private AStar pathFinder;
    private BMGameResult gameResult = new BMGameResult();
    private GameOverStatus gameOverStatus = GameOverStatus.NONE;
    private BMBehaviorFactory behaviorFactory = new BMBehaviorFactory();
    private BMEffectFactory effectFactory = new BMEffectFactory();
    private BMUsableResources resources = new BMUsableResources();
    private String roadCells;
    private String fogCells;
    private Map<String, Integer> countUsedSkills = new HashMap();
    private BMActionExecutor actionExecutor;
    
    public BMField(int maxSizeX, int maxSizeY) {
        this.actionExecutor = new BMActionExecutor(this);
        this.maxSizeX = maxSizeX;
        this.maxSizeY = maxSizeY;
        initCells();
    }
    
    public BMActionExecutor getActionExecutor() {
        return actionExecutor;
    }
    
    public List<BMFractionEntity> getAllEntities() {
        return allEntities;
    }
    
    public int getTotalGameFrames() {
        return totalGameFrames;
    }
    
    public void setTotalGameFrames(int totalGameFrames) {
        this.totalGameFrames = totalGameFrames;
    }
    
    public int getItterationSize() {
        return itterationSize;
    }
    
    public void setItterationSize(int itterationSize) {
        this.itterationSize = itterationSize;
    }
    
    public int getGameProcessStep() {
        return gameProcessStep;
    }
    
    public void setGameProcessStep(int gameProcessStep) {
        this.gameProcessStep = gameProcessStep;
    }
    
    public BMEffectFactory getEffectFactory() {
        return effectFactory;
    }
    
    public BMGameResult getGameResult() {
        return gameResult;
    }
    
    public List<IBMBehaviorable> getDurableActions() {
        return durableActions;
    }
    
    public void setDurableActions(List<IBMBehaviorable> durableSkills) {
        this.durableActions = durableSkills;
    }
    
    public List<BMBaseBuilding> getAllyWinCondBuildings() {
        return allyWinCondBuildings;
    }
    
    public List<BMBaseBuilding> getEnemyWinCondBuildings() {
        return enemyWinCondBuildings;
    }
    
    public List<BMBaseUnit> getEnemyUnits() {
        return enemyUnits;
    }
    
    public List<BMBaseUnit> getAllyUnits() {
        return allyUnits;
    }
    
    public void registerUnit(BMBaseUnit unit) {
        activities.put(unit.getIdentifier(), unit);
        allEntities.add(unit);
        if (unit.getFraction() == ALLY_FRACTION_IDX) {
            unit.setInGame(false);
            allyUnits.add(unit);
            unit.setFractionList(allyUnits);
        } else {
            unit.setInGame(true);
            enemyUnits.add(unit);
            unit.setFractionList(enemyUnits);
        }
    }
    
    public void registerResources(Collection<BMResource> bMResources) {
        resources.addAllResources(bMResources);
    }
    
    public Collection<BMResource> getResourceList() {
        return resources.getResourceList();
    }
    
    public void enterUnit(String unitId, int x, int y) {
        BMCell pos = getCell(x, y);
        if (pos == null) {
            return;
        }
        BMBaseUnit unit = (BMBaseUnit) activities.get(unitId);
        if (unit == null) {
            return;
        }
        enterUnit(unit, pos);
    }
    
    public void enterUnit(BMBaseUnit unit, BMCell pos) {
        unit.setPosition(pos);
        unit.setInGame(true);
        unit.reinitBehavior();
    }
    
    public void addTransport(BMTransport transport) {
        activities.put(transport.getIdentifier(), transport);
        allEntities.add(transport);
        transport.setInGame(true);
    }
    
    public void addBuilding(BMBaseBuilding building) {
        buildings.put(building.getIdentifier(), building);
        allEntities.add(building);
        building.setInGame(true);
        if (IBMBehaviorable.class.isInstance(building)) {
            activities.put(building.getIdentifier(), (IBMBehaviorable) building);
        }
        
        if (building.isWinCondition()) {
            if (building.getFraction() == ALLY_FRACTION_IDX) {
                building.setFractionList(allyWinCondBuildings);
                allyWinCondBuildings.add(building);
            } else {
                building.setFractionList(enemyWinCondBuildings);
                enemyWinCondBuildings.add(building);
                gameResult.getWinConditionHandler().initBuildingRates(building);
            }
        }
    }
    
    @Deprecated
    public BMBaseBuilding captureBuilding(String buildingId) {
        BMBaseBuilding building = buildings.get(buildingId);
        
        if (building.getFractionList() != null) {
            building.getFractionList().remove(building);
        }
        
        gameResult.addCapturedBuilding(building);
        resources.inc(building.getResources());
        building.setBuildingState(BMBuildingState.captured);
        
        return building;
    }
    
    public void addSkill(BMBaseSkill skill) {
        availabeSkills.put(skill.getIdentifier(), skill);
    }
    
    public Map<String, BMBaseSkill> getSkills() {
        return availabeSkills;
    }
    
    private void incUsedSkill(String skillId) {
        if (countUsedSkills.containsKey(skillId)) {
            countUsedSkills.put(skillId, countUsedSkills.get(skillId) + 1);
        } else {
            countUsedSkills.put(skillId, 1);
        }
    }
    
    private Collection<BMResource> calculateSkillCost(BMBaseSkill skill) {
        int cnt = 1;
        if (countUsedSkills.containsKey(skill.getIdentifier())) {
            cnt = countUsedSkills.get(skill.getIdentifier()) + 1;
        }
        
        if (cnt == 1) {
            return skill.getResources();
        }
        
        Collection<BMResource> newResources = new HashSet<>();
        for (BMResource resource : skill.getResources()) {
            int oldCost = resource.getValue();
            BMResource newResource = new BMResource(resource.getIdentifier(), oldCost + (oldCost / 100 * SKILL_COST_INCREASED_PERSENT) * cnt);
            newResources.add(newResource);
        }
        return newResources;
    }
    
    private void useSkill(BMBaseSkill skill) {
        if (skill == null) {
            return;
        }
        Collection<BMResource> skillCost = calculateSkillCost(skill);
        if (!resources.canDecResources(skillCost)) {
            return;
        }
        resources.dec(skillCost);
        incUsedSkill(skill.getIdentifier());
        skill.use();
    }
    
    public void useSkill(String skillId, String targetId, int step) {
        BMBaseSkill skill = availabeSkills.get(skillId);
        if (skill == null) {
            return;
        }
        
        BMFractionEntity skillTarget = (BMFractionEntity) activities.get(targetId);
        if (skillTarget == null) {
            return;
        }
        skill.setSkillTarget(skillTarget);
        useSkill(skill);
    }
    
    public void useSkill(String skillId, int posX, int posY, int step) {
        BMBaseSkill skill = availabeSkills.get(skillId);
        if (skill == null) {
            return;
        }
        BMCell p = this.getCell(posX, posY);
        if (p == null) {
            return;
        }
        skill.setSkillApplyPos(p);
        useSkill(skill);
    }
    
    public void finalizeField() {
        areaMap = new AreaMap(this);
        pathFinder = new AStar(areaMap, heuristic);
    }
    
    public Map<String, IBMBehaviorable> getActivities() {
        return activities;
    }
    
    public Collection<IBMBehaviorable> getActivitiesAsList() {
        if (activities != null) {
            return activities.values();
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
    
    public double getSpeedKoef() {
        return speedKoef;
    }
    
    public void setSpeedKoef(double speedKoef) {
        this.speedKoef = speedKoef;
    }
    
    public AreaMap getAreaMap() {
        return areaMap;
    }
    
    public Map<String, BMBaseBuilding> getBuildings() {
        return buildings;
    }
    
    public BMCell getCell(int x, int y) {
        if (x > maxSizeX - 1 || y > maxSizeY - 1 || y < 0 || x < 0) {
            return null;
        }
        return cells.get(x).get(y);
    }
    
    public BMCell getCellByCode(String code) {
        int chX = code.indexOf("x");
        String x = code.substring(0, chX);
        String y = code.substring(chX + 1);
        
        return getCell(Integer.valueOf(x).intValue(), Integer.valueOf(y).intValue());
    }
    
    public ArrayList<ArrayList<BMCell>> getCells() {
        return cells;
    }
    
    public int getMaxSizeX() {
        return maxSizeX;
    }
    
    public int getMaxSizeY() {
        return maxSizeY;
    }
    
    private void initCells() {
        
        cells = new ArrayList<>(maxSizeX);
        for (int x = 0; x < maxSizeX; x++) {
            cells.add(new ArrayList<BMCell>(maxSizeY));
            for (int y = 0; y < maxSizeY; y++) {
                BMCell cell = new BMCell();
                cell.setField(this);
                cell.setPosX(x);
                cell.setPosY(y);
                cell.setType(BMCellType.NEUTRAL);
                cell.setMovementType(BMMovementType.GROUND);
                cells.get(x).add(cell);
            }
        }
    }
    
    public GameOverStatus getGameOverStatus() {
        return gameOverStatus;
    }
    
    public AStar pathFinder() {
        return pathFinder;
    }
    
    public void resetAreaMap() {
        areaMap.clear();
    }
    
    public void setMaxSizeX(int maxSizeX) {
        this.maxSizeX = maxSizeX;
    }
    
    public void setMaxSizeY(int maxSizeY) {
        this.maxSizeY = maxSizeY;
    }
    
    public BMWinConditionHandler getWinConditionHandler() {
        return gameResult.getWinConditionHandler();
    }
    
    @Override
    public Map<String, Object> toMapObject() {
        return super.toMapObject();
    }
    
    public void addActivities(IBMBehaviorable activity) {
        activities.put(activity.getIdentifier(), activity);
        ((BMEntity) activity).setInGame(true);
    }
    
    public boolean checkNotGameOver() {
        
        boolean needToFinish = false;
        if (allyUnits.isEmpty()) {
            gameOverStatus = GameOverStatus.GAME_OVER_LOSE;
            needToFinish = true;
        }
        
        if (gameResult.getWinConditionHandler().checkValues()) {
            gameOverStatus = GameOverStatus.GAME_OVER_WIN;
        }
        
        if (enemyWinCondBuildings.isEmpty()) {
            needToFinish = true;
        }
        
        return !needToFinish;
    }
    
    public List<BMBaseUnit> returnAliveAllyUnits() {
        List<BMBaseUnit> result = new ArrayList<>();
        for (BMBaseUnit u : allyUnits) {
            if (u.isAlive()) {
                result.add(u);
            }
        }
        return result;
    }
    
    public BMBehaviorFactory getBehaviorFactory() {
        return this.behaviorFactory;
    }
    
    public String getRoadCells() {
        return this.roadCells;
    }
    
    public String getFogCells() {
        return this.fogCells;
    }
    
    public void setRoadCells(String roadCells) {
        this.roadCells = roadCells;
    }
    
    public void setFogCells(String fogCells) {
        this.fogCells = fogCells;
    }
    
    public static enum GameOverStatus {
        
        NONE,
        GAME_OVER_WIN,
        GAME_OVER_LOSE
    }
    
    public static enum BMLocationCellType {
        
        free(0),
        building(1),
        unit(2),
        trap(3),
        trash(4);
        private int value;
        
        private BMLocationCellType(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
}
