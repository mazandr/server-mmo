package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.entity.BMCell;

/**
 *
 * @author vlischyshyn
 */
public class BMHipnoticBehavior extends BMBaseUnitBehavior {

    private BMCell dest;

    @Override
    public void init(Object initParams) {
        try {
            dest = (BMCell) initParams;
        } catch (Exception e) {
            dest = null;
        }
    }

    @Override
    public void notify(Object arg) {
    }

    @Override
    public void behave(int step) {
        if (!heals.isAlive() || !getEntity().isInGame()) {
            return;
        }
        if (dest != null) {
            move.setDestinationCell(dest);
            move.move(step);
        }
    }
}
