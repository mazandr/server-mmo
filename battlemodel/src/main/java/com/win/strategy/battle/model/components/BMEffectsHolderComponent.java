package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.basic.BMEntity.BMEntityType;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.actions.BMActionTargetType;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect;
import com.win.strategy.battle.model.entity.basic.effects.BMBaseEffect.BMEffectHolder;
import com.win.strategy.battle.model.entity.basic.effects.BMEffectApplyType;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vlischyshyn
 */
public class BMEffectsHolderComponent extends BMBaseComponent {

    private Map<String, BMEffectHolder> effects;

    public BMEffectsHolderComponent() {
        effects = new HashMap<>();
    }

    public void addEffect(BMActionProperties effectProperties) {
        if (effectProperties == null) {
            return;
        }
        BMActionTargetType pe = effectProperties.getActionTargetType();
        if (pe != BMActionTargetType.any) {
            if (pe == BMActionTargetType.buildings && !getEntity().innerType().contains(BMEntityType.building)) {
                return;
            }
            if (pe == BMActionTargetType.units && !getEntity().innerType().contains(BMEntityType.unit)) {
                return;
            }
        }

        BMEffectHolder effectCategory = effects.get(effectProperties.effectKey());
        if (effectCategory == null) {
            effectCategory = new BMEffectHolder(getEntity().getField().getEffectFactory());
            effects.put(effectProperties.effectKey(), effectCategory);
        }
        BMBaseEffect effect = effectCategory.addByProperties(effectProperties);
        if (effect != null) {
            effect.addTarget((BMFractionEntity) getEntity());
        }
    }

    @Override
    public void notify(Object arg) {
    }

    public void applyAffects() {
        BMFractionEntity fr = (BMFractionEntity) getEntity();

        for (BMEffectHolder category : effects.values()) {
            for (BMBaseEffect effect : category) {
                if (effect.getApplyType() == BMEffectApplyType.area) {
                    if (effect.getZone() == null) {
                        effect.dismiss();
                    } else {
                        boolean flag = false;
                        for (BMCell c : fr.getLocation()) {
                            if (effect.getZone().contains(c)) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            effect.dismiss();
                        }
                    }
                }
                effect.puls();
            }
            category.invalidate();
        }
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        Set<String> effectNames = new HashSet<>();
        for (BMEffectHolder el : effects.values()) {
            for (BMBaseEffect e : el) {
                effectNames.add(e.getAbilitylType().toString());
            }
        }

        if (!effectNames.isEmpty()) {
            result.put(ProtocolStrings.EFFECTS, effectNames);
        }
        return result;
    }

    public Map<String, BMEffectHolder> getEffects() {
        return effects;
    }
}
