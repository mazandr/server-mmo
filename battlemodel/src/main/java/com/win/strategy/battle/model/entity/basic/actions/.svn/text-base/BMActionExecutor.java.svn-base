package com.win.strategy.battle.model.entity.basic.actions;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.IBMAttactable;
import static com.win.strategy.battle.model.entity.basic.actions.BMActionTargetType.any;
import static com.win.strategy.battle.model.entity.basic.actions.BMActionTargetType.buildings;
import static com.win.strategy.battle.model.entity.basic.actions.BMActionTargetType.units;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;
import com.win.strategy.battle.model.entity.basic.effects.BMEffectApplyType;
import com.win.strategy.battle.model.entity.basic.effects.BMEffectFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author okopach
 */
public class BMActionExecutor extends BMObject {

    private BMEffectFactory factory;
    private BMActionProperties actionProperties = null;
    private BMField field;
    private Map<String, Map<String, BMAction>> actions;

    public BMActionExecutor(BMField field) {
        this.field = field;
        this.factory = field.getEffectFactory();
        this.actions = new HashMap<>();
    }

    public void addAction(BMAction action) {
        //Only for test
        if(action.getEntity().getIdentifier().equals("enemyAlcoholstand"))
        {
            System.out.println("");
        }
        //
        BMActionProperties acProps = action.getProperties();
        if (acProps == null) {
            return;
        }
        acProps.incAccTime();
        if (!acProps.canPuls()) {
            return;
        }
        if (actions.containsKey(action.getProperties().getOwner().getIdentifier())) {
            Map<String, BMAction> entityActions = actions.get(action.getProperties().getOwner().getIdentifier());
            if (!entityActions.containsKey(action.getIdentifier())) {
                entityActions.put(action.getIdentifier(), action);
            }
        } else {
            Map<String, BMAction> entityActions = new HashMap<>();
            entityActions.put(action.getIdentifier(), action);
            actions.put(action.getProperties().getOwner().getIdentifier(), entityActions);
        }
    }

    public void execute() {
        for (Map<String, BMAction> entityActions : actions.values()) {
            Iterator iterator = entityActions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BMAction> action = (Map.Entry<String, BMAction>) iterator.next();
                if (executeAction(action.getValue())) {
                    iterator.remove();
                }
            }
        }
    }

    private boolean executeAction(BMAction action) {
        action.incActionTime();
        if (!action.canActionApply()) {
            return false;
        }
        action.resetActionTime();

        BMActionProperties aprop = action.getProperties();
        if (aprop == null) {
            return true;
        }
        actionProperties = aprop.clone();
        if (aprop.canPuls()) {
            aprop.resetPusl();
        }

        ReactionMethod cellMod = null;
        ReactionMethod targetMod = null;

        BMBaseEffect effect = null;
        if (actionProperties.getActionType() == BMActionType.instant) {
            effect = factory.getEffect(actionProperties);
        }

        if (actionProperties.getApplyType() == BMEffectApplyType.area
                && actionProperties.getActionType() == BMActionType.distributed) {
            cellMod = new ReactionMethod(effect, actionProperties) {
                @Override
                public void invoke(BMObject obj) {
                    ((BMCell) obj).addEffect(getProperties().clone());
                }

                @Override
                public void invoke(Set<BMFractionEntity> targets) {
                }
            };
        } else {
            targetMod = new ReactionMethod(effect, actionProperties) {
                @Override
                public void invoke(Set<BMFractionEntity> trgts) {

                    if (getProperties().getActionType() == BMActionType.instant) {
                        getEffect().setTargets(trgts);
                        if (getEffect().canPuls()) {
                            getEffect().puls();
                        }
                    } else {
                        for (BMFractionEntity t : trgts) {
                            ((IBMAttactable) t).addEffect(getProperties());
                        }
                    }
                }

                @Override
                public void invoke(BMObject obj) {
                    if (getProperties().getActionType() == BMActionType.instant) {
                        getEffect().addTarget((BMFractionEntity) obj);
                        if (getEffect().canPuls()) {
                            getEffect().puls();
                        }
                    } else {
                        ((IBMAttactable) obj).addEffect(getProperties());
                    }
                }
            };
        }

        if (actionProperties.getRadius() == 0 && action.getActionTarget() != null) {
            if (targetMod != null) {
                targetMod.invoke(action.getActionTarget());
            }
        } else {
            if (action.getActionPosition() != null) {
                placingReaction(action.getActionPosition(), cellMod, targetMod);
            }
        }
        return true;
    }

    private void placingReaction(BMCell pos, ReactionMethod cellsMod, ReactionMethod targetMod) {
        int radius = actionProperties.getRadius();
        BMActionTargetType targetType = actionProperties.getActionTargetType();

        Set<BMFractionEntity> trgts = new HashSet<>();
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if ((i * i + j * j < radius * radius) || radius == 0) {
                    BMCell cell = pos.getField().getCell(pos.getPosX() + i, pos.getPosY() + j);
                    if (cell != null) {
                        if (cellsMod != null) {
                            actionProperties.addZoneCell(cell);
                            cellsMod.invoke(cell);
                        }
                        if (targetMod != null) {
                            for (BMEntity t : cell.getInnerObjects().values()) {
                                BMFractionEntity trg = (BMFractionEntity) t;

                                if (trg != null && trg.isInGame()) {
                                    switch (targetType) {
                                        case any:
                                            if (!trg.innerType().contains(BMEntity.BMEntityType.trap)) {
                                                trgts.add(trg);
                                            }
                                            break;
                                        case units:
                                            if (trg.innerType().contains(BMEntity.BMEntityType.unit)) {
                                                trgts.add(trg);
                                            }
                                            break;
                                        case buildings:
                                            if (trg.innerType().contains(BMEntity.BMEntityType.building)
                                                    || trg.innerType().contains(BMEntity.BMEntityType.wall)
                                                    || trg.innerType().contains(BMEntity.BMEntityType.trap)) {
                                                trgts.add(trg);
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (targetMod != null) {
            targetMod.invoke(trgts);
        }
    }

    private static abstract class ReactionMethod {

        private BMBaseEffect effect;
        private BMActionProperties properties;

        public BMBaseEffect getEffect() {
            return effect;
        }

        public BMActionProperties getProperties() {
            return properties;
        }

        public ReactionMethod(BMBaseEffect effect, BMActionProperties properties) {
            this.effect = effect;
            this.properties = properties;
        }

        public abstract void invoke(Set<BMFractionEntity> targets);

        public abstract void invoke(BMObject obj);
    }
}
