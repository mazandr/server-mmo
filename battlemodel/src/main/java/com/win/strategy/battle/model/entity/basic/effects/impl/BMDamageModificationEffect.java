package com.win.strategy.battle.model.entity.basic.effects.impl;

import com.win.strategy.battle.model.components.BMReactionComponent;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.actions.BMAbilitylType;
import com.win.strategy.battle.model.entity.basic.actions.BMAction;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;

/**
 *
 * @author okopach
 */
public class BMDamageModificationEffect extends BMBaseEffect {
    
    public BMDamageModificationEffect(BMActionProperties source) {
        super(source);
    }
    
    @Override
    protected void beforeEffect() {
        if (getValue() == 0) {
            return;
        }
        if (getValue() > 0) {
            incDamage(getValue());
        } else {
            decDamage(getValue());
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
            decDamage(getValue());
        } else {
            incDamage(getValue());
        }
    }
    
    private void incDamage(int percent) {
        for (BMFractionEntity entity : getTargets()) {
            BMReactionComponent component = entity.getComponent(BMReactionComponent.class);
            if (component != null) {
                for (BMAction action : component.getActions()) {
                    BMActionProperties actionProperties = action.getProperties();
                    if (actionProperties != null && actionProperties.getAbilitylType() == BMAbilitylType.damage) {
                        int damage = action.getProperties().getValue();
                        if (damage > 0) {
                            damage = Math.abs(damage - (damage / 100 * percent));
                            if (damage == 0) {
                                damage = 1;
                            }
                            actionProperties.setValue(damage);
                        }
                    }
                }
                component.getEntity().setModified(true);
            }
        }
    }
    
    private void decDamage(int percent) {
        for (BMFractionEntity entity : getTargets()) {
            BMReactionComponent component = entity.getComponent(BMReactionComponent.class);
            if (component != null) {
                for (BMAction action : component.getActions()) {
                    BMActionProperties actionProperties = action.getProperties();
                    if (actionProperties != null && actionProperties.getAbilitylType() == BMAbilitylType.damage) {
                        int damage = action.getProperties().getValue();
                        if (damage > 0) {
                            damage = Math.abs(damage + (damage / 100 * percent));
                            actionProperties.setValue(damage);
                        }
                    }
                }
                component.getEntity().setModified(true);
            }
        }
    }
}
