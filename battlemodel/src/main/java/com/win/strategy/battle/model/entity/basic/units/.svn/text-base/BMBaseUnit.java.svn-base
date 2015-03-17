package com.win.strategy.battle.model.entity.basic.units;

import com.win.strategy.battle.model.entity.basic.actions.BMAction;
import com.win.strategy.battle.model.components.BMAllSeeAllVisionComponent;
import com.win.strategy.battle.model.components.BMEffectsHolderComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMMoveComponent;
import com.win.strategy.battle.model.components.BMReactionComponent;
import com.win.strategy.battle.model.components.BMSmallLocatorComponent;
import com.win.strategy.battle.model.components.behavior.BMBaseBehavior;
import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.BMMovementType;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMAttactable;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.IBMMovable;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public class BMBaseUnit extends BMFractionEntity implements IBMMovable,
        IBMBehaviorable, IBMAttactable {

    private BMAllSeeAllVisionComponent vision;
    private BMMoveComponent move;
    private BMReactionComponent attack;
    private BMSmallLocatorComponent location;
    private BMHealsComponent heals;
    private BMBaseBehavior behavior;
    private BMBattleBehavior battleBehavior;
    private BMEffectsHolderComponent effects;
    private int housingSpace = 1;
    private boolean actionOnMove;

    @Deprecated
    public boolean isActionOnMove() {
        return actionOnMove;
    }

    /**
     * TODO Bad solution, must be refactored in behavior parameter
     *
     * @param actionOnMove
     * @deprecated
     */
    @Deprecated
    public void setActionOnMove(boolean actionOnMove) {
        this.actionOnMove = actionOnMove;
    }

    public int getHousingSpace() {
        return housingSpace;
    }

    public void setHousingSpace(int housingSpace) {
        this.housingSpace = housingSpace;
    }

    public BMBaseUnit(BMField field) {
        super(field);

        setCanBeFocused(true);
        setCanBeCaptured(true);
        setCanBeDestroyed(true);

        heals = addComponent(new BMHealsComponent());
        location = addComponent(new BMSmallLocatorComponent());
        attack = addComponent(new BMReactionComponent());
        move = addComponent(new BMMoveComponent());
        vision = addComponent(new BMAllSeeAllVisionComponent());
        effects = addComponent(new BMEffectsHolderComponent());
        setPriorityState(BMAttackPriorityType.units);
    }

    public BMBattleBehavior getBattleBehavior() {
        return battleBehavior;
    }

    public void setBehavior(BMBattleBehavior behavior, Object initParams) {
        if (this.battleBehavior != behavior) {
            BMBaseBehavior bhv = getField().getBehaviorFactory().getBehavior(behavior);
            this.behavior = addComponent(bhv);
        }
        if (this.behavior != null) {
            this.behavior.init(initParams);
        }
    }

    @Override
    public List<BMCell> getLocation() {
        List<BMCell> loc = new ArrayList<>();
        loc.add(location.getPosition());
        return loc;
    }

    @Override
    public void behave(int step) {
        effects.applyAffects();
        behavior.behave(step);
    }

    @Override
    public void setPosition(BMCell cell) {
        location.setPosition(cell);
        vision.calculateVision();
    }

    @Override
    public BMCell getPosition() {
        return location.getPosition();
    }

    @Override
    public BMCell getCenter() {
        return location.getPosition();
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
    public BMFractionEntity getFocus() {
        return attack.getTarget();
    }

    @Override
    public void setFocus(IBMTargetable target) {
        attack.setTarget(target);
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

    public void setAttackRange(int attackRange) {
        vision.setAttackRange(attackRange);
    }

    public int getAttackRange() {
        return vision.getAttackRange();
    }

    public void setCurrentHP(int currentHP) {
        heals.setCurrentHP(currentHP);
    }

    public int getCurrentHP() {
        return heals.getCurrentHP();
    }

    public void setMaxHP(int maxHP) {
        heals.setMaxHP(maxHP);
    }

    public void setMoveSpeed(double moveSpeed) {
        move.setMoveSpeed(moveSpeed);
    }

    public void setVisionRange(int visionRange) {
        vision.setVisionRange(visionRange);
    }

    @Override
    public EnumSet<BMEntityType> innerType() {
        if (battleBehavior == BMBattleBehavior.hero) {
            return EnumSet.of(BMEntityType.unit, BMEntityType.hero);
        }
        return EnumSet.of(BMEntityType.unit);
    }

    @Override
    public void notify(Object arg) {
        super.notify(arg);
        if (!heals.isAlive()) {
            location.clearLocation();
            if (getFractionList() != null) {
                getFractionList().remove(this);
            }
            getField().getAllEntities().remove(this);
        }
    }

    @Override
    public void setAttackPriorityType(BMAttackPriorityType attackPriority) {
        this.vision.setAttackPriorityType(attackPriority);
    }

    @Override
    public BMAttackPriorityType getAttackPriorityType() {
        return vision.getAttackPriorityType();
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

    @Override
    public void reinitBehavior() {
        behavior.reinitBehavior();
    }

    @Override
    public void addEffect(BMActionProperties properties) {
        effects.addEffect(properties);
    }

    @Override
    public void addReactionItem(BMActionProperties properties) {
        attack.addReactionAction(new BMAction(properties));
    }

    public List<BMAction> getActions() {
        return attack.getActions();
    }
}
