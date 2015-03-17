package com.win.strategy.battle.model.entity.basic;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BMFractionEntity extends BMEntity implements IBMTargetable {

    private int fraction;
    private List<? extends BMFractionEntity> fractionList;
    private BMAttackPriorityType priorityState = BMAttackPriorityType.any;
    private boolean canBeFocused;
    private boolean canBeCaptured;
    private boolean canBeDestroyed;
    private Map<BMFractionEntity, BMCell> reservedCells = new ConcurrentHashMap<>();

    public Collection<BMCell> getReservedCellsList() {
        return reservedCells.values();
    }

    public Map<BMFractionEntity, BMCell> getReservedCells() {
        return reservedCells;
    }

    public void addReservedCell(BMFractionEntity focuser, BMCell cell) {
        validateReservedCells();
        reservedCells.put(focuser, cell);
    }

    protected void validateReservedCells() {
        for (Map.Entry<BMFractionEntity, BMCell> entry : reservedCells.entrySet()) {
            if (!entry.getKey().isInGame() || !entry.getKey().isAlive()) {
                reservedCells.remove(entry.getKey());
            }
        }
    }

    public BMFractionEntity(BMField field) {
        super(field);
    }

    public void setCanBeFocused(boolean canBeFocused) {
        this.canBeFocused = canBeFocused;
    }

    public void setCanBeCaptured(boolean canBeCaptured) {
        this.canBeCaptured = canBeCaptured;
    }

    public void setCanBeDestroyed(boolean canBeDestroyed) {
        this.canBeDestroyed = canBeDestroyed;
    }

    @Override
    public boolean canBeCaptured() {
        return canBeCaptured;
    }

    @Override
    public boolean canBeDestroyed() {
        return canBeDestroyed;
    }

    @Override
    public boolean canBeFocused() {
        return canBeFocused;
    }

    @Override
    public BMAttackPriorityType getPriorityState() {
        return priorityState;
    }

    protected void setPriorityState(BMAttackPriorityType priorityState) {
        this.priorityState = priorityState;
    }

    public List<? extends BMFractionEntity> getFractionList() {
        return fractionList;
    }

    public void setFractionList(List<? extends BMFractionEntity> fractionList) {
        this.fractionList = fractionList;
    }

    public int getFraction() {
        return fraction;
    }

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = super.toMapObject();
        result.put(ProtocolStrings.FRACTION, this.fraction);
        return result;
    }

    public BMCell nearestDestCell(BMFractionEntity e, BMCell from, BMCell to, int range) {
        BMCell oldReserv = reservedCells.get(e);
        if (oldReserv != null) {
            if (range > to.distanceBetween(oldReserv)) {
                return oldReserv;
            }
            reservedCells.remove(e);
        }

        int distance = Integer.MAX_VALUE;
        int tmpDistance;
        BMCell result = null;
        int i, j;
        for (i = -range; i <= range; i++) {
            for (j = -range; j <= range; j++) {
                if (i * i + j * j < range * range) {
                    BMCell cell = getField().getCell(to.getPosX() + i, to.getPosY() + j);
                    if (cell != null) {
                        if (cell.getBuilding() == null && cell.getTrash() == null) {
                            if (!reservedCells.containsValue(cell)) {
                                tmpDistance = from.squreDistanceBetween(cell);
                                if (tmpDistance < distance) {
                                    result = cell;
                                    distance = tmpDistance;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
