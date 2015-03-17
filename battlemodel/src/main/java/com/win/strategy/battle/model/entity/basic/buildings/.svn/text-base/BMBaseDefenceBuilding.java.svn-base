package com.win.strategy.battle.model.entity.basic.buildings;

import com.win.strategy.battle.model.entity.basic.actions.BMAction;
import com.win.strategy.battle.model.components.BMAllSeeAllVisionComponent;
import com.win.strategy.battle.model.components.BMEffectsHolderComponent;
import com.win.strategy.battle.model.components.BMReactionComponent;
import com.win.strategy.battle.model.components.behavior.BMBaseBehavior;
import com.win.strategy.battle.model.components.behavior.building.BMDefenceBuildingBehavior;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.IBMAttactable;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import java.util.EnumSet;


/**
 *
 * @author vlischyshyn
 */
public class BMBaseDefenceBuilding extends BMBaseBuilding implements IBMBehaviorable, IBMAttactable {

    private BMAllSeeAllVisionComponent vision;
    protected BMReactionComponent attack;
    protected BMBaseBehavior behavior;
    private BMEffectsHolderComponent effects;

    public BMBaseDefenceBuilding(BMField field) {
        super(field);

        setCanBeCaptured(false);
        setCanBeDestroyed(true);
        setCanBeFocused(false);

        vision = addComponent(new BMAllSeeAllVisionComponent());
        attack = addComponent(new BMReactionComponent());
        effects = addComponent(new BMEffectsHolderComponent());
        initBehavior();
        setBuildingState(BMBuildingState.none);
    }

    protected void initBehavior() {
        behavior = addComponent(new BMDefenceBuildingBehavior());
    }

    @Override
    public void behave(int step) {
        effects.applyAffects();
        behavior.behave(step);
    }

    public void setAttackRange(int attackRange) {
        vision.setAttackRange(attackRange);
    }

    public void setVisionRange(int visionRange) {
        vision.setVisionRange(visionRange);
    }

    public int getAttackRange() {
        return vision.getAttackRange();
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
    public IBMTargetable getFocus() {
        return attack.getTarget();
    }

    @Override
    public void setFocus(IBMTargetable target) {
        attack.setTarget(target);
    }

    public EnumSet<BMEntityType> innerType() {
        return EnumSet.of(BMEntityType.building, BMEntityType.defence);
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
}
