package com.win.strategy.battle.model.components;

import java.util.HashMap;
import java.util.Map;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.BMMovementType;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.IBMLocable;
import com.win.strategy.battle.pathfinder.Path;
import com.win.strategy.battle.utils.BMPhysicsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public class BMMoveComponent extends BMBaseComponent {

    private int pathMaxLength = 30;
    private BMField field;
    private double moveSpeed;
    private int currentPathStep = 0;
    private int currentSmothedIdx = -1;
    private int nextIteration;
    private int fieldBuildingCnt = -1;
    private BMMovementType movementType;
    private Path path;
    private List<WayPoint> wayPoints;
    private List<BMCell> smothedPath;
    private BMCell destinationCell;
    private BMCell nextPosition;
    private BMCell tempDest;
    private double distance;
    private boolean destChanged = false;
    private long scellStep;
    private int lastStep;
    private int nextStep;
    private int speedModKoef = 0;
    private BMCell reservedCell = null;
    private boolean speedChanged;

    public BMMoveComponent() {
        this.setIdentifier("move");
        smothedPath = new ArrayList<>();
    }

    public void resetPath() {
        this.nextIteration = -1;
        this.nextPosition = null;
        this.destChanged = true;
        this.scellStep = -1;
        this.lastStep = -1;
        this.nextStep = -1;
    }

    @Override
    public void setEntity(BMEntity entity) {
        super.setEntity(entity);
        field = entity.getField();
    }

    private List<WayPoint> wayPoints() {
        List<WayPoint> cels = new ArrayList<>();
        smothedPath.clear();

        if (path.getLength() < 2) {
            smothedPath.add(path.getWayPoint(0).getCell());
        }

        BMCell p = getPos();
        List<BMCell> tmpCells = new ArrayList<>();
        List<BMCell> tmpCellsPrev = new ArrayList<>();
        for (int i = 0; i < path.getLength() - 1; i++) {
            boolean visible = BMPhysicsUtils.los(p, path.getWayPoint(i + 1).getCell(), tmpCells, getEntity());
            if (!visible) {
                BMCell nextp = path.getWayPoint(i).getCell();
                WayPoint wp = new WayPoint(nextp, p);
                if (tmpCellsPrev.isEmpty()) {
                    smothedPath.add(nextp);
                    wp.cellsCnt = 1;
                } else {
                    smothedPath.addAll(tmpCellsPrev);
                    wp.cellsCnt = tmpCellsPrev.size();
                    tmpCellsPrev.clear();
                }
                wp.smothedIdx = smothedPath.size() - 1;
                p = nextp;
                cels.add(wp);
            } else if (visible) {
                if (i == path.getLength() - 2) {
                    smothedPath.addAll(tmpCells);
                } else {
                    tmpCellsPrev.clear();
                    tmpCellsPrev.addAll(tmpCells);
                }
            }
            tmpCells.clear();
        }

        WayPoint wp = new WayPoint(path.getWayPoint(path.getLength() - 1)
                .getCell(), p);
        wp.cellsCnt = (cels.size() > 0)
                ? smothedPath.size() - cels.get(cels.size() - 1).smothedIdx
                : smothedPath.size();
        wp.smothedIdx = smothedPath.size() - 1;
        cels.add(wp);
        return cels;
    }

    private void calculatePath() {
        currentPathStep = 0;
        currentSmothedIdx = -1;
        if (tempDest == null) {
            return;
        }

        calculateSpeedByStep();

        BMCell currentPosition = getPos();

        field.resetAreaMap();
        path = field.pathFinder().calcShortestPath(currentPosition, tempDest);

        if (path != null) {
            wayPoints = wayPoints();
        }
        destChanged = false;
    }

    public BMCell getDestinationCell() {
        return destinationCell;
    }

    public BMMovementType getMovementType() {
        return movementType;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public int getNextIteration() {
        return nextIteration;
    }

    public BMCell getNextPosition() {
        return nextPosition;
    }

    public Path getPath() {
        return path;
    }

    private BMCell getPos() {
        return ((IBMLocable) getEntity()).getPosition();
    }

    public void move(int step) {
        if (!checkCanMove()) {
            return;
        }

        boolean hasConflicts = checkConflictsInPath();

        BMCell pos = getPos();
        if (isFieldStateChanged()
                || path == null
                || (pos.equals(tempDest) && !tempDest.equals(destinationCell))
                || destChanged
                || hasConflicts) {

            trimPathForQuickCalc(pos);
            calculatePath();
        }

        if (path == null) {
            return;
        }

        this.getEntity().setActionState(BMEntityActionState.move);
        doMotion(pos, step);
    }

    public void setDestinationCell(BMCell destinationCell) {
        if (destinationCell != this.destinationCell) {
            destChanged = true;
            this.destinationCell = destinationCell;
            this.notifyEntity(this);
        }
    }

    public void setMovementType(BMMovementType movementType) {
        this.movementType = movementType;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();

        if (this.getEntity().getActionState().contains(BMEntityActionState.move)) {
            if (this.destinationCell != null) {
                result.put("destX", this.destinationCell.getPosX());
                result.put("destY", this.destinationCell.getPosY());
            }
            if (this.nextPosition != null) {
                result.put("nextX", this.nextPosition.getPosX());
                result.put("nextY", this.nextPosition.getPosY());
                result.put("nextOnStep", this.nextIteration);
            }
        }
        return result;
    }

    private boolean isFieldStateChanged() {
        int buildingNumber = getPos().getField().getBuildings().size();
        boolean result = fieldBuildingCnt != buildingNumber;
        fieldBuildingCnt = buildingNumber;
        return result;
    }

    @Override
    public void notify(Object arg) {
    }

    public double calculateMoveSpeed() {
        return moveSpeed + moveSpeed * speedModKoef / 100;
    }

    private void calculateSpeedByStep() {
        double ms = calculateMoveSpeed();
        distance = ms * field.getSpeedKoef()
                * field.getSpeedMultiplier() / 1000;

        if (distance <= 0) {
            distance = 0.0001;
        }
        this.speedChanged = false;
        getEntity().setModified(true);
    }

    public void setSpeedModLoef(int speedModKoef) {
        if (this.speedModKoef != speedModKoef) {
            this.speedChanged = true;
            this.speedModKoef = speedModKoef;
        }
    }

    private void trimPathForQuickCalc(BMCell pos) {
        int dx = Math.abs(destinationCell.getPosX() - pos.getPosX());
        int dy = Math.abs(destinationCell.getPosY() - pos.getPosY());
        double hyp = Math.sqrt(dx * dx + dy * dy);

        if (hyp > pathMaxLength) {
            double sinx = dx / hyp;
            double cosy = dy / hyp;

            int sx = 1;
            int sy = 1;

            if (pos.getPosX() >= destinationCell.getPosX()) {
                sx = -1;
            }
            if (pos.getPosY() >= destinationCell.getPosY()) {
                sy = -1;
            }

            int x1 = (int) (pathMaxLength * sx * sinx);
            int y1 = (int) (pathMaxLength * sy * cosy);

            tempDest = field
                    .getCell(pos.getPosX() + x1, pos.getPosY() + y1);

            if (tempDest.getBuilding() != null) {
                BMCell[] corners = tempDest.getBuilding()
                        .getComponent(BMLargeLocatorComponent.class)
                        .getCorners();
                int minDist = Integer.MAX_VALUE;
                for (BMCell c : corners) {
                    int hyp2 = destinationCell.squreDistanceBetween(c);
                    if (minDist > hyp2) {
                        minDist = hyp2;
                        tempDest = c;
                    }
                }
            }
        } else {
            tempDest = destinationCell;
        }
    }

    private boolean checkCanMove() {
        if (this.destinationCell == null) {
            return false;
        }
        if (this.destinationCell.equals(getPos())) {
            this.nextPosition = null;
            if (getEntity().getActionState().contains(BMEntityActionState.move)) {
                if (getEntity().getActionState().contains(BMEntityActionState.attack)) {
                    getEntity().getActionState().remove(BMEntityActionState.move);
                } else {
                    getEntity().setActionState(BMEntityActionState.idle);
                }
            }
            return false;
        }
        return true;
    }

    private boolean checkConflictsInPath() {
        if (path != null) {
            for (BMCell c : smothedPath) {
                if (field.getAreaMap().getNode(c.getPosX(), c.getPosY()).isObstacle()) {
                    if (reservedCell != null && c.equals(reservedCell)) {
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void doMotion(BMCell pos, int step) {
        WayPoint nextwp = wayPoints.get(currentPathStep);
        if (pos == nextwp.cell) {
            currentPathStep++;
            nextwp = wayPoints.get(currentPathStep);
        }

        if (this.nextPosition != nextwp.cell) {
            this.nextPosition = nextwp.cell;
            double stp = nextwp.hdistance / distance;
            scellStep = Math.round(stp / (nextwp.cellsCnt) + 0.5);
            lastStep = step;
            nextStep = (int) (lastStep + scellStep);
            this.nextIteration = (int) (lastStep + scellStep * nextwp.cellsCnt);
        } else if (speedChanged) {
            calculateSpeedByStep();
            double stp = nextwp.hdistance / distance;
            scellStep = Math.round(stp / (nextwp.cellsCnt) + 0.5);
            int correction = lastStep - step;
            lastStep = lastStep + correction;
            nextStep = (int) (lastStep + scellStep);
            this.nextIteration = (int) (lastStep + scellStep * nextwp.cellsCnt);
        }

        if (step >= nextStep) {
            currentSmothedIdx++;
            BMCell nextcell;
            if (currentSmothedIdx < 0 || currentSmothedIdx >= smothedPath.size()) {
                System.err.println("ERROR in move component >> smothedIdx = " + currentSmothedIdx);
                currentSmothedIdx = smothedPath.size() - 1;
                nextcell = smothedPath.get(currentSmothedIdx);

                if (!nextcell.equals(tempDest)) {
                    nextcell = tempDest;
                }
//                return;
            } else {
                nextcell = smothedPath.get(currentSmothedIdx);
            }

            lastStep = step;
            nextStep = (int) (step + scellStep);

            if (nextcell == null) {
                System.out.println(getEntity().getIdentifier() + " ERRROR ");
            }

            if (!pos.equals(nextcell)) {
                if (pos.equals(reservedCell)) {
//                    restetReservedCell();
                }

                ((IBMLocable) getEntity()).setPosition(nextcell);

                if (path.getLength() > 0) {
                    this.notifyEntity(this);
                }
            }
        }
    }

    private static class WayPoint {

        public WayPoint(BMCell cell, BMCell start) {
            this.cell = cell;
            this.start = start;
            int dx = Math.abs(start.getPosX() - cell.getPosX());
            int dy = Math.abs(start.getPosY() - cell.getPosY());
            hdistance = Math.sqrt(dx * dx + dy * dy);
        }
        public BMCell start;
        public BMCell cell;
        public double hdistance;
        public int smothedIdx;
        public int cellsCnt;
    }
}
