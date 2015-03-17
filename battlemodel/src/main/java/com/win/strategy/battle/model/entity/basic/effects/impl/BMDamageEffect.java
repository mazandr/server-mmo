package com.win.strategy.battle.model.entity.basic.effects.impl;

import com.win.strategy.battle.model.components.BMHealsComponent;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMAttactable;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;

/**
 *
 * @author vlischyshyn
 */
public class BMDamageEffect extends BMBaseEffect {

    public BMDamageEffect(BMActionProperties props) {
        super(props);
    }

    @Override
    protected void afterEffect() {
    }

    @Override
    protected void effect() {
        for (BMFractionEntity target : getTargets()) {
            if (target == null
                    || !target.isAlive()
                    || !target.isInGame()) {
                return;
            }
            int dmg = getValue();
            if (getOwner() != null
                    && IBMAttactable.class.isInstance(getOwner())) {
                if (((IBMAttactable) getOwner()).getAttackPriorityType() == target.getPriorityState()) {
                    dmg = (int) (getValue() * getFavoriteTargetMultiplier());
                }
            }

            if (target.getFraction() != getFraction()) {
                target.getComponent(BMHealsComponent.class).damage(dmg);
            }

            if (getOwner() != null
                    && BMFractionEntity.class.isInstance(getOwner())
                    && getSelfDamage() > 0) {
                ((BMFractionEntity) getOwner()).damage(getSelfDamage());
            }
        }
    }

    @Override
    protected void beforeEffect() {
    }
}
