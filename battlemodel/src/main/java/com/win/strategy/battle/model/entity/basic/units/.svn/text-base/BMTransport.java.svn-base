package com.win.strategy.battle.model.entity.basic.units;

import com.win.strategy.battle.model.components.BMCarrierComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMMoveComponent;
import com.win.strategy.battle.model.components.BMSmallLocatorComponent;
import com.win.strategy.battle.model.components.behavior.unit.BMCarrierBehavior;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.BMMovementType;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.IBMMovable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author vgryb
 */
public class BMTransport extends BMFractionEntity implements IBMBehaviorable, IBMMovable {

    private BMMoveComponent move;
    private BMSmallLocatorComponent location;
    private BMHealsComponent heals;
    private BMCarrierBehavior behavior;
    private BMCarrierComponent carrier;

    public BMTransport(BMField field) {
        super(field);

        setCanBeFocused(true);
        setCanBeCaptured(false);
        setCanBeDestroyed(true);

        heals = addComponent(new BMHealsComponent());
        location = addComponent(new BMSmallLocatorComponent());
        move = addComponent(new BMMoveComponent());
        carrier = addComponent(new BMCarrierComponent());
        behavior = addComponent(new BMCarrierBehavior());
        setPriorityState(BMAttackPriorityType.transport);
    }

    public int getCurrentCapacity() {
        return this.carrier.getCurrentCapacity();
    }

    public int getMaxCapacity() {
        return carrier.getMaxCapacity();
    }

    public void setMaxCapacity(int maxCapacity) {
        this.carrier.setMaxCapacity(maxCapacity);
    }

    @Override
    public EnumSet<BMEntityType> innerType() {
        return EnumSet.of(BMEntityType.transport);
    }

    @Override
    public void notify(Object arg) {
        super.notify(arg);
        if (!heals.isAlive()) {
            location.clearLocation();
            carrier.onePrionLandUnits();
            setCanBeFocused(false);
            if (getFractionList() != null) {
                getFractionList().remove(this);
            }
            getField().getAllEntities().remove(this);
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
        return location.getPosition();
    }

    @Override
    public List<BMCell> getLocation() {
        List<BMCell> loc = new ArrayList<>();
        loc.add(location.getPosition());
        return loc;
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
    public void behave(int step) {
        behavior.behave(step);
    }

    @Override
    public BMMovementType getMovementType() {
        return move.getMovementType();
    }

    @Override
    public BMCell getDestinationCell() {
        return move.getDestinationCell();
    }

    @Override
    public double getMoveSpeed() {
        return move.getMoveSpeed();
    }

    public boolean addUnit(BMBaseUnit unit) {
        return carrier.addUnit(unit);
    }

    public List<BMBaseUnit> getTransportableUnits() {
        return carrier.getCarriedUnits();
    }

    public void setDestinationCell(BMCell cell) {
        move.setDestinationCell(cell);
    }

    @Override
    public void heal(int healValue) {
        heals.heal(healValue);
    }

    public void setCurrentHP(int hp) {
        heals.setCurrentHP(hp);
    }

    public void setMaxHP(int hp) {
        heals.setMaxHP(hp);
    }

    @Override
    public void reinitBehavior() {
        behavior.reinitBehavior();
    }

    public void setMoveSpeed(double speed) {
        move.setMoveSpeed(speed);
    }
}
