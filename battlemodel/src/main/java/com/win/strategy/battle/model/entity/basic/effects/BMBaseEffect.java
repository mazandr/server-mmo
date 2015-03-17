package com.win.strategy.battle.model.entity.basic.effects;

import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author vlischyshyn
 */
public abstract class BMBaseEffect extends BMActionProperties implements IBMEffect {

    private int accTime = 0;
    private int accTick = 0;
    private Set<BMFractionEntity> targets = new HashSet<>();

    private BMBaseEffect() {
    }

    public BMBaseEffect(BMActionProperties source) {
        super(source);
    }

    public Set<BMFractionEntity> getTargets() {
        return targets;
    }

    public void setTargets(Set<BMFractionEntity> target) {
        this.targets.addAll(target);
    }

    public void addTarget(BMFractionEntity target) {
        this.targets.add(target);
    }

    @Override
    public void puls() {

        if (accTime != 0 && expired()) {
            return;
        }

        if (accTime == 0) {
            beforeEffect();
        }

        if (accTick >= getTick()) {
            effect();
            accTick = getField().getGameProcessStep();
        } else {
            accTick += getField().getGameProcessStep();
        }

        accTime += getField().getGameProcessStep();

        if (expired()) {
            afterEffect();
        }
    }

    public void dismiss() {
        accTime = getTime();
        afterEffect();
    }

    protected abstract void afterEffect();

    protected abstract void effect();

    protected abstract void beforeEffect();

    private boolean expired() {
        return accTime >= getTime();
    }

    public static class BMEffectHolder extends ArrayList<BMBaseEffect> {

        private BMEffectFactory factory;

        public BMEffectHolder(BMEffectFactory factory) {
            super();
            this.factory = factory;
        }

        public BMBaseEffect addByProperties(BMActionProperties properties) {
            if (properties == null) {
                return null;
            }
            if (properties.getMaxStack() > this.size()) {
                BMBaseEffect effect = factory.getEffect(properties);
                super.add(effect);
                return effect;
            } else {
                return null;
            }
        }

        @Override
        public boolean add(BMBaseEffect e) {
            if (e == null) {
                return false;
            }
            if (e.getMaxStack() < this.size()) {
                return super.add(e);
            } else {
                return false;
            }
        }

        public void invalidate() {
            for (BMBaseEffect e : this) {
                if (e.expired()) {
                    e.dismiss();
                    this.remove(e);
                }
            }
        }
    }
}
