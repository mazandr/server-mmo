package com.win.strategy.battle.model.command.impl;

import com.win.strategy.battle.model.BattleModelEngine;
import com.win.strategy.battle.model.components.BMAttackComponent;
import com.win.strategy.battle.model.components.BMMoveComponent;
import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMMoveUnitCommand extends BMBaseBattleCommand {

    private Map<String, Object> params;
    private int radius = 9;
    private BMCell applyPos;
    private BMCell targetPos;
    private BMFractionEntity target = null;
    private boolean valid;

    public BMMoveUnitCommand(BattleModelEngine battleModelEngine) {
        super(battleModelEngine);
    }

    public void moveUnits() {
        List<BMBaseUnit> units = calculateActiveUnits();

        for (BMBaseUnit u : units) {
            if (target != null) {
                u.getComponent(BMAttackComponent.class).setTarget(target);
            } else {
                u.getComponent(BMMoveComponent.class).setDestinationCell(targetPos);
            }
        }

    }

//    public void moveHero() {
//        List<Map<String, Object>> units = (List<Map<String, Object>>) params.get(ProtocolStrings.UNITS);
//        if (units != null) {
//            for (Map<String, Object> u : units) {
//                String unitId = String.valueOf(u.get(ProtocolStrings.ID));
//                BMBaseUnit unit = (BMBaseUnit) getField().getActivities().get(unitId);
//                if (unit.getBattleBehavior() == BMBattleBehavior.hero) {
//                    Object behaviorPrms = u.get("behaviorParams");
//                    unit.setBehavior("hero", behaviorPrms);
//                }
//            }
//        }
//    }
    
    @Override
    public void init(Map<String, Object> params) {
        this.params = params;
        String applay = String.valueOf(this.params.get(ProtocolStrings.POSITION));
        applyPos = getField().getCellByCode(applay);

        String trg = String.valueOf(this.params.get(ProtocolStrings.TARGET));
        targetPos = getField().getCellByCode(trg);

        if (targetPos == null || applyPos == null) {
            valid = false;
            return;
        }

        if (targetPos.getBuilding() != null) {
            target = (BMFractionEntity) targetPos.getBuilding();
        }

        valid = true;
    }

    @Override
    public Map<String, Object> execute() {
        if (valid) {
            moveUnits();
        }
        return null;
    }

    @Override
    public Map<String, Object> info() {
        return null;
    }

    private List<BMBaseUnit> calculateActiveUnits() {
        List<BMBaseUnit> applyUnits = new ArrayList<>();

        int x = this.applyPos.getPosX();
        int y = this.applyPos.getPosY();
        int i, j;
        for (i = -radius; i <= radius; i++) {
            for (j = -radius; j <= radius; j++) {
                if (i * i + j * j < radius * radius) {
                    BMCell cell = getField().getCell(x + i, y + j);
                    if (cell != null) {
                        for (BMEntity entity : cell.getInnerObjects().values()) {
                            if (entity.innerType().contains(BMEntity.BMEntityType.unit)) {
                                BMFractionEntity fractionEntity = (BMFractionEntity) entity;
                                if (fractionEntity.getFraction() == 1) {
                                    applyUnits.add((BMBaseUnit) entity);
                                }
                            }
                        }
                    }
                }
            }
        }
        return applyUnits;
    }
}
