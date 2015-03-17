package com.win.strategy.battle.configuration.factory;

import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.actions.BMAbilitylType;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties.BMActionPropertiesGroup;
import com.win.strategy.battle.model.entity.basic.actions.BMActionTargetType;
import com.win.strategy.battle.model.entity.basic.actions.BMActionType;
import com.win.strategy.battle.model.entity.basic.effects.BMEffectApplyType;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.battle.model.entity.basic.units.BMTransport;
import com.win.strategy.common.utils.ProtocolStrings;
import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import java.util.List;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class BMBaseInstanceFactory {

    private BMField field;
    private IWorldModelConfiguration worldModelConfiguration;

    public void init(BMField field, IWorldModelConfiguration worldModelConfiguration) {
        this.field = field;
        this.worldModelConfiguration = worldModelConfiguration;
    }

    public BMField getField() {
        return field;
    }

    public IWorldModelConfiguration getWorldModelConfiguration() {
        return worldModelConfiguration;
    }

    protected BMBaseUnit makeRawUnit(String type, int lvl, int fraction) {
        try {
            BMBaseUnit unit = new BMBaseUnit(field);
            Map<String, Object> unitType = (Map<String, Object>) getWorldModelConfiguration().getUnitTypes().get(type);
            if (unitType == null) {
                return null;
            }
            String strLvl = ProtocolStrings.LVL + lvl;
            Map<String, Object> unitLvl = (Map<String, Object>) ((Map<String, Object>) unitType.get(ProtocolStrings.LVLS)).get(strLvl);
            if (unitLvl == null) {
                return null;
            }

            unit.setType(type);
            unit.setAttackRange(Double.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.AB_R))).intValue());
            unit.setCurrentHP(Double.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.HP))).intValue());
            unit.setMaxHP(Double.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.HP))).intValue());
            unit.setMoveSpeed(Double.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.SPD))).intValue());
            unit.setVisionRange(Double.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.VR))).intValue());
            unit.setHousingSpace(Double.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.HSP))).intValue());
            unit.setActionOnMove(Boolean.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.HSP))));
            unit.setAttackPriorityType(BMAttackPriorityType.valueOf(String.valueOf(unitLvl.get(ProtocolStrings.TT))));
            unit.setFraction(fraction);
            List<Map<String, Object>> actionList = (List<Map<String, Object>>) unitLvl.get(ProtocolStrings.ACTIONS);

            for (Map<String, Object> item : actionList) {
                BMActionProperties ability = createActionProperty(item);
                ability.setFraction(fraction);
                unit.addReactionItem(ability);
            }
            return unit;
        } catch (Exception e) {
            return null;
        }
    }

    protected BMActionProperties createActionProperty(Map<String, Object> item) {
        BMActionProperties ability = new BMActionProperties();
        Map<String, Object> acT = (Map<String, Object>) item.get(ProtocolStrings.AC_T);
        BMAbilitylType abilitylType = BMAbilitylType.valueOf(String.valueOf(item.get(ProtocolStrings.AB_T)));
        BMActionType actionType = BMActionType.valueOf(String.valueOf(acT.get(ProtocolStrings.TYPE)));
        BMEffectApplyType applyType = BMEffectApplyType.valueOf(String.valueOf(item.get(ProtocolStrings.AP_T)));

        ability.setAbilitylType(abilitylType);
        ability.setActionType(actionType);
        ability.setApplyType(applyType);

        if (acT.containsKey(ProtocolStrings.AC_SPD)) {
            ability.setActionSpeed(Double.valueOf(String.valueOf(acT.get(ProtocolStrings.AC_SPD))).intValue());
        }

        if (acT.containsKey(ProtocolStrings.C_T_T)) {
            ability.setCaptureTargetTime(Double.valueOf(String.valueOf(acT.get(ProtocolStrings.C_T_T))).intValue());
        }

        if (acT.containsKey(ProtocolStrings.R_T)) {
            ability.setReloadTime(Double.valueOf(String.valueOf(acT.get(ProtocolStrings.R_T))).intValue());
        }

        BMActionPropertiesGroup group = BMActionPropertiesGroup.offensive;
        Object ogroup = item.get(ProtocolStrings.GROUP);
        if (ogroup != null) {
            try {
                group = BMActionPropertiesGroup.valueOf(String.valueOf(ogroup));
            } catch (Exception e) {
            }
        }
        ability.setGroup(group);

        Object sTT = item.get(ProtocolStrings.ATT);
        if (sTT == null) {
            ability.setActionTargetType(BMActionTargetType.any);
        } else {
            ability.setActionTargetType(BMActionTargetType.valueOf(String.valueOf(sTT)));
        }

        ability.setMaxStack(1);
        if (actionType == BMActionType.distributed) {
            ability.setTime(Double.valueOf(String.valueOf(acT.get(ProtocolStrings.TIME))).intValue());
            ability.setTick(Double.valueOf(String.valueOf(acT.get(ProtocolStrings.TICK))).intValue());
            Object stacks = acT.get(ProtocolStrings.MAX_STACK);
            if (stacks != null) {
                ability.setMaxStack(Double.valueOf(String.valueOf(stacks)).intValue());
            }
        }

        ability.setRadius(Double.valueOf(String.valueOf(item.get(ProtocolStrings.SDR))).intValue());
        ability.setValue(Double.valueOf(String.valueOf(item.get(ProtocolStrings.VALUE))).intValue());
        ability.setField(field);

        return ability;
    }

    protected BMBaseUnit makeRawHero(String type, int lvl) {
        try {
            BMBaseUnit hero = new BMBaseUnit(field);

            Map<String, Object> heroType = (Map<String, Object>) getWorldModelConfiguration().getHeroTypes().get(type);
            if (heroType == null) {
                return null;
            }
            String strLvl = ProtocolStrings.LVL + lvl;
            Map<String, Object> heroLvl = (Map<String, Object>) ((Map<String, Object>) heroType.get(ProtocolStrings.LVLS)).get(strLvl);
            if (heroLvl == null) {
                return null;
            }

            hero.setIdentifier(ProtocolStrings.HERO);
            hero.setType(ProtocolStrings.HERO);
            hero.setAttackRange(Double.valueOf(String.valueOf(heroLvl.get(ProtocolStrings.AB_R))).intValue());
            hero.setCurrentHP(Double.valueOf(String.valueOf(heroLvl.get(ProtocolStrings.HP))).intValue());
            hero.setMaxHP(Double.valueOf(String.valueOf(heroLvl.get(ProtocolStrings.HP))).intValue());
            hero.setHousingSpace(Double.valueOf(String.valueOf(heroLvl.get(ProtocolStrings.HSP))).intValue());
            hero.setMoveSpeed(Double.valueOf(String.valueOf(heroLvl.get(ProtocolStrings.SPD))).intValue());
            hero.setVisionRange(Double.valueOf(String.valueOf(heroLvl.get(ProtocolStrings.VR))).intValue());
            hero.setAttackPriorityType(BMAttackPriorityType.valueOf(String.valueOf(heroLvl.get(ProtocolStrings.TT))));
            hero.setFraction(BMField.ALLY_FRACTION_IDX);

            List<Map<String, Object>> actionList = (List<Map<String, Object>>) heroLvl
                    .get(ProtocolStrings.ACTIONS);

            for (Map<String, Object> item : actionList) {
                BMActionProperties ability = createActionProperty(item);
                ability.setFraction(BMField.ALLY_FRACTION_IDX);
                hero.addReactionItem(ability);
            }

            hero.setBehavior(BMBattleBehavior.hero, null);
            return hero;
        } catch (Exception e) {
            return null;
        }
    }

    protected BMTransport makeRawTransport(String type, int lvl) {
        BMTransport transport = new BMTransport(field);
        transport.setType(type);
        Map<String, Object> carType = (Map<String, Object>) getWorldModelConfiguration().getCarTypes().get(type);

        if (carType == null) {
            return null;
        }
        String strLvl = ProtocolStrings.LVL + lvl;
        Map<String, Object> carTypeLvl = (Map<String, Object>) ((Map<String, Object>) carType.get(ProtocolStrings.LVLS)).get(strLvl);
        if (carTypeLvl == null) {
            return null;
        }

        transport.setCurrentHP(Double.valueOf(String.valueOf(carTypeLvl.get(ProtocolStrings.HP))).intValue());
        transport.setMaxHP(Double.valueOf(String.valueOf(carTypeLvl.get(ProtocolStrings.HP))).intValue());
        transport.setFraction(BMField.ALLY_FRACTION_IDX);
        transport.setMoveSpeed(100);

        Map<String, Object> cap = (Map<String, Object>) carTypeLvl.get(ProtocolStrings.CAP);
        int capNumber = 0;
        if (cap != null) {
            capNumber = Double.valueOf(String.valueOf(cap.get(ProtocolStrings.UNIT))).intValue();
        }
        transport.setMaxCapacity(capNumber);

        double carSpeed = 100;
        Object speed = carTypeLvl.get(ProtocolStrings.SPD);
        if (speed != null) {
            carSpeed = Double.valueOf(String.valueOf(speed));
        }
        transport.setMoveSpeed(carSpeed);

        return transport;
    }
}
