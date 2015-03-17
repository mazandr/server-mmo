package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import java.util.ArrayList;
import java.util.List;

public class BMPatrolBehavior extends BMBaseUnitBehavior {

    private final List<BMCell> cellsPath = new ArrayList<>();
    private BMCell endPos;
    private boolean patrollingInCircle;
    private BMCell startPos;
    private int currentPosRouteNumber;
    private boolean forwardMovementDirection = true;

    @Override
    public void setEntity(BMEntity entity) {
        super.setEntity(entity);
    }

    @Override
    public void init(Object initParams) {
        initializedParams = initParams;
        if (!getEntity().isInGame()) {
            return;
        }


        String patrolPath = (String) initParams;
        String[] cellsPathArr = patrolPath.split(",");

        List<BMCell> tmpPathCells = new ArrayList<>();
        for (int i = 0; i < cellsPathArr.length; i++) {
            BMCell cell = getEntity().getField().getCellByCode(
                    cellsPathArr[i].trim());
            tmpPathCells.add(cell);
        }

        initPath(tmpPathCells);

        this.startPos = this.cellsPath.get(0);
        this.endPos = this.cellsPath.get(this.cellsPath.size() - 1);

        if (startPos.equals(endPos)) {
            this.patrollingInCircle = true;
        }
    }

    private void initPath(List<BMCell> rawCells) {
        for (BMCell c : rawCells) {
            if (c.getBuilding() == null) {
                cellsPath.add(c);
            }
        }
        if (cellsPath.size() < 2) {
            System.out.println("Path does not allow patroll behavior");
        }
    }

    private BMCell findClosesCell() {
        BMCell pos = location.getPosition();

        BMCell cell = null;
        double dist = Double.MAX_VALUE;
        this.currentPosRouteNumber = 0;
        int i = 0;
        for (BMCell c : cellsPath) {
            int d = pos.squreDistanceBetween(c);
            if (d < dist) {
                dist = d;
                cell = c;
                this.currentPosRouteNumber = i;
            }
            i++;
        }
        return cell;
    }

    @Override
    public void behave(int step) {
        if (!heals.isAlive() || !getEntity().isInGame()) {
            return;
        }

        if (location.getPosition().equals(move.getDestinationCell())
                && cellsPath.contains(location.getPosition())) {
            BMCell nextCell = getNextPathCell();
            move.setDestinationCell(nextCell);
        }

        manageTarget();

        if (attack.getTarget() != null) {

            BMFractionEntity attcakableTarget = (BMFractionEntity) attack.getTarget();

            if (attcakableTarget.isAlive() && attcakableTarget.isInGame()) {

                attackPos = attcakableTarget.getCenter();
                targetDest = attcakableTarget.nearestDestCell((BMFractionEntity) getEntity(), location.getPosition(), attackPos, vision.getAttackRange());

                attcakableTarget.addReservedCell((BMFractionEntity) getEntity(), targetDest);
                attack.setReactPos(attackPos);
                move.setDestinationCell(targetDest);


                if (vision.canAttact(attackPos)) {
                    attack.react();

                    if (!attcakableTarget.isAlive() || !attcakableTarget.isInGame()) {
                        clearFocus();
                    }
                    return;
                }
            } else {
                clearFocus();
            }
        }
        move.move(step);
    }

    @Override
    protected void clearFocus() {
        super.clearFocus();
        BMCell backCell = findClosesCell();
        move.setDestinationCell(backCell);
    }

    @Override
    public void notify(Object arg) {
        // TODO Auto-generated method stub
    }

    private BMCell getNextPathCell() {
        if (this.patrollingInCircle) { // Patrolling in the circle
            if ((this.currentPosRouteNumber + 1) < this.cellsPath.size()) {
                this.currentPosRouteNumber++;
            } else {
                this.currentPosRouteNumber = 1;
            }
        } else { // Patrolling not in the circle
            if (this.forwardMovementDirection) { // go forward
                if ((this.currentPosRouteNumber + 1) < this.cellsPath.size()) {
                    currentPosRouteNumber++;
                } else {
                    this.forwardMovementDirection = false;
                    currentPosRouteNumber--;
                }
            } else { // go backrward
                if ((this.currentPosRouteNumber) > 0) {
                    currentPosRouteNumber--;
                } else {
                    this.forwardMovementDirection = true;
                    currentPosRouteNumber++;
                }
            }
        }
        return this.cellsPath.get(this.currentPosRouteNumber);
    }
}