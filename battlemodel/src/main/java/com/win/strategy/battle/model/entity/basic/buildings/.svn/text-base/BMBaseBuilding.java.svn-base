package com.win.strategy.battle.model.entity.basic.buildings;

import com.win.strategy.battle.model.entity.BMCell;

import java.util.List;

import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.components.BMResourceComponent;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.resources.BMResource;
import com.win.strategy.battle.model.entity.basic.IBMLocable;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author vlischyshyn
 */
public class BMBaseBuilding extends BMFractionEntity implements IBMLocable {

    private BMHealsComponent heals;
    private BMLargeLocatorComponent location;
    private BMBuildingsGroup group = BMBuildingsGroup.common;
    private boolean winCondition;
    private Map<String, Double> winRate;
    private BMBuildingState buildingState;
    private BMResourceComponent resources;

    public BMBaseBuilding(BMField field) {
        super(field);
        this.location = addComponent(new BMLargeLocatorComponent());
        this.heals = addComponent(new BMHealsComponent());
        this.resources = addComponent(new BMResourceComponent());
        this.buildingState = BMBuildingState.free;

        setCanBeCaptured(true);
        setCanBeDestroyed(false);
        setCanBeFocused(false);
    }

    public BMBuildingState getBuildingState() {
        return buildingState;
    }

    public void setBuildingState(BMBuildingState buildingState) {
        this.buildingState = buildingState;
    }

    public boolean isWinCondition() {
        return winCondition;
    }

    public void setWinCondition(boolean winCondition) {
        this.winCondition = winCondition;
    }

    public BMBuildingsGroup getGroup() {
        return group;
    }

    public void setGroup(BMBuildingsGroup group) {
        this.group = group;

        if (group == BMBuildingsGroup.defensive) {
            setPriorityState(BMAttackPriorityType.defence);
        } else if (group == BMBuildingsGroup.wall) {
            setPriorityState(BMAttackPriorityType.walls);
        } else if (group == BMBuildingsGroup.produce || group == BMBuildingsGroup.storage) {
            setPriorityState(BMAttackPriorityType.resources);
        }
    }

    @Override
    public void damage(int damageValue) {
        heals.damage(damageValue);
    }

    @Override
    public boolean isAlive() {
        return heals.isAlive();
    }

    @Override
    public void setPosition(BMCell cell) {
        location.setPosition(cell);
    }

    @Override
    public BMCell getPosition() {
        return location.getPosition();
    }

    public BMCell getCenter() {
        return location.getCenter();
    }

    @Override
    public List<BMCell> getLocation() {
        return location.getInnerArea();
    }

    public void setCurrentHP(int currentHP) {
        heals.setCurrentHP(currentHP);
    }

    public void setMaxHP(int maxHP) {
        heals.setMaxHP(maxHP);
    }

    public void setSizeX(int sizeX) {
        location.setSizeX(sizeX);
    }

    public void setSizeY(int sizeY) {
        location.setSizeY(sizeY);
    }

    public BMCell getCloserAttackPos(BMCell from) {

        int i = 0;
        BMCell cell = location.getInnerArea().get(i);
        int distance = from.squreDistanceBetween(cell);
        i++;
        for (; i < location.getInnerArea().size(); i++) {
            int tmp_dist = from.squreDistanceBetween(location.getInnerArea().get(i));
            if (distance >= tmp_dist) {
                cell = location.getInnerArea().get(i);
                distance = tmp_dist;
            }
        }
        return cell;
    }

    @Override
    public EnumSet<BMEntityType> innerType() {
        return EnumSet.of(BMEntityType.building);
    }

    public BMCell getCloserDestPos(final BMCell from) {
        if (from == null) {
            return null;
        }

        List<BMCell> outerCells = location.getOuterArea();
        Set<BMCell> cellDefiner = new TreeSet<>(new Comparator<BMCell>() {
            @Override
            public int compare(BMCell o1, BMCell o2) {
                long d1 = from.squreDistanceBetween(o1);
                long d2 = from.squreDistanceBetween(o2);
                return Long.compare(d1, d2);
            }
        });

        if (outerCells != null) {
            for (BMCell c : outerCells) {
                cellDefiner.add(c);
            }
            return cellDefiner.iterator().next();
        }

        return null;
    }

    @Override
    public void notify(Object arg) {
        super.notify(arg);
        if (!heals.isAlive()) {
            setActionState(BMEntityActionState.idle);
            if (canBeDestroyed()) {
                location.clearLocation();
                buildingState = BMBuildingState.destroyed;
                if (getFractionList() != null) {
                    getFractionList().remove(this);
                }
//                TODO: refactor calculate result 
                getField().getGameResult().addDestroyedBuilding(this);
                getField().getAllEntities().remove(this);
            }
        }
        // TODO -oArthas -tBattle : capture building notification
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = super.toMapObject();
        result.put("bstate", buildingState.toString());
        result.put(ProtocolStrings.WIN_C, winCondition);
        return result;
    }

    @Override
    public boolean isVisible() {
        return location.isVisible();

    }

    @Override
    public void setVisible(boolean visible) {
        location.setVisible(visible);
    }

    @Override
    public void heal(int healValue) {
        heals.heal(healValue);
    }

    public Map<String, Double> getWinRate() {
        return winRate;
    }

    public void setWinRate(Map<String, Double> winRate) {
        this.winRate = winRate;
    }

    public void addResource(BMResource resource) {
        resources.addResource(resource);
    }

    public Collection<BMResource> getResources() {
        return resources.getResourceList();
    }

    public void setRotated(boolean orientation) {
        location.setRotated(orientation);
    }
}
