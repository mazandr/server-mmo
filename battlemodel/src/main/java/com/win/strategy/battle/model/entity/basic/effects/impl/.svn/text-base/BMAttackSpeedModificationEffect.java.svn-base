package com.win.strategy.battle.model.entity.basic.effects.impl;

import com.win.strategy.battle.model.components.BMReactionComponent;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.actions.BMAction;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;

/**
 *
 * @author vlischyshyn
 */
public class BMAttackSpeedModificationEffect extends BMBaseEffect {

    public BMAttackSpeedModificationEffect(BMActionProperties props) {
        super(props);
    }

    @Override
    protected void beforeEffect() {
        if (getValue() == 0) {
            return;
        }
        if (getValue() > 0) {
            incAttackSpeed(getValue());
        } else {
            decAttackSpeed(getValue());
        }
    }

    @Override
    protected void effect() {
    }

    @Override
    protected void afterEffect() {
        if (getValue() == 0) {
            return;
        }
        if (getValue() > 0) {
            decAttackSpeed(getValue());
        } else {
            incAttackSpeed(getValue());
        }
    }

    private void incAttackSpeed(int percent) {
        for (BMFractionEntity entity : getTargets()) {
            BMReactionComponent component = entity.getComponent(BMReactionComponent.class);
            if (component != null) {
                for (BMAction action : component.getActions()) {
                    BMActionProperties actionProperties = action.getProperties();
                    if (actionProperties != null) {
                        int delay = action.getProperties().getCaptureTargetTime();
                        if (delay > 0) {
                            delay = Math.abs(delay - (delay / 100 * percent));
                            if (delay == 0) {
                                delay = 1;
                            }
                            actionProperties.setCaptureTargetTime(delay);
                        }
                    }
                }
                component.getEntity().setModified(true);
            }
        }
    }

    private void decAttackSpeed(int percent) {
        for (BMFractionEntity entity : getTargets()) {
            BMReactionComponent component = entity.getComponent(BMReactionComponent.class);
            if (component != null) {
                for (BMAction action : component.getActions()) {
                    BMActionProperties actionProperties = action.getProperties();
                    if (actionProperties != null) {
                        int delay = action.getProperties().getCaptureTargetTime();
                        if (delay > 0) {
                            delay = Math.abs(delay + (delay / 100 * percent));
                            actionProperties.setCaptureTargetTime(delay);
                        }
                    }
                }
                component.getEntity().setModified(true);
            }
        }
    }
}
