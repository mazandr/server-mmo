package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.entity.basic.BMFractionEntity;

/**
 *
 * @author vlischyshyn
 */
public class BMHoldPositionBehavior extends BMBaseUnitBehavior {

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

        attackEnemy();
    }

    private void attackEnemy() {
        manageTarget();
        if (attack.getTarget() != null) {
            BMFractionEntity attcakableTarget = (BMFractionEntity) attack.getTarget();

            if (attcakableTarget.isAlive() && attcakableTarget.isInGame()) {
                attackPos = attcakableTarget.getCenter();
                attack.setReactPos(attackPos);
                if (vision.canAttact(attackPos) && getEntity().isInGame()) {
                    attack.react();

                    if (!attcakableTarget.isAlive() || !attcakableTarget.isInGame()) {
                        clearFocus();
                    }
                }
            } else {
                clearFocus();
            }
        }
    }
}
