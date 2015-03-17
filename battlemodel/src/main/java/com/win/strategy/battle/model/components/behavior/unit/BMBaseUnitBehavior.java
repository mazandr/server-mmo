package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.components.BMAllSeeAllVisionComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMMoveComponent;
import com.win.strategy.battle.model.components.BMReactionComponent;
import com.win.strategy.battle.model.components.BMSmallLocatorComponent;
import com.win.strategy.battle.model.components.behavior.BMBaseBehavior;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;

/**
 *
 * @author vlischyshyn
 */
public abstract class BMBaseUnitBehavior extends BMBaseBehavior {

    protected BMAllSeeAllVisionComponent vision;
    protected BMHealsComponent heals;
    protected BMReactionComponent attack;
    protected BMMoveComponent move;
    protected BMSmallLocatorComponent location;
    protected BMCell targetDest;
    protected BMCell attackPos;

    @Override
    public void setEntity(BMEntity entity) {
        super.setEntity(entity);
        vision = entity.getComponent(BMAllSeeAllVisionComponent.class);
        heals = entity.getComponent(BMHealsComponent.class);
        attack = entity.getComponent(BMReactionComponent.class);
        move = entity.getComponent(BMMoveComponent.class);
        // TODO -oArthas resolve for large location component
        location = entity.getComponent(BMSmallLocatorComponent.class);
    }

    protected IBMTargetable manageTarget() {
        IBMTargetable target = vision.findTarget();
        if (attack.getTarget() == null) {
            if (target != null) {
                attack.setTarget(target);
            } else {
                clearFocus();
            }
            targetChanged();
        } else {
            if (target != null
                    && !attack.getTarget().equals(target)
                    && vision.getAttackPriorityType() != BMAttackPriorityType.any) {
                attack.setTarget(target);
                targetChanged();
            }
        }
        return target;
    }

    protected void clearFocus() {
        getEntity().setActionState(BMEntityActionState.idle);
        attack.setTarget(null);
        attack.setReactPos(null);
    }

    protected void targetChanged() {
    }
}
