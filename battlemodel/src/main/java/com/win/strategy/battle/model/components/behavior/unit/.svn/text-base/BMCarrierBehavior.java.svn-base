package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.components.BMCarrierComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMMoveComponent;
import com.win.strategy.battle.model.components.BMSmallLocatorComponent;
import com.win.strategy.battle.model.components.behavior.BMBaseBehavior;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;

/**
 *
 * @author vgryb
 */
public class BMCarrierBehavior extends BMBaseBehavior {

    private BMMoveComponent move;
    private BMSmallLocatorComponent location;
    private BMCarrierComponent carrier;
    private BMHealsComponent heals;

    @Override
    public void setEntity(BMEntity entity) {
        super.setEntity(entity);
        move = entity.getComponent(BMMoveComponent.class);
        location = entity.getComponent(BMSmallLocatorComponent.class);
        carrier = entity.getComponent(BMCarrierComponent.class);
        heals = entity.getComponent(BMHealsComponent.class);
    }

    @Override
    public void behave(int step) {

        if (!getEntity().isInGame()) {
            return;
        }

        if (!heals.isAlive() || carrier.isEmpty()) {
            getEntity().getField().getActivities().remove(getEntity().getIdentifier());
            return;
        }

        if (location.getPosition().equals(move.getDestinationCell())) {
            carrier.onePrionLandUnits();
            ((BMFractionEntity) getEntity()).setCanBeFocused(false);
        } else {
            move.move(step);
        }
    }

    @Override
    public void init(Object initParams) {
        // TODO Auto-generated method stub
    }

    @Override
    public void notify(Object arg) {
        // TODO Auto-generated method stub
    }
}
