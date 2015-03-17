package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;

public final class BMAgresiveHoldPositionBehavior extends BMBaseUnitBehavior {

    private BMCell holdPos;

    @Override
    public void setEntity(BMEntity entity) {
        super.setEntity(entity);
    }

    @Override
    public void behave(int step) {
        if (!heals.isAlive() || !getEntity().isInGame()) {
            return;
        }

        manageTarget();

        if (attack.getTarget() != null) {
            BMFractionEntity attcakableTarget = (BMFractionEntity) attack.getTarget();

            if (attcakableTarget.isAlive() && attcakableTarget.isInGame()) {

                attackPos = attcakableTarget.getCenter();
                targetDest = attcakableTarget.nearestDestCell((BMFractionEntity) getEntity(), location.getPosition(), attackPos, vision.getAttackRange());

                attcakableTarget.addReservedCell((BMFractionEntity) getEntity(), targetDest);
                attack.setReactPos(attackPos);
                move.setDestinationCell(targetDest);

                if (vision.canAttact(attackPos)) {
                    attack.react();

                    if (!attcakableTarget.isAlive() || !attcakableTarget.isInGame()) {
                        clearFocus();
                    }
                    return;
                }
            } else {
                clearFocus();
            }
        }

        move.move(step);
    }

    @Override
    protected void clearFocus() {
        super.clearFocus(); //To change body of generated methods, choose Tools | Templates.
        move.setDestinationCell(holdPos);
    }

    @Override
    public void notify(Object arg) {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(Object initParams) {
        initializedParams = initParams;
        if (!getEntity().isInGame()) {
            return;
        }
        String cellCode = String.valueOf(initParams);
        holdPos = getEntity().getField().getCellByCode(cellCode);
        if (holdPos == null) {
            holdPos = location.getPosition();
        }
    }
}
