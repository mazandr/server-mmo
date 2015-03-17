package com.win.battle;

import com.win.battle.utils.LocalTestBattleConfigLoader;
import com.win.battle.utils.LocalWorldModelConfigLoader;
import com.win.strategy.battle.model.components.BMCarrierComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.model.support.IModelConfiguration;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;

/**
 *
 * @author okopach
 */
public class UnitLandingTest extends BaseBattleTest implements IModelCallback {

    private static final String ENEMY_BUILDING_IDENTITY = "enemy";
    private TreeSet<BMCell> firstPriorityLandingArea;
    private Set<BMCell> secondPriorityArea = new HashSet<>();
    private boolean useAreas = false;
    private BMCell buildingPosition;
    private List<BMBaseUnit> carriedUnits = new ArrayList<>();

    @Test
    public void test() {
        engine.addCallback(this);
        engine.getField().setTotalGameFrames(3);
        engine.run();
    }

    @Override
    public void executeInsideLoop() {
        BMField field = engine.getField();
        switch (engine.getTimeTicks()) {
            case 1:
                BMBaseBuilding building = field.getBuildings().get(ENEMY_BUILDING_IDENTITY);
                carriedUnits.addAll(building.getComponent(BMCarrierComponent.class).getCarriedUnits());
                buildingPosition = building.getPosition();

                BMLargeLocatorComponent locator = building.getComponent(BMLargeLocatorComponent.class);
                if (locator == null) {
                    useAreas = false;
                    return;
                }

                firstPriorityLandingArea = new TreeSet<>(new Comparator<BMCell>() {
                    @Override
                    public int compare(BMCell o1, BMCell o2) {
                        int d1 = buildingPosition.squreDistanceBetween(o1);
                        int d2 = buildingPosition.squreDistanceBetween(o2);
                        return Integer.compare(d1, d2);
                    }
                });

                boolean evenCenter = locator.getCenter().evenCell();
                for (BMCell pos : locator.getInnerArea()) {
                    if (pos.evenCell() == evenCenter) {
                        firstPriorityLandingArea.add(pos);
                    } else {
                        secondPriorityArea.add(pos);
                    }
                }

                for (BMCell pos : locator.getOuterArea()) {
                    if (pos.evenCell() == evenCenter) {
                        firstPriorityLandingArea.add(pos);
                    } else {
                        secondPriorityArea.add(pos);
                    }
                }
                useAreas = true;

                building.getComponent(BMHealsComponent.class).damage(1000000);
                break;
            case 2:
                if (useAreas) {
                    for (BMBaseUnit unit : carriedUnits) {
                        if (firstPriorityLandingArea.size() > 0) {
                            BMCell cell = firstPriorityLandingArea.first();
                            boolean result = false;
                            for (BMEntity object : cell.getInnerObjects().values()) {
                                if (object.getIdentifier().equals(unit.getIdentifier())) {
                                    result = true;
                                    break;
                                }
                            }
                            if (!result) {
                                throw new RuntimeException("Unit in cells not found");
                            }
                            firstPriorityLandingArea.remove(cell);
                        } else if (secondPriorityArea.size() > 0) {
                            BMCell cell = secondPriorityArea.iterator().next();
                            boolean result = false;
                            for (BMEntity object : cell.getInnerObjects().values()) {
                                if (object.getIdentifier().equals(unit.getIdentifier())) {
                                    result = true;
                                    break;
                                }
                            }
                            if (!result) {
                                throw new RuntimeException("Unit in cells not found");
                            }
                            secondPriorityArea.remove(cell);
                        } else {
                            boolean result = false;
                            for (BMEntity object : buildingPosition.getInnerObjects().values()) {
                                if (object.getIdentifier().equals(unit.getIdentifier())) {
                                    result = true;
                                    break;
                                }
                            }
                            if (!result) {
                                throw new RuntimeException("Unit in cells not found");
                            }
                        }
                    }
                } else {
                    if (field.getCell(buildingPosition.getPosX(), buildingPosition.getPosY()).getInnerObjects().size() < carriedUnits.size()) {
                        throw new RuntimeException("error");
                    }
                }
                break;
            default:
                return;
        }
    }

    @Override
    public void executeEndLoop() {
        System.out.println("finish");
    }

    @Override
    public IWorldModelConfiguration getWorldModelConfiguration() {
        LocalWorldModelConfigLoader loader = new LocalWorldModelConfigLoader();
        loader.load("config/wm.json");
        return loader;
    }

    @Override
    public IModelConfiguration getBattleConfiguration() {
        LocalTestBattleConfigLoader loader = new LocalTestBattleConfigLoader();
        loader.load("config/bt_unitLanding.json");
        return loader;
    }
}
