package com.win.strategy.battle.model.entity.basic.buildings;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public enum BMBuildingsGroup {

    common("building.common"),
    produce("building.produce"),
    storage("building.storage"),
    defensive("building.defensive"),
    wall("building.wall"),
    traps("building.traps"),
    unitcreator("building.unitCreator"),
    changeBehavior("building.changeBehavior");
    private String string;
    private final static Map<String, BMBuildingsGroup> instances = new HashMap<>();

    static {
        for (BMBuildingsGroup v : BMBuildingsGroup.values()) {
            instances.put(v.string, v);
        }
    }

    private BMBuildingsGroup(String string) {
        this.string = string;
    }

    public static BMBuildingsGroup _valueOf(String name) {
        return instances.get(name);
    }
}
