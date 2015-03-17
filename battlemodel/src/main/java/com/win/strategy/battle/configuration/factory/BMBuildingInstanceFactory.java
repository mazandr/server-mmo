package com.win.strategy.battle.configuration.factory;

import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseDefenceBuilding;
import com.win.strategy.battle.model.entity.basic.buildings.BMCoverBuilding;
import com.win.strategy.battle.model.entity.basic.buildings.BMBuildingsGroup;
import com.win.strategy.battle.model.entity.basic.buildings.BMTrap;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.battle.model.entity.specific.BMBillboard;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class BMBuildingInstanceFactory extends BMBaseInstanceFactory {

    public BMBaseBuilding createBuilding(Map<String, Object> props) {

        int lvl = 1;
        if (props.get(ProtocolStrings.LVL) != null) {
            lvl = Double.valueOf(String.valueOf(props.get(ProtocolStrings.LVL))).intValue();
        }
        String buildingTypeName = String.valueOf(props.get(ProtocolStrings.TYPE));

        Map<String, Object> buildingType = (Map<String, Object>) getWorldModelConfiguration().getBuildingTypes().get(buildingTypeName);
        String strLvl = ProtocolStrings.LVL + lvl;
        Map<String, Object> buildingTypeLvl = (Map<String, Object>) ((Map<String, Object>) buildingType.get(ProtocolStrings.LVLS)).get(strLvl);
        String group = String.valueOf(buildingType.get(ProtocolStrings.GROUP));
        BMBuildingsGroup buildingGruop = BMBuildingsGroup._valueOf(group);
        boolean winCondition = Boolean.valueOf(String.valueOf(props.get(ProtocolStrings.WIN_C))).booleanValue();

        boolean unitInBuilding = false;
        if (props.get(ProtocolStrings.UNITS) != null) {
            unitInBuilding = ((Map<String, Object>) props.get(ProtocolStrings.UNITS)).size() > 0;
        }

        BMBaseBuilding bb;
        try {
            if ("billboard".equalsIgnoreCase(buildingTypeName)) {
                bb = createBillBoard(props, buildingTypeLvl);
            } else if (buildingGruop == BMBuildingsGroup.defensive) {
                bb = createDefenciveBuilding(props, buildingTypeLvl);
            } else if (buildingGruop == BMBuildingsGroup.traps) {
                bb = createTrap(props, buildingTypeLvl);
            } else if (buildingGruop == BMBuildingsGroup.wall) {
                bb = createWall(props, buildingTypeLvl);
            } else {
//            if (unitInBuilding) {
                bb = createCommonBuildingWithUnit(props, buildingTypeLvl);
//            } else {
//                bb = createCommonBuilding(props, buildingTypeLvl);
//            }
            }
        } catch (Exception e) {
            return null;
        }

        if (props.get(ProtocolStrings.WR) == null || ((Map<String, Object>) props.get(ProtocolStrings.WR)).isEmpty()) {
            bb.setWinRate(getWinRate(buildingTypeLvl.get(ProtocolStrings.WR)));
        } else {
            bb.setWinRate(getWinRate(props.get(ProtocolStrings.WR)));
        }

        bb.setGroup(buildingGruop);
        bb.setWinCondition(winCondition);
        return bb;
    }

    private BMBaseBuilding createDefenciveBuilding(Map<String, Object> props, Map<String, Object> buildingTypeLvl) {
        BMBaseDefenceBuilding sb = new BMBaseDefenceBuilding(getField());
        sb.setIdentifier(String.valueOf(props.get(ProtocolStrings.NAME)));
        sb.setType(String.valueOf(props.get(ProtocolStrings.TYPE)));

        sb.setMaxHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        sb.setCurrentHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        defineBuildingPosition(sb, buildingTypeLvl, buildingTypeLvl);
        sb.setFraction(Double.valueOf(String.valueOf(props.get(ProtocolStrings.FRACTION))).intValue());
        sb.setPosition(getField().getCellByCode(String.valueOf(props.get(ProtocolStrings.POSITION))));
        sb.setAttackPriorityType(BMAttackPriorityType.any);

        int visionRange = Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.VR))).intValue();
        sb.setVisionRange(visionRange);
        sb.setAttackRange(visionRange);

        List<Map<String, Object>> actions = (List<Map<String, Object>>) buildingTypeLvl.get(ProtocolStrings.ACTIONS);
        if (actions != null && !actions.isEmpty()) {
            for (Map<String, Object> item : actions) {
                BMActionProperties ability = createActionProperty(item);
                ability.setFraction(sb.getFraction());
                sb.addReactionItem(ability);
            }
        }
        return sb;
    }

    private Map<String, Double> getWinRate(Object wr) {
        Map<String, Double> normalWr = new HashMap<>();
        if (wr == null) {
            return normalWr;
        }

        Map<String, Object> wrMap = (Map<String, Object>) wr;
        for (String wrName : wrMap.keySet()) {
            if (wrMap.get(wrName) != null) {
                normalWr.put(wrName, Double.valueOf(String.valueOf(wrMap.get(wrName))));
            }
        }
        return normalWr;
    }

    private BMBaseBuilding createTrap(Map<String, Object> props, Map<String, Object> buildingTypeLvl) {
        BMTrap bt = new BMTrap(getField());
        bt.setIdentifier(String.valueOf(props.get(ProtocolStrings.NAME)));
        bt.setType(String.valueOf(props.get(ProtocolStrings.TYPE)));

        bt.setMaxHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        bt.setCurrentHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        defineBuildingPosition(bt, buildingTypeLvl, buildingTypeLvl);

        bt.setFraction(Double.valueOf(String.valueOf(props.get(ProtocolStrings.FRACTION))).intValue());
        bt.setPosition(getField().getCellByCode(String.valueOf(props.get(ProtocolStrings.POSITION))));

        double attackSpeed = 0;
        int dps = Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.DPS))).intValue();
        int splashRadius = Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.SDR))).intValue();
        bt.setAttackSpeed(attackSpeed);
        bt.setDps(dps);
        bt.setVisionRange(splashRadius);
        bt.setAttackRange(splashRadius);
        bt.setSplashRadius(splashRadius);
        bt.setSelfDamage(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.SELF_DAMAGE))).intValue());
        bt.setAttackPriorityType(BMAttackPriorityType.transport);
        bt.setDamageOnlyPriorityType(true);
        return bt;
    }

    private BMBaseBuilding createWall(Map<String, Object> props, Map<String, Object> buildingTypeLvl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BMBaseBuilding createCommonBuildingWithUnit(Map<String, Object> props, Map<String, Object> buildingTypeLvl) {
        BMCoverBuilding sb = new BMCoverBuilding(getField());
        sb.setIdentifier(String.valueOf(props.get(ProtocolStrings.NAME)));
        sb.setType(String.valueOf(props.get(ProtocolStrings.TYPE)));
        defineBuildingPosition(sb, props, buildingTypeLvl);
        sb.setMaxHuosingSpace(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HSP))).intValue());
        sb.setAttackPriorityType(BMAttackPriorityType.any);

        int fraction = Double.valueOf(String.valueOf(props.get(ProtocolStrings.FRACTION))).intValue();
        sb.setFraction(fraction);

        Map<String, Object> units = (Map<String, Object>) props.get(ProtocolStrings.UNITS);
        int i = 0;
        if (units != null) {
            for (Map.Entry<String, Object> e : units.entrySet()) {
                Map<String, Object> uProps = (Map<String, Object>) e.getValue();
                int lvl = 1;
                if (uProps.get(ProtocolStrings.LVL) != null) {
                    lvl = Double.valueOf(String.valueOf(uProps.get(ProtocolStrings.LVL))).intValue();
                }
                int cnt = 1;
                if (uProps.get(ProtocolStrings.COUNT) != null) {
                    cnt = Double.valueOf(String.valueOf(uProps.get(ProtocolStrings.COUNT))).intValue();
                }
                String behavior = "smartAttack";
                if (uProps.get(ProtocolStrings.BEHAVIOR) != null) {
                    behavior = String.valueOf(uProps.get(ProtocolStrings.BEHAVIOR));
                }
                BMBattleBehavior bhv = BMBattleBehavior.smartAttack;
                try {
                    bhv = BMBattleBehavior.valueOf(behavior);
                } catch (Exception e1) {
                }

                for (int c = 0; c < cnt; c++) {
                    BMBaseUnit unit = makeRawUnit(e.getKey(), lvl, fraction);
                    if (unit != null) {
                        unit.setIdentifier(sb.getIdentifier() + "_unit_" + i);
                        getField().registerUnit(unit);
                        Object prms = null;
                        unit.setBehavior(bhv, prms);
                        sb.putUnit(unit);
                    }
                    i++;
                }
            }
        }

        sb.setMaxHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        sb.setCurrentHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        sb.setPosition(getField().getCellByCode(String.valueOf(props.get(ProtocolStrings.POSITION))));

        return sb;
    }

    private BMBaseBuilding createCommonBuilding(Map<String, Object> props, Map<String, Object> buildingTypeLvl) {
        BMBaseBuilding sb = new BMBaseBuilding(getField());
        sb.setIdentifier(String.valueOf(props.get(ProtocolStrings.NAME)));
        sb.setType(String.valueOf(props.get(ProtocolStrings.TYPE)));

        sb.setMaxHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        sb.setCurrentHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        defineBuildingPosition(sb, buildingTypeLvl, buildingTypeLvl);
        sb.setFraction(Double.valueOf(String.valueOf(props.get(ProtocolStrings.FRACTION))).intValue());
        sb.setPosition(getField().getCellByCode(String.valueOf(props.get(ProtocolStrings.POSITION))));
        return sb;
    }

    private BMBaseBuilding createBillBoard(Map<String, Object> props, Map<String, Object> buildingTypeLvl) {
        BMBillboard sb = new BMBillboard(getField());
        sb.setIdentifier(String.valueOf(props.get(ProtocolStrings.NAME)));
        sb.setType(String.valueOf(props.get(ProtocolStrings.TYPE)));

        sb.setMaxHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        sb.setCurrentHP(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.HP))).intValue());
        defineBuildingPosition(sb, buildingTypeLvl, buildingTypeLvl);
        sb.setFraction(Double.valueOf(String.valueOf(props.get(ProtocolStrings.FRACTION))).intValue());
        sb.setPosition(getField().getCellByCode(String.valueOf(props.get(ProtocolStrings.POSITION))));
        sb.setRadius(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.RADIUS))).intValue());
        sb.setWorkTime(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.WORK_TIME))).intValue());
        sb.setDelayTime(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.DELAY))).intValue());
        return sb;
    }

    private void defineBuildingPosition(BMBaseBuilding sb, Map<String, Object> props, Map<String, Object> buildingTypeLvl) throws NumberFormatException {
        Object ornt = props.get(ProtocolStrings.ORNT);
        if (ornt != null
                && Double.valueOf(String.valueOf(ornt)).intValue() == 1) {
            sb.setSizeX(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.SY))).intValue());
            sb.setSizeY(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.SX))).intValue());
            sb.setRotated(true);
            return;
        }

        sb.setSizeX(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.SX))).intValue());
        sb.setSizeY(Double.valueOf(String.valueOf(buildingTypeLvl.get(ProtocolStrings.SY))).intValue());
    }
}
