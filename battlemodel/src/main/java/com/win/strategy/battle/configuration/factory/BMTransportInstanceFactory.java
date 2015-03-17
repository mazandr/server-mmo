package com.win.strategy.battle.configuration.factory;

import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.battle.model.entity.basic.units.BMTransport;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.List;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class BMTransportInstanceFactory extends BMBaseInstanceFactory {

    public BMTransport createTransport(Map<String, Object> props) {
        int lvl = 1;
        if (props.get(ProtocolStrings.LVL) != null) {
            lvl = Double.valueOf(String.valueOf(props.get(ProtocolStrings.LVL))).intValue();
        }
        BMTransport transport = makeRawTransport(String.valueOf(props.get(ProtocolStrings.TYPE)), lvl);

        if (transport == null) {
            return null;
        }

        transport.setIdentifier(String.valueOf(props.get(ProtocolStrings.NAME)));

        List<Map<String, Object>> units = (List<Map<String, Object>>) props.get(ProtocolStrings.UNITS);

        int uid = 0;
        for (Map<String, Object> unit : units) {
            int cnt = Double.valueOf(String.valueOf(unit.get(ProtocolStrings.NUMBER))).intValue();
            for (int i = 0; i < cnt; i++) {
                BMBaseUnit baseUnit;
                String uType = String.valueOf(unit.get(ProtocolStrings.TYPE));
                int uLvl = Double.valueOf(String.valueOf(unit.get(ProtocolStrings.LVL))).intValue();
                if (uType.equals(ProtocolStrings.HERO)) {
                    baseUnit = makeRawHero(uType, uLvl);
                } else {
                    baseUnit = makeRawUnit(uType, uLvl, BMField.ALLY_FRACTION_IDX);
                    if (baseUnit != null) {
                        baseUnit.setIdentifier(transport.getIdentifier() + "_" + ProtocolStrings.UNIT + "_" + uid);
                        baseUnit.setBehavior(BMBattleBehavior.smartAttack, null);
                    }
                    uid++;
                }
                if (baseUnit != null) {
                    transport.addUnit(baseUnit);
                }
            }
        }
        return transport;
    }
}
