package com.win.strategy.battle.model.entity.basic.effects.impl;

import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;

/**
 *
 * @author vlischyshyn
 */
public class BMHealEffect extends BMBaseEffect {

    public BMHealEffect(BMActionProperties props) {
        super(props);
    }

    @Override
    protected void afterEffect() {
    }

    @Override
    protected void effect() {
        for (BMFractionEntity target : getTargets()) {
            if (target == null
                    || !target.isInGame()
                    || !target.isAlive()) {
                return;
            }
            if (target.getFraction() == getFraction()) {
                target.getComponent(BMHealsComponent.class).heal(getValue());
            }
        }
    }

    @Override
    protected void beforeEffect() {
    }
}
