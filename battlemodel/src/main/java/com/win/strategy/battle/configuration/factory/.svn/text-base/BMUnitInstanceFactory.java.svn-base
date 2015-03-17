package com.win.strategy.battle.configuration.factory;

import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author okopach
 */
public class BMUnitInstanceFactory extends BMBaseInstanceFactory {

    public BMBaseUnit createUnitForField(Map<String, Object> props) {
        int lvl = 1;
        Object lvlObj = props.get(ProtocolStrings.LVL);

        if (lvlObj != null) {
            lvl = Double.valueOf(String.valueOf(lvlObj)).intValue();
        }
        Object fractionObj = props.get(ProtocolStrings.FRACTION);

        int fraction = BMField.ALLY_FRACTION_IDX;
        if (fractionObj != null) {
            fraction = Double.valueOf(String.valueOf(fractionObj)).intValue();
        }

        BMBaseUnit unit = makeRawUnit(String.valueOf(props.get(ProtocolStrings.TYPE)), lvl, fraction);

        if (unit == null) {
            return null;
        }
        Object name = props.get(ProtocolStrings.NAME);
        if (name == null) {
            unit.setIdentifier("unit::" + UUID.randomUUID().toString());
        } else {
            unit.setIdentifier(String.valueOf(name));
        }

        BMBattleBehavior behavior;
        Object prms = null;
        try {
            behavior = BMBattleBehavior.valueOf(String.valueOf(props.get(ProtocolStrings.BEHAVIOR)));
            prms = props.get("behaviorPrms");
        } catch (Exception e) {
            behavior = BMBattleBehavior.smartAttack;
        }
        unit.setBehavior(behavior, prms);
        Object objX = props.get(ProtocolStrings.POS_X);
        Object objY = props.get(ProtocolStrings.POS_Y);
        Object objPos = props.get(ProtocolStrings.POSITION);

        if (objX != null && objY != null) {
            try {
                int posX = Double.valueOf(String.valueOf(objX)).intValue();
                int posY = Double.valueOf(String.valueOf(objY)).intValue();
                unit.setPosition(getField().getCell(posX, posY));
            } catch (Exception e) {
            }
        } else if (objPos != null) {
            unit.setPosition(getField().getCellByCode(String.valueOf(objPos)));
        }

        unit.setPerforatigData((Map<String, Object>) props.get(ProtocolStrings.PERFOR_DATA));
        return unit;
    }
}
