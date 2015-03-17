package com.win.strategy.battle.model.entity.basic.effects.impl;

import com.win.strategy.battle.model.components.BMMoveComponent;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;

/**
 *
 * @author vlischyshyn
 */
public class BMMovementModificationEffect extends BMBaseEffect {

    public BMMovementModificationEffect(BMActionProperties props) {
        super(props);
    }

    @Override
    protected void beforeEffect() {
        for (BMFractionEntity target : getTargets()) {
            if (target.getFraction() != getFraction()) {
                BMMoveComponent move = target.getComponent(BMMoveComponent.class);
                if (move != null) {
                    move.setSpeedModLoef(getValue());
                }
            }
        }
    }

    @Override
    protected void effect() {
        System.out.println("");
    }

    @Override
    protected void afterEffect() {
        for (BMFractionEntity target : getTargets()) {
            if (target.getFraction() != getFraction()) {
                BMMoveComponent move = target.getComponent(BMMoveComponent.class);
                if (move != null) {
                    move.setSpeedModLoef(0);
                }
            }
        }
    }
}
