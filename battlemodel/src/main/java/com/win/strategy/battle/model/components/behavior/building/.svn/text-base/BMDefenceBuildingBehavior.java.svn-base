package com.win.strategy.battle.model.components.behavior.building;

import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;

/**
 *
 * @author vlischyshyn
 */
public class BMDefenceBuildingBehavior extends BMBaseBiuldingBehavior {

    @Override
    public void init(Object initParams) {
    }

    @Override
    public void notify(Object arg) {
    }

    @Override
    public void behave(int step) {
        if (!heals.isAlive() || !getEntity().isInGame()) {
            return;
        }

        if (attack.getActions().isEmpty()) {
            return;
        }

        attack.setReactPos(location.getCenter());
        IBMTargetable target = vision.findTarget();

        if (target == null) {
            clearFocus();
            return;
        }
        BMFractionEntity attcakableTarget = (BMFractionEntity) target;
        attackPos = null;
        if (attcakableTarget.isAlive() && attcakableTarget.isInGame()) {
            if (attcakableTarget.innerType().contains(BMEntity.BMEntityType.building)) {
                BMBaseBuilding targetBuilding = (BMBaseBuilding) attack.getTarget();
                if (targetBuilding != null) {
                    attackPos = targetBuilding.getCloserAttackPos(location.getCenter());
                }
            } else {
                attackPos = attcakableTarget.getPosition();
            }

            if (vision.canAttact(attackPos)) {
                attack.react();
            } else {
                getEntity().setActionState(BMEntityActionState.idle);
            }
        }
    }

    private void clearFocus() {
        getEntity().setActionState(BMEntityActionState.idle);
        attack.setTarget(null);
        attack.setReactPos(null);
    }
}
