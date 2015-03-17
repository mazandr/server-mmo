package com.win.strategy.battle.model.entity.specific;

import com.win.strategy.battle.model.components.behavior.BMBattleBehavior;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vlischyshyn
 */
public class BMBillboard extends BMBaseBuilding implements IBMBehaviorable {

    private int workTime;
    private int delayTime;
    private int radius;
    private int accTime = 0;
    private Set<BMBaseUnit> unitHistory;
    private int accWorkTime = 0;

    public BMBillboard(BMField field) {
        super(field);
        unitHistory = new HashSet<>();
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void behave(int step) {
        if (accTime >= delayTime) {

            if (accWorkTime < workTime) {
                setActionState(BMEntityActionState.attack);
                hipnose();
                accWorkTime += getField().getGameProcessStep();
            } else {
                accTime = 0;
                accWorkTime = 0;
                dismisHipnose();
                setActionState(BMEntityActionState.idle);
            }
        } else {
            accTime += getField().getGameProcessStep();
        }
    }

    @Override
    public void reinitBehavior() {
    }

    private List<BMBaseUnit> calculateActiveUnits() {
        List<BMBaseUnit> applyUnits = new ArrayList<>();
        int x = getCenter().getPosX();
        int y = getCenter().getPosY();
        int i, j;
        for (i = -radius; i <= radius; i++) {
            for (j = -radius; j <= radius; j++) {
                if (i * i + j * j < radius * radius) {
                    BMCell cell = getField().getCell(x + i, y + j);
                    if (cell != null) {
                        for (BMEntity entity : cell.getInnerObjects().values()) {
                            if (entity.innerType().contains(BMEntity.BMEntityType.unit)) {
                                BMFractionEntity fractionEntity = (BMFractionEntity) entity;
                                if (fractionEntity.getFraction() != getFraction()
                                        && fractionEntity.isAlive()
                                        && fractionEntity.isInGame()
                                        && fractionEntity.isVisible()) {
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

    private void hipnose() {
        List<BMBaseUnit> units = calculateActiveUnits();

        for (BMBaseUnit unit : units) {
            unitHistory.add(unit);
            unit.setBehavior(BMBattleBehavior.hipnose, getCloserDestPos(unit.getCenter()));
        }
    }

    private void dismisHipnose() {
        for (BMBaseUnit unit : unitHistory) {
            unit.setBehavior(BMBattleBehavior.smartAttack, null);
        }
        unitHistory.clear();
    }
}
