package com.win.strategy.battle.configuration.factory;

import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.List;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class BMHeroInstanceFactory extends BMBaseInstanceFactory {

    public BMBaseUnit createHero(Map<String, Object> props) {
        BMBaseUnit unit = new BMBaseUnit(getField());
        unit.setIdentifier(ProtocolStrings.HERO);
        unit.setType(ProtocolStrings.HERO);
        unit.setFraction(BMField.ALLY_FRACTION_IDX);
        unit.setAttackPriorityType(BMAttackPriorityType.any);
        unit.setVisionRange(Double.valueOf(String.valueOf(props.get(ProtocolStrings.VR))).intValue());
        unit.setCurrentHP(Double.valueOf(String.valueOf(props.get(ProtocolStrings.HP))).intValue());
        unit.setMaxHP(Double.valueOf(String.valueOf(props.get(ProtocolStrings.HP))).intValue());
        unit.setAttackRange(Double.valueOf(String.valueOf(props.get(ProtocolStrings.AB_R))).intValue());
        unit.setMoveSpeed(Double.valueOf(String.valueOf(props.get(ProtocolStrings.SPD))).intValue());

        List<Map<String, Object>> actionList = (List<Map<String, Object>>) props
                .get(ProtocolStrings.ACTIONS);

        for (Map<String, Object> item : actionList) {
            BMActionProperties ability = createActionProperty(item);
            ability.setFraction(BMField.ALLY_FRACTION_IDX);
            unit.addReactionItem(ability);
        }

        unit.setBehavior(BMBattleBehavior.hero, null);
        return unit;
    }
}
