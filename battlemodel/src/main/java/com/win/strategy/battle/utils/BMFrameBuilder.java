package com.win.strategy.battle.utils;

import com.win.strategy.common.utils.ProtocolStrings;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vlischyshyn
 */
public class BMFrameBuilder {

    public static Map<String, Object> build(BMField field, int step) {
        return build(field, step, true);
    }

    /**
     * Build frame for response
     *
     * @param field
     * @param step
     * @param checkModification set flag <b>modify<b/> to false in entity
     * @return frame
     */
    public static Map<String, Object> build(BMField field, int step, boolean checkModification) {

        Set<Map<String, Object>> activities = new HashSet<>();

        for (BMBaseBuilding building : field.getBuildings().values()) {
            if (checkModification) {
                if (building.isModified() && building.isInGame()) {
                    activities.add(building.toMapObject());
                    building.setModified(false);
                }
            } else {
                activities.add(building.toMapObject());
            }
        }

        for (IBMBehaviorable u : field.getActivitiesAsList()) {
            BMEntity unit = (BMEntity) u;
            if (checkModification) {
                if (unit.isModified() && unit.isInGame()) {
                    activities.add(unit.toMapObject());
                    unit.setModified(false);
                }
            } else {
                activities.add(unit.toMapObject());
            }
        }

        if (activities.size() > 0) {
            Map<String, Object> frame = new HashMap<>();
            frame.put("iterationstep", step);
            frame.put("activities", activities);
            return frame;
        }

        return null;
    }

    public static Map<String, Object> initialFrameBuild(BMField field) {
        Map<String, Object> frame = new HashMap<>();
        frame.put(ProtocolStrings.SIZE_X, field.getMaxSizeX());
        frame.put(ProtocolStrings.SIZE_Y, field.getMaxSizeY());
        frame.put(ProtocolStrings.GFRAMES_NUM, field.getTotalGameFrames());
        frame.put(ProtocolStrings.GFRAMES_PACK_SIZE, field.getItterationSize());
        frame.put(ProtocolStrings.GSTEP_VAL, field.getGameProcessStep());
        frame.put(ProtocolStrings.ROAD_CELLS, field.getRoadCells());
        frame.put(ProtocolStrings.FOG_CELLS, field.getFogCells());

        Set<Map<String, Object>> activities = new HashSet<>();
        for (BMBaseBuilding building : field.getBuildings().values()) {
            activities.add(building.toMapObject());
        }

        for (IBMBehaviorable u : field.getActivitiesAsList()) {
            BMEntity unit = (BMEntity) u;
            activities.add(unit.toMapObject());
        }
        if (activities.size() > 0) {
            frame.put("activities", activities);
        }

        Map<String, Set<String>> cars = new HashMap<>();
        for (BMBaseUnit u : field.getAllyUnits()) {
            String carId = u.getPerfotatingData(ProtocolStrings.CAR_ID);
            if (carId != null && !"null".equals(carId)) {
                Set<String> units = cars.get(carId);
                if (units == null) {
                    units = new HashSet<>();
                    cars.put(carId, units);
                }
                units.add(u.getIdentifier());
            }
        }
        if (cars.size() > 0) {
            frame.put(ProtocolStrings.AVAILABLE_UNITS, cars);
        }

        if (field.getSkills().size() > 0) {
            frame.put(ProtocolStrings.AVAILABLE_SKILLS, field.getSkills().keySet());
        }

        return frame;
    }

    public static Map<String, Object> endFrame(BMField field, boolean userStopedInProcessing, boolean timeIsOut) {
        Map<String, Object> frame = new HashMap<>();
        frame.put("gameoverStatus", "lose");
        if (userStopedInProcessing || timeIsOut) {
            return frame;
        }
        if (field.getGameOverStatus() == BMField.GameOverStatus.GAME_OVER_WIN) {
            frame.put("gameoverStatus", "win");
        }

        return frame;
    }
}
