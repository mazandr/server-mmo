package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;

/**
 *
 * @author vlischyshyn
 */
public class BMBossDefanceBehavior extends BMBaseUnitBehavior {

    private BMCell bossPos;

    @Override
    public void init(Object initParams) {
        bossPos = null;
    }

    @Override
    public void notify(Object arg) {
    }

    @Override
    public void behave(int step) {
        if (!heals.isAlive() || !getEntity().isInGame()) {
            return;
        }

        if (location.getPosition().equals(bossPos)) {
            attackEnemy();
        } else {
            bossPos = getCloseBossPosition();
            move.setDestinationCell(bossPos);
            move.move(step);
        }
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

    private BMCell getCloseBossPosition() {
        double distance = Integer.MAX_VALUE;
        BMCell result = null;
        BMField field = getEntity().getField();

        for (BMBaseBuilding b : field.getEnemyWinCondBuildings()) {
            BMLargeLocatorComponent bloc = b.getComponent(BMLargeLocatorComponent.class);
            if (bloc != null) {
                for (BMCell cell : bloc.getOuterArea()) {
                    int tmpDistance = cell.squreDistanceBetween(location.getPosition());
                    if (tmpDistance < distance) {
                        result = cell;
                    }
                }
            }
        }
        return result;
    }
}
