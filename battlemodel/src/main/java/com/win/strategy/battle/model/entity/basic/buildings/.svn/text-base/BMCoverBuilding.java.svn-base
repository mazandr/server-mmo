package com.win.strategy.battle.model.entity.basic.buildings;

import com.win.strategy.battle.model.entity.basic.actions.BMAction;
import com.win.strategy.battle.model.components.BMCarrierComponent;
import com.win.strategy.battle.model.components.behavior.building.BMCoverBuildingBehavior;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public class BMCoverBuilding extends BMBaseDefenceBuilding {

    private BMCarrierComponent units;
    /**
     * Coefficient of absorb damage (percent)
     */
    private int absorbCoefficient = 100;

    public BMCoverBuilding(BMField field) {
        super(field);
        units = addComponent(new BMCarrierComponent());

        setCanBeCaptured(true);
        setCanBeDestroyed(true);
        setBuildingState(BMBuildingState.free);
    }

    @Override
    protected void initBehavior() {
        behavior = addComponent(new BMCoverBuildingBehavior());
    }

    public List<BMBaseUnit> getUnits() {
        return units.getCarriedUnits();
    }

    @Override
    public boolean canBeFocused() {
        return !units.isEmpty();
    }

    public EnumSet<BMEntityType> innerType() {
        return EnumSet.of(BMEntityType.building, BMEntityType.defence, BMEntityType.cover);
    }

    @Override
    public void notify(Object arg) {
        super.notify(arg);
        if (!isAlive()) {
            units.despersionLandUnits();
            attack.getActions().clear();
            setBuildingState(BMBuildingState.destroyed);
        }
    }

    public void putUnit(BMBaseUnit unit) {
        if (unit == null) {
            return;
        }
        units.addUnit(unit);
        defineAttackRange(unit.getAttackRange());

        for (BMAction p : unit.getActions()) {
            attack.addReactionAction(p);
        }
        setBuildingState(BMBuildingState.occupied);
    }

    public void removeUnit(BMBaseUnit unit) {
        if (unit == null) {
            return;
        }
        units.landUnit(unit);
        for (BMAction item : attack.getActions()) {
            if (unit.equals(item.getProperties().getOwner())) {
                attack.getActions().remove(item);
            }
        }

        if (units.isEmpty()) {
            setBuildingState(BMBuildingState.free);
        }
    }

    public int getHousingSpace() {
        return units.getCurrentCapacity();
    }

    public int getFreeHousingSpace() {
        return units.getFreeHousingSpace();
    }

    public void setMaxHuosingSpace(int maxCapacity) {
        units.setMaxCapacity(maxCapacity);
    }

    private void defineAttackRange(int attackRange) {
        int newAttackRange = attackRange;
        if (newAttackRange > getAttackRange()) {
            setAttackRange(newAttackRange);
        }

    }

    @Override
    public void setPosition(BMCell cell) {
        super.setPosition(cell);
        units.initLandingAreas();
    }

    public int getAbsorbCoefficient() {
        return absorbCoefficient;
    }

    public void setAbsorbCoefficient(int absorbCoefficient) {
        this.absorbCoefficient = absorbCoefficient;
    }

    @Override
    public void damage(int damageValue) {
        super.damage(damageValue);
        if (this.absorbCoefficient == 100) {
            return;
        }
        int damageByUnit = damageValue;
        if (this.absorbCoefficient != 0) {
            damageByUnit = damageValue - (damageValue * this.absorbCoefficient / 100);
        }
        if (units != null) {
            for (BMBaseUnit unit : units.getCarriedUnits()) {
                unit.damage(damageByUnit);
            }
        }
    }
}
