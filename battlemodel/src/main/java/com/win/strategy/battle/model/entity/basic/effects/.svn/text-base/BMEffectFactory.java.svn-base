package com.win.strategy.battle.model.entity.basic.effects;

import com.win.strategy.battle.model.entity.basic.effects.impl.BMDamageEffect;
import com.win.strategy.battle.model.entity.basic.effects.impl.BMHealEffect;
import com.win.strategy.battle.model.entity.basic.effects.impl.BMAttackSpeedModificationEffect;
import com.win.strategy.battle.model.entity.basic.effects.impl.BMMovementModificationEffect;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.actions.BMAbilitylType;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.effects.impl.BMDamageModificationEffect;
import java.util.HashMap;
import java.util.Map;

public class BMEffectFactory extends BMObject {

    private Map<BMAbilitylType, Class<?>> effectsTypesCache;

    public BMEffectFactory() {
        init();
    }

    private void init() {
        effectsTypesCache = new HashMap<>(BMAbilitylType.values().length);
        effectsTypesCache.put(BMAbilitylType.damage, BMDamageEffect.class);
        effectsTypesCache.put(BMAbilitylType.heal, BMHealEffect.class);
//        abilityTypesCache.put(BMAbilitylType.resurrection, BMResurrectionActionBehavior.class);
        effectsTypesCache.put(BMAbilitylType.mod_attackspeed, BMAttackSpeedModificationEffect.class);
        effectsTypesCache.put(BMAbilitylType.mod_movespeed, BMMovementModificationEffect.class);
        effectsTypesCache.put(BMAbilitylType.mod_damage, BMDamageModificationEffect.class);
    }

    public BMBaseEffect getEffect(BMActionProperties item) {
        BMBaseEffect effect = null;
        Class<?> clazz = effectsTypesCache.get(item.getAbilitylType());
        if (clazz != null) {
            effect = createEffect(clazz, item);
        }
        return effect;
    }

    private BMBaseEffect createEffect(Class<?> clazz, BMActionProperties item) {
        BMBaseEffect instance;
        Class<?>[] parameterTypes = {BMActionProperties.class};
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            instance = (BMBaseEffect) constructor.newInstance(item);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }

        return instance;
    }
}
