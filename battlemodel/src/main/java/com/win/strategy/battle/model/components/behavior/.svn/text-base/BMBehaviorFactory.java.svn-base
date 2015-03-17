package com.win.strategy.battle.model.components.behavior;

import com.win.strategy.battle.model.components.behavior.unit.BMAttackBehavior;
import com.win.strategy.battle.model.components.behavior.unit.BMBossDefanceBehavior;
import com.win.strategy.battle.model.components.behavior.unit.BMHipnoticBehavior;
import com.win.strategy.battle.model.components.behavior.unit.BMAgresiveHoldPositionBehavior;
import com.win.strategy.battle.model.components.behavior.unit.BMSmartAttackBehavior;
import com.win.strategy.battle.model.components.behavior.unit.BMHeroBehavior;
import com.win.strategy.battle.model.components.behavior.unit.BMHoldPositionBehavior;
import com.win.strategy.battle.model.components.behavior.unit.BMPatrolBehavior;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMBehaviorFactory {

    private Map<BMBattleBehavior, Class<?>> behaviorTypesCache;

    public BMBehaviorFactory() {
        init();
    }

    private void init() {
        behaviorTypesCache = new HashMap<>(BMBattleBehavior.values().length);
        behaviorTypesCache.put(BMBattleBehavior.attack, BMAttackBehavior.class);
//        behaviorTypesCache.put(BMBattleBehavior.hero, BMHeroBehavior.class);
//        behaviorTypesCache.put(BMBattleBehavior.agresivehold, BMAgresiveHoldPositionBehavior.class);
        behaviorTypesCache.put(BMBattleBehavior.hold, BMHoldPositionBehavior.class);
//        behaviorTypesCache.put(BMBattleBehavior.patrol, BMPatrolBehavior.class);
        behaviorTypesCache.put(BMBattleBehavior.smartAttack, BMSmartAttackBehavior.class);
        behaviorTypesCache.put(BMBattleBehavior.bossDefence, BMBossDefanceBehavior.class);
        behaviorTypesCache.put(BMBattleBehavior.hipnose, BMHipnoticBehavior.class);
    }

    public BMBaseBehavior getBehavior(BMBattleBehavior behaviorType) {
        BMBaseBehavior behavior = null;
        Class<?> clazz = behaviorTypesCache.get(behaviorType);
        if (clazz != null) {
            behavior = createBehavior(clazz);
        }
        return behavior;
    }

    private BMBaseBehavior createBehavior(Class<?> clazz) {
        BMBaseBehavior instance;
        Class<?>[] parameterTypes = {};
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            instance = (BMBaseBehavior) constructor.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }

        return instance;
    }
}
