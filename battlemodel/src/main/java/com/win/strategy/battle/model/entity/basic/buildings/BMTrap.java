package com.win.strategy.battle.model.entity.basic.buildings;

import com.win.strategy.battle.model.components.BMAttackComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.components.BMPhysicVisionComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public class BMTrap extends BMBaseBuilding implements IBMBehaviorable {

    private BMAttackComponent attack;
    private BMPhysicVisionComponent vision;
    private int activationTimesNumber;
    private BMHealsComponent heals;
    private BMLargeLocatorComponent locator;

    public BMTrap(BMField field) {
        super(field);
        heals = getComponent(BMHealsComponent.class);
        locator = getComponent(BMLargeLocatorComponent.class);
        setVisible(false);
        locator.setMargin(0);
        this.attack = addComponent(new BMAttackComponent());
        this.vision = addComponent(new BMPhysicVisionComponent());
        this.attack.setInstanceSplashDamage(true);

        setCanBeCaptured(false);
        setCanBeDestroyed(true);
        setCanBeFocused(true);
    }

    @Override
    public EnumSet<BMEntityType> innerType() {
        return EnumSet.of(BMEntityType.trap);
    }

    @Override
    public void behave(int step) {
        activationTimesNumber++;

        if (!heals.isAlive()) {
            return;
        }

        IBMTargetable target = vision.findTarget();
        if (target != null) {
            if (target.getPriorityState() != getAttackPriorityType()) {
                target = null;
            }
        }

        if (attack.getTarget() == null) {
            if (target != null) {
                attack.setTarget(target);
            }
        } else {
            if (target != null
                    && !attack.getTarget().equals(target)
                    && vision.getAttackPriorityType() != BMAttackPriorityType.any) {
                attack.setTarget(target);
            }
        }

        if (attack.getTarget() != null) {
            attack.attack();
            if (!attack.getTarget().isAlive()) {
                this.setActionState(BMEntityActionState.idle);
                attack.setTarget(null);
            }
        }

    }

    public List<BMCell> getVision() {
        return this.vision.getVision();
    }

    public IBMTargetable getFocus() {
        return this.attack.getTarget();
    }

    public void setFocus(IBMTargetable target) {
        this.attack.setTarget(target);
    }

    public void setAttackPriorityType(BMAttackPriorityType attackPriorityType) {
        this.vision.setAttackPriorityType(attackPriorityType);
        this.attack.setAttackPriorityType(attackPriorityType);
    }

    public BMAttackPriorityType getAttackPriorityType() {
        return this.vision.getAttackPriorityType();
    }

    public void setDps(int dps) {
        this.attack.setDps(dps);
    }

    public void setAttackRange(int attackRange) {
        this.vision.setAttackRange(attackRange);
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attack.setAttackSpeed(attackSpeed);
    }

    public int getActivationTimesNumber() {
        return activationTimesNumber;
    }

    public void setActivationTimesNumber(int activationTimesNumber) {
        this.activationTimesNumber = activationTimesNumber;
    }

    public void setVisionRange(int visionRange) {
        this.vision.setVisionRange(visionRange);
    }

    public void setSplashRadius(int splashRadius) {
        this.attack.setSplashRadius(splashRadius);
    }

    public void setFavoriteTargetMultiplier(double multiplier) {
        this.attack.setFavoriteTargetMultiplier(multiplier);
    }

    public void setSelfDamage(int selfDamage) {
        this.attack.setSelfDamage(selfDamage);
    }

    @Override
    public void reinitBehavior() {
    }

    public void setDamageOnlyPriorityType(boolean flag) {
        attack.setDamageOnlyPriorityType(flag);
    }
}
