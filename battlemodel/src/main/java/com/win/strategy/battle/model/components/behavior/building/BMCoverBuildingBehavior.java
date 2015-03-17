package com.win.strategy.battle.model.components.behavior.building;

import com.win.strategy.battle.model.components.BMCarrierComponent;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.buildings.BMBuildingState;

public final class BMCoverBuildingBehavior extends BMBaseBiuldingBehavior {

    @Override
    public void behave(int step) {
        if (!heals.isAlive() || !getEntity().isInGame()) {
            return;
        }

        if (attack.getActions().isEmpty()) {
            return;
        }

        IBMTargetable target = vision.findTarget();
        //todo: need to correct cleaning target
        if (attack.getTarget() == null
                || !attack.getTarget().isAlive()
                || !attack.getTarget().isInGame()) {
            if (target != null) {
                attack.setTarget(target);
            } else {
                clearFocus();
            }
        } else {
            if (target != null) {
                if (!attack.getTarget().equals(target)
                        && !vision.canAttact(attack.getTarget().getPosition())) {
                    attack.setTarget(target);
                }
            }
        }

        if (attack.getTarget() != null) {
            BMFractionEntity attcakableTarget = (BMFractionEntity) attack.getTarget();
            if (attcakableTarget.isAlive() && attcakableTarget.isInGame()) {
                if (attcakableTarget.innerType().contains(BMEntity.BMEntityType.building)) {
                    BMBaseBuilding targetBuilding = (BMBaseBuilding) attack.getTarget();
                    attackPos = targetBuilding.getCloserAttackPos(location.getCenter());
                } else {
                    attackPos = attcakableTarget.getPosition();
                }
                attack.setReactPos(attackPos);

                if (vision.canAttact(attackPos)) {
                    attack.react();

                    if (!attack.getTarget().isAlive() || !attcakableTarget.isInGame()) {
                        clearFocus();
                    }
                } else {
                    clearFocus();
                    land();
                }
            } else {
                clearFocus();
                land();
            }
        }
    }

    @Override
    public void notify(Object arg) {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(Object initParams) {
        // TODO Auto-generated method stub
    }

    private void clearFocus() {
        getEntity().setActionState(BMEntityActionState.idle);
        attack.setTarget(null);
        attack.setReactPos(null);
    }

    private void land() {
        if (((BMFractionEntity) getEntity()).getFraction() == 1) {
            ((BMBaseBuilding) getEntity()).setBuildingState(BMBuildingState.free);
            BMCarrierComponent carrier = getEntity().getComponent(BMCarrierComponent.class);
            if (carrier != null) {
                carrier.onePrionLandUnits();
            }
        }
    }
}
