package com.win.strategy.battle.model.markers;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vlischyshyn
 */
public class BMMoveMotivatorMarker extends BMObject implements IBMBehaviorable {

    private int expireTime;
    private int accTime = 0;
    private int radius;
    private BMCell targetCell;
    private BMField field;
    private BMFractionEntity targetEntity;
    private Set<BMBaseUnit> unitHistory;
    private Map<String, Object> params;
    private boolean valid = false;
    private BMMoveMotivatorMarkerType type;

    public BMMoveMotivatorMarker(int expireTime, int radius, BMCell targetCell) {
        this.unitHistory = new HashSet<>();
        this.expireTime = expireTime;
        this.radius = radius;
        this.targetCell = targetCell;
        this.type = BMMoveMotivatorMarkerType.position;
        if (this.targetCell != null) {
            this.field = this.targetCell.getField();
            this.targetEntity = (BMFractionEntity) this.targetCell.getBuilding();
            if (targetEntity != null) {
                this.type = BMMoveMotivatorMarkerType.target;
            }
        }
    }

    public BMMoveMotivatorMarkerType getType() {
        return type;
    }

    public BMCell getTargetCell() {
        return targetCell;
    }

    public BMFractionEntity getTargetEntity() {
        return targetEntity;
    }

    public void prepareParams() {
        params = new HashMap<>();
        params.put("marker", this);
        valid = true;
    }

    private List<BMBaseUnit> calculateActiveUnits() {
        List<BMBaseUnit> applyUnits = new ArrayList<>();
        int x = this.targetCell.getPosX();
        int y = this.targetCell.getPosY();
        int i, j;
        for (i = -radius; i <= radius; i++) {
            for (j = -radius; j <= radius; j++) {
                if (i * i + j * j < radius * radius) {
                    BMCell cell = field.getCell(x + i, y + j);
                    if (cell != null) {
                        for (BMEntity entity : cell.getInnerObjects().values()) {
                            if (entity.innerType().contains(BMEntity.BMEntityType.unit)) {
                                BMFractionEntity fractionEntity = (BMFractionEntity) entity;
                                if (fractionEntity.getFraction() == BMField.ALLY_FRACTION_IDX
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

    @Override
    public void behave(int step) {
        if (!valid || accTime >= expireTime) {
            return;
        }

        List<BMBaseUnit> units = calculateActiveUnits();
        for (BMBaseUnit unit : units) {
            if (!this.unitHistory.contains(unit)) {
                unit.setBehavior(null, params);
                this.unitHistory.add(unit);
            }
        }
        accTime += field.getGameProcessStep();
    }

    public boolean expired() {
        return accTime >= expireTime;
    }

    @Override
    public void reinitBehavior() {
    }

    public static enum BMMoveMotivatorMarkerType {

        target,
        position
    }
}
