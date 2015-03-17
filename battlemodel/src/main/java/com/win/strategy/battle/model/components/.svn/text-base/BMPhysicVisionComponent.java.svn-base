package com.win.strategy.battle.model.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;

/**
 * Vision component used in finding targets, that can be attacked
 *
 * @author vlischyshyn
 */
public class BMPhysicVisionComponent extends BMBaseComponent {

    private int visionRange;
    private int attackRange;
    private List<BMCell> vision;
    private List<BMCell> attackable;
    private Set<BMFractionEntity> targets;
    private BMField field;
    private BMFractionEntity entity;
    private BMAttackPriorityType attackPriorityType = BMAttackPriorityType.any;
    private int compareCoef = 10000;

    public BMPhysicVisionComponent() {
        this.setIdentifier("vision");
        this.vision = new ArrayList<>();
        this.attackable = new ArrayList<>();
        this.targets = new TreeSet<>(new Comparator<BMFractionEntity>() {
            @Override
            public int compare(BMFractionEntity o1, BMFractionEntity o2) {
                BMCell pos = getPos();
                long d1 = pos.squreDistanceBetween(o1.getPosition());
                long d2 = pos.squreDistanceBetween(o2.getPosition());

                if (attackPriorityType != BMAttackPriorityType.any) {
                    d1 = (o1.getPriorityState() == attackPriorityType) ? d1 : d1 * compareCoef;
                    d2 = (o2.getPriorityState() == attackPriorityType) ? d2 : d2 * compareCoef;
                }
                return Long.compare(d1, d2);
            }
        });
    }

    public void calculateVision() {
        if (getPos() == null || visionRange == 0) {
            return;
        }

        vision.clear();
        attackable.clear();
        entity = (BMFractionEntity) getEntity();
        field = entity.getField();

        targets.clear();
        // fildOfVisionFast();
        // or
        findFieldOfVisionAccuracy();

    }

    public IBMTargetable findTarget() {
        calculateVision();
        if (!targets.isEmpty()) {
            return targets.iterator().next();
        }
        return null;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public boolean canAttact(BMCell targetPos) {
        return attackable.contains(targetPos);
    }

    private BMCell getPos() {
        BMFractionEntity fe = (BMFractionEntity) getEntity();
        return fe.getCenter();
    }

    public List<BMCell> getVision() {
        return vision;
    }

    public int getVisionRange() {
        return visionRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setVisionRange(int visionRange) {
        this.visionRange = visionRange;
    }

    @Override
    public void notify(Object arg) {
    }

    /**
     * fast algorithm, but middle accuracy
     */
    private void fildOfVisionFast() {
        double x, y;
        for (int i = 0; i < 360; i += 2) {
            x = Math.cos((float) i * 0.01745f);
            y = Math.sin((float) i * 0.01745f);
            doFov(x, y);
        }
    }

    private void doFov(double x, double y) {
        float ox, oy;

        ox = (float) getPos().getPosX() + 0.5f;
        oy = (float) getPos().getPosY() + 0.5f;

        BMCell cell;
        for (int i = 0; i < visionRange + 1; i++) {
            int ix = (int) ox;
            int iy = (int) oy;

            if (ix < 0 || iy < 0 || ix >= field.getMaxSizeX()
                    || iy >= field.getMaxSizeY()) {
                return;
            }

            cell = field.getCell(ix, iy);

            for (BMEntity t : cell.getInnerObjects().values()) {
                BMFractionEntity target = canAddEntityToTarget(t);
                if (target != null) {
                    targets.add(target);
                }
            }

            if (cell.getBuilding() != null || cell.getTrash() != null) {
                return;
            }

            vision.add(cell);

            ox += x;
            oy += y;
        }
    }

    private void findFieldOfVisionAccuracy() {
        int i, j;
        for (i = -visionRange; i <= visionRange; i++) {
            for (j = -visionRange; j <= visionRange; j++) {
                if (i * i + j * j < visionRange * visionRange) {
                    los(i, j);
                }
            }
        }
    }

    private void los(int x1, int y1) {
        BMCell pos = getPos();
        int posx = pos.getPosX();
        int posy = pos.getPosY();

        x1 += posx;
        y1 += posy;

        if (x1 < 0 || y1 < 0 || x1 >= field.getMaxSizeX()
                || y1 >= field.getMaxSizeY()) {
            return;
        }

        int sx, sy;
        int dx = x1 - posx;
        int dy = y1 - posy;
        if (posx < x1) {
            sx = 1;
        } else {
            sx = -1;
        }
        if (posy < y1) {
            sy = 1;
        } else {
            sy = -1;
        }
        // sx and sy are switches that enable us to compute the LOS in a single
        // quarter of x/y plan
        int xnext = posx;
        int ynext = posy;
        double denom = Math.sqrt(dx * dx + dy * dy);
        BMCell cell;
        while (xnext != x1 || ynext != y1) {
            // check map bounds here if needed

            cell = field.getCell(xnext, ynext);

            if (!getEntity().equals(cell.getBuilding())) {

                if (cell.getBuilding() != null || cell.getTrash() != null) // or any equivalent
                {
                    for (BMEntity t : cell.getInnerObjects().values()) {
                        BMFractionEntity target = canAddEntityToTarget(t);
                        if (target != null) {
                            targets.add(target);
                        }
                    }

                    return;
                }
            }
            // Line-to-point distance formula < 0.5
            if (Math.abs(dy * (xnext - posx + sx) - dx * (ynext - posy))
                    / denom < 0.5f) {
                xnext += sx;
            } else if (Math.abs(dy * (xnext - posx) - dx * (ynext - posy + sy))
                    / denom < 0.5f) {
                ynext += sy;
            } else {
                xnext += sx;
                ynext += sy;
            }
        }

        cell = field.getCell(xnext, ynext);
        for (BMEntity t : cell.getInnerObjects().values()) {
            BMFractionEntity target = canAddEntityToTarget(t);
            if (target != null) {
                targets.add(target);
            }
        }

        vision.add(cell);

        if (attackRange < 2) {
            if (Math.abs(pos.getPosX() - cell.getPosX()) < 2
                    && Math.abs(pos.getPosY() - cell.getPosY()) < 2) {
                attackable.add(cell);
            }
        } else {
            if (pos.distanceBetween(cell) <= attackRange) {
                attackable.add(cell);
            }
        }
    }

    /**
     * Check if entity can be focused, if true then method returns this entity,
     * in other way it returns null;
     *
     * @param e entity
     * @return {@link BMFractionEntity} target;
     */
    private BMFractionEntity canAddEntityToTarget(BMEntity e) {
        if (IBMTargetable.class
                .isInstance(e)) {
            BMFractionEntity target = (BMFractionEntity) e;

            if (target.getFraction() != entity.getFraction()
                    && target.isInGame()
                    && target.isAlive()
                    && target.isVisible()
                    && target.canBeFocused()) {
                return target;
            }
        }
        return null;
    }

    public void setAttackPriorityType(BMAttackPriorityType attackPriorityType) {
        this.attackPriorityType = attackPriorityType;
    }

    public BMAttackPriorityType getAttackPriorityType() {
        return this.attackPriorityType;
    }
}
