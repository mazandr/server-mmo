package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import java.util.List;

public final class BMAttackBehavior extends BMBaseUnitBehavior {

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
        super.clearFocus();

        BMCell attackTarget = getNextAttackDest();
        move.setDestinationCell(attackTarget);
    }

    @Override
    public void notify(Object arg) {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(Object initParams) {
        move.setDestinationCell(getNextAttackDest());
    }

    private BMCell getNextAttackDest() {
        if (location.getPosition() == null) {
            return null;
        }
        List<BMBaseBuilding> buildings;
        if (((BMFractionEntity) getEntity()).getFraction() == BMField.ALLY_FRACTION_IDX) {
            buildings = getEntity().getField().getEnemyWinCondBuildings();
        } else {
            buildings = getEntity().getField().getAllyWinCondBuildings();
        }
        int dest = Integer.MAX_VALUE;
        BMCell cell = null;

        for (BMBaseBuilding b : buildings) {
            if (b.canBeFocused() && b.isAlive() && b.isInGame()) {
                for (BMCell c : b.getComponent(BMLargeLocatorComponent.class).getCorners()) {
                    int d = location.getPosition().squreDistanceBetween(c);
                    if (d < dest) {
                        dest = d;
                        cell = c;
                    }
                }
            }
        }
        return cell;
    }
}
