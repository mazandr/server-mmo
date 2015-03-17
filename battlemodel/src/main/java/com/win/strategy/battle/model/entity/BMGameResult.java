package com.win.strategy.battle.model.entity;

import com.win.strategy.battle.BMWinConditionHandler;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vlischyshyn
 */
public class BMGameResult {

    private String recivedEntityId;
    private Set<String> capturedBuildings = new HashSet<>();
    private Set<String> destroedBuildings = new HashSet<>();
    private Map<String, Integer> resources = new HashMap();
    private BMWinConditionHandler winConditionHandler = new BMWinConditionHandler();

    public BMGameResult() {
    }

    public String getRecivedEntityId() {
        return recivedEntityId;
    }

    public void setRecivedEntityId(String recivedEntityId) {
        this.recivedEntityId = recivedEntityId;
    }

    public void addCapturedBuilding(BMBaseBuilding building) {
        capturedBuildings.add(building.getIdentifier());
        winConditionHandler.auditBuildingRate(building);
    }

    public void addDestroyedBuilding(BMBaseBuilding building) {
        destroedBuildings.add(building.getIdentifier());
        winConditionHandler.auditBuildingRate(building);
    }

    public Set<String> getCapturedBuildings() {
        return capturedBuildings;
    }

    public BMWinConditionHandler getWinConditionHandler() {
        return winConditionHandler;
    }

    public void incResourceValue(String resourceName, Integer value) {
        if (!resources.containsKey(resourceName)) {
            resources.put(resourceName, value);
        } else {
            resources.put(resourceName, resources.get(resourceName) + value);
        }
    }

    public Map<String, Integer> getResources() {
        return resources;
    }
}
