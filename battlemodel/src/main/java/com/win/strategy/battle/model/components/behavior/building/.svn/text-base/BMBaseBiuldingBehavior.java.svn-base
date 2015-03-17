package com.win.strategy.battle.model.components.behavior.building;

import com.win.strategy.battle.model.components.BMAllSeeAllVisionComponent;
import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.components.BMReactionComponent;
import com.win.strategy.battle.model.components.behavior.BMBaseBehavior;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.basic.BMEntity;

/**
 *
 * @author vlischyshyn
 */
public abstract class BMBaseBiuldingBehavior extends BMBaseBehavior {

    protected BMAllSeeAllVisionComponent vision;
    protected BMHealsComponent heals;
    protected BMReactionComponent attack;
    protected BMLargeLocatorComponent location;
    protected BMCell attackPos;

    @Override
    public void setEntity(BMEntity entity) {
        super.setEntity(entity);
        vision = entity.getComponent(BMAllSeeAllVisionComponent.class);
        heals = entity.getComponent(BMHealsComponent.class);
        attack = entity.getComponent(BMReactionComponent.class);
        location = entity.getComponent(BMLargeLocatorComponent.class);
    }
}
